package de.tudresden.slr.model.modelregistry;

import java.util.HashMap;
import java.util.Observable;
import java.util.Optional;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.provider.BibtexItemProviderAdapterFactory;
import de.tudresden.slr.model.taxonomy.Model;

public class ModelRegistry extends Observable {
	private Document activeDocument;
	private Model activeTaxonomy;
	private AdapterFactoryEditingDomain sharedEditingDomain;

	public ModelRegistry() {
		createEditingDomain();
	}

	private void createEditingDomain() {
		// Create an adapter factory that yields item providers.
		//
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory
				.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new BibtexItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the command stack that will notify this editor as commands are
		// executed.
		//
		BasicCommandStack commandStack = new BasicCommandStack();

		// Create the editing domain with a special command stack.
		//
		sharedEditingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				commandStack, new HashMap<Resource, Boolean>());

		// ModelRegistryPlugin.getModelRegistry().setEditingDomain(
		// sharedEditingDomain);
	}

	public Optional<AdapterFactoryEditingDomain> getEditingDomain() {
		return sharedEditingDomain == null ? Optional.empty() : Optional
				.of(sharedEditingDomain);
	}

	public void setEditingDomain(AdapterFactoryEditingDomain editingDomain) {
		if (sharedEditingDomain != editingDomain) {
			sharedEditingDomain = editingDomain;

			setChanged();
			notifyObservers(sharedEditingDomain);
		}
	}

	public Optional<Document> getActiveDocument() {
		return activeDocument == null ? Optional.empty() : Optional
				.of(activeDocument);
	}

	public void setActiveDocument(Document document) {
		if (activeDocument != document) {
			activeDocument = document;
			setChanged();
			notifyObservers(activeDocument);
		}
	}

	public Optional<Model> getActiveTaxonomy() {
		return activeTaxonomy == null ? Optional.empty() : Optional
				.of(activeTaxonomy);
	}

	public void setActiveTaxonomy(Model taxonomy) {
		activeTaxonomy = taxonomy;
		setChanged();
		notifyObservers(activeTaxonomy);
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
