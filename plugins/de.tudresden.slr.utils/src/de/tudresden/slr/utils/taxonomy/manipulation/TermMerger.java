package de.tudresden.slr.utils.taxonomy.manipulation;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexFileWriter;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.TaxonomyFactory;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.util.TermUtils;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.model.utils.TaxonomyUtils;

public class TermMerger {
	
	/**
	 * Merges all terms into the target. 
	 * @param termsToMerge All terms to merge, must contain the target.
	 * @param targetTerm The target into which all terms will be merged.
	 * @return The resulting, merged term. 
	 */
	public static Term merge(Collection<Term> termsToMerge, Term targetTerm) {
		// Merging can be dangerous, the case might occur in which the target term does not exist in a document
		// In this case, the target term must first be created for the document
		// find all documents that contain the target or, if not but the documents contains a term to merge than 
		// rebuild the target structure
		Map<Document, Term> documentsWithTarget = new HashMap<>();
		for (Document d : SearchUtils.getDocumentList()) {
			Term targetInDocument = SearchUtils.findTermInDocument(d, targetTerm);
			if (targetInDocument != null) {
				documentsWithTarget.put(d,  targetInDocument);
			} else {
				for (Term term : termsToMerge) {
					if (SearchUtils.findTermInDocument(d, term) != null) {
						// the target does not exist in d. However, d contains a term to merge
						// therefore, the target must be created
						// copied from TermMover TODO extract
						// build structure in document										
						Term targetDuplicate = EcoreUtil.copy(targetTerm);
						Deque<Term> taxonomyRebuild = new LinkedList<>();
						do {
							taxonomyRebuild.offerFirst(TaxonomyFactory.eINSTANCE.createTerm());
							taxonomyRebuild.peekFirst().setName(targetDuplicate.getName());
							targetDuplicate = targetDuplicate.eContainer() instanceof Term ? (Term) targetDuplicate.eContainer() : null;
						} while (targetDuplicate != null && SearchUtils.findTermInDocument(d, targetDuplicate) == null);
						Term rebuild = taxonomyRebuild.pollFirst();
						Term last = rebuild;
						while (taxonomyRebuild.peekFirst() != null) {						
							last.getSubclasses().add(taxonomyRebuild.peekFirst());
							last = taxonomyRebuild.pollFirst();						
						}
						if (targetDuplicate == null) {
							// dimension is missing
							d.getTaxonomy().getDimensions().add(rebuild);
						} else {
							// term is missing
							SearchUtils.findTermInDocument(d, targetDuplicate).getSubclasses().add(rebuild);
						}
						documentsWithTarget.put(d,  SearchUtils.findTermInDocument(d, term));
						break;
					}
				}
			}
		}
		
		// now merge all terms for the main taxonomy as well as for the taxonomies of the documents		
		// first, for each document, otherwise the terms would be manipulated
		Set<Resource> resourcesToUpdate = new TreeSet<>(
				(Resource r1, Resource r2) -> r1 == r2 ? 0 : 1);
		for (Map.Entry<Document, Term> entry : documentsWithTarget.entrySet()) {
			entry.getKey().setTaxonomy(doMerge(entry.getKey().getTaxonomy(), termsToMerge, entry.getValue()));
			resourcesToUpdate.add(entry.getKey().eResource());
		}		
		resourcesToUpdate.forEach(r -> BibtexFileWriter.updateBibtexFile(r));
		// secondly, for the main taxonomy
				Optional<Model> model = SearchUtils.getConainingModel(targetTerm);		
				if (model.isPresent()) {			
					TaxonomyUtils.saveTaxonomy(doMerge(model.get(), termsToMerge, targetTerm));
				}
		return SearchUtils.findTermInTaxonomy(targetTerm);
	}
	
	/**
	 * Merges all terms and returns the updated model
	 * @param taxonomy
	 * @param termsToMerge
	 * @param targetTerm
	 * @return The updated model
	 */
	private static Model doMerge(Model taxonomy, Collection<Term> termsToMerge, Term targetTerm) {
		Term targetTermInTaxonomy = SearchUtils.findTermInTaxonomy(taxonomy, targetTerm);
		for (Term term : termsToMerge) {			
			if (!TermUtils.equals(term, targetTerm)) {
				Term termInTaxonomy = SearchUtils.findTermInTaxonomy(taxonomy, term);				
				// remove container
				EcoreUtil.remove(termInTaxonomy);
				// append child's to target
				targetTermInTaxonomy.getSubclasses().addAll(termInTaxonomy.getSubclasses());
			}
		}
		return taxonomy;		
	}

}
