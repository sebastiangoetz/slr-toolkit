package de.tudresden.slr.classification.classifiers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.utils.TaxonomyUtils;
import de.tudresden.slr.utils.taxonomy.manipulation.TermCreator;

public class StandardTaxonomyClassifier {

	
	
	public StandardTaxonomyClassifier() {
		
	}
	
	public static void createStandardTaxonomy(Document doc) {
		Map<String,String> fields = doc.getAdditionalFields();
		Set<String> fieldKeys = fields.keySet();
		if(fieldKeys.contains("document_type")) {
			classifyDocument(doc,"Document Type", fields.get("document_type"));
		} else {
			classifyDocument(doc,"Document Type", doc.getType());
		}
		

		String[] lookUp = {"booktitle","journal","howpublished"};
		for(String type: lookUp) {
			if(fieldKeys.contains(type)) {
				String field = fields.get(type).replaceAll("[^A-Za-z0-9 ]", "");
				if(!Character.isLetter(field.charAt(0))) {
					
					InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							 "Invalid Term name",
							 "Please enter a new term name that starts with a letter.",
							 field,
							 (s -> (!Character.isLetter(s.charAt(0))) ? "" : null)
									 );
					 field = (dlg.open() == 0)? field = dlg.getValue() : "PLACEHOLDER " + field;
					 field.replaceAll("[^A-Za-z0-9 ]", "");
				}
				classifyDocument(doc,"Document Venue", type, field);
			}

		
		}
	 }
	 
	 public static void classifyDocument(Document doc, String ...types) {
		 Optional<Model> modelOptional =  ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy();
		 if(modelOptional.isPresent()) {
			 //create Dimension
			 Model model = modelOptional.get();
			 
			 EObject taxonomyElement = model;
			 EObject docTaxonomyElement = doc.getTaxonomy();
			 
			for(String type: types) {
				 //create element in general taxonomy 
				 taxonomyElement = TermCreator.createChildIfNotExisting(taxonomyElement,type);
				 docTaxonomyElement = TermCreator.createChildIfNotExisting(docTaxonomyElement,type,false);
			 }
			 
			 TaxonomyUtils.saveTaxonomy(model);
			 try {
				 
				 doc.eResource().save(null);
			 } catch (IOException e) {
				 System.err.println("Could not save document " + doc.getKey());
			 }
			 
			 
		 }
	 }
	 
	 public static void classifyDocumentsInProject(IProject project) {
		 List<IFile> bibFiles = new ArrayList<IFile>();
		 IFile taxonomyFile = null;
		 try {
			 for(IResource res:project.members()) {
				 if(res.getFileExtension().equals("bib")) {
					 bibFiles.add(project.getFile(res.getFullPath().lastSegment()));
					 
				 }
				 
				 if(res.getFileExtension().equals("taxonomy")) {
					 taxonomyFile = project.getFile(res.getFullPath().lastSegment());
				 }
			 }
			 
			 ModelRegistryPlugin.getModelRegistry().setTaxonomyFile(taxonomyFile);
			 Optional<AdapterFactoryEditingDomain> domainOpt = ModelRegistryPlugin.getModelRegistry().getEditingDomain();
			 if(domainOpt.isPresent()) {
				 for(IFile file:bibFiles) {
						URI uri = URI.createURI(file.getFullPath().toString());			
						Resource bibRes = domainOpt.get().getResourceSet().getResource(uri, true);
						for(EObject e:bibRes.getContents()) {
							Document doc = (Document) e;
							if(doc.getAuthors().size()!=0) {
								//Very strange bug: If a dialog is opened when the line below is uncommented, saving of docs fails
								//ModelRegistryPlugin.getModelRegistry().setActiveDocument(doc);
								createStandardTaxonomy(doc);
							}
							
						}
				 }
			 }
			
			 
			 
			 
		 } catch(CoreException e) {
			 
		 }
	 }

}
