package de.tudresden.slr.wizards;

import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import de.tudresden.slr.wizards.pages.NewSlrProjectCreationPage;
import de.tudresden.slr.wizards.projects.SlrProjectSupport;

public class NewSlrProjectWizard extends Wizard implements INewWizard,
		IExecutableExtension {

	private static final String WIZARD_NAME = "New SLR Project"; //$NON-NLS-1$
	private static final String PAGE_NAME = "SLR Project Wizard"; //$NON-NLS-1$
	private static final String PAGE2_NAME = "SLR Project Wizard Imports"; //$NON-NLS-1$
	private WizardNewProjectCreationPage _pageOne;
	private NewSlrProjectCreationPage _pageOneOne;
	private IConfigurationElement _configurationElement;

	public NewSlrProjectWizard() {
		setWindowTitle(WIZARD_NAME);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_pageOneOne = new NewSlrProjectCreationPage("PAGE2_NAME", selection); // NON-NLS-1
	}

	@Override
	public void addPages() {
		super.addPages();

		_pageOne = new WizardNewProjectCreationPage(PAGE_NAME);
		_pageOne.setTitle(SlrWizardMessages.NewSlrProjectWizard_SLR_Project);
		_pageOne.setDescription(SlrWizardMessages.NewSlrProjectWizard_Project_Name);

		addPage(_pageOne);
		addPage(_pageOneOne);
	}

	@Override
	public boolean performFinish() {
		String name = _pageOne.getProjectName();
		URI location = null;
		if (!_pageOne.useDefaults()) {
			location = _pageOne.getLocationURI();
		} // else location == null
		String fileName = _pageOneOne.getFileName();
		String taxFileName = _pageOneOne.getTaxFileName();
		String bibContent = _pageOneOne.getBibContent();
		String taxContent = _pageOneOne.getTaxContent();

		SlrProjectSupport.createProject(name, fileName, taxFileName,
				bibContent, taxContent, location);
		BasicNewProjectResourceWizard.updatePerspective(_configurationElement);

		// IFile file = _pageOneOne.createNewFile();
		// if (file == null)
		// return false;
		return true;
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		_configurationElement = config;

	}

}
