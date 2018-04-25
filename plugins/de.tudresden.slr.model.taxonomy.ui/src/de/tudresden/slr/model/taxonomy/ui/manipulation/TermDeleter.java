package de.tudresden.slr.model.taxonomy.ui.manipulation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

public class TermDeleter {
	
	public static void delete(List<Term> termsToDelete) {
		Map<Document, List<Term>> termsInDocuments = new HashMap<>(); 
		for (Term term : termsToDelete) {
			Map<Document, Term> termInDocuments = SearchUtils.findDocumentsWithTerm(term);
			termInDocuments.forEach((k, v) -> {
					if (termsInDocuments.containsKey(k)) termsInDocuments.get(k).add(v);
					else termsInDocuments.put(k, new LinkedList<Term>(Arrays.asList(v)));
			});  
			Optional<Model> model = SearchUtils.getConainingModel(term);
			EcoreUtil.remove(term);			
			if (model.isPresent()) {
				TaxonomyUtils.saveTaxonomy(model.get());
			}
		}
		Set<Resource> resourcesToUpdate = new TreeSet<>(
				(Resource r1, Resource r2) -> r1 == r2 ? 0 : 1);
		for (Map.Entry<Document, List<Term>> entry : termsInDocuments.entrySet()) {	
			entry.getValue().forEach(t -> EcoreUtil.remove(t));
			resourcesToUpdate.add(entry.getKey().eResource());
		}
		resourcesToUpdate.forEach(r -> BibtexFileWriter.updateBibtexFile(r));
	}

}
