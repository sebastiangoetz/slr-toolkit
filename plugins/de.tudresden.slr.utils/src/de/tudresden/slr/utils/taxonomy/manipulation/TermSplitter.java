package de.tudresden.slr.utils.taxonomy.manipulation;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.ecore.resource.Resource;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexFileWriter;
import de.tudresden.slr.model.taxonomy.TaxonomyFactory;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.utils.TermPosition;

public class TermSplitter {
	
	public static Term split (Term termToSplit, String defaultTermName, List<String> furtherTermNames) {
		// create term first for the taxonomy
		TermCreator.create(defaultTermName, termToSplit, TermPosition.SUBTERM);
		furtherTermNames.forEach(n -> TermCreator.create(n, termToSplit, TermPosition.SUBTERM));
		// create term for all documents which contain the termToSplit
		Map<Document, Term> documentsWithTerm = SearchUtils.findDocumentsWithTerm(termToSplit);
		Set<Resource> resourcesToUpdate = new TreeSet<>(
				(Resource r1, Resource r2) -> r1 == r2 ? 0 : 1);
		for (Map.Entry<Document, Term> entry : documentsWithTerm.entrySet()) {
			Term createdTerm = TaxonomyFactory.eINSTANCE.createTerm();
			createdTerm.setName(defaultTermName);
			entry.getValue().getSubclasses().add(createdTerm);
			resourcesToUpdate.add(entry.getKey().eResource());
		}
		resourcesToUpdate.forEach(r -> BibtexFileWriter.updateBibtexFile(r));
		return termToSplit;
	}

}
