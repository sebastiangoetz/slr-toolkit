package de.tudresden.slr.latexexport.handlers;

import de.tudresden.slr.latexexport.wizard.LatexExportWizard;
import de.tudresden.slr.metainformation.util.DataProvider;
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
import java.util.List;
import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class exportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ensureTaxonomyIsLoaded();

		String errorMessage = "No taxonomy is present. Please load a taxonomy to be able to use the LaTex export.";
		if (!ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy().isPresent()) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error",
					errorMessage);
			return null;
		} else {
			Shell activeShell = HandlerUtil.getActiveShell(event);
			IWizard wizard = new LatexExportWizard();
			WizardDialog wizardDialog = new WizardDialog(activeShell, wizard);
			wizardDialog.open();
			return null;
		}

	}

	//TODO loading
	private void ensureTaxonomyIsLoaded() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage activePage = window.getActivePage();

		IEditorPart activeEditor = activePage.getActiveEditor();

		if (activeEditor != null) {
		   IEditorInput input = activeEditor.getEditorInput();

		   IProject project = input.getAdapter(IProject.class);
		   if (project == null) {
		      IResource resource = input.getAdapter(IResource.class);
		      if (resource != null) {
		         project = resource.getProject();
		         //System.out.println(project.getProjectRelativePath());
		      }
		   }
		}		
	}

}
