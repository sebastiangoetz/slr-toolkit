package de.tudresden.slr.model.mendeley.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;

import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;

public class SyncHandler  extends AbstractHandler{
	WorkspaceManager wm = WorkspaceManager.getInstance();
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Job job = new Job("Updating") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
            	SubMonitor subMonitor = SubMonitor.convert(monitor, 2);
            	subMonitor.setTaskName("Synchronizing Files linked to Mendeley");
            	wm.updateSyncFolders();
               
            	return Status.OK_STATUS;
            }
            

        };
        job.setUser(true);
        job.schedule();
		
		
		return null;
	}

}
