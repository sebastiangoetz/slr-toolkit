package de.tudresden.slr.model.mendeley.ui;


import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import de.tudresden.slr.model.mendeley.api.client.MendeleyClient;
import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;
import de.tudresden.slr.model.mendeley.util.SyncItem;

/**
 * This class implements the MSyncWizard which is used to choose a Bib-File
 * and an online Mendeley Folder that should be synchronized
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 * @see org.eclipse.jface.wizard.Wizard
 */
public class MSyncWizard extends Wizard {

	protected MSyncWizardFilePage zero;
	
	protected MSyncWizardFolderPage one;
    
	protected MSyncWizardOverviewPage two;
    
	protected MSyncWizardConflictPage three;
    
	protected AdapterFactoryEditingDomain editingDomain;
    
	private MendeleyClient mc;
    
	private WorkspaceManager wm;
    
	/**
	 * Flag to determin if the context Wizard or the common Wizard will be called
	 */
	private boolean preSelected;
    
	/**
	 * URI is used when a Bib-File is directly chosen before starting the Wizard
	 */
	private URI uri;

	/**
	 * Constructor starts the common MSyncWizard
	 */
    public MSyncWizard() {
        super();
        setNeedsProgressMonitor(true);
        mc = MendeleyClient.getInstance();
        wm = WorkspaceManager.getInstance();
        preSelected = false;
    }
    
    /**
     * Constructor should be used when user directly selects an Bib-File from the Project Explorer
     * 
     * @param uri URI of the Bib-File that you want to synchronize
     */
    public MSyncWizard(URI uri) {
        super();
        setNeedsProgressMonitor(true);
        mc = MendeleyClient.getInstance();
        wm = WorkspaceManager.getInstance();
        preSelected = true;
        this.uri = uri;
    }

    @Override
    public String getWindowTitle() {
        return "Mendeley Synchronisation Wizard";
    }

    @Override
    public void addPages() {
    	//if preSelected use another first page
    	if(preSelected) {
    		zero = new MSyncWizardFilePage(uri);
    	}
    	else {
    		zero = new MSyncWizardFilePage();
    	}
    	
    	one = new MSyncWizardFolderPage();
        two = new MSyncWizardOverviewPage();
        three = new MSyncWizardConflictPage();
        addPage(zero);
        addPage(one);
        addPage(two);
        addPage(three);
    }

    @Override
    public boolean performFinish() {
    	Display.getCurrent().readAndDispatch();
    	
    	// instantiate ProgressBarDialog to show progress of UIJob
    	ProgressBarDialog dialog = new ProgressBarDialog(Display.getCurrent().getActiveShell());
    	
    	UIJob job = new UIJob(Display.getCurrent(), "Perform Finish") {
			
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				List<SyncItem> syncItems = three.getSyncItems();
				List<BibTeXEntry> entries = two.getMissingInMendeley();
				
				int number_of_tasks = syncItems.size() + entries.size() + 2;
				int tasks_finished = 0;
				
				if(dialog != null) {
					dialog.setMaxValue(number_of_tasks);
				}
				
				// Iterate Documents with conflicts
				for(SyncItem si : syncItems){
		    		MendeleyDocument document = one.getFolder_selected().getDocumentById(si.getId());
		    		
		    		if(dialog != null) {
		    			dialog.setValue(tasks_finished);
		    			if(document != null) 
		    				if(document.getTitle() != null) 
		    					dialog.setTaskText("Updating Document \"" +document.getTitle() + "\"");
					}
		    		
		    		//update fields that were selected during conflict management
		    		document.updateFields(si.getSelectedFields());
		    		
		    		//only update documents that had conflicts - identical documents will be left out
		    		if(si.hasConflicts())
		    			mc.updateDocument(document);
		    		
		    		tasks_finished++;
		    	}
				
				// add all documents that are missing in Mendeley
		    	for(BibTeXEntry entry : entries){
		    		MendeleyDocument document = mc.addDocument(entry);
		    		
		    		if(dialog != null) {
		    			dialog.setValue(tasks_finished);
		    			if(document != null) 
		    				if(document.getTitle() != null) 
		    					dialog.setTaskText("Upload Document \"" +document.getTitle() + "\"");
					}
		    		// document can't be directly uploaded into folders
		    		// so you have to move them from all documents to a specific folder
		    		mc.addDocumentToFolder(document, one.getFolder_selected().getId());
		    		
		    		tasks_finished++;
		    	}
		    			    	
				try {
					if(dialog != null) {
		    			dialog.setValue(tasks_finished);
		    			dialog.setTaskText("Download Updated Mendeley Folder");
					}
					// get Mendeley Folder with updated content
					// this is necessary to get get documents that are missing in Bib-File
					MendeleyFolder mf = mc.getMendeleyFolder(one.getFolder_selected().getId());
					mf.setName(one.getFolder_selected().getName());
					zero.getResourceSelected().setMendeleyFolder(mf);
					
					tasks_finished++;
		    		dialog.setValue(tasks_finished);
		    		
				} catch (TokenMgrException | IOException | ParseException e) {
					e.printStackTrace();
				}
				
				if(dialog != null) {
	    			dialog.setValue(tasks_finished);
	    			dialog.setTaskText("Write Content to Bib-File ...");
				}
				
				// add WorkspaceBibTexEntry of bib-file
		    	wm.addWorkspaceBibtexEntry(zero.getResourceSelected());
		    	// update Bib-File with latest content of updated Folder
		    	wm.updateWorkspaceBibTexEntry(zero.getResourceSelected());
		    	
		    	tasks_finished++;
		    	if(dialog != null)
		    		dialog.setValue(tasks_finished);
				
		    	if(dialog != null)
		    		dialog.close();
				
				return Status.OK_STATUS;
			}
	
		};
		job.setUser(true);
		// add short delay in order to build the dialog first before the job starts
		job.schedule(500);
		dialog.open();   	
		
		IDecoratorManager decoratorManager = PlatformUI.getWorkbench().getDecoratorManager();
		decoratorManager.update("de.tudresden.slr.model.mendeley.decorators.MendeleyOverlayDecorator");
		
        return true;
    }
    
    @Override
    public IWizardPage getNextPage(IWizardPage page) {
    	return super.getNextPage(page);
    }   
    
    @Override
    public boolean performCancel() {
		// if wizard is cancelled connection between the selected Bib-file and its Folder will be lost
    	if(zero.getResourceSelected()!=null) {
			WorkspaceBibTexEntry entry = wm.getWorkspaceBibTexEntryByUri(zero.getResourceSelected().getUri());
			entry.setMendeleyFolder(null);
			IDecoratorManager decoratorManager = PlatformUI.getWorkbench().getDecoratorManager();
			decoratorManager.update("de.tudresden.slr.model.mendeley.decorators.MendeleyOverlayDecorator");
		}
    	return super.performCancel();
    }
    
}