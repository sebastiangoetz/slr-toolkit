package de.tudresden.slr.model.modelregistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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

	/**
	 * Assigns the supplied project as the "active" project, i. e. specifying which
	 * files are currently loaded and can be edited
	 * 
	 * @param project
	 *            The project that should be designated "active" similar to
	 *            activeDocument and activeTaxonomy
	 * @throws CoreException
	 */
	public void setActiveProject(IProject project) throws CoreException {
		if (project != activeProject) {
			unloadResources();
			activeProject = project;
			loadProject(project);
			setChanged();
			notifyObservers(project);
		}

	}

	/**
	 * Returns the active Project
	 * 
	 * @return activeProject The currently active project
	 */
	public IProject getActiveProject() {
		return activeProject;
	}

	public ResourceManager(AdapterFactoryEditingDomain editingDomain) {
		this.editingDomain = editingDomain;

	}

	/**
	 * Unloads all resources and clears the ResourceSet
	 */
	public void unloadResources() {
		for (Resource resource : editingDomain.getResourceSet().getResources()) {
			resource.unload();
		}
		editingDomain.getResourceSet().getResources().clear();

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
			if (res.getFileExtension().equals("bib")) {
				bibFiles.add(project.getFile(res.getFullPath().lastSegment()));

			}

			if (res.getFileExtension().equals("taxonomy")) {
				taxonomyFile = project.getFile(res.getFullPath().lastSegment());
			}
		}

		ModelRegistryPlugin.getModelRegistry().setTaxonomyFile(taxonomyFile);
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
