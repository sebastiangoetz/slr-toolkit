package de.tudresden.slr.wizards;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import de.tudresden.slr.classification.classifiers.StandardTaxonomyClassifier;

public class NewSlrProjectWithStandardTaxonomyWizard extends NewSlrProjectWizard {

	public NewSlrProjectWithStandardTaxonomyWizard() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean performFinish() {
		try {
			IProject project = createSlrProject();
			StandardTaxonomyClassifier.classifyDocumentsInProject(project);
		} catch(CoreException e) {
			return false;
		}
		return true;
	}

}
