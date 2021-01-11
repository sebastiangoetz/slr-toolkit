package de.tudresden.slr.model.modelregistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import de.tudresden.slr.model.bibtex.Document;

public class ResourceManager extends Observable {

	AdapterFactoryEditingDomain editingDomain;
	IProject activeProject;
	
	public ResourceManager(AdapterFactoryEditingDomain editingDomain) {
		this.editingDomain = editingDomain;

	}

	/**
	 * Assigns the supplied project as the "active" project, i. e. specifying which
	 * files are currently loaded and can be edited
	 * 
	 * @param project
	 *            The project that should be designated "active" similar to
	 *            activeDocument and activeTaxonomy
	 * @param forceLoad
	 * 			  Sets whether the project should be reloaded if it is already the active project
	 * @throws CoreException
	 */
	public synchronized void setActiveProject(IProject project,boolean forceLoad) throws CoreException {
		if ((project != activeProject) || forceLoad) {
			unloadResources(true);
			activeProject = project;
			loadProject(project);
			setChanged();
			notifyObservers(project);
		}

	}
	
	/**
	 * Alias for setActiveProject(project, false)
	 * 
	 * @param project
	 *            The project that should be designated "active" similar to
	 *            activeDocument and activeTaxonomy
	 * @throws CoreException
	 */
	public synchronized void setActiveProject(IProject project) throws CoreException {
		setActiveProject(project, false);
	}

	/**
	 * Returns the active Project
	 * 
	 * @return activeProject The currently active project
	 */
	public Optional<IProject> getActiveProject() { 
		return activeProject==null ? Optional.empty() : Optional.of(activeProject);
	}

	/**
	 * Unloads all resources and clears the ResourceSet
	 */
	public void unloadResources(boolean silent) {
		for (Resource resource : editingDomain.getResourceSet().getResources()) {
			resource.unload();
		}
		editingDomain.getResourceSet().getResources().clear();
		activeProject = null;
		
		if(!silent) {
			notifyObservers(activeProject);
		}

	}

	/**
	 * Loads all documents from all .bib files in the given project and sets the last found taxonomy file in ModelRegistry
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public List<Document> loadProject(IProject project) throws CoreException {
		List<IFile> bibFiles = new ArrayList<IFile>();
		IFile taxonomyFile = null;

		for (IResource res : project.members()) { // throws CoreException because of members()
			if (res.getFileExtension() == null) continue;
			if (res.getFileExtension().equals("bib")) {
				bibFiles.add(project.getFile(res.getFullPath().lastSegment()));

			}

			if (res.getFileExtension().equals("taxonomy")) {
				taxonomyFile = project.getFile(res.getFullPath().lastSegment());
				ModelRegistryPlugin.getModelRegistry().setTaxonomyFile(taxonomyFile);
			}
		}
		List<Document> docs = new ArrayList<Document>();

		for (IFile file : bibFiles) {
			URI uri = URI.createURI(file.getFullPath().toString());
			Resource bibRes = editingDomain.getResourceSet().getResource(uri, true);
			for (EObject e : bibRes.getContents()) {
				docs.add((Document) e);
			}
		}

		return docs;

	}

	@Override
	public void notifyObservers(Object arg) {
		try {
			super.notifyObservers(arg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
