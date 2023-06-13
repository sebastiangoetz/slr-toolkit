package de.tudresden.slr.model.utils;

import java.io.IOException;

import org.eclipse.xtext.resource.SaveOptions;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;

public class TaxonomyUtils {

	public static void saveTaxonomy(Model taxonomy) {
		ModelRegistryPlugin.getModelRegistry().setActiveTaxonomy(taxonomy);
		try {					
			SaveOptions.Builder optionsBuilder = SaveOptions.newBuilder();
			optionsBuilder.format();
			//if (taxonomy.eResource() == null) taxonomy.setResource(taxonomy.eResource()); TODO what the hell?
			taxonomy.eResource().save(optionsBuilder.getOptions().toOptionsMap());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
