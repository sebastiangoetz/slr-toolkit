package de.tudresden.slr.questionnaire;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.questionnaire.model.Question;
import de.tudresden.slr.questionnaire.questionview.QuestionViewBase;
import de.tudresden.slr.questionnaire.wizard.NewQuestionWizard;
import de.tudresden.slr.questionnaire.wizard.NewQuestionnaireWizard;

public class QuestionnaireView extends ViewPart {

    private Composite parent;
    private ScrolledComposite scroll;

    private ComboProject projectSelector;
    private ComboQuestionnaire questionnaireSelector;

    private BibtexEntryWatcher documentWatcher;
    private Label documentLabel;

    private Questionnaire questionnaire;
    private Document document;

    private List<QuestionViewBase<?>> questionViews = new LinkedList<>();

    private static String NO_DOCUMENT_PLACEHOLDER = "<no document>";

    private UnansweredDocumentsView unansweredDocumentsView;

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;

        parent.setLayout(new GridLayout(2, false));

        new Label(parent, 0).setText("Project");
        projectSelector = new ComboProject(parent, new GridData());

        new Label(parent, 0).setText("Questionnaire");
        questionnaireSelector = new ComboQuestionnaire(parent, new GridData());

        new Label(parent, 0).setText("Document");
        documentLabel = new Label(parent, 0);
        documentLabel.setText(NO_DOCUMENT_PLACEHOLDER);

        createButtonNewQuestion(parent);
        createButtonNewQuestionnaire(parent);

        createSummarryButton(parent);
        new Label(parent, 0); // dummy to fill the grid cell

        unansweredDocumentsView = new UnansweredDocumentsView(parent, getSite(), questionnaireSelector::getSelected,
                projectSelector::getSelected);

        documentWatcher = new BibtexEntryWatcher(getSite().getWorkbenchWindow().getSelectionService());
        documentWatcher.addDocumentListener(this::onDocumentChanged);

        projectSelector.addObserver(questionnaireSelector::setProject);
        questionnaireSelector.addObserver(this::setQuestionnaire);
        questionnaireSelector.addObserver(q -> unansweredDocumentsView.findDocuments());

        projectSelector.updateOptionsDisplay();
    }

    private void createSummarryButton(Composite parent2) {
        Button btn = new Button(parent, 0);
        btn.setText("Summary");
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                if (questionnaire == null)
                    return;
                HtmlSummary summary = new HtmlSummary(questionnaire);
                summary.showSummary();
            }
        });
    }

    @Override
    public void setFocus() {
    }

    private void createButtonNewQuestion(Composite parent) {
        Button buttonNewQuestion = new Button(parent, 0);
        buttonNewQuestion.setText("New Question");
        buttonNewQuestion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                WizardDialog dialog = new WizardDialog(shell, new NewQuestionWizard(questionnaire, null));
                dialog.open();
                setQuestionnaire(questionnaire);
            }
        });
    }

    private void createButtonNewQuestionnaire(Composite parent) {
        Button btn = new Button(parent, 0);
        btn.setText("New Questionnaire");
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                IWorkbench wb = PlatformUI.getWorkbench();
                IStructuredSelection selection = (IStructuredSelection) wb.getActiveWorkbenchWindow()
                        .getSelectionService().getSelection();
                if (selection == null)
                    selection = StructuredSelection.EMPTY;
                NewQuestionnaireWizard wiz = new NewQuestionnaireWizard();
                wiz.init(wb, selection);
                WizardDialog w = new WizardDialog(parent.getShell(), wiz);
                w.open();
                questionnaireSelector.updateOptionsDisplay();
            }
        });
    }

    private void onDocumentChanged(Document document) {
        if (document == null)
            return;
        this.document = document;
        documentLabel.setText(document.getKey());
        documentLabel.getParent().layout();

        setQuestionnaire(questionnaire);
        updateEnableAnswering();
    }

    private void setQuestionnaire(Questionnaire newQuestionnaire) {
        commitQuestionnaire();
        questionnaire = newQuestionnaire;
        questionViews.clear();
        if (scroll != null)
            scroll.dispose();

        if (questionnaire != null) {
            // TODO new ScrolledComposite should retain scroll height if questionnaire hasn't changed
            scroll = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
            GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
            scroll.setLayoutData(gd);
            Composite innerContainer = new Composite(scroll, 0);
            innerContainer.setLayout(new GridLayout(1, false));
            for (Question<?> question : questionnaire.getQuestions()) {
                String documentKey = (document == null) ? null : document.getKey();
                QuestionViewBase<?> view = QuestionViewBase.qvFor(innerContainer, question, questionnaire, documentKey);
                view.setOnQuestionChanged(this::onQuestionChanged);
                view.setOnQuestionnaireChanged(this::setQuestionnaire);
                questionViews.add(view);
            }
            updateEnableAnswering();

            scroll.setContent(innerContainer);
            innerContainer.setSize(innerContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        }
        parent.layout(true);
    }

    private void updateEnableAnswering() {
        final boolean shouldEnable = document != null;
        questionViews.forEach(it -> it.enableControls(shouldEnable));
    }

    private void commitQuestionnaire() {
        if (questionnaire == null)
            return;
        QuestionnaireStorage qs = QuestionnaireStorage.getInstance();
        qs.persist(questionnaire);
        unansweredDocumentsView.findDocuments();
    }

    private void onQuestionChanged(Question<?> question) {
        questionnaire.setDirty(true);
    }

}
