package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.utils.taxonomy.manipulation.TermDeleter;

public class DeleteTermHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() > 0) {			
			MessageDialog dialog = new MessageDialog(null,
					"Delete Term",
					null,
					"Are you sure you want to delete terms: " + 
							currentSelection.toList()
								.stream()
								.filter(s -> s instanceof Term)
								.map(s -> ((Term) s).getName())
								.collect(Collectors.joining(", ")),
					MessageDialog.QUESTION,
					new String[]{"Yes", "Cancel"},
					0);
			dialog.setBlockOnOpen(true);
			if (dialog.open() == MessageDialog.OK && dialog.getReturnCode() == MessageDialog.OK) {
				List<Term> termsToDelete = new ArrayList<>(currentSelection.size());
				currentSelection.toList().stream()
						.filter(s -> s instanceof Term)
						.forEach(s -> termsToDelete.add((Term) s));;
				TermDeleter.delete(termsToDelete);
			}
		}
		
		return null;
	}

}
