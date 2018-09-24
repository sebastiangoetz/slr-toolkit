package de.tudresden.slr.latexexport.helpers;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ExportProjectChooser extends TitleAreaDialog {

	private int chosenProject;
	private Combo comboDropDown;
	private List<IProject> openProjects;

	/**
	 * Constructor
	 * @param parentShell Active shell
	 * @param openProjects List of the workspace's projects
	 */
	public ExportProjectChooser(Shell parentShell, List<IProject> openProjects) {
		super(parentShell);
		this.openProjects = openProjects;
	}

	
	@Override
	public void create() {
		super.create();
		setTitle("Export Project");
		setMessage("Choose which project is to be exported. The selected project's taxonomy and document entries will be loaded. Please cancel, if you want to avoid that.", IMessageProvider.INFORMATION);
	
	}

	/**
	 * Generate drop down menu for selecting the active project
	 */
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
		labelName.setText("Select one of the open projects ");
		comboDropDown = new Combo(container, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);

		for(IProject p : openProjects) {
			comboDropDown.add(p.getName());
		}
		
		//ensure, that a project is selected
		comboDropDown.select(0);
	
		return control;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * Save, which project is chosen. Otherwise, information would be lost.
	 */
	private void saveInput() {
		this.chosenProject = comboDropDown.getSelectionIndex();

	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public int getChosenProject() {
		return chosenProject;
	}
}