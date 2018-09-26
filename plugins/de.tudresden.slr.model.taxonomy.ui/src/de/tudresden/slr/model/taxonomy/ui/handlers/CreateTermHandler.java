package de.tudresden.slr.model.taxonomy.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.ui.dialog.CreateTermDialog;
import de.tudresden.slr.utils.taxonomy.manipulation.TermCreator;

public class CreateTermHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() == 1) {
			Term term = (Term) currentSelection.getFirstElement();	
			CreateTermDialog dialog = new CreateTermDialog(null, term.getName());
			dialog.setBlockOnOpen(true);
			if (dialog.open() == InputDialog.OK) {
				TermCreator.create(dialog.getTermName(), term, dialog.getTermPosition());							
			}
		}	
		return null;
	}

}
