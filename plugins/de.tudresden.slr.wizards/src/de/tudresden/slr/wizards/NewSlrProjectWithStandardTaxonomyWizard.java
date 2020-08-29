package de.tudresden.slr.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.command.AbstractCommand;

import de.tudresden.slr.classification.classifiers.StandardTaxonomyClassifier;
import de.tudresden.slr.classification.pages.EntryExcludeSelectionPage;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;

public class NewSlrProjectWithStandardTaxonomyWizard extends NewSlrProjectWizard {

	protected EntryExcludeSelectionPage entryExcludeSelectionPage;
	
	public NewSlrProjectWithStandardTaxonomyWizard() {
		super();
		entryExcludeSelectionPage = new EntryExcludeSelectionPage("Setup exlusionSelection");
		entryExcludeSelectionPage.setTitle("Exclude Bibtex Entry Types");
		firstPage.setTitle("Create a new SLR Project and automatically classify entries");
	}
	
	public void addPages() {
		super.addPages();
		addPage(entryExcludeSelectionPage);
	}
	
	public boolean performFinish() {
		try {
			createSlrProject();
			//(new StandardTaxonomyClassifier()).classifyDocumentsInProject(project);
			
		} catch(CoreException e) {
			return false;
		}
		return true;
	}
	
	
	
	protected void onAllFilesLoaded() {
		super.onAllFilesLoaded();
			if(ModelRegistryPlugin.getModelRegistry().getEditingDomain().isPresent()) {
				ModelRegistryPlugin.getModelRegistry().getEditingDomain().get().getCommandStack().execute(new AbstractCommand() {
					
					@Override
					protected boolean prepare() {
						return true;
					}
					
					@Override
					public void redo() {
						execute();
						
					}
					
					@Override
					public void execute() {
						(new StandardTaxonomyClassifier(false)).classifyDocumentsInProject(project,entryExcludeSelectionPage.getExclusionList());
						
					}
				});
			}
			
	}

}
