package de.tudresden.slr.util.stringSimilarity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringSimilarityTests {
	
	private static final String stringA = "this is string a";
	private static final String stringBnotEqualsA = "this is string b";
	
	@BeforeClass
	public static void assertUnequalInputStrings() {
		assertNotEquals(stringA, stringBnotEqualsA);
	}
	

	@Test
	public void testCosine() {
		Cosine cosineComparator = new Cosine();
		assertEquals(0.0, cosineComparator.distance(stringA, stringA), 0.0);
		assertNotEquals(0.0, cosineComparator.distance(stringA, stringBnotEqualsA));
	}
	
	@Test
	public void testJaroWinkler() {
		JaroWinkler cosineComparator = new JaroWinkler();
		assertEquals(0.0, cosineComparator.distance(stringA, stringA), 0.0);
		assertNotEquals(0.0, cosineComparator.distance(stringA, stringBnotEqualsA));
	}
}
