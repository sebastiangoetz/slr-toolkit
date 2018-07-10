package de.tudresden.slr.metainformation.editors;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewAuthorDialog extends TitleAreaDialog {

    private Text textboxName;
    private Text textboxEmail;
    private Text textboxAffiliation;


    private String name;
    private String mail;
    private String affiliation;


    public NewAuthorDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    public void create() {
        super.create();
        setTitle("Add author");
        setMessage("At least the new author's name has to be specified.", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite control = (Composite) super.createDialogArea(parent);
        
        Composite container = new Composite(control, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        
        Label labelName = new Label(container, SWT.NONE);
        labelName.setText("Name");
        textboxName = new Text(container, SWT.BORDER);
        textboxName.setLayoutData(gridData);
        
        Label labelMail = new Label(container, SWT.NONE);
        labelMail.setText("Email");
        textboxEmail = new Text(container, SWT.BORDER);
        textboxEmail.setLayoutData(gridData);
        
        Label labelAffiliation = new Label(container, SWT.NONE);
        labelAffiliation.setText("Affiliation");
        textboxAffiliation = new Text(container, SWT.BORDER);
        textboxAffiliation.setLayoutData(gridData);

        return control;
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    private void saveInput() {
        name = textboxName.getText();
        mail = textboxEmail.getText();
        affiliation = textboxAffiliation.getText();

    }

    @Override
    protected void okPressed() {
        saveInput();
        super.okPressed();
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }
    
    public String getAffiliation() {
    	return affiliation;
    }
}