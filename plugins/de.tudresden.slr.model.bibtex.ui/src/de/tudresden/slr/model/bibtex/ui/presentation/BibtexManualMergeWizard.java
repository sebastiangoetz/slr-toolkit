package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;

public class BibtexManualMergeWizard extends Wizard {

    protected BibtexMergeData data;
    private String results;

    public BibtexManualMergeWizard(BibtexMergeData data) {
        super();
        this.data = data;
        this.results = "";
        setNeedsProgressMonitor(false);
    }

    @Override
    public String getWindowTitle() {
        return "Manually resolve merge conflicts";
    }

    @Override
    public void addPages() {
    	List<BibtexMergeConflict> conflicts = data.getConflicts();
    	int amount = data.getConflicts().size();
    	for(int i = 0; i < amount; i++) {
            addPage(new WizardManualMergePage(conflicts.get(i), i + 1, amount));
    	}
    }

    @Override
    public boolean performFinish() {
    	for(Object o : getPages()) {
    		results += ((WizardManualMergePage) o).getResult();
			if(results.endsWith("}")) {
				results += System.lineSeparator() + System.lineSeparator();
			}
    	}
        return true;
    }
    
    public boolean canFinish() {
    	return getContainer().getCurrentPage().getNextPage() == null && getContainer().getCurrentPage().isPageComplete();
    }
    
    public String getResults(){
    	return results;
    }
}
