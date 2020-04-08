package de.tudresden.slr.wizards;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.command.AbstractCommand;

import de.tudresden.slr.classification.classifiers.StandardTaxonomyClassifier;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;

public class NewSlrProjectWithStandardTaxonomyWizard extends NewSlrProjectWizard {

	public NewSlrProjectWithStandardTaxonomyWizard() {
		// TODO Auto-generated constructor stub
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
						(new StandardTaxonomyClassifier()).classifyDocumentsInProject(project);
						
					}
				});
			}
			
	}

}
