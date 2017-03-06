package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.ui.dialog.MergeTermsDialog;
import de.tudresden.slr.model.taxonomy.ui.manipulation.TermMerger;
import de.tudresden.slr.model.taxonomy.ui.manipulation.TermMover;
import de.tudresden.slr.model.taxonomy.util.TermUtils;

public class MergeTermsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() > 1) {
			List<Term> termsToMerge = new ArrayList<>(currentSelection.size());
			currentSelection.toList().stream().filter(s -> s instanceof Term).forEach(s -> termsToMerge.add((Term) s));
			if (selectionValid(termsToMerge)) {
				MergeTermsDialog dialog = new MergeTermsDialog(null, termsToMerge);
				dialog.setBlockOnOpen(true);
				if (dialog.open() == MessageDialog.OK && dialog.getReturnCode() == MessageDialog.OK) {
					TermMerger.merge(termsToMerge, dialog.getTargetTerm());
				}
			} else {
				// TODO warning
			}
			
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

}
