package de.tudresden.slr.utils.taxonomy.manipulation;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexFileWriter;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.model.utils.TaxonomyUtils;

public class TermRenamer {

	public static Term rename(Term term, String name) {
		Map<Document, Term> termsInDocuments = SearchUtils.findDocumentsWithTerm(term);
		term.setName(name);
		Set<Resource> resourcesToUpdate = new TreeSet<>(
				(Resource r1, Resource r2) -> r1 == r2 ? 0 : 1);
		for (Map.Entry<Document, Term> entry : termsInDocuments.entrySet()) {
			entry.getValue().setName(name);
			Optional<Model> model = SearchUtils.getConainingModel(entry.getValue());
			if (model.isPresent()) {
				Model newTaxonomy = EcoreUtil.copy((Model) model.get());
				entry.getKey().setTaxonomy(newTaxonomy);					
			}				
			resourcesToUpdate.add(entry.getKey().eResource());			
		}			
		resourcesToUpdate.forEach(r -> BibtexFileWriter.updateBibtexFile(r));		
		Optional<Model> model = SearchUtils.getConainingModel(term);
		if (model.isPresent()) {
			TaxonomyUtils.saveTaxonomy(model.get());			
		}
		return term;
	}
}
