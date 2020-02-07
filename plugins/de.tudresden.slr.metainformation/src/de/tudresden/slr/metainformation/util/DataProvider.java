package de.tudresden.slr.metainformation.util;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;

/**
 * 
 * Data provider which encapsulates taxonomy dimensions and Bibtex documents
 */
public class DataProvider {
	/**
	 * List of documents from bibtex plugins
	 */
	protected List<Document> documents = SearchUtils.getDocumentList();
	/**
	 * Optional which may or may not contain taxonomy
	 */
	protected Optional<Model> taxonomyOptional = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy();
	
	public DataProvider() {
	}

	public List<Document> getDocuments() {
		return documents;
	}
	
	/**
	 * Returns the main dimensions of the taxonomy if it is present
	 */
	public EList<Term> getMainDimensions() {
		if(taxonomyOptional.isPresent()) {
			return taxonomyOptional.get().getDimensions();
		}
		else {
			return new BasicEList<Term>();
		}
	}
	
	/**
	 * 
	 * @return number of documents which are in the current SLR project
	 */
	public int getNumberOfDocuments() {
		return documents.size();
	}
	
	/**
	 * 
	 * @param t Term whose subdimensions are to be determined
	 * @return List of subdimensions for a certain term
	 */
	public EList<Term> getAllSubdimenions(Term t){
		EList<Term> toReturn = new BasicEList<Term>();
		for(Term subTerm : t.getSubclasses()) {
			toReturn.add(subTerm);
			toReturn.addAll(getAllSubdimenions(subTerm));
		}
		
		return toReturn;
		
	}
	
	/**
	 * Generates a list of all dimensions and their sub dimensions.
	 * Order: Every dimension has as successor - if existent - it's first child. If not present,
	 * it's the next dimension of the same or higher level.
	 * @return
	 */
	public EList<Term> getAllDimensionsOrdered(){
		EList<Term> allTerms = new BasicEList<Term>();
		EList<Term> allClasses = getMainDimensions();

		for (Term t : allClasses) {
			allTerms.add(t);
			allTerms.addAll(getAllSubdimenions(t));
		}
		
		return allTerms;
	}
	
	/**
	 * 
	 * @param t Term of taxonomy
	 * @return Number of documents, which are annotated which the specified term of the taxonomy
	 */
	public int getNumberOfElementsInDimension(Term t) {
		Map<Document, Term> termsInDocuments = SearchUtils.findDocumentsWithTerm(t);
		return termsInDocuments.size();
	}
}
