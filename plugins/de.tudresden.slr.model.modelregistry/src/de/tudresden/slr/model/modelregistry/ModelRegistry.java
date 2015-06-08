package de.tudresden.slr.model.modelregistry;

import org.eclipse.emf.ecore.EObject;

import de.tudresden.slr.model.bibtex.Document;

public class ModelRegistry {
	private Document activeDocument;

	private EObject activeTaxonomy;

	public ModelRegistry() {

	}

	public Document getActiveDocument() {
		return activeDocument;
	}

	public void setActiveDocument(Document document) {
		activeDocument = document;
	}

	public EObject getActiveTaxonomy() {
		return activeTaxonomy;
	}

	public void setActiveTaxonomy(EObject taxonomy) {
		if (activeTaxonomy != taxonomy) {
			activeTaxonomy = taxonomy;
		}
	}
}
