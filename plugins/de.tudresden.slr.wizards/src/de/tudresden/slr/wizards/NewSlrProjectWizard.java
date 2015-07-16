package de.tudresden.slr.wizards;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import de.tudresden.slr.wizards.pages.WizardSetupBibtexPage;
import de.tudresden.slr.wizards.pages.WizardSetupTaxonomyPage;
import de.tudresden.slr.wizards.projects.SlrProjectSupport;

public class NewSlrProjectWizard extends Wizard implements INewWizard {

	private static final String BIBTEX_RESOURCE = "platform:/plugin/de.tudresden.slr.wizards/resources/my_bibtex.bib";
	private static final String TAXONOMY_RESOURCE = "platform:/plugin/de.tudresden.slr.wizards/resources/my_taxonomy.taxonomy";

	private WizardNewProjectCreationPage firstPage;
	private WizardSetupBibtexPage secondPage;
	private WizardSetupTaxonomyPage thirdPage;

	public NewSlrProjectWizard() {
		setWindowTitle("New SLR Project");
		firstPage = new WizardNewProjectCreationPage("SLR Project Wizard");
		firstPage.setTitle("Create a new SLR Project");
		firstPage.setDescription("Please enter the project name.");

		secondPage = new WizardSetupBibtexPage("Setup bibtex");
		thirdPage = new WizardSetupTaxonomyPage("Setup taxonomy");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public void addPages() {
		super.addPages();

		addPage(firstPage);
		addPage(secondPage);
		addPage(thirdPage);
	}

	private void copyBibtexFile(IProject project) {
		try {
			if (secondPage.hasFileImported()) {
				String path = secondPage.getFilePath();
				String fileName = path.substring(path
						.lastIndexOf(File.separator) + 1);
				String local = project.getLocation().append(fileName)
						.toOSString();
				copy("file:///" + path, local);
			} else {
				String path = project.getLocation()
						.append(secondPage.getFilePath()).toOSString();
				copy(BIBTEX_RESOURCE, path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void copyTaxonomyFile(IProject project) {
		try {
			if (thirdPage.hasFileImported()) {
				String path = thirdPage.getFilePath();
				String fileName = path.substring(path
						.lastIndexOf(File.separator) + 1);
				String local = project.getLocation().append(fileName)
						.toOSString();
				copy("file:///" + path, local);
			} else {
				String path = project.getLocation()
						.append(thirdPage.getFilePath()).toOSString();
				copy(TAXONOMY_RESOURCE, path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean performFinish() {
		// create the project set up in the first wizard page
		IProject project = firstPage.getProjectHandle();
		try {
			project.create(null);
			project.open(null);
			SlrProjectSupport.addNature(project);
		} catch (CoreException e) {
			return false;
		}

		copyBibtexFile(project);
		copyTaxonomyFile(project);
		return true;
	}

	private static void copy(String from, String to) throws IOException {
		final URL url = new URL(from);
		try (InputStream in = url.openConnection().getInputStream()) {
			Path out = Paths.get(to);
			Files.copy(in, out);
		}
	}
}
