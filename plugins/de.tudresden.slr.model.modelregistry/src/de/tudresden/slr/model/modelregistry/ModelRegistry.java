package de.tudresden.slr.model.modelregistry;

import java.util.Observable;
import java.util.Optional;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Model;

public class ModelRegistry extends Observable {
	private Document activeDocument;
	private Model activeTaxonomy;
	private AdapterFactoryEditingDomain sharedEditingDomain;

	public ModelRegistry() {
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
		if (activeTaxonomy != taxonomy) {
			activeTaxonomy = taxonomy;
			setChanged();
			notifyObservers(activeTaxonomy);
		}
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
