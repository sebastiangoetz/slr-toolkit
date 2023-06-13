package de.tudresden.slr.classification.dialog;

import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

import de.tudresden.slr.classification.views.TermContentProvider;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.utils.TermPosition;

public class MoveTermDialog extends ElementTreeSelectionDialog {
		
	private TermPosition termPosition;
	
	private List positionList;	
	
	public MoveTermDialog(Shell parentShell, Collection<String> selectedTerms, Model allowedTargets) {        
		super(parentShell, new TermLabelProvider(), new TermContentProvider());
		this.setTitle("Move Term(s)");
		this.setMessage("Move " + 
            		    (selectedTerms.size() == 1 ? "Term: " : "Terms: ") + 
            		    selectedTerms.stream().collect(Collectors.joining(", ")) + " To:");
		this.setInput(allowedTargets);		
		this.setAllowMultiple(false);
	}
	
	@Override
    protected Control createDialogArea(Composite parent) {
        Composite control = (Composite) super.createDialogArea(parent);
        
        Label termPositionLabel = new Label(control, SWT.NONE);
        GridData termPositionGrid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        termPositionGrid.horizontalIndent = 1;
        termPositionLabel.setLayoutData(termPositionGrid);
        termPositionLabel.setText("Create Term: ");
        this.positionList = new List(control, SWT.SINGLE);
        GridData positionListGrid = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        positionListGrid.horizontalAlignment = 1;
        this.positionList.setLayoutData(positionListGrid);
        this.positionList.add("As Subterm.");
        this.positionList.add("As Neighbor.");
               
        return control;
    }
	
	private void saveInput() {
		this.termPosition = this.positionList.getSelectionIndex() == 0 ? TermPosition.SUBTERM : TermPosition.NEIGHBOR;
}
	
	@Override
    protected void okPressed() {
            saveInput();
            super.okPressed();
    }
	
	public TermPosition getTermPosition() {
		return this.termPosition;
	}
}
