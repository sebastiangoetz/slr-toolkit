package de.tudresden.slr.questionnaire;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.gson.Gson;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.questionnaire.model.Question;
import de.tudresden.slr.questionnaire.util.GsonFactory;
import de.tudresden.slr.questionnaire.wizard.NewQuestionWizard;
import de.tudresden.slr.questionnaire.wizard.NewQuestionnaireWizard;

public class QuestionnaireView extends ViewPart {

	private Composite parent;
	private Composite questionsContainer;

	private ComboProject projectSelector;
	private ComboQuestionnaire questionnaireSelector;

	private BibtexEntryWatcher documentWatcher;
	private Label documentLabel;

	private Questionnaire questionnaire;
	private Document document;

	private Collection<QuestionView> questionViews = new LinkedList<>();

	private static String NO_DOCUMENT_PLACEHOLDER = "<no document>";

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(new RowLayout(SWT.VERTICAL));

		Composite selectors = new Composite(parent, 0);
		selectors.setLayout(new GridLayout(2, false));

		new Label(selectors, 0).setText("Project");
		projectSelector = new ComboProject(selectors, new GridData());

		new Label(selectors, 0).setText("Questionnaire");
		questionnaireSelector = new ComboQuestionnaire(selectors, new GridData());

		new Label(selectors, 0).setText("Document");
		documentLabel = new Label(selectors, 0);
		documentLabel.setText(NO_DOCUMENT_PLACEHOLDER);
		createButtonNewQuestion(selectors);
		createButtonNewQuestionnaire(selectors);

		documentWatcher = new BibtexEntryWatcher(getSite().getWorkbenchWindow()
				.getSelectionService());
		documentWatcher.addDocumentListener(this::onDocumentChanged);

		projectSelector.addObserver(questionnaireSelector::setProject);
		questionnaireSelector.addObserver(this::setQuestionnaire);

		projectSelector.updateOptionsDisplay();
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
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow()
						.getShell();
				WizardDialog dialog = new WizardDialog(shell, new NewQuestionWizard(questionnaire));
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
						.getSelectionService()
						.getSelection();
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
		this.document = document;

		String text = (document == null) ? NO_DOCUMENT_PLACEHOLDER : document.getKey();
		documentLabel.setText(text);
		documentLabel.getParent()
				.layout();

		setQuestionnaire(questionnaire);
		updateEnableAnswering();
	}

	private void setQuestionnaire(Questionnaire newQuestionnaire) {
		questionnaire = newQuestionnaire;
		questionViews.clear();
		if (questionsContainer != null) {
			questionsContainer.dispose();
		}
		if (questionnaire != null) {
			commitQuestionnaire();
			questionsContainer = new Composite(parent, 0);
			// TODO make scrollable
			// https://stackoverflow.com/questions/14445580/scrollable-composite-auto-resize-swt
			questionsContainer.setLayout(new RowLayout(SWT.VERTICAL));
			for (Question<?> question : questionnaire.getQuestions()) {
				QuestionView view = new QuestionView(questionsContainer, question, this::getDocument,
						this::onQuestionChanged);
				view.render();
				questionViews.add(view);
			}
			updateEnableAnswering();
		}
		parent.pack();
		parent.layout(true);
	}

	private void updateEnableAnswering() {
		questionViews.forEach(it -> it.enableAnswering(document != null));
	}

	private Document getDocument() {
		return document;
	}

	private void commitQuestionnaire() {
		QuestionnaireStorage qs = QuestionnaireStorage.getInstance();
		qs.persist(questionnaire);
	}

	private void onQuestionChanged(Question<?> question) {
		// TODO maybe do something here? flag questionnaire as dirty to prevent unnecessary writes?
	}

}
