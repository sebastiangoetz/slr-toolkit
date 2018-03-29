package de.tudresden.slr.model.mendeley.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.File;
import org.eclipse.e4.core.commands.ExpressionContext;
import org.eclipse.e4.core.internal.contexts.EclipseContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;

/**
 * This Class implements the command handler for removing a connection between a Bib-File and a
 * Mendeley Folder. Will be triggered by clicking the 'Remove Connection' entry in
 * the popup menu that appears after performing a right click on a Bib-File in the Project Explorer.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 * @see org.eclipse.core.commands.AbstractHandler
 * @see org.eclipse.core.commands.IHandler
 */
public class RemoveConnectionHandler extends AbstractHandler {
	
	private final String ID = "de.tudresden.slr.model.mendeley.RemoveConnection";
	
	private WorkspaceManager wm = WorkspaceManager.getInstance();
	
	/**
	 * This IDecoratorManager is needed to update the Project Explorer after a connection has been removed 
	 */
	private IDecoratorManager decoratorManager = PlatformUI.getWorkbench().getDecoratorManager();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
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
							// by setting the MendeleyFolder of a WorkspaceBibTexEntry to null, the connection will be removed
							entry.setMendeleyFolder(null);
							decoratorManager.update("de.tudresden.slr.model.mendeley.decorators.MendeleyOverlayDecorator");
						}
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void setEnabled(Object evaluationContext) {
		/*
		 * set the popup menu entry to enabled if there is a MendeleyFolder for this File
		 * This is done in 4 steps:
		 * 	1.	Check if selection is a bib file
		 * 	2.	Check if there is a WorkspaceBibTexEntry for the URI of this bib file
		 * 	3.	Check if there is a MendeleyFolder assigned to this WorkspaceBibTexEntry
		 * 	4.	Enable command if there is MendeleyFolder
		 */
		
		if(evaluationContext instanceof ExpressionContext) {
			ExpressionContext expressionContext = (ExpressionContext) evaluationContext;
			if(expressionContext.eclipseContext instanceof EclipseContext) {
				EclipseContext eclipseContext = (EclipseContext) expressionContext.eclipseContext;
				Object selection = expressionContext.getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME);
				if(selection instanceof TreeSelection) {
					TreeSelection treeSelection = (TreeSelection) selection;
					if(treeSelection.getFirstElement() instanceof File) {
						File file = (File) treeSelection.getFirstElement();
						if(file.getFileExtension().equals("bib")){
							WorkspaceBibTexEntry entry = wm.getWorkspaceBibTexEntryByUri(file.getLocationURI());
							if(entry != null) {
								if(entry.getMendeleyFolder() == null) {
									setBaseEnabled(false);
								}
								else {
									setBaseEnabled(true);
								}
							}
							else {
								setBaseEnabled(false);
							}
						}
					}
				}
			}
		}
		super.setEnabled(evaluationContext);
	}
}
