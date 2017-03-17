package de.tudresden.slr.model.taxonomy.ui.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
            
            new Label(container, SWT.NONE).setText("Default Term: ");
            this.defaultTermNameText = new Text(container, SWT.BORDER);
            this.defaultTermNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            this.defaultTermNameText.setText("");
            /*this.defaultTermNameText.addModifyListener((e) -> {
            	defaultTermName = ((Text) e.getSource()).getText();
            }); */         
            
            new Label(container, SWT.NONE).setText("Further Terms (comma-seperated): ");
            this.furtherTermNamesText = new Text(container, SWT.BORDER | SWT.V_SCROLL);
            /*this.furtherTermNamesText.addModifyListener((e) -> {
            	furtherTermNames = ((Text) e.getSource()).getText();
            });  */                  
            
            return container;
    }
	
	private void saveInput() {
		this.defaultTermName = this.defaultTermNameText.getText();
		this.furtherTermNames = new ArrayList<>();
		Stream.of(this.furtherTermNamesText.getText().split(",")).forEach(n -> furtherTermNames.add(n.trim()));
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
