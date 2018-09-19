package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;

public class BibtexMergeWizard extends Wizard {

    protected BibtexMergeData data;
    private String results;

    public BibtexMergeWizard(BibtexMergeData data) {
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
            addPage(new BibtexMergeWizardPage(conflicts.get(i), i + 1, amount));
    	}
    }

    @Override
    public boolean performFinish() {
    	for(Object o : getPages()) {
    		results += ((BibtexMergeWizardPage) o).getResult();
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
