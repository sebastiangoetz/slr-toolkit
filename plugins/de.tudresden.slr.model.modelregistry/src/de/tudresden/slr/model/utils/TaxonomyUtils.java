package de.tudresden.slr.model.utils;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.ecore.util.EcoreUtil;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;

public class TaxonomyUtils {

	public static void saveTaxonomy(Model taxonomy) {
		Model newTaxonomy = EcoreUtil.copy(taxonomy);
		ModelRegistryPlugin.getModelRegistry().setActiveTaxonomy(newTaxonomy);
		try {					
			if (newTaxonomy.getResource() == null) newTaxonomy.setResource(taxonomy.eResource());
			newTaxonomy.getResource().save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
