package de.tudresden.slr.questionnaire.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.internal.dialogs.NewWizard;

import de.tudresden.slr.questionnaire.ComboProject;
import de.tudresden.slr.questionnaire.ComboQuestionnaire;
import de.tudresden.slr.questionnaire.Questionnaire;
import de.tudresden.slr.questionnaire.QuestionnaireStorage;

public class NewQuestionnaireWizard extends NewWizard {

	private NewQuestionnaireWizardPage pageFileName;
	private PickBaseQuestionnaireWizardPage pageUseBase;
	private IStructuredSelection selection;

	public NewQuestionnaireWizard() {
		setWindowTitle("New Questionnaire");
	}

	@Override
	public void init(IWorkbench aWorkbench, IStructuredSelection currentSelection) {
		selection = currentSelection;
	}

	@Override
	public void addPages() {
		pageFileName = new NewQuestionnaireWizardPage(selection);
		addPage(pageFileName);
		pageUseBase = new PickBaseQuestionnaireWizardPage();
		addPage(pageUseBase);
	}

	@Override
	public boolean performFinish() {
		IFile f = pageFileName.createNewFile();
		if (f == null) {
			return false;
		}
		Questionnaire q = pageUseBase.getQuestionnaire();
		if (q == null) 
			q = new Questionnaire(f.getName());
		q.setName(f.getName());
		q.setDirty(true);
		QuestionnaireStorage.getInstance().persist(f, q);
		return true;
	}
}

class NewQuestionnaireWizardPage extends WizardNewFileCreationPage {
	public NewQuestionnaireWizardPage(IStructuredSelection selection) {
		super(NewQuestionnaireWizardPage.class.getName(), selection);
		setTitle("New Questionnaire");
		setDescription("Create a new questionnaire");
		setFileExtension("questionnaire");
	}
}

class PickBaseQuestionnaireWizardPage extends WizardPage {
	private Questionnaire baseQuestionnaire;
	private Button checkUseBase;
	private Button checkDiscardAnswers;

	protected PickBaseQuestionnaireWizardPage() {
		super(PickBaseQuestionnaireWizardPage.class.getName());
		setTitle("New Questionnaire");
		setDescription("(Optional) Chose a base questionnaire");
	}

	@Override
	public void createControl(Composite parent) {
		parent = new Composite(parent, 0);
		parent.setLayout(new GridLayout(2, false));
		setControl(parent);

		checkUseBase = new Button(parent, SWT.CHECK);
		checkUseBase.setText("Use an existing questionnaire as a base");
		checkUseBase.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false, 2, 1));

		checkDiscardAnswers = new Button(parent, SWT.CHECK);
		checkDiscardAnswers.setText("Discard existing answers in copy");
		checkDiscardAnswers.setSelection(true);
		checkDiscardAnswers.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false, 2, 1));

		new Label(parent, SWT.NONE).setText("Project");
		ComboProject cp = new ComboProject(parent, new GridData());

		new Label(parent, SWT.NONE).setText("Base Questionnaire");
		ComboQuestionnaire cq = new ComboQuestionnaire(parent, new GridData());

		cp.addObserver(project -> cq.setProject(project));
		cq.addObserver(q -> this.baseQuestionnaire = q);

		cp.updateOptionsDisplay();
		cq.updateOptionsDisplay();
	}

	/**
	 * Returns a copy of the selected {@link Questionnaire}. If the user selected to
	 * discard answers, the copy will not contain any answers. Does not modify the
	 * original {@link Questionnaire}.
	 * 
	 * @return <code>null</code> if the user chooses not to use a base, otherwise a
	 *         modified copy of the base {@link Questionnaire}
	 */
	public Questionnaire getQuestionnaire() {
		if (!checkUseBase.getSelection())
			return null;
		Questionnaire copy = baseQuestionnaire.deepCopy();
		if (checkDiscardAnswers.getSelection())
			copy.clearAnswers();
		return copy;
	}

}