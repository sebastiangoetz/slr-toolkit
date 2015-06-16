package de.tudresden.slr.model.modelregistry;

import java.util.Observable;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Model;

public class ModelRegistry extends Observable {
	private Document activeDocument;
	private Model activeTaxonomy;
	private Resource activeResource;

	public ModelRegistry() {
	}

	public Optional<Resource> getActiveResource() {
		if (activeResource == null) {
			return Optional.empty();
		}
		return Optional.of(activeResource);
	}

	public void setActiveResource(Resource resource) {
		if (activeResource != resource) {
			activeResource = resource;
			setChanged();
			notifyObservers(activeResource);
		}
	}

	public Optional<Document> getActiveDocument() {
		if (activeDocument == null) {
			return Optional.empty();
		}
		return Optional.of(activeDocument);
	}

	public void setActiveDocument(Document document) {
		if (activeDocument != document) {
			activeDocument = document;
			setChanged();
			notifyObservers(activeDocument);
		}
	}

	public Optional<Model> getActiveTaxonomy() {
		if (activeTaxonomy == null) {
			return Optional.empty();
		}
		return Optional.of(activeTaxonomy);
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
