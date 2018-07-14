package de.tudresden.slr.latexexport.handlers;

import de.tudresden.slr.latexexport.helpers.ExportProjectChooser;
import de.tudresden.slr.latexexport.wizard.LatexExportWizard;
import de.tudresden.slr.metainformation.MetainformationActivator;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.MetainformationUtil;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.swt.widgets.Shell;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class exportHandler extends AbstractHandler {
	
	/**
	 * Constant, which signals that the selection event was cancelled
	 */
	private final int SELECTIONEVENT_CANCEL = -1;
	
	//AdapterFactory and EditingDomain for loading of resources, copied and adapted from de.tudresden.slr.model.bibtex.ui.presentation.BibtexEntryView
	protected AdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	
	/**
	 * Sets bibtex documents, taxonomy and metainformation from input argument's project as active elements in ModelRegistry and Metainformation plugin activator
	 * @param project project whose workspace is searched for resources whose file extensions are checked. Matching .taxonomy, .bib and .slrproject-files are loaded in the respective plugins or ModelRegistry
	 */
	private void setProjectForExport(IProject project) {
		IResource[] resources = null;
		try {
			resources = project.members();
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}
		for (IResource res : resources) {
			if (res.getType() == IResource.FILE && "bib".equals(res.getFileExtension())) {
				URI uri = URI.createURI(((IFile) res).getFullPath().toString());
				editingDomain.getResourceSet().getResource(uri, true);
			} else if (res.getType() == IResource.FILE && "taxonomy".equals(res.getFileExtension())){
				ModelRegistryPlugin.getModelRegistry().setTaxonomyFile((IFile) res);
				
			} else if (res.getType() == IResource.FILE && "slrproject".equals(res.getFileExtension())){
				String absolutePathMetainformation = res.getLocation().toOSString();
				MetainformationActivator.setCurrentFilepath(absolutePathMetainformation);
				
				SlrProjectMetainformation metainformation = null;
				try {
					metainformation = MetainformationUtil.getMetainformationFromFile(new File(absolutePathMetainformation));
				} catch (JAXBException e) {
					String errorMessage = "Metainformation file can't be loaded.";
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error",
							errorMessage);
					e.printStackTrace();
				}
				MetainformationActivator.setMetainformation(metainformation);
			}
		}
	}

	/**
	 * Tries to load the wizard for the LaTex export. If a project can be loaded, it will be opened.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {	
		if(tryLoadingProjectFiles(event)) {
			Shell activeShell = HandlerUtil.getActiveShell(event);
			IWizard wizard = new LatexExportWizard();
			WizardDialog wizardDialog = new WizardDialog(activeShell, wizard);
			wizardDialog.open();
		}
		else {

//			String errorMessage = "No taxonomy is present. Please load a taxonomy to be able to use the LaTex export.";
//			if (!ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy().isPresent()) {
//				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error",
//						errorMessage);
//				return null;
//			} else {
//
//			}	
		}
		return null;


	}

	/**
	 * Tries to load a project and all corresponding files in ModelRegistry and metainformation plugin. If more than one projects are in the workspace, a dialog will be opened and the user
	 * input determines, which project is to be loaded.
	 * @param event Execution Event
	 * @return false, if no project can be loaded; true, if a project could be loaded
	 */
	private boolean tryLoadingProjectFiles(ExecutionEvent event) {
		initializeEditingDomain();
		
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		List<IProject> openProjects = new ArrayList<>();
		for(IProject project : projects)
		{
		    if(project.isOpen())
		        openProjects.add(project);
		}
		
		if(openProjects.size() == 0) {
			String errorMessage = "There are no projects to export in your workspace.";
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error",
					errorMessage);
			return false;
		}
		
		else if(openProjects.size() == 1) {
			setProjectForExport(openProjects.get(0));
			return true;
		}
		
		else{
			int projectSelection = getProjectSelection(openProjects);
			
			if(projectSelection == this.SELECTIONEVENT_CANCEL) {
				return false;
			}
			else {
				setProjectForExport(openProjects.get(projectSelection));
				return true;
			}
		}
	}
	
	/**
	 * Determines, which project is to be loaded. A dialog is opened.
	 * @param openProjects List of all open projects in the workspace
	 * @return index (in the argument) of the project which is to be loaded
	 */
	private int getProjectSelection(List<IProject> openProjects) {
    	ExportProjectChooser dialog = new ExportProjectChooser(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), openProjects);
    	dialog.create();
    	
		if (dialog.open() == Window.OK) {
			return dialog.getChosenProject();
		} else {
			return this.SELECTIONEVENT_CANCEL;
		}
	}

	//copied and adapted from de.tudresden.slr.model.bibtex.ui.presentation.BibtexEntryView
	protected void initializeEditingDomain() {
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent((domain) -> editingDomain = domain);
		if (editingDomain == null) {
			System.err.println("uninitailised editing domain");
			return;
		}
		adapterFactory = editingDomain.getAdapterFactory();
	}
	

}
