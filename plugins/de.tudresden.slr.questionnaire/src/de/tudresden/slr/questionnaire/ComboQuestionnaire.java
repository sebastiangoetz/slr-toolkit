package de.tudresden.slr.questionnaire;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class ComboQuestionnaire extends ObservableCombo<Questionnaire> {

	private IProject project;
	private QuestionnaireStorage storage;

	public ComboQuestionnaire(Composite parent, GridData gridData) {
		super(parent, gridData);
		storage = QuestionnaireStorage.getInstance();
	}

	@Override
	protected void updateLabelsAndElements() {
		storage.listExistingQuestionnaires(project).forEach(questionnaire -> {
			elements.add(questionnaire);
			labels.add(questionnaire.getName());
		});
	}

	public void setProject(IProject project) {
		this.project = project;
		updateOptionsDisplay();
	}
}
