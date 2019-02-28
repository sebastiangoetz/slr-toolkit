package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;

import info.debatty.java.stringsimilarity.*;

@RunWith(Parameterized.class)
public class AuthorStringSimilarityTest {

	private String s1, s2;

	public AuthorStringSimilarityTest(String s1, String s2) {
		this.s1 = s1;
		this.s2 = s2;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { 
			{"Akin, Ömer", "Akn, Ömer"}, 
			{"Akin, Ömer", "Akn, Omer"},
			{"Arayici, Y", "Arayici, Yusuf"},
			{"Beck III, Henry C", "Beck, Henry"},
			{"Eastman, CM", "Eastman, Charles M"},
			{"Eastman, CM", "Eastman, Chuck"},
			{"Eastman, Charles M", "Eastman, Chuck"},
			{"Fuller, Pierre", "Fuller, Pierre Pierre Henri"},
			{"Fieller, Peter", "Fuller, Pierre Pierre Henri"}
		});
	}

	@Test
	public void testJaroWinkler() {
		JaroWinkler jw = new JaroWinkler();
		System.out.println("JaroWinkler: " + jw.similarity(s1, s2));
	}

	@Test
	public void testNormalizedLevenshtein() {
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		System.out.println("Levenshtein: " + l.distance(s1, s2));
	}

	@Test
	public void testDamerau() {
		Damerau d = new Damerau();
		System.out.println("Damerau: " + d.distance(s1, s2));
	}

	@Test
	public void testNGram() {
		NGram n = new NGram(5);
		System.out.println("NGram: " + n.distance(s1, s2));
	}

	@Test
	public void testQGram() {
		QGram q = new QGram(2);
		System.out.println("QGram: " + q.distance(s1, s2));
	}

	@Test
	public void testCosine() {
		Cosine c = new Cosine(2);
		System.out.println("Cosine: " + c.similarity(c.getProfile(s1), c.getProfile(s2)));
	}

	@Test
	public void testSorensenDice() {
		System.out.println("\n" + s1 + "\n" + s2 + "\n");
		SorensenDice s = new SorensenDice();
		System.out.println("SorensenDice: " + s.similarity(s1, s2));
	}
}
