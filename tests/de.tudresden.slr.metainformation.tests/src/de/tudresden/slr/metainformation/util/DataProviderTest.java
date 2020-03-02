package de.tudresden.slr.metainformation.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;

public class DataProviderTest {

	DataProvider dataProvider;

	Model model;
	@SuppressWarnings("rawtypes")
	BasicEList mainDimensions;
	
	Document document1, document2, document3, document4;
	Term rootTerm;
	Term mainTerm1, mainTerm2;
	Term subTerm11, subTerm21, subTerm22;

	@SuppressWarnings({ "unchecked" })
	@Before
	public void setUp() throws Exception {
		dataProvider = new DataProvider();
		
		// create terms
		rootTerm = Mockito.mock(Term.class);
		EList<Term> rootTermAsList = new BasicEList<Term>();
		rootTermAsList.add(rootTerm);
		
		mainTerm1 = Mockito.mock(Term.class);
		mainTerm2 = Mockito.mock(Term.class);
		
		EList<Term> rootTermSubdimensions = new BasicEList<Term>();
		
		rootTermSubdimensions.add(mainTerm1);
		rootTermSubdimensions.add(mainTerm2);
		
		EList<Term> mainTerm1Subdimensions = new BasicEList<Term>();
		EList<Term> mainTerm2Subdimensions = new BasicEList<Term>();
		
		subTerm11 = Mockito.mock(Term.class);
		mainTerm1Subdimensions.add(subTerm11);
		Mockito.when(subTerm11.getSubclasses()).thenReturn(new BasicEList<Term>());
		
		subTerm21 = Mockito.mock(Term.class);
		subTerm22 = Mockito.mock(Term.class);
		mainTerm2Subdimensions.add(subTerm21);
		mainTerm2Subdimensions.add(subTerm22);
		Mockito.when(subTerm21.getSubclasses()).thenReturn(new BasicEList<Term>());
		Mockito.when(subTerm22.getSubclasses()).thenReturn(new BasicEList<Term>());
		
		Mockito.when(rootTerm.getSubclasses()).thenReturn(rootTermSubdimensions);
		Mockito.when(mainTerm1.getSubclasses()).thenReturn(mainTerm1Subdimensions);
		Mockito.when(mainTerm2.getSubclasses()).thenReturn(mainTerm2Subdimensions);

		// fill model with main dimensions, add it to optional which would be returned from ModelRegistryPlugin
		model = Mockito.mock(Model.class);
		mainDimensions = new BasicEList<>();
		mainDimensions.add(mainTerm1);
		mainDimensions.add(mainTerm2);
		dataProvider.taxonomyOptional = Optional.of(model);
		Mockito.when(model.getDimensions()).thenReturn(rootTermAsList);
		
		// create documents
		document1 = Mockito.mock(Document.class);
		document2 = Mockito.mock(Document.class);
		document3 = Mockito.mock(Document.class);
		document4 = Mockito.mock(Document.class);
		List<Document> documentList = Arrays.asList(new Document[] {document1, document2, document3, document4});
		dataProvider.documents = documentList;
	}

	@Test
	public void testGetMainDimensions() {
		assertTrue(dataProvider.getMainDimensions().size() == 1 && dataProvider.getMainDimensions().contains(rootTerm));
	}

	@Test
	public void testGetNumberOfDocuments() {
		assertEquals(4, dataProvider.getNumberOfDocuments());
	}

	@Test
	public void testGetAllSubdimenions() {
		EList<Term> result = dataProvider.getAllSubdimenions(rootTerm);
		// root term not included, thus size should be 5
		assertTrue(result.size() == 5);
	}

	@Test
	public void testGetAllDimensionsOrdered() {
		EList<Term> result = dataProvider.getAllDimensionsOrdered();
		// root term not included, thus size should be 5
		assertTrue(result.size() == 6);
	}

}
