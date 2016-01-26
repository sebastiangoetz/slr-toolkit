package de.tudresden.slr.model.taxonomy.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.TaxonomyStandaloneSetup;

public final class TaxonomyStandaloneParser {
	private TaxonomyStandaloneParser(){}
	
	public static Model parseTaxonomyFile(IFile taxonomyFile){
		Injector injector = new TaxonomyStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		String filePath = taxonomyFile.getLocation().toString();
		Resource res = resourceSet.getResource(URI.createFileURI(filePath), true);
		return parseTaxonomyFile(res);
	}
	
	public static Model parseTaxonomyFile(Resource taxonomyFile){
		return (Model)taxonomyFile.getContents().get(0);
	}
}
