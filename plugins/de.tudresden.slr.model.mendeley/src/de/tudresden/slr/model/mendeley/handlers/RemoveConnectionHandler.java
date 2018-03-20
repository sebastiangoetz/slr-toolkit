package de.tudresden.slr.model.mendeley.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.File;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;

public class RemoveConnectionHandler extends AbstractHandler {
	WorkspaceManager wm = WorkspaceManager.getInstance();
	IDecoratorManager decoratorManager = PlatformUI.getWorkbench().getDecoratorManager();
	
	
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
					WorkspaceBibTexEntry entry = wm.getWorkspaceBibTexEntryByUri(file.getLocationURI());
					if(entry != null) {
						if(entry.getMendeleyFolder() != null) {
							entry.setMendeleyFolder(null);
							decoratorManager.update("de.tudresden.slr.model.mendeley.decorators.MendeleyOverlayDecorator");
						}
					}
				}
			}
		}
		return null;
	}
}
