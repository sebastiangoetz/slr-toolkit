package de.tudresden.slr.model.utils;

import java.io.IOException;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.SaveOptions;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;

public class TaxonomyUtils {

	public static void saveTaxonomy(Model taxonomy) {
		Model newTaxonomy = EcoreUtil.copy(taxonomy);
		ModelRegistryPlugin.getModelRegistry().setActiveTaxonomy(newTaxonomy);
		try {					
			if (newTaxonomy.getResource() == null) newTaxonomy.setResource(taxonomy.eResource());
			SaveOptions.Builder optionsBuilder = SaveOptions.newBuilder();
			optionsBuilder.format();
			newTaxonomy.getResource().save(optionsBuilder.getOptions().toOptionsMap());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
