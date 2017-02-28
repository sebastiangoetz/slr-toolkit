package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexFileWriter;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.ui.dialog.MoveTermDialog;
import de.tudresden.slr.model.taxonomy.ui.dialog.MoveTermDialog.TermPosition;
import de.tudresden.slr.model.taxonomy.util.TermUtils;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.model.utils.TaxonomyUtils;

public class MoveTermHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() > 0 ) {
			List<Term> termsToMove = new ArrayList<>(currentSelection.size());
			currentSelection.toList().stream().filter(s -> s instanceof Term).forEach(s -> termsToMove.add((Term) s));
			if (selectionValid(termsToMove)) {
				Model allowedTargets = computeAllowedTargets(termsToMove);				
				MoveTermDialog dialog = new MoveTermDialog(
						null, 
						termsToMove.stream().map(t -> t.getName()).collect(Collectors.toList()), 
						allowedTargets);
				dialog.setBlockOnOpen(true);
				if (dialog.open() == MessageDialog.OK && dialog.getReturnCode() == MessageDialog.OK) {
					moveTerm(termsToMove, (Term) dialog.getResult()[0], dialog.getTermPosition());
				}
			} else {
				// TODO warning
			}
		}
		
		return null;
	}
	
	private void moveTerm(List<Term> termsToMove, Term target, TermPosition position) {
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
			for (Term termToMove : entry.getValue()) {
				Term targetInDocument = SearchUtils.findTermInDocument(entry.getKey(), target);
				// remove the original term from the taxonomy
				EcoreUtil.remove(termToMove);
				// store the term at the new position
				// TODO extract logic
				// TODO !!! targetInDocument can be null
				if (position == TermPosition.SUBTERM) {
					targetInDocument.getSubclasses().add(termToMove);
				} else if (position == TermPosition.NEIGHBOR) {
					EObject container = targetInDocument.eContainer();
					if (container instanceof Term) {
						((Term) container).getSubclasses().add(termToMove);
					} else if (container instanceof Model) {
						((Model) container).getDimensions().add(termToMove);
					}
				}
			}
			Model newTaxonomy = EcoreUtil.copy(entry.getKey().getTaxonomy());
			resourcesToUpdate.add(entry.getKey().eResource());		
		}
		resourcesToUpdate.forEach(r -> BibtexFileWriter.updateBibtexFile(r));
	}
	
	private Model computeAllowedTargets(List<Term> termsToMove) {
		Optional<Model> completeTaxonomyOptional = SearchUtils.getConainingModel(termsToMove.get(0));
		if (completeTaxonomyOptional.isPresent()) {
			Model completeTaxonomyCopy = EcoreUtil.copy(completeTaxonomyOptional.get());
			for (Term t : termsToMove) {
				removeTermFromSubterms(t, completeTaxonomyCopy.getDimensions());
			}
			return completeTaxonomyCopy;
		}
		return null;
	}
	
	private boolean selectionValid(List<Term> termsToMove) {
		for (Term t1 : termsToMove) {
			for (Term t2 : termsToMove) {
				if (TermUtils.contains(t1, t2) || TermUtils.contains(t2,  t1)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean removeTermFromSubterms(Term termToRemove, EList<Term> subterms) {
		for (Term t : subterms) {
			if (TermUtils.equals(termToRemove, t)) {
				EcoreUtil.remove(t);
				return true;
			}
		}		
		for (Term t : subterms) {
			if (t.getSubclasses() != null && removeTermFromSubterms(termToRemove, t.getSubclasses())) {
				return true;
			}
		}
		return false;
	}

}
