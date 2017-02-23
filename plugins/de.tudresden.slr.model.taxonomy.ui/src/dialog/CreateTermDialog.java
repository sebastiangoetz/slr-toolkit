package dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CreateTermDialog extends Dialog {
	
	private Text termNameText; 
	
	private List positionList;

	private String termName = ""; 
	
	private String selectedTermName;
	
	private TermPosition termPosition;
	
	public enum TermPosition {
		SUBTERM,
		NEIGHBOR
	}
	
	public CreateTermDialog(Shell parentShell, String selectedTermName) {        
		super(parentShell);
		this.selectedTermName = selectedTermName;
	}
	
	@Override
    protected Control createDialogArea(Composite parent) {
            Composite container = (Composite) super.createDialogArea(parent);
            GridLayout layout = new GridLayout(2, false);
            layout.marginRight = 5;
            layout.marginLeft = 10;
            container.setLayout(layout);
            
            Label termNameLabel = new Label(container, SWT.NONE);
            termNameLabel.setText("Term Name: ");
            this.termNameText = new Text(container, SWT.BORDER);
            this.termNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            this.termNameText.setText(this.termName);
            this.termNameText.addModifyListener((e) -> {
            	termName = ((Text) e.getSource()).getText();
            });          
            
            Label termPositionLabel = new Label(container, SWT.NONE);
            GridData termPositionGrid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
            termPositionGrid.horizontalIndent = 1;
            termPositionLabel.setLayoutData(termPositionGrid);
            termPositionLabel.setText("Create Term: ");
            this.positionList = new List(container, SWT.SINGLE);
            GridData positionListGrid = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
            positionListGrid.horizontalAlignment = 1;
            this.positionList.setLayoutData(positionListGrid);
            this.positionList.add("As Subterm of: " + this.selectedTermName);
            this.positionList.add("As Neighbor of: " + this.selectedTermName);
            
            return container;
    }
	
	private void saveInput() {
		this.termName = this.termNameText.getText();
		this.termPosition = this.positionList.getSelectionIndex() == 0 ? TermPosition.SUBTERM : TermPosition.NEIGHBOR;
}
	
	@Override
    protected void okPressed() {
            saveInput();
            super.okPressed();
    }
	
	public String getTermName() {
		return this.termName;
	}
	
	public TermPosition getTermPosition() {
		return this.termPosition;
	}
}
