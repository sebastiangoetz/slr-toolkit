package de.tudresden.slr.latexexport.handlers;

import de.tudresden.slr.latexexport.helpers.ExportProjectChooser;
import de.tudresden.slr.latexexport.wizard.LatexExportWizard;
import de.tudresden.slr.metainformation.MetainformationActivator;
import de.tudresden.slr.metainformation.data.Author;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.metainformation.util.MetainformationUtil;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.Bundle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class exportHandler extends AbstractHandler {
	
	private final int SELECTIONEVENT_CANCEL = -1;
	
	protected AdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	
	/**
	 * 
	 * @param project
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

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//läuft bei selection
//		ISelection sel = HandlerUtil.getCurrentSelection(event);
//
//	    if (sel instanceof IStructuredSelection)
//	     {
//	       Object selected = ((IStructuredSelection)sel).getFirstElement();
//
//	       IResource resource = (IResource)Platform.getAdapterManager().getAdapter(selected, IResource.class);
//
//	       if (resource != null)
//	        {
//	          IProject project = resource.getProject();
//	        }
//	     }		
		
		if(ensureTaxonomyIsLoaded(event)) {
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

	private boolean ensureTaxonomyIsLoaded(ExecutionEvent event) {
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
	
	private int getProjectSelection(List<IProject> openProjects) {
    	ExportProjectChooser dialog = new ExportProjectChooser(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), openProjects);
    	dialog.create();
    	
		if (dialog.open() == Window.OK) {
			return dialog.getChosenProject();
		} else {
			return this.SELECTIONEVENT_CANCEL;
		}
	}

	protected void initializeEditingDomain() {
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent((domain) -> editingDomain = domain);
		// Add a listener to set the most recent command's affected objects
		// to be the selection of the viewer with focus.
		if (editingDomain == null) {
			System.err.println("[BibtexEntryView#initializeEditingDomain] uninitailised editing domain");
			return;
		}
		adapterFactory = editingDomain.getAdapterFactory();
		//closeEditors();
	}
	

}
