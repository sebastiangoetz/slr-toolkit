package de.tudresden.slr.model.bibtex.ui.presentation;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
 
public class ProgressBarDemo extends Dialog {
	/**
	 * Label that displays percentage of completion
	 */
	private Label completedInfo;
	
	/**
	 * displays progress as a ProgressBar
	 */
	private ProgressBar progressBar;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ProgressBarDemo(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
        completedInfo = new Label(container, SWT.NONE);
        completedInfo.setAlignment(SWT.CENTER);
        GridData gd_completedInfo = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
        gd_completedInfo.minimumWidth = 250;
        completedInfo.setLayoutData(gd_completedInfo);
        completedInfo.setSize(250, 40);
        completedInfo.setText("Processing Data ...");
        new Label(container, SWT.NONE);
        
        progressBar = new ProgressBar(container, SWT.INDETERMINATE);
        GridData gd_progressBar = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
        gd_progressBar.minimumWidth = 250;
        progressBar.setLayoutData(gd_progressBar);
        progressBar.setBounds(10, 23, 400, 17);
        progressBar.setSelection(100);
        
		return container;
	}

	/**
	 * Creates contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// no Buttons needed
		//createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		//createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(300, 150);
	}
}