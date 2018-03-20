package de.tudresden.slr.model.mendeley.ui;


import java.io.IOException;
import java.util.List;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;
import de.tudresden.slr.model.mendeley.api.authentication.MendeleyClient;
import de.tudresden.slr.model.mendeley.api.model.*;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;
import de.tudresden.slr.model.mendeley.util.*;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;

public class MSyncWizard extends Wizard {

	protected MSyncWizardPage zero;
	protected MSyncWizardPageOne one;
    protected MSyncWizardPageTwo two;
    protected MSyncWizardPageThree three;
    protected AdapterFactoryEditingDomain editingDomain;
    private MendeleyClient mc;
    private WorkspaceManager wm;
    private boolean preSelected;
    private URI uri;

    public MSyncWizard() {
        super();
        setNeedsProgressMonitor(true);
        mc = MendeleyClient.getInstance();
        wm = WorkspaceManager.getInstance();
        preSelected = false;
    }
    
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
    	if(preSelected) {
    		zero = new MSyncWizardPage(uri);
    	}
    	else {
    		zero = new MSyncWizardPage();
    	}
    	
    	one = new MSyncWizardPageOne();
        two = new MSyncWizardPageTwo();
        three = new MSyncWizardPageThree();
        addPage(zero);
        addPage(one);
        addPage(two);
        addPage(three);
    }

    @Override
    public boolean performFinish() {
    	
		List<SyncItem> syncItems = three.getSyncItems();
		
		for(SyncItem si : syncItems){
    		MendeleyDocument document = one.getFolder_selected().getDocumentById(si.getId());
    		document.updateFields(si.getSelectedFields());
    		mc.updateDocument(document);
    	}


    	List<BibTeXEntry> entries = two.getMissingInMendeley();
    	
    	for(BibTeXEntry entry : entries){
    		MendeleyDocument document = mc.addDocument(entry);
    		mc.addDocumentToFolder(document, one.getFolder_selected().getId());
    	}
    	
		try {
			MendeleyFolder mf = mc.getMendeleyFolder(one.getFolder_selected().getId());
			mf.setName(one.getFolder_selected().getName());
			zero.getResourceSelected().setMendeleyFolder(mf);
		} catch (TokenMgrException | IOException | ParseException e) {
			e.printStackTrace();
		}
    	wm.addWorkspaceBibtexEntry(zero.getResourceSelected());
    	wm.updateWorkspaceBibTexEntry(zero.getResourceSelected());
   
    	IDecoratorManager decoratorManager = PlatformUI.getWorkbench().getDecoratorManager();
		decoratorManager.update("de.tudresden.slr.model.mendeley.decorators.MendeleyOverlayDecorator");   	
		
        return true;
    }
    
    @Override
    public IWizardPage getNextPage(IWizardPage page) {
    	return super.getNextPage(page);
    }   
    
}