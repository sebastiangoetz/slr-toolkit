package de.tudresden.slr.wizards.pages;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class NewSlrProjectCreationPage extends WizardPage {

	private String fileName = null;
	private String taxFileName = null;

	private Composite container;
	private String[] extensions = new String[] { "*.bib", "*.taxonomy" };
	protected FileFieldEditor editor;
	protected StringButtonFieldEditor stringEditor;
	protected StringButtonFieldEditor taxStringEditor;

	public NewSlrProjectCreationPage(String pageName, IStructuredSelection selection) {
		super(pageName);
		setTitle("Import Files");
		setDescription("Choose Bibtex and Taxonomy File");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;

		stringEditor = new StringButtonFieldEditor("choose Bibtex",
				"Choose Bibtex",
				container) {

			@Override
			protected String changePressed() {
				File f = new File(getTextControl().getText());
				if (!f.exists()) {
					f = null;
				}
				File d = getFile(f);
				if (d == null) {
					return null;
				}
				setPageComplete(true);
				return d.getAbsolutePath();
			}
		};

		taxStringEditor = new StringButtonFieldEditor("choose Taxonomy",
				"Choose Taxonomy", container) {

			@Override
			protected String changePressed() {
				File f = new File(getTextControl().getText());
				if (!f.exists()) {
					f = null;
				}
				File d = getFile(f);
				if (d == null) {
					return null;
				}
				setPageComplete(true);
				return d.getAbsolutePath();
			}
		};

		stringEditor.setChangeButtonText("Browse");
		taxStringEditor.setChangeButtonText("Browse");
		stringEditor.getTextControl(container).addModifyListener(
				new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						IPath path = new Path(
								NewSlrProjectCreationPage.this.stringEditor
										.getStringValue());
						setFileName(path.lastSegment());
					}

				});

		taxStringEditor.getTextControl(container).addModifyListener(
				new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						IPath path = new Path(
								NewSlrProjectCreationPage.this.taxStringEditor
										.getStringValue());
						setTaxFileName(path.lastSegment());
					}

				});

		setControl(container);
		setPageComplete(false);
	}

	private File getFile(File startingDirectory) {

		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN | SWT.SHEET);
		if (startingDirectory != null) {
			dialog.setFileName(startingDirectory.getPath());
		}
		if (extensions != null) {
			dialog.setFilterExtensions(extensions);
		}
		String file = dialog.open();
		if (file != null) {
			file = file.trim();
			if (file.length() > 0) {
				return new File(file);
			}
		}

		return null;
	}

	public String getFileName() {
		return this.fileName;
	}

	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTaxFileName() {
		return this.taxFileName;
	}

	private void setTaxFileName(String fileName) {
		this.taxFileName = fileName;
	}

}
