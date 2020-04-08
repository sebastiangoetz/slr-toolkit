package de.tudresden.slr.wizards;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.wizards.pages.WizardSetupBibtexPage;
import de.tudresden.slr.wizards.pages.WizardSetupMetainformationPage;
import de.tudresden.slr.wizards.pages.WizardSetupPage;
import de.tudresden.slr.wizards.pages.WizardSetupTaxonomyPage;
import de.tudresden.slr.wizards.projects.SlrProjectSupport;

public class NewSlrProjectWizard extends Wizard implements INewWizard {

	protected static final String BIBTEX_RESOURCE = "platform:/plugin/de.tudresden.slr.wizards/resources/my_bibtex.bib";
	protected static final String TAXONOMY_RESOURCE = "platform:/plugin/de.tudresden.slr.wizards/resources/my_taxonomy.taxonomy";
	protected static final String METAINFORMATION_RESOURCE = "platform:/plugin/de.tudresden.slr.wizards/resources/my_metainformation.slrproject";

	
	protected WizardNewProjectCreationPage firstPage;
	protected WizardSetupBibtexPage secondPage;
	protected WizardSetupTaxonomyPage thirdPage;
	protected WizardSetupMetainformationPage fourthPage;
	
	IProject project;
	
	protected AtomicInteger filesLoaded;
	protected final int filesToLoad;

	public NewSlrProjectWizard() {
		filesLoaded = new AtomicInteger(0);
		filesToLoad = 3;
		setWindowTitle("New SLR Project");
		firstPage = new WizardNewProjectCreationPage("SLR Project Wizard");
		firstPage.setTitle("Create a new SLR Project");
		firstPage.setDescription("Please enter the project name.");

		secondPage = new WizardSetupBibtexPage("Setup bibtex");
		thirdPage = new WizardSetupTaxonomyPage("Setup taxonomy");
		fourthPage = new WizardSetupMetainformationPage("Setup metainformation");
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
		addPage(fourthPage);
	}

	@Override
	public boolean performFinish() {
		// create the project set up in the first wizard page
		
		try {
			createSlrProject();
		} catch(CoreException e) {
			return false;
		}
		
		return true;
	}
	
	
	protected IProject createSlrProject() throws CoreException {
		project = firstPage.getProjectHandle();
		project.create(null);
		project.open(null);
		createResourceFile(project, secondPage, BIBTEX_RESOURCE);
		createResourceFile(project, thirdPage, TAXONOMY_RESOURCE);
		createResourceFile(project, fourthPage, METAINFORMATION_RESOURCE);
		SlrProjectSupport.addNature(project);
		return project;

	}
	
	protected synchronized void onFileCreated() {
		if(filesLoaded.incrementAndGet() == filesToLoad) onAllFilesLoaded();
	}
	
	protected void onAllFilesLoaded() {
		try {
			ModelRegistryPlugin.getModelRegistry().getResourceManager().setActiveProject(project);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a project file
	 * @param project The project
	 * @param wizardPage The wizard page that provides the file to be imported/created
	 * @param defaultResource Platform URL (platform:/) pointing to default file
	 */
	private void createResourceFile(IProject project, WizardSetupPage wizardPage, String defaultResource){
		try {
			if (wizardPage.hasFileImported()) {	
				File existingFile = new File(wizardPage.getFilePath());
				URL existingFileUrl = existingFile.toURI().toURL();
				String newFileName = existingFile.getName();
				createFile(project, existingFileUrl, newFileName);
			} else {
				URL defaultResourceUrl = new URL(defaultResource);
				createFile(project, defaultResourceUrl, wizardPage.getFilePath());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void createFile(IProject project, URL sourceFile, String newFileName){
		try (InputStream fileStream = sourceFile.openConnection().getInputStream()) {
			IFile taxonomyFile = project.getFile(newFileName);
			taxonomyFile.create(fileStream, false, new NullProgressMonitor() {
				@Override
				public void done() {
					onFileCreated();
				}
			});
		} catch (IOException | CoreException e) {
			e.printStackTrace();
		}
	}
}
