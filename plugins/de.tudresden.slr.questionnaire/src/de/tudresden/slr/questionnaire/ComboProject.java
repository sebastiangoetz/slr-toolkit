package de.tudresden.slr.questionnaire;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import de.tudresden.slr.questionnaire.util.EclipseUtils;

public class ComboProject extends ObservableCombo<IProject> {

	public ComboProject(Composite parent, GridData gridData) {
		super(parent, gridData);
	}

	@Override
	protected void updateLabelsAndElements() {
		EclipseUtils.getOpenProjects().forEach(p -> {
			elements.add(p);
			labels.add(p.getName());
		});
	}

}
