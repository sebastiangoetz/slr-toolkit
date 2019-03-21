package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.ui.presentation.BibtexMergeData.BibtexEntrySimilarity;
import de.tudresden.slr.model.bibtex.ui.presentation.BibtexMergeData.BibtexMergeConflict;
import de.tudresden.slr.model.bibtex.ui.presentation.BibtexMergeData.Criteria;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class SimilarityMatrixTest {
	
	private BibtexMergeData mergeData;
	private Map<DocumentImpl, Map<DocumentImpl, BibtexEntrySimilarity>> similarityMatrix;
	
	public SimilarityMatrixTest() throws IOException {
		// read bibtex test file from resources
		InputStream is = getClass().getResourceAsStream("/BibtexTest.bib");
		
		// parse as BibtexResourceImpl
		BibtexResourceImpl resources = new BibtexResourceImpl(null);
		resources.load(is, null);
		
		// create MergeData + similarityMatrix
		mergeData = new BibtexMergeData(Arrays.asList(new BibtexResourceImpl[]{resources, resources}));
		Map<Criteria, Integer> weights = new HashMap<Criteria, Integer>() {{
			put(Criteria.authors, 100);
			put(Criteria.title, 100);
			put(Criteria.year, 100);
		}};
		mergeData.setWeights(weights);
		similarityMatrix = mergeData.getSimilarityMatrix();
	}

	@Test
	public void testCreateSimilarityMatrix() {
		Assert.assertNotNull(similarityMatrix);
	}
	
	@Test
	public void testGetTotalScore() {
		Map<Criteria, Integer> weights = new HashMap<Criteria, Integer>() {{
			put(Criteria.doi, 100);
			put(Criteria.authors, 100);
			put(Criteria.title, 100);
			put(Criteria.year, 100);
		}};
		
		BibtexEntrySimilarity entrySimilarity = mergeData.new BibtexEntrySimilarity();
		entrySimilarity.setAuthorSimilarity(1);
		entrySimilarity.setTitleSimilarity(0);
		entrySimilarity.setDoiEquals(true);
		entrySimilarity.setYearDifference(0);
		Assert.assertTrue("total score should be computed correctly", entrySimilarity.getTotalScore(weights) == 0.75);
		weights.put(Criteria.title, 0);
		Assert.assertTrue("total score should be computed correctly", entrySimilarity.getTotalScore(weights) == 1);
	}
	
	@Test
	public void testGetConflicts() {
		List<BibtexMergeConflict> conflicts = mergeData.getConflicts(0.9);
		int firstSize = conflicts.size();
		Assert.assertNotNull(conflicts);
		
		// as the threshold decreases there should be more similar entries
		conflicts = mergeData.getConflicts(0.7);
		Assert.assertTrue(conflicts.size() > firstSize);
	}
	
	@Test
	public void testRegex() {
		String input = "https://this/is/a/url/with/doi/10.1234/121399.34593-23/bla";
		String result = "";
		String[] parts = input.split("/");
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if (part.matches("\\d\\d\\.\\d\\d\\d\\d") && i + 1 < parts.length)
				result = parts[i] + "/" + parts[i + 1];
		}
		Assert.assertTrue(result.contentEquals("10.1234/121399.34593-23"));
	}
	
	@Test
	public void testGetDoiFromUrl() {
		Map<Criteria, Integer> weights = new HashMap<Criteria, Integer>() {{
			put(Criteria.doi, 100);
			put(Criteria.authors, 0);
			put(Criteria.title, 0);
			put(Criteria.year, 0);
		}};
		mergeData.setWeights(weights);
		
		Assert.assertTrue("There should be exactly four results for doi equality", mergeData.getConflicts(0.9).size() == 4);
	}
}
