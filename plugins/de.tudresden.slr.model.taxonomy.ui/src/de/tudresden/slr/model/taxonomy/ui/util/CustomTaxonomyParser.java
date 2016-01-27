package de.tudresden.slr.model.taxonomy.ui.util;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.resource.XtextResourceFactory;

public class CustomTaxonomyParser {
	@SuppressWarnings("unused")
	public void Parse(IFile file){
		//IResourceServiceProvider rsp = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(uri);
		//XtextResource res = null;

		ResourceSet resourceSet = null;
		Resource resource = null;
		URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		try {
			//Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("taxonomy", new BibtexResourceFactoryImpl());
			XtextResourceFactory xrf =  (XtextResourceFactory)Resource.Factory.Registry.INSTANCE.getFactory(uri);
			resourceSet = new ResourceSetImpl();
			resource = resourceSet.createResource(uri);
			EList<EObject> contents = resource.getContents();

			resource.load(file.getContents(), Collections.EMPTY_MAP); //crucial
			EList<EObject> contents2 = resource.getContents();
			String a = "";
			
		} catch (IOException | CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
