package de.tudresden.slr.classification.ui;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class MalformedTermNameDialog extends InputDialog {
	
	Button useDefaultButton;
	boolean useDefault;
	
	public MalformedTermNameDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue, IInputValidator validator) {
		super(parentShell,dialogTitle,dialogMessage,initialValue,validator);
		useDefault = false;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		useDefaultButton = new Button(composite.getShell(), SWT.CHECK);
		useDefaultButton.setText("Do not show this again and use default");
		SelectionListener selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useDefault = useDefaultButton.getSelection();
			}
		};
		useDefaultButton.addSelectionListener(selectionListener);
		return composite;
	}
	
	public boolean getUseDefault() {
		return useDefault;
	}
}
