package de.tudresden.slr.model.mendeley.handlers;


import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import de.tudresden.slr.model.mendeley.api.authentication.MendeleyClient;
import de.tudresden.slr.model.mendeley.ui.MSyncWizard;
import de.tudresden.slr.model.mendeley.util.MutexRule;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class WizardHandler extends AbstractHandler {
	private MendeleyClient mc = MendeleyClient.getInstance();
	private boolean loggedIn;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Display display = Display.getCurrent();
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		UIJob job1 = new UIJob(display, "Validating Mendeley Login") {
            @Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
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
		Job job2 = new Job("Preparing Mendeley Sync Wizard") {
            @Override
			public IStatus run(IProgressMonitor monitor) {
            	SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
            	subMonitor.subTask("Downloading latest Mendeley Folders");
				try {
					if(loggedIn)
						mc.updateMendeleyFolders();
					else
						return Status.CANCEL_STATUS;
				} catch (TokenMgrException | IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
            	return Status.OK_STATUS;
            }
        };
		
        UIJob job3 = new UIJob("Starting Mendeley UI Wizard") {
            @Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
            	if(loggedIn) {
            		WizardDialog wizardDialog = new WizardDialog(window.getShell(),
				            new MSyncWizard());
			        if (wizardDialog.open() == Window.OK) {
			            System.out.println("Ok pressed");
			        } else {
			            System.out.println("Cancel pressed");
			        }
            	}
            	else {
            		return Status.CANCEL_STATUS;
            	}
				
            	return Status.OK_STATUS;
            }
        };
        MutexRule rule = new MutexRule();
        
        job2.setUser(true);
        job1.setUser(true);
        job1.setRule(rule);
        job2.setRule(rule);
        job3.setRule(rule);
        
        job1.schedule();
        job2.schedule();
        job3.schedule();
		return null;
	}
	
}
