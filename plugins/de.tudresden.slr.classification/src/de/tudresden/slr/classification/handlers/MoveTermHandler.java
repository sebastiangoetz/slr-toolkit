package de.tudresden.slr.classification.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.classification.Activator;
import de.tudresden.slr.classification.dialog.MoveTermDialog;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.utils.TermUtils;
import de.tudresden.slr.utils.taxonomy.manipulation.TermMover;

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
					TermMover.move(termsToMove, (Term) dialog.getResult()[0], dialog.getTermPosition());
				}
			} else {
				ErrorDialog.openError(null, "Error", null, new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Invalid selection. The selected terms must not share their paths.", null));
			}
		}
		
		return null;
	}
	
	private Model computeAllowedTargets(List<Term> termsToMove) {
		Optional<Model> completeTaxonomyOptional = SearchUtils.getContainingModel(termsToMove.get(0));
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
