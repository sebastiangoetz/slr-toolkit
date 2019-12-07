package de.tudresden.slr.classification.wizards;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;

import de.tudresden.slr.classification.classifiers.StandardTaxonomyClassifier;

public class GenerateStandardTaxonomyWizard extends Wizard {

	private WizardNewProjectReferencePage projectsPage;

	
	public GenerateStandardTaxonomyWizard() {
		projectsPage = new WizardNewProjectReferencePage("projects");
		projectsPage.setTitle("Generate Taxonomy from Bibtex");
	}
	
	public void addPages() {
		super.addPages();
		addPage(projectsPage);
	}

	@Override
	public boolean performFinish() {
		for(IProject project:projectsPage.getReferencedProjects())
		{
			StandardTaxonomyClassifier.classifyDocumentsInProject(project);
		}
		return true;
	}

}
