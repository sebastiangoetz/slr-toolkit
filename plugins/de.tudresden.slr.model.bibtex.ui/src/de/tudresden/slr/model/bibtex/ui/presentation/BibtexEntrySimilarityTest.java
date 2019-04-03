package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.tudresden.slr.model.bibtex.ui.presentation.BibtexMergeData.Criteria;

public class BibtexEntrySimilarityTest {
	
	@Test
	public void testGetTotalScore() {
		Map<Criteria, Integer> weights = new HashMap<Criteria, Integer>() {{
			put(Criteria.doi, 100);
			put(Criteria.authors, 100);
			put(Criteria.title, 100);
			put(Criteria.year, 100);
		}};
		
		BibtexEntrySimilarity entrySimilarity = new BibtexEntrySimilarity();
		entrySimilarity.setAuthorSimilarity(1);
		entrySimilarity.setTitleSimilarity(0);
		entrySimilarity.setDoiEquals(true);
		entrySimilarity.setYearDifference(0);
		Assert.assertTrue("total score should be computed correctly", entrySimilarity.getTotalScore(weights) == 0.75);
		weights.put(Criteria.title, 0);
		Assert.assertTrue("total score should be computed correctly", entrySimilarity.getTotalScore(weights) == 1);
	}
	
	@Test
	public void testRegex() {
		// regex101.com
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
	
	//TODO
	@Test
	public void testGetDoiFromUrl() {		
		String url = "https://www.some.url.com/10.1234/32743n3284/some/path";
		Assert.assertEquals("10.1234/32743n3284", BibtexEntrySimilarity.getDoiFromUrl(url));
		url = "https://www.some.url.com/10.1234.xy/32743n3284/some/path";
		Assert.assertEquals("10.1234.xy/32743n3284", BibtexEntrySimilarity.getDoiFromUrl(url));
	}
}
