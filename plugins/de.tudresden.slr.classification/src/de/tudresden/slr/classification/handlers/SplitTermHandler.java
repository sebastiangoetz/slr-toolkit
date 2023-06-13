package de.tudresden.slr.classification.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.classification.Activator;
import de.tudresden.slr.classification.dialog.SplitTermDialog;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.utils.taxonomy.manipulation.TermSplitter;

public class SplitTermHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() == 1) {
			Term termToSplit = (Term) currentSelection.getFirstElement();
			if (selectionValid(termToSplit)) {
				SplitTermDialog dialog = new SplitTermDialog(null, termToSplit.getName());
				if (dialog.open() == MessageDialog.OK && dialog.getReturnCode() == MessageDialog.OK) {
					TermSplitter.split(termToSplit, dialog.getDefaultTermName(), dialog.getFurtherTermNames());
				}
			} else {
				ErrorDialog.openError(null, "Error", null, new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Invalid selection. The selected term must not have children.", null));
			}
		}
		return null;
	}
	
	/**
	 * Checks whether or not the selected term can be split. This is the case if it has no children.
	 * @param termToSplit The term to check.	
	 * @return True if splitting is allowed, false otherwise.
	 */
	private boolean selectionValid(Term termToSplit) {
		return termToSplit != null && termToSplit.getSubclasses().size() == 0; 
	}

}
