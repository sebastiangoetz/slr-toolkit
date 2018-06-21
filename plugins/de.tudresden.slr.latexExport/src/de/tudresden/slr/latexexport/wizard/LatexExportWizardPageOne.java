package de.tudresden.slr.latexexport.wizard;

import java.io.File;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.latexexport.data.DataProvider;
import de.tudresden.slr.model.bibtex.Document;

public class LatexExportWizardPageOne extends WizardPage {
	private Text filename;
	private Composite container;
	
	private boolean includeTitle;
	private boolean includeAbstract;
	private boolean includeAuthors;
	private boolean includeKeywords;
	private boolean includeStatistics;
	private boolean includeTaxonomyDescription;
	

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
		//Label label1 = new Label(container, SWT.NONE);

		//
		filename = new Text(container, SWT.BORDER | SWT.SINGLE);
		filename.setEditable(false);
		filename.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button button = new Button(container, SWT.PUSH);
		button.setText("Path");
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				

				//if file exists, ask for permission to overwrite, if permission not given - repeat and ask for new path
				boolean canContinueFileCheck = false;
				FileDialog dialog = new FileDialog(container.getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.tex" });
				
				while (!canContinueFileCheck) {
					String path = dialog.open();
					if (path != null) {
						// ensure, that file ends with .tex
						if (!path.endsWith(".tex")) {
							path = path + ".tex";
						}
						// TODO workflow
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

						filename.setText(path);
						canContinueFileCheck = true;
						setPageComplete(true);
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

		Label labelTitle = new Label(container, SWT.NONE);
		labelTitle.setText("Include Title");
		Button checkTitle = new Button(container, SWT.CHECK);
		checkTitle.setSelection(true);

		Label labelAbstract = new Label(container, SWT.NONE);
		labelAbstract.setText("Include Abstract");
		Button checkAbstract = new Button(container, SWT.CHECK);
		checkAbstract.setSelection(true);

		Label labelAuthors = new Label(container, SWT.NONE);
		labelAuthors.setText("Include Authors");
		Button checkAuthors = new Button(container, SWT.CHECK);
		checkAuthors.setSelection(true);

		Label labelKeywords = new Label(container, SWT.NONE);
		labelKeywords.setText("Inlude Keywords");
		Button checkKeywords = new Button(container, SWT.CHECK);
		checkKeywords.setSelection(true);

		Label labelStatistics = new Label(container, SWT.NONE);
		labelStatistics.setText("Include Statistics");
		Button checkStatistics = new Button(container, SWT.CHECK);
		checkStatistics.setSelection(true);

		Label labelTaxonomy = new Label(container, SWT.NONE);
		labelTaxonomy.setText("Include Taxonomy Description");
		Button checkTaxonomy = new Button(container, SWT.CHECK);
		checkTaxonomy.setSelection(true);

		// required to avoid an error in the system
		setControl(container);
		
		
  		includeTitle = checkTitle.getSelection();
  		includeAbstract = checkAbstract.getSelection();
  		includeAuthors = checkAuthors.getSelection();
  		includeKeywords = checkKeywords.getSelection();
  		includeStatistics = checkStatistics.getSelection();
  		includeTaxonomyDescription = checkTaxonomy.getSelection();
	}
	
	public boolean getIncludeTitle() {
		return includeTitle;
	}
	
	public boolean getIncludeAbstract() {
		return includeAbstract;
	}
	
	public boolean getIncludeAuthors() {
		return includeAuthors;
	}
	
	public boolean getIncludeKeywords() {
		return includeKeywords;
	}
	
	public boolean getIncludeStatistics() {
		return includeStatistics;
	}
	
	public boolean getIncludeTaxonomyDescription() {
		return includeTaxonomyDescription;
	}

	public String getFilename() {
		return filename.getText();
	}
}