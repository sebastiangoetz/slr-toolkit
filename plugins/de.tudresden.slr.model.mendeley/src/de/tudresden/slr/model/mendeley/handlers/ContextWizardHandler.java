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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import de.tudresden.slr.model.mendeley.api.client.MendeleyClient;
import de.tudresden.slr.model.mendeley.ui.MSyncWizard;
import de.tudresden.slr.model.mendeley.util.MutexRule;

/**
 * This Class implements the command handler for starting the Mendeley Synchronization Wizard
 * with a specific context. Will be triggered by clicking the 'Mendeley Sync Wizard' entry in
 * the popup menu that appears after performing a right click on a Bib-File in the Project Explorer.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 * @see org.eclipse.core.commands.AbstractHandler
 * @see org.eclipse.core.commands.IHandler
 */
public class ContextWizardHandler extends AbstractHandler {

	private MendeleyClient mc = MendeleyClient.getInstance();
	
	/**
	 * A flag that returns if the user is logged into his Mendeley Profile
	 */
	private boolean loggedIn;
	
	/**
	 * A flag that returns if the latest state of your Mendeley Folder have been downloaded
	 */
	private boolean isReady;
	
	private final String ID = "de.tudresden.slr.model.mendeley.startContextWizardCommand";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		loggedIn = false;
		isReady = false;
		
		/*
		 * command is only available if entry in Project explorer is a .bib file
		 */
		Display display = Display.getCurrent();
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
                .getActivePage().getSelection();
		if(selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			if(treeSelection.getFirstElement() instanceof File) {
				File file = (File) treeSelection.getFirstElement();
				if(file.getFileExtension().equals("bib")){
					/*
					 * Starting the wizard is separated in 3 jobs
					 * 	1.	Checking if the current login is valid
					 * 		-	if that's not the case a login screen will be displayed
					 * 	2.	Downloading the latest state of your Mendeley Folders
					 * 	3.	Executing the Wizard
					 */
					UIJob job1 = new UIJob(display, "Validating Mendeley Login") {
			            @Override
						public IStatus runInUIThread(IProgressMonitor monitor) {
			            	//checks if somebody is logged in
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
			            	subMonitor.setTaskName("Downloading latest Mendeley Folders");
							try {
								// only executes if previous job was successful
								if(loggedIn) {
									if(!monitor.isCanceled()) {
										mc.updateMendeleyFolders(monitor);
										isReady = true;
									}
								}
								else
									return Status.CANCEL_STATUS;
							} catch (TokenMgrException | IOException | ParseException e) {
								e.printStackTrace();
							}
							
			            	return Status.OK_STATUS;
			            }
			        };
					
			        UIJob job3 = new UIJob("Starting Mendeley UI Wizard") {
			            @Override
						public IStatus runInUIThread(IProgressMonitor monitor) {
			            	// only executes if both previous job were successful
			            	if(loggedIn & isReady) {
			            		WizardDialog wizardDialog = new WizardDialog(window.getShell(),
							            new MSyncWizard(file.getLocationURI()));
//						        if (wizardDialog.open() == Window.OK) {
//						            
//						        } else {
//						           
//						        }
			            	}
			            	else {
			            		return Status.CANCEL_STATUS;
			            	}
			            	return Status.OK_STATUS;
			            }
			        };
			        // a rule is instantiated to oppress parallel execution of jobs
			        MutexRule rule = new MutexRule();
			        
			        job2.setUser(true);
			        job1.setUser(true);
			        job1.setRule(rule);
			        job2.setRule(rule);
			        job3.setRule(rule);
			        
			        job1.schedule();
			        job2.schedule();
			        job3.schedule();
				}
			}
		}
		return null;
	}



}
