package de.tudresden.slr.classification.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;

import de.tudresden.slr.classification.classifiers.StandardTaxonomyClassifier;
import de.tudresden.slr.classification.pages.EntryExcludeSelectionPage;

public class GenerateStandardTaxonomyWizard extends Wizard {

	private WizardNewProjectReferencePage projectsPage;
	private EntryExcludeSelectionPage entryTypeExclusionPage;

	
	public GenerateStandardTaxonomyWizard() {
		projectsPage = new WizardNewProjectReferencePage("projects");
		entryTypeExclusionPage = new EntryExcludeSelectionPage("excludeTypes");
		projectsPage.setTitle("Generate Taxonomy from Bibtex");
		entryTypeExclusionPage.setTitle("Exclude Bibtex Entry Types");
		
	}
	
	public void addPages() {
		super.addPages();
		addPage(projectsPage);
		addPage(entryTypeExclusionPage);
	}

	@Override
	public boolean performFinish() {
		for(IProject project:projectsPage.getReferencedProjects())
		{
			(new StandardTaxonomyClassifier()).classifyDocumentsInProject(project,entryTypeExclusionPage.getExclusionList());
		}
		return true;
	}

}
