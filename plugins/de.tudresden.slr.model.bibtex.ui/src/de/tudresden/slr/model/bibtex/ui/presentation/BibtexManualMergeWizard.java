package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;

public class BibtexManualMergeWizard extends Wizard {

    protected BibtexMergeData data;

    public BibtexManualMergeWizard(List<Object> resources) {
        super();
        this.data = new BibtexMergeData(resources);
        setNeedsProgressMonitor(true);
    }

    @Override
    public String getWindowTitle() {
        return "Resolve merge conflicts";
    }

    @Override
    public void addPages() {
    	BibtexMergeConflict c;
    	for(Iterator<BibtexMergeConflict> i = data.getConflicts().iterator(); i.hasNext();) {
    		c = i.next();
            addPage(new WizardManualMergePage(c));
    	}
    }

    @Override
    public boolean performFinish() {

        return true;
    }
    
    public boolean canFinish() {
    	return getContainer().getCurrentPage().getNextPage() == null;
    }
}
