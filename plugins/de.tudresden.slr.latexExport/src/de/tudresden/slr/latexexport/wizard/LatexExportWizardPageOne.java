package de.tudresden.slr.latexexport.wizard;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
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
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;

import de.tudresden.slr.latexexport.helpers.LatexResourceLoader;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.SlrLatexTemplate;
import de.tudresden.slr.metainformation.MetainformationActivator;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.bibtex.Document;

public class LatexExportWizardPageOne extends WizardPage {
	private Text textboxFilename, textboxMetainformation;
	private Composite container;
	private Combo comboDropDown;

//	private boolean includeTitle;
//	private boolean includeAbstract;
//	private boolean includeAuthors;
//	private boolean includeKeywords;
//	private boolean includeStatistics;
//	private boolean includeTaxonomyDescription;

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

		Button buttonMetainformation = new Button(container, SWT.NONE);
		buttonMetainformation.setText("Source of metainformation");
		textboxMetainformation = new Text(container, SWT.BORDER | SWT.SINGLE);
		textboxMetainformation.setEditable(false);
		textboxMetainformation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (MetainformationActivator.getCurrentFilepath() != null) {
			textboxMetainformation.setText(MetainformationActivator.getCurrentFilepath());
			selectedMetainformation = true;
		}
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
		
//		Label labelTitle = new Label(container, SWT.NONE);
//		labelTitle.setText("Include Title");
//		Button checkTitle = new Button(container, SWT.CHECK);
//		checkTitle.setSelection(true);
//
//		Label labelAbstract = new Label(container, SWT.NONE);
//		labelAbstract.setText("Include Abstract");
//		Button checkAbstract = new Button(container, SWT.CHECK);
//		checkAbstract.setSelection(true);
//
//		Label labelAuthors = new Label(container, SWT.NONE);
//		labelAuthors.setText("Include Authors");
//		Button checkAuthors = new Button(container, SWT.CHECK);
//		checkAuthors.setSelection(true);
//
//		Label labelKeywords = new Label(container, SWT.NONE);
//		labelKeywords.setText("Inlude Keywords");
//		Button checkKeywords = new Button(container, SWT.CHECK);
//		checkKeywords.setSelection(true);
//
//		Label labelStatistics = new Label(container, SWT.NONE);
//		labelStatistics.setText("Include Statistics");
//		Button checkStatistics = new Button(container, SWT.CHECK);
//		checkStatistics.setSelection(true);
//
//		Label labelTaxonomy = new Label(container, SWT.NONE);
//		labelTaxonomy.setText("Include Taxonomy Description");
//		Button checkTaxonomy = new Button(container, SWT.CHECK);
//		checkTaxonomy.setSelection(true);

		// required to avoid an error in the system
		setControl(container);

//		includeTitle = checkTitle.getSelection();
//		includeAbstract = checkAbstract.getSelection();
//		includeAuthors = checkAuthors.getSelection();
//		includeKeywords = checkKeywords.getSelection();
//		includeStatistics = checkStatistics.getSelection();
//		includeTaxonomyDescription = checkTaxonomy.getSelection();
	}

//	public boolean getIncludeTitle() {
//		return includeTitle;
//	}
//
//	public boolean getIncludeAbstract() {
//		return includeAbstract;
//	}
//
//	public boolean getIncludeAuthors() {
//		return includeAuthors;
//	}
//
//	public boolean getIncludeKeywords() {
//		return includeKeywords;
//	}
//
//	public boolean getIncludeStatistics() {
//		return includeStatistics;
//	}
//
//	public boolean getIncludeTaxonomyDescription() {
//		return includeTaxonomyDescription;
//	}

	public String getFilename() {
		return textboxFilename.getText();
	}
	
	public String getTemplate() {
		return comboDropDown.getText();
	}

	// public static IProject getCurrentProject() {
	// ISelectionService selectionService =
	// Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
	//
	// ISelection selection = selectionService.getSelection();
	//
	// IProject project = null;
	// if (selection instanceof IStructuredSelection) {
	// Object element = ((IStructuredSelection) selection).getFirstElement();
	//
	// if (element instanceof IResource) {
	// project = ((IResource) element).getProject();
	// }
	// }
	// return project;
	//
	// }
}