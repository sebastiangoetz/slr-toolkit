package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.ui.presentation.BibtexMergeData.Criteria;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class SimilarityMatrixTest {
	
	private BibtexMergeData mergeData;
	private Map<DocumentImpl, Map<DocumentImpl, BibtexEntrySimilarity>> similarityMatrix;
	
	@Before
	public void setUp() throws IOException {
		// read bibtex test file from resources
		File metainformationFile = new File("resources"+File.separator+"BibtexTest.bib");
		InputStream is = new FileInputStream(metainformationFile);
		
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
		System.out.println(mergeData == null);
		mergeData.createSimilarityMatrix();
		mergeData.setWeights(weights);
		similarityMatrix = mergeData.getSimilarityMatrix();
	}

	@Test
	public void testCreateSimilarityMatrix() {
		Assert.assertNotNull(similarityMatrix);
	}
	
//	@Test
//	public void testGetConflictsForWeights() {
//		Map<Criteria, Integer> weights = new HashMap<Criteria, Integer>() {{
//			put(Criteria.authors, 90);
//			put(Criteria.title, 90);
//			put(Criteria.year, 90);
//		}};
//		mergeData.setWeights(weights);
//		List<BibtexMergeConflict> conflicts = mergeData.getConflicts();
//		int firstSize = conflicts.size();
//		Assert.assertNotNull(conflicts);
//		
//		// as the weight for some criteria decreases there should be more similar entries
//		weights.put(Criteria.title, 0);
//		weights.put(Criteria.authors, 0);
//		weights.put(Criteria.year, 0);
//		mergeData.setWeights(weights);
//		conflicts = mergeData.getConflicts();
//		Assert.assertTrue(conflicts.size() > firstSize);
//	}
}
