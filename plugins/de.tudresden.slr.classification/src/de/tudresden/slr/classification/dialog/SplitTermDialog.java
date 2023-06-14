package de.tudresden.slr.classification.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SplitTermDialog extends Dialog {
	
	private Text defaultTermNameText; 
	
	private Text furtherTermNamesText;	

	private String defaultTermName = ""; 
	
	private List<String> furtherTermNames;
	
	private String selectedTermName;
	
	public SplitTermDialog(Shell parentShell, String selectedTermName) {        
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
            
            new Label(container, SWT.NONE).setText("Split Term: ");
            new Label(container, SWT.NONE).setText(this.selectedTermName);
            
            new Label(container, SWT.NONE).setText("Default Term*: ");
            this.defaultTermNameText = new Text(container, SWT.BORDER);
            this.defaultTermNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            this.defaultTermNameText.setText("");
            this.defaultTermNameText.addModifyListener((e) -> {
            	if (((Text) e.getSource()).getText() == null || ((Text) e.getSource()).getText().isEmpty()) {
            		getButton(IDialogConstants.OK_ID).setEnabled(false);
            	} else {
            		getButton(IDialogConstants.OK_ID).setEnabled(true);
            	}
            }); 
            
            new Label(container, SWT.NONE).setText("Further Terms (comma-seperated): ");
            this.furtherTermNamesText = new Text(container, SWT.BORDER | SWT.V_SCROLL);                		
            return container;
    }
	
	private void saveInput() {
		this.defaultTermName = this.defaultTermNameText.getText();
		this.furtherTermNames = new ArrayList<>();
		Stream.of(this.furtherTermNamesText.getText().split(",")).forEach(n -> furtherTermNames.add(n.trim()));
	}
	
	@Override
	protected Button createButton(Composite parent,
            int id,
            String label,
            boolean defaultButton) {
		Button button = super.createButton(parent, id, label, defaultButton);
		if (id == IDialogConstants.OK_ID) {
			button.setEnabled(false);
		}
		return button;
	}
	
	@Override
    protected void okPressed() {
            saveInput();
            super.okPressed();
    }
	
	public String getDefaultTermName() {
		return this.defaultTermName;
	}
	
	public List<String> getFurtherTermNames() {
		return this.furtherTermNames;
	}
}
