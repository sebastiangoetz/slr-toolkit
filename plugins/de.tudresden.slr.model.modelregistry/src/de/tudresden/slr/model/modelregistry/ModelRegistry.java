package de.tudresden.slr.model.modelregistry;

import java.util.Observable;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Model;

public class ModelRegistry extends Observable {
	private Document activeDocument;
	private Model activeTaxonomy;

	public ModelRegistry() {
	}

	public Document getActiveDocument() {
		return activeDocument;
	}

	public void setActiveDocument(Document document) {
		if (activeDocument != document) {
			activeDocument = document;
			setChanged();
			notifyObservers(activeDocument);
		}
	}

	public Model getActiveTaxonomy() {
		return activeTaxonomy;
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
