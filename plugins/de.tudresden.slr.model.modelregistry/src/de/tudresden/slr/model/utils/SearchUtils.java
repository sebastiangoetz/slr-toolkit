package de.tudresden.slr.model.utils;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.util.TermComparator;

public class SearchUtils {
	private static TermComparator termComparator = new TermComparator();
	
	public static Term findTermInDocument(Document document, Term other) {
		TaxonomyIterator iter = new TaxonomyIterator(document.getTaxonomy());
		Stream<Term> stream = StreamSupport.stream(iter.spliterator(), false);
		Optional<Term> result = stream.filter(
				term -> termComparator.equals(term, other)).findAny();
		return result.isPresent() ? result.get() : null;
	}

	public static Term findTermInTaxonomy(Term other) {
		Optional<Model> taxonomy = ModelRegistryPlugin.getModelRegistry()
				.getActiveTaxonomy();
		if (taxonomy.isPresent()) {
			TaxonomyIterator iter = new TaxonomyIterator(taxonomy.get());
			for (Term term : iter) {
				if (termComparator.equals(term, other)) {
					if (term.eContainer() instanceof Model) {
						return term;
					} else {
						if (term.eContainer() instanceof Term
								&& other.eContainer() instanceof Term) {
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
		}
		return other;
	}
}
