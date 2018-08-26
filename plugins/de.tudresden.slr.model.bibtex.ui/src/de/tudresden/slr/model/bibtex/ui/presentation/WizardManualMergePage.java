package de.tudresden.slr.model.bibtex.ui.presentation;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class WizardManualMergePage  extends WizardPage {
	private Composite container;
    private BibtexMergeConflict conflict;

    public WizardManualMergePage(BibtexMergeConflict conflict) {
        super("Step 2: Manual conflict solving");
    	this.conflict = conflict;
        setTitle("Step 2: Manual conflict solving");
    }
    
    @Override
    public void createControl(Composite parent) {
        setDescription("Manual merging");
        container = new Composite(parent, SWT.NONE);
        container.setLayout (new GridLayout(1, false));

		
        // required to avoid an error in the system
        setControl(container);
        setPageComplete(true);

    }
}
