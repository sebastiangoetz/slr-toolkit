package de.tudresden.slr.latexexport.wizard;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.latexexport.latexgeneration.documentclasses.SlrLatexTemplate;
import de.tudresden.slr.metainformation.MetainformationActivator;

public class LatexExportWizardPageOne extends WizardPage {
	private Text textboxFilename, textboxMetainformation;
	private Composite container;
	private Combo comboDropDown;

	private boolean selectedMetainformation = false;
	private boolean selectedFile = false;

	public LatexExportWizardPageOne() {
		super("");
		setTitle("LaTex export");
		setDescription("Generate a LaTex skeleton from a SLR project");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		setPageComplete(false);

		layout.numColumns = 2;

		Label labelMetainformationFile = new Label(container, SWT.NONE);
		labelMetainformationFile.setText("Source of metainformation");
		
		textboxMetainformation = new Text(container, SWT.BORDER | SWT.SINGLE);
		textboxMetainformation.setEditable(false);
		textboxMetainformation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (MetainformationActivator.getCurrentFilepath() != null) {
			textboxMetainformation.setText(MetainformationActivator.getCurrentFilepath());
			selectedMetainformation = true;
		}
		
		//selection listener which checks, whether metainformation source or target file are empty.
		//OK-Button can only be pressed, if both are set.
		textboxMetainformation.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (textboxMetainformation.getText() != null && !textboxMetainformation.getText().isEmpty()
						&& selectedFile) {
					setPageComplete(true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText("Target file for LaTex-Document");
		
		//check, whether file already exists, if so, whether it can be overridden
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// if file exists, ask for permission to overwrite, if permission not given -
				// repeat and ask for new path
				boolean canContinueFileCheck = false;
				FileDialog dialog = new FileDialog(container.getShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String[] { "*.tex" });

				while (!canContinueFileCheck) {
					String path = dialog.open();
					if (path != null) {
						// ensure, that file ends with .tex
						if (!path.endsWith(".tex")) {
							path = path + ".tex";
						}
						File file = new File(path);
						if (file.exists()) {
							MessageBox mb = new MessageBox(dialog.getParent(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
							mb.setMessage(file + " already exists. Do you want to replace it?");

							if (mb.open() == SWT.YES) {
								canContinueFileCheck = true;
							} else {
								continue;
							}

						}

						textboxFilename.setText(path);
						canContinueFileCheck = true;

						selectedFile = true;
						if (selectedMetainformation) {
							setPageComplete(true);
						}
					} else {
						// if no file is selected, exit loop
						canContinueFileCheck = true;
					}
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		textboxFilename = new Text(container, SWT.BORDER | SWT.SINGLE);
		textboxFilename.setEditable(false);
		textboxFilename.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label labelTemplate = new Label(container, SWT.NONE);
		labelTemplate.setText("LaTex Template");
		comboDropDown = new Combo(container, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		for(String type : SlrLatexTemplate.documentTypes) {
			comboDropDown.add(type);
		}
		comboDropDown.select(0);
		
		setControl(container);
	}

	public String getFilename() {
		return textboxFilename.getText();
	}
	
	public String getTemplate() {
		return comboDropDown.getText();
	}
}