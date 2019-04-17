package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;

import info.debatty.java.stringsimilarity.*;

@RunWith(Parameterized.class)
public class StringSimilarityTest {

	private String s1, s2;

	public StringSimilarityTest(String s1, String s2) {
		this.s1 = s1;
		this.s2 = s2;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { 
			// authors
			{"Clevenger, Caroline M and Swamy, Surya", "John Haymaker, AIA and Swamy, LEEDAP2 Surya"},
			{"Wakelam, Robert and Beck, Henry and Phillips, Bradley and Powell, Gregory and O'Kelly, Niall", 
				"Wakelam, Robert Bruce and Beck III, Henry C and Phillips, Bradley Paul and Powell, Gregory Wayne and O'kelly, Niall Brendan"},
			{"Han, Kevin K and Golparvar-Fard, Mani", "Han, Kevin K and Golparvar-Fard, Mani"},
			{"Gaitan Cardona, Juan Sebastian", "Gait{\\'a}n Cardona, Juan Sebasti{\\'a}n and G{\\'o}mez Cabrera, Adriana"},
			
			// titles
			{"Automated monitoring of operation-level construction progress using 4D BIM and daily site photologs", 
				"Appearance-based material classification for monitoring of operation-level construction progress using 4D BIM and site photologs"},
			{"A Preliminary Study on the Framework and Technologies for Bridging BIM and Building", 
				"CM12-06 A Preliminary Study on the Framework and Technologies for Bridging BIM and Building"},
			{"Technology ontology and BIM-enabled estimating for owners and contractors", 
				"BIM-Enabled Estimating through Technology Ontology for Owners and Contractors"},
			{"Uso de la metodologia BrIM (Bridge Information Modeling) como herramientapara la planificacion de la construccion de un puente de concreto en Colombia", 
				"Uso de la metodolog{\\'\\i}a BRIM (Bridge Information Modeling) como herramienta para la planificaci{\\'o}n de la construcci{\\'o}n de un puente de concreto en Colombia"},
			{"", "BIM"}
		});
	}

	@Test
	public void testJaroWinkler() {
		System.out.println(BibtexEntrySimilarity.escapeLatexSpecialChars(s1));
		System.out.println(BibtexEntrySimilarity.escapeLatexSpecialChars(s2));
		JaroWinkler jw = new JaroWinkler();
		System.out.println("JaroWinkler: " + jw.similarity(BibtexEntrySimilarity.escapeLatexSpecialChars(s1), 
				BibtexEntrySimilarity.escapeLatexSpecialChars(s2)));
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
		System.out.println(BibtexEntrySimilarity.escapeLatexSpecialChars(s1));
		System.out.println(BibtexEntrySimilarity.escapeLatexSpecialChars(s2));
		System.out.println("Cosine: " + c.similarity(c.getProfile(BibtexEntrySimilarity.escapeLatexSpecialChars(s1)), 
				c.getProfile(BibtexEntrySimilarity.escapeLatexSpecialChars(s2))));
	}

	@Test
	public void testSorensenDice() {
		System.out.println("\n" + s1 + "\n" + s2 + "\n");
		SorensenDice s = new SorensenDice();
		System.out.println("SorensenDice: " + s.similarity(s1, s2));
	}
}
