package de.tudresden.slr.model.bibtex.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Model;

public class BibtexFileWriter {

	public static void updateBibtexFileForDocument(Document document) {
		//IFile iFile = getIFilefromDocument(document);		
		//File file = iFile.getRawLocation().makeAbsolute().toFile();
		//StringWriter writer = new StringWriter();
		//Resource resource = (new ResourceSetImpl()).createResource(URI.createFileURI(file.getAbsolutePath()));;
		Resource resource = document.eResource();
		List<EObject> objects = resource.getContents();
		
		resource.toString();
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String bibRef = document.getKey();
		Model taxonomy = document.getTaxonomy();
		//String taxonomyRepresentation = TaxonomyToStringConverter.convertToBib(taxonomy);
	}
	
}
