package de.tudresden.slr.model.mendeley.handlers;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.internal.resources.File;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.mendeley.ui.MSyncWizard;

import org.eclipse.core.commands.AbstractHandler;

public class ContextWizardHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
                .getActivePage().getSelection();
		if(selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			if(treeSelection.getFirstElement() instanceof File) {
				File file = (File) treeSelection.getFirstElement();
				if(file.getFileExtension().equals("bib")){
					System.out.println(file.getName());
					WizardDialog wizardDialog = new WizardDialog(window.getShell(),
				            new MSyncWizard(file.getLocationURI()));
			        if (wizardDialog.open() == Window.OK) {
			            System.out.println("Ok pressed");
			        } else {
			            System.out.println("Cancel pressed");
			        }
					return null;
				}
			}
		}
		return null;
	}



}
