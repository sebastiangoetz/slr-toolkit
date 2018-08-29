package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;

public class BibtexManualMergeWizard extends Wizard {

    protected BibtexMergeData data;

    public BibtexManualMergeWizard(BibtexMergeData data) {
        super();
        this.data = data;
        setNeedsProgressMonitor(true);
    }

    @Override
    public String getWindowTitle() {
        return "Manually resolve merge conflicts";
    }

    @Override
    public void addPages() {
    	BibtexMergeConflict c;
    	// TODO: replace with actual implementation {
        List<Object> resources = data.getResourceList();
        for(int i = 0; i < resources.size(); i++) {
        	data.addConflict(new BibtexMergeConflict(new String[] {"sadasdasdas", "asdasadasdasd", "sadasdasdas", "asdasadasdasd", "sadasdasdas", "asdasadasdasd", "sadasdasdas", "asdasadasdasd"}, new String[] {"file1", "file2", "file3", "file4", "file5", "file6", "file7", "file8"}));
        }
    	// TODO: } replace with actual implementation
    	int amount = data.getConflicts().size();
    	for(int i = 0; i < amount; i++) {
    		c = data.getConflicts().get(i);
            addPage(new WizardManualMergePage(c, i + 1, amount));
    	}
    }

    @Override
    public boolean performFinish() {
        return true;
    }
    
    @Override
    public boolean needsProgressMonitor() {
        return false;
    }
        
    public boolean canFinish() {
    	return getContainer().getCurrentPage().getNextPage() == null && getContainer().getCurrentPage().isPageComplete();
    }
}
