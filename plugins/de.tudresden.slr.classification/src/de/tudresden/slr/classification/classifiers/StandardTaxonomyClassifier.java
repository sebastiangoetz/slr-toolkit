package de.tudresden.slr.classification.classifiers;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;

import de.tudresden.slr.classification.validators.IMalformedTermNameHandler;
import de.tudresden.slr.classification.validators.ITermNameValidator;
import de.tudresden.slr.classification.validators.MalformedTermNameHandlerImpl;
import de.tudresden.slr.classification.validators.TermNameValidatorImpl;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.utils.TaxonomyUtils;
import de.tudresden.slr.utils.taxonomy.manipulation.TermCreator;

public class StandardTaxonomyClassifier {

	
	public final static String NO_VENUE = "none";
	
	private ITermNameValidator validator;
	private IMalformedTermNameHandler malformedTermNameHandler;
	
	public StandardTaxonomyClassifier() {
		validator = new TermNameValidatorImpl();
		MalformedTermNameHandlerImpl handler = new MalformedTermNameHandlerImpl();
		handler.setUseDefault(true);
		malformedTermNameHandler = handler;
	}
	
	public StandardTaxonomyClassifier(boolean useDefault) {
		validator = new TermNameValidatorImpl();
		MalformedTermNameHandlerImpl handler = new MalformedTermNameHandlerImpl();
		handler.setUseDefault(useDefault);
		malformedTermNameHandler = handler;
	}
	
	public StandardTaxonomyClassifier(ITermNameValidator validator, IMalformedTermNameHandler malformedTermNameHandler) {
		this.validator = validator;
		this.malformedTermNameHandler = malformedTermNameHandler;
	}

	/**
	 * Alias for createStandardTaxonomy(d, null)
	 * 
	 * @param d
	 *            Document to classify
	 */
	public void createStandardTaxonomy(Document d) {
		createStandardTaxonomy(d, null);
	}

	/**
	 * Creates a taxonomy for the given document based on bibtex attributes
	 * (Document type and Document Venue). If provided, it will use a cross
	 * reference to avoid inconsistent journal/book names If SCOPUS document types
	 * are found, those specified in that field will be used instead of annotated
	 * types Term names are stripped of non-alphanumeric characters and malformed
	 * term names will be corrected by malformedTermNameHandler
	 * 
	 * @param doc
	 *            Document to be classified
	 * @param crossRefTitle
	 *            Title of the document to be cross referenced If null, no cross
	 *            reference will be used
	 */
	public void createStandardTaxonomy(Document doc, Document crossRefDoc) {
		Map<String, String> fields = doc.getAdditionalFields();
		Set<String> fieldKeys = fields.keySet();
		
		if (fieldKeys.contains("document_type")) {
			classifyDocument(doc, "Document Type", fields.get("document_type"));
		} else {
			classifyDocument(doc, "Document Type", doc.getType());
		}

		
		String[] lookUp = { "booktitle", "journal", "howpublished" };
		String venueType = "";
		String venueTitle ="";
		boolean venueFound = false;
		for (String s : lookUp) {
			if (fields.containsKey(s)) {
				venueType = s;
				venueTitle = fields.get(s);
				venueFound = true;
				break;
			}
		}
		
		if(crossRefDoc!=null) {
			if(!venueFound) {
				venueType = crossRefDoc.getType();
				venueFound = true;
			}
			venueTitle = crossRefDoc.getTitle();
		}
		
		
		
		if (venueFound) {
			classifyDocument(doc, "Document Venue", venueType, venueTitle);
		} else {
			classifyDocument(doc, "Document Venue", NO_VENUE);
		}

	}


	/**
	 * Adds hierarchical terms to a document based on the order of supplied strings. Order goes from highest level term name to lowest order Term name.
	 * 
	 * @param doc  document for which to create terms
	 * @param types  variable amount of strings starting with the highest order term name
	 */
	public void classifyDocument(Document doc, String... types) {
		Optional<Model> modelOptional = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy();
		if (modelOptional.isPresent()) {
			Model model = modelOptional.get();

			EObject taxonomyElement = model;
			EObject docTaxonomyElement = doc.getTaxonomy();

			for (String type : types) {
				// create element in general taxonomy
				if(!validator.isTermNameValid(type)) {
					type = malformedTermNameHandler.handleMalformedTermName(type);
				}
				taxonomyElement = TermCreator.createChildIfNotExisting(taxonomyElement, type);
				docTaxonomyElement = TermCreator.createChildIfNotExisting(docTaxonomyElement, type, false);
			}

			TaxonomyUtils.saveTaxonomy(model);
			try {
				doc.eResource().save(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loads project if necessary and generates a taxonomy based on bibtex and taxonomy files. Alias for classifyDocumentsInProject(project,new LinkedList<String>())
	 * 
	 * @param project  Project containing bibtex and taxonomy files
	 */
	public void classifyDocumentsInProject(IProject project) {
		classifyDocumentsInProject(project,new LinkedList<String>());
	}
	
	/**
	 * Instantiates a map that associates a Document's key with a reference to the Document itself. 
	 * 
	 * @param docs  A List of document objects
	 */
	public Map<String,Document> createDocMap(List<Document> docs) {
		Map<String, Document> docMap = new HashMap<String,Document>();

		for (Document d : docs) {
			docMap.put(d.getKey(), d);
		}
		return docMap;
	}
	
	/**
	 * Loads project if necessary and generates a taxonomy based on bibtex and taxonomy files
	 * Documents with entry types found in exclusionList will not be classifier, but still used in cross referencing.
	 * 
	 * @param project  Project containing bibtex and taxonomy files
	 * @param excludeList  List of bibtex entry types for documents to be ignored
	 */
	public void classifyDocumentsInProject(IProject project,List<String> excludeList) {
		try {
			List<Document> docs = ModelRegistryPlugin.getModelRegistry().getResourceManager().loadProject(project);
			Map<String, Document> docMap = createDocMap(docs);

			//using docMap.values() intead of docs list to enable swapping of docs in tests using overloaded createDocMap()
			for (Document doc : docMap.values()) {
				if(!excludeList.contains(doc.getType())) {
					if (doc.getAdditionalFields().containsKey("crossref")) {
						createStandardTaxonomy(doc, docMap.get(doc.getAdditionalFields().get("crossref")));
					} else {
						createStandardTaxonomy(doc);
					}
				}
			}
		} catch (CoreException e) {

		}

	}

	public ITermNameValidator getValidator() {
		return validator;
	}

	public void setValidator(ITermNameValidator validator) {
		this.validator = validator;
	}

	public IMalformedTermNameHandler getMalformedTermNameHandler() {
		return malformedTermNameHandler;
	}

	public void setMalformedTermNameHandler(IMalformedTermNameHandler malformedTermNameHandler) {
		this.malformedTermNameHandler = malformedTermNameHandler;
	}

}
