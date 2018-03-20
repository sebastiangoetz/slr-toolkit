package de.tudresden.slr.model.mendeley.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;
import de.tudresden.slr.model.mendeley.ui.MSyncWizard;

public class SyncContextHandler extends AbstractHandler {
	WorkspaceManager wm = WorkspaceManager.getInstance();
	
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
							
							Job job = new Job("Updating") {
					            @Override
					            protected IStatus run(IProgressMonitor monitor) {
					            	SubMonitor subMonitor = SubMonitor.convert(monitor, 2);
					            	subMonitor.setTaskName("Synchronizing " + file.toString() + " with MendeleyFolder: " + entry.getMendeleyFolder().getName());
					            	wm.updateSyncFolder(entry);
					               
					            	return Status.OK_STATUS;
					            }
					            

					        };
					        job.setUser(true);
					        job.schedule();
						}
					}
				}
			}
		}
		return null;
	}

}
