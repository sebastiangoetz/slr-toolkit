package de.tudresden.slr.model.taxonomy.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.TaxonomyFactory;

public final class TaxonomyStandaloneParser {

	private Model parseTaxonomy(URI uri, InputStream stream){
		ResourceSet resourceSet = null;
		Resource resource = null;
		Model model = TaxonomyFactory.eINSTANCE.createModel();
		try {
			resourceSet = new ResourceSetImpl();
			resource = resourceSet.createResource(uri);
			EList<EObject> contents = resource.getContents();
			resource.load(stream, Collections.EMPTY_MAP);
			if(contents != null && !contents.isEmpty()){
				model = (Model)contents.get(0);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
			     stream.close();
			  } catch (IOException e){
				  e.printStackTrace();
			  }
		}
		return model;
	}
	
	public Model parseTaxonomyFile(IFile taxonomyFile){
		Model model = TaxonomyFactory.eINSTANCE.createModel();
		try {
			model = parseTaxonomy(URI.createPlatformResourceURI(taxonomyFile.getFullPath().toString(), true), taxonomyFile.getContents());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return model;
	}
	
	public Model parseTaxonomyText(String taxonomyString){
		return parseTaxonomy(URI.createURI("dummy:/tax.taxonomy"), new URIConverter.ReadableInputStream(taxonomyString, "UTF-8"));
	}
	
}
