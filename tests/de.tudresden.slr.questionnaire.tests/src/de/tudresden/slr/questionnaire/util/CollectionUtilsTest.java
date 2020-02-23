package de.tudresden.slr.questionnaire.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CollectionUtilsTest {
	private List<String> emptyCollection;
	private List<String> listThreeElements;
	private String relevantObject;

	@Before
	public void setUp() throws Exception {
		relevantObject = "myString";
		
		emptyCollection = new ArrayList<>();
		
		listThreeElements = new ArrayList<>();
		listThreeElements.add("1");
		listThreeElements.add(relevantObject);
		listThreeElements.add("3");
	}

	@Test
	public void testMoveUp() {
		// empty collection not in list should return false
		assertFalse(CollectionUtils.moveUp(emptyCollection, relevantObject));
		
		// moving relevantObject up should move it to index 0
		CollectionUtils.moveUp(listThreeElements, relevantObject);
		assertEquals(0, listThreeElements.indexOf(relevantObject));
	}

	@Test
	public void testMoveDown() {
		// empty collection not in list should return false
		assertFalse(CollectionUtils.moveDown(emptyCollection, relevantObject));
		
		// moving relevantObject up should move it to index 2
		CollectionUtils.moveDown(listThreeElements, relevantObject);
		assertEquals(2, listThreeElements.indexOf(relevantObject));
	}

}
