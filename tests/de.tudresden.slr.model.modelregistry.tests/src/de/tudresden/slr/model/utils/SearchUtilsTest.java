package de.tudresden.slr.model.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.eclipse.emf.common.util.BasicEList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;

public class SearchUtilsTest {

	Document document;
	Model model;

	String term1Str;
	String term2Str;
	String term3Str;

	Term term1;
	Term term2;
	Term term3;

	Term otherTerm;
	Term termWithSameSameAsTerm1;

	@Before
	public void setUp() {
		// document has a model with 3 classes
		document = Mockito.mock(Document.class);
		model = Mockito.mock(Model.class);
		Mockito.when(document.getTaxonomy()).thenReturn(model);

		term1Str = "term1";
		term2Str = "term2";
		term3Str = "term3";

		term1 = Mockito.mock(Term.class);
		Mockito.when(term1.getName()).thenReturn(term1Str);
		Mockito.when(term1.getSubclasses()).thenReturn(new BasicEList<Term>());
		Mockito.when(term1.eContainer()).thenReturn(model);

		term2 = Mockito.mock(Term.class);
		Mockito.when(term2.getName()).thenReturn(term2Str);
		Mockito.when(term2.getSubclasses()).thenReturn(new BasicEList<Term>());
		Mockito.when(term1.eContainer()).thenReturn(model);

		term3 = Mockito.mock(Term.class);
		Mockito.when(term3.getName()).thenReturn(term3Str);
		Mockito.when(term3.getSubclasses()).thenReturn(new BasicEList<Term>());
		Mockito.when(term1.eContainer()).thenReturn(model);

		BasicEList<Term> dimensions = new BasicEList<>();
		dimensions.addAll(Arrays.asList(term1, term2, term3));
		Mockito.when(model.getDimensions()).thenReturn(dimensions);

		otherTerm = Mockito.mock(Term.class);
		Mockito.when(otherTerm.getName()).thenReturn("otherTermNotEquals");

		termWithSameSameAsTerm1 = Mockito.mock(Term.class);
		Mockito.when(termWithSameSameAsTerm1.getName()).thenReturn(term1Str);
	}

	@Test
	public void testFindTermInDocument() {
		// term null should yield null
		assertNull(SearchUtils.findTermInDocument(document, null));

		// term which is not in taxonomy should yield null
		assertNull(SearchUtils.findTermInDocument(document, otherTerm));

		// same name as term 1, but different object, should yield term with that name
		Term result = SearchUtils.findTermInDocument(document, termWithSameSameAsTerm1);
		assertNotNull(result);
		assertEquals(term1.getName(), result.getName());
	}

	@Test
	public void testFindTermInTaxonomy() {
		// term null should yield null
		assertNull(SearchUtils.findTermInTaxonomy(model, null));

		// term which is not in taxonomy should yield that same term (apparently :))
		assertEquals(otherTerm, SearchUtils.findTermInTaxonomy(model, otherTerm));

		// term with same name as term1, but different object should yield original
		// object which contains term1
		assertEquals(term1, SearchUtils.findTermInTaxonomy(model, termWithSameSameAsTerm1));
	}
	
	@Test
	public void testGetContainingModel() {
		Term term1subterm = Mockito.mock(Term.class);
		BasicEList<Term> term1Subterms = new BasicEList<>();
		term1Subterms.add(term1subterm);
		Mockito.when(term1subterm.eContainer()).thenReturn(term1);
		Mockito.when(term1.getSubclasses()).thenReturn(term1Subterms);
		
		assertEquals(SearchUtils.getContainingModel(term1).orElse(null), model);
		assertEquals(SearchUtils.getContainingModel(term1subterm).orElse(null), model);
	}

}
