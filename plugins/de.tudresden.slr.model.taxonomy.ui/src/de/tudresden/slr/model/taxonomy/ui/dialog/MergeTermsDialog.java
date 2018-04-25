package de.tudresden.slr.model.taxonomy.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import de.tudresden.slr.model.taxonomy.Term;

public class MergeTermsDialog extends Dialog {
	
	private List targetList;

	private Term targetTerm; 
	
	private java.util.List<Term> selectedTerms;
	
	public MergeTermsDialog(Shell parentShell, java.util.List<Term> selectedTerms) {        
		super(parentShell);
		this.selectedTerms = selectedTerms;
	}
	
	@Override
    protected Control createDialogArea(Composite parent) {
            Composite container = (Composite) super.createDialogArea(parent);
            GridLayout layout = new GridLayout(2, false);
            layout.marginRight = 5;
            layout.marginLeft = 10;
            container.setLayout(layout);                  
            
            Label termPositionLabel = new Label(container, SWT.NONE);
            GridData termPositionGrid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
            termPositionGrid.horizontalIndent = 1;
            termPositionLabel.setLayoutData(termPositionGrid);
            termPositionLabel.setText("Merge Terms Into: ");
            this.targetList = new List(container, SWT.SINGLE);
            GridData positionListGrid = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
            positionListGrid.horizontalAlignment = 1;
            this.targetList.setLayoutData(positionListGrid);
            this.selectedTerms.forEach(t -> this.targetList.add(t.getName()));
            
            return container;
    }
	
	private void saveInput() {
		this.targetTerm = this.selectedTerms.get(this.targetList.getSelectionIndex());
}
	
	@Override
    protected void okPressed() {
            saveInput();
            super.okPressed();
    }
	
	public Term getTargetTerm() {
		return this.targetTerm;
	}
}
