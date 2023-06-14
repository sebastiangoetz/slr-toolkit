package de.tudresden.slr.model.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;

public class SearchUtils {
	
	public static Term findTermInDocument(Document document, Term other) {
		if(other == null) return null;
		TaxonomyIterator iter = new TaxonomyIterator(document.getTaxonomy());
		Stream<Term> stream = StreamSupport.stream(iter.spliterator(), false);
		Optional<Term> result = stream.filter(term -> term.getName().equals(other.getName())).findAny();
		return result.isPresent() ? result.get() : null;
	}

	public static Term findTermInTaxonomy(Term other) {
		Optional<Model> taxonomy = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy();
		if (taxonomy.isPresent()) {
			return findTermInTaxonomy(taxonomy.get(), other);
			
			
		}
		return other;
	}
	
	public static Term findTermInTaxonomy(Model taxonomy, Term other) {
		if(other == null) return null;
		TaxonomyIterator iter = new TaxonomyIterator(taxonomy);
		for (Term term : iter) {
			if (term.getName().equals(other.getName())) {
				if (term.eContainer() instanceof Model) {
					return term;
				} else {
					if (term.eContainer() instanceof Term && other.eContainer() instanceof Term) {
						// compare the parents
						Term parent = (Term) term.eContainer();
						Term otherParent = (Term) other.eContainer();
						if (parent.getName().equals(otherParent.getName())) {
							return term;
						}
					}
				}
			}
		}
		return other;
	}
	
	public static List<Term> findTermInAllDocuments(Term other) {	
		Collection<Term> terms = findDocumentsWithTerm(other).values();
		return Arrays.asList(terms.toArray(new Term[terms.size()]));
	}
	
	public static Map<Document, Term> findDocumentsWithTerm(Term other) {
		Map<Document, Term> result = new HashMap<>();
		for (Document d : getDocumentList()) {
			Term current = findTermInDocument(d, other);
			if (other != current && current != null) {
				result.put(d, current);
			}
		}
		return result;
	}
	
	public static Optional<Model> getContainingModel(Term t) {
		EObject currentNode = t;
		while (!(currentNode instanceof Model) && currentNode.eContainer() != null) {
			currentNode = ((Term) currentNode).eContainer();
		}
		return currentNode instanceof Model ? Optional.of((Model) currentNode) : Optional.empty();
	}
	
	/**
	 * Helper function to get a list of documents from a resource
	 * 
	 * @param r
	 *            The resource
	 * @return
	 */
	public static List<Document> getDocumentList() {
		ArrayList<Document> results = new ArrayList<>();
		ArrayList<Resource> resources = null;
		Optional<AdapterFactoryEditingDomain> oDomain = ModelRegistryPlugin.getModelRegistry().getEditingDomain();
		if (oDomain.isPresent()) {
			resources = new ArrayList<>(oDomain.get().getResourceSet().getResources());
			for(Resource r : resources) {
				for (EObject e : r.getContents()) {
					if (e instanceof Document) {
						results.add((Document) e);
					}
				}
			}
		}
		return results;
	}
	
	 public static Optional<Term> findChildWithName(EObject parent, String subClassName) {
		 Term foundTerm = null;
		 for(Term t: getChildren(parent)) {
			 if(t.getName().equals(subClassName)) {
				 foundTerm = t;
				 break;
			 }
		 }
		 
		 if(foundTerm == null) {
			 return Optional.empty();
		 } else {
			 return Optional.of(foundTerm);
		 }
		 
	 }
	 
	 public static List<Term> getChildren(EObject parent) {
		 if(parent instanceof Model) {
			return  ((Model) parent).getDimensions();
		 } else if (parent instanceof Term) {
			return ((Term) parent).getSubclasses();
		 } else {
			 throw new IllegalArgumentException();
		 }
	 }
	 
	 public static boolean childWithNameExists(EObject parent, String name) {
		 List<Term> children = getChildren(parent);
		 
		 for(Term t: children) {
			 if (t.getName().equals(name)) return true;
		 }
		 return false;
	 }
}
