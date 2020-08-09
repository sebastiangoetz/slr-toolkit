package de.tudresden.slr.classification.classifiers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.classification.ui.MalformedTermNameDialog;
import de.tudresden.slr.classification.validators.ITermNameValidator;
import de.tudresden.slr.classification.validators.TermNameValidatorImpl;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.TaxonomyUtils;
import de.tudresden.slr.utils.taxonomy.manipulation.TermCreator;

public class StandardTaxonomyClassifier {

	public final static String EXP_NONALPHANUMERIC = "[^A-Za-z0-9 ]";
	
	boolean useDefaultMalformedTermNameHandling;
	ITermNameValidator validator;
	
	public StandardTaxonomyClassifier() {
		useDefaultMalformedTermNameHandling = false;
		validator = new TermNameValidatorImpl();
		
	}
	
	public StandardTaxonomyClassifier(boolean useDefaultMalformedTermNameHandling,ITermNameValidator validator) {
		this.useDefaultMalformedTermNameHandling = useDefaultMalformedTermNameHandling;
		this.validator = validator;
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
	 * term names will be corrected by handleMalformedTermName
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
			venueTitle = venueTitle.replaceAll(EXP_NONALPHANUMERIC, "");
			if (!validator.isTermNameValid(venueTitle)) {
				venueTitle = handleMalformedTermName(venueTitle, useDefaultMalformedTermNameHandling);
			}

			classifyDocument(doc, "Document Venue", venueType, venueTitle);
		}

	}

	/**
	 * Transforms strings that would be invalid when used as term names into valid
	 * ones
	 * 
	 * @param name
	 *            Term name to be transformed
	 * @param useDialog
	 *            Indicates, wether a pop-up string field that allows users to set
	 *            the term name manually should appear
	 * @return Valid term name
	 */
	private String handleMalformedTermName(String name, boolean useDialog) {

		String returnName = "T " + name;
		if (!useDefaultMalformedTermNameHandling) {
			MalformedTermNameDialog dlg = new MalformedTermNameDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Invalid Term name", "Please enter a new term name that starts with a letter.", returnName,
					(s -> (!Character.isLetter(s.charAt(0))) ? "" : null));
			returnName = (dlg.open() == 0) ? name = dlg.getValue() : returnName;
			returnName.replaceAll("[^A-Za-z0-9 ]", "");
			useDefaultMalformedTermNameHandling = dlg.getUseDefault();
		}
		return returnName;
	}

	/**
	 * Adds hierarchical terms to a document based on the order of supplied strings
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
	 * Loads project if necessary and generates a taxonomy based on bibtex and taxonomy files
	 * 
	 * @param project  Project containing bibtex and taxonomy files
	 */
	public void classifyDocumentsInProject(IProject project) {
		try {
			List<Document> docs = ModelRegistryPlugin.getModelRegistry().getResourceManager().loadProject(project);
			Map<String, Document> docMap = new HashMap<String,Document>();

			for (Document d : docs) {
				docMap.put(d.getKey(), d);
			}

			for (Document doc : docs) {
				if (doc.getAdditionalFields().containsKey("crossref")) {
					createStandardTaxonomy(doc, docMap.get(doc.getAdditionalFields().get("crossref")));
				} else {
					createStandardTaxonomy(doc);
				}

			}
		} catch (CoreException e) {

		}

	}

}
