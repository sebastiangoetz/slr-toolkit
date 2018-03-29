package de.tudresden.slr.model.mendeley.ui;

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

/**
 * This class implements a ProgressBarDialog that is used to
 * show the Progress of a Job to the user.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 * @see org.eclipse.jface.dialogs.Dialog
 */
public class ProgressBarDialog extends Dialog {

	/**
	 * Label that displays percentage of completion
	 */
	private Label completedInfo;
	
	/**
	 * displays progress as a ProgressBar
	 */
	private ProgressBar progressBar;
	
	/**
	 * Label that is used to display current Task
	 */
	private Label taskLabel;
	
	/**
	 * Maximum of tasks
	 */
	private int max;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ProgressBarDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM);
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
        
        progressBar = new ProgressBar(container, SWT.SMOOTH);
        GridData gd_progressBar = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
        gd_progressBar.minimumWidth = 250;
        progressBar.setLayoutData(gd_progressBar);
        progressBar.setBounds(10, 23, 400, 17);
        
        taskLabel = new Label(container, SWT.NONE);
        taskLabel.setAlignment(SWT.CENTER);
        GridData gd_taskLabel = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
        gd_taskLabel.heightHint = 15;
        gd_taskLabel.minimumWidth = 250;
        taskLabel.setLayoutData(gd_taskLabel);
        taskLabel.setSize(250, 40);
        
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
	
	/**
	 * This Method is used to display the current task in the Dialog.
	 * String with a length > 40 will be trimmed.
	 * 
	 * @param text
	 */
	public void setTaskText(String text) {
		if(text != null) {
			if(text.length() > 40) {
				text = text.substring(0, 39) + " ... \"";
				this.taskLabel.setText(text);
			}
			else {
				this.taskLabel.setText(text);
			}
		}
	}
	
	/**
	 * This method sets the Percentage of the current Job
	 * 
	 * @param value Number of tasks that are already worked
	 */
	public void setPercentage(int value) {
		float percentage = ((float)value / (float) max) * 100;
		String input = "Processing Data - " + String.valueOf((int)percentage) + "% Completed";
		completedInfo.setText(input);
	}
	
	/**
	 * 
	 * @param max total number of tasks
	 */
	public void setMaxValue(int max) {
		this.max = max;
		progressBar.setMaximum(max);
	}
	
	/**
	 * 
	 * @param value current number of worked tasks
	 */
	public void setValue(int value) {
		progressBar.setSelection(value);
		setPercentage(value);
	}

}
