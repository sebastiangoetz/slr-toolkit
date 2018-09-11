package de.tudresden.slr.utils.taxonomy.manipulation;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexFileWriter;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.TaxonomyFactory;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.model.utils.TaxonomyUtils;
import de.tudresden.slr.utils.TermPosition;

public class TermMover {
	
	public static void move(List<Term> termsToMove, Term target, TermPosition position) {
		Map<Document, List<Term>> termsInDocuments = new HashMap<>(); 
		for (Term termToMove : termsToMove) {
			// find all documents that contain the current term and get the specific term object
			Map<Document, Term> termInDocuments = SearchUtils.findDocumentsWithTerm(termToMove);
			termInDocuments.forEach((k, v) -> {
				if (termsInDocuments.containsKey(k)) termsInDocuments.get(k).add(v);
				else termsInDocuments.put(k, new LinkedList<Term>(Arrays.asList(v)));
			});
			// get the taxonomy of the term before the term is changed
			Optional<Model> model = SearchUtils.getConainingModel(termToMove);
			// remove the original term from the taxonomy
			EcoreUtil.remove(termToMove);
			// store the term at the new position
			// TODO extract logic
			if (model.isPresent()) {
				Term targetInModel = SearchUtils.findTermInTaxonomy(model.get(), target);
				if (position == TermPosition.SUBTERM) {
					targetInModel.getSubclasses().add(termToMove);
				} else if (position == TermPosition.NEIGHBOR) {
					EObject container = targetInModel.eContainer();
					if (container instanceof Term) {
						((Term) container).getSubclasses().add(termToMove);
					} else if (container instanceof Model) {
						((Model) container).getDimensions().add(termToMove);
					}
				}
				TaxonomyUtils.saveTaxonomy(model.get());
			}						
		}															
		Set<Resource> resourcesToUpdate = new TreeSet<>(
				(Resource r1, Resource r2) -> r1 == r2 ? 0 : 1);
		// update each bibtex entry
		for (Map.Entry<Document, List<Term>> entry : termsInDocuments.entrySet()) {
			Document doc = entry.getKey();			
			for (Term termToMove : entry.getValue()) {
				if (SearchUtils.findTermInDocument(doc, target) == null) {
					// build structure in document										
					Term targetDuplicate = EcoreUtil.copy(target);
					Deque<Term> taxonomyRebuild = new LinkedList<>();
					do {
						taxonomyRebuild.offerFirst(TaxonomyFactory.eINSTANCE.createTerm());
						taxonomyRebuild.peekFirst().setName(targetDuplicate.getName());
						targetDuplicate = targetDuplicate.eContainer() instanceof Term ? (Term) targetDuplicate.eContainer() : null;
					} while (targetDuplicate != null && SearchUtils.findTermInDocument(doc, targetDuplicate) == null);
					Term rebuild = taxonomyRebuild.pollFirst();
					Term last = rebuild;
					while (taxonomyRebuild.peekFirst() != null) {						
						last.getSubclasses().add(taxonomyRebuild.peekFirst());
						last = taxonomyRebuild.pollFirst();						
					}
					if (targetDuplicate == null) {
						// dimension is missing
						doc.getTaxonomy().getDimensions().add(rebuild);
					} else {
						// term is missing
						SearchUtils.findTermInDocument(doc, targetDuplicate).getSubclasses().add(rebuild);
					}										
				}									
				EcoreUtil.remove(termToMove);
				// store the term at the new position
				// TODO extract logic
				Term targetInModel = SearchUtils.findTermInDocument(doc, target);
				if (position == TermPosition.SUBTERM) {
					targetInModel.getSubclasses().add(termToMove);
				} else if (position == TermPosition.NEIGHBOR) {
					EObject container = targetInModel.eContainer();
					if (container instanceof Term) {
						((Term) container).getSubclasses().add(termToMove);
					} else if (container instanceof Model) {
						((Model) container).getDimensions().add(termToMove);
					}
				}				
			}
			resourcesToUpdate.add(entry.getKey().eResource());		
		}
		resourcesToUpdate.forEach(r -> BibtexFileWriter.updateBibtexFile(r));
	}

}
