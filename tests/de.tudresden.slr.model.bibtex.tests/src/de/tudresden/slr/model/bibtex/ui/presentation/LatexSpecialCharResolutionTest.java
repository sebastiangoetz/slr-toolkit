package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LatexSpecialCharResolutionTest {

	private String input, expected;

	public LatexSpecialCharResolutionTest(String input, String expected) {
		this.input = input;
		this.expected = expected;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { 
			{"{\\'\\i}", "i"},
			{"{\\\"A}", "Ae"},
			{"\\\"U", "Ue"},
			{"\\ss", "ss"},
			{"\"u", "ue"}
		});
	}

	@Test
	public void test() {
		if (!expected.equals(BibtexEntrySimilarity.escapeLatexSpecialChars(input)))
			System.out.println("expected " + expected + " but was " + BibtexEntrySimilarity.escapeLatexSpecialChars(input));
		Assert.assertTrue(expected.equals(BibtexEntrySimilarity.escapeLatexSpecialChars(input)));
	}
}
