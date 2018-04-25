package de.tudresden.slr.model.mendeley.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.commands.ExpressionContext;
import org.eclipse.e4.core.internal.contexts.EclipseContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;

import de.tudresden.slr.model.mendeley.api.client.MendeleyClient;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;
import de.tudresden.slr.model.mendeley.util.MutexRule;

/**
 * This Class implements the command handler for Updating a specific Bib-File that is connected to
 * a Mendeley Folder. Will be triggered by clicking the 'Update Folders' entry in
 * the popup menu that appears after performing a right click on a Bib-File in the Project Explorer.
 * This command downloads the latest state of the connected Mendeley Folder and synchronizes
 * its Bib-File content with the Folder content.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 * @see org.eclipse.core.commands.AbstractHandler
 * @see org.eclipse.core.commands.IHandler
 */
public class SyncContextHandler extends AbstractHandler {
	
	private final String ID = "de.tudresden.slr.model.mendeley.SyncFolder";
	
	private WorkspaceManager wm = WorkspaceManager.getInstance();
	
	private MendeleyClient mc = MendeleyClient.getInstance();
	
	/**
	 * A flag that returns if the user is logged into his Mendeley Profile
	 */
	private boolean loggedIn;
	
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
	
	@Override
	protected void setBaseEnabled(boolean state) {
		super.setBaseEnabled(state);
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		loggedIn = false;
		
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
							
							/*
							 * Synchronizing a Folder is separated in 2 jobs
							 * 	1.	Checking if the current login is valid
							 * 		-	if that's not the case a login screen will be displayed
							 * 	2.	Executing an updateSyncFolder method via the WorkspaceManager
							 */
							UIJob job1 = new UIJob("Validating Mendeley Login") {
					            @Override
								public IStatus runInUIThread(IProgressMonitor monitor) {
					            	SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
					            	subMonitor.subTask("Checking if Login is available");
					            	if(mc.refreshTokenIfNecessary()) {
					            		loggedIn = true;
					            		
					            	}
					            	else {
					            		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					            		MessageDialog.openError(activeShell, "Error", "Login Failed. Wizard will be closed.");
					            		return Status.CANCEL_STATUS;
					            	}
					            	
					            	return Status.OK_STATUS;
					            }
					        };
							Job job2 = new Job("Updating") {
					            @Override
					            protected IStatus run(IProgressMonitor monitor) {
					            	SubMonitor subMonitor = SubMonitor.convert(monitor, 2);
					            	subMonitor.setTaskName("Synchronizing " + file.toString() + " with MendeleyFolder: " + entry.getMendeleyFolder().getName());
					            	if(loggedIn)
					            		wm.updateSyncFolder(entry, monitor);
					            	else
					            		return Status.CANCEL_STATUS;
					            	
					            	return Status.OK_STATUS;
					            }
					            

					        };
					        // a rule is instantiated to oppress parallel execution of jobs
					        MutexRule rule = new MutexRule();
					        job1.setRule(rule);
					        job1.setUser(true);
					        job2.setRule(rule);
					        job2.setUser(true);
					        job1.schedule();
					        job2.schedule();
						}
					}
				}
			}
		}
		return null;
	}

}
