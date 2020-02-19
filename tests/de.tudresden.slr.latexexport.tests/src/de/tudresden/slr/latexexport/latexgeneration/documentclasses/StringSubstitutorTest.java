package de.tudresden.slr.latexexport.latexgeneration.documentclasses;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.junit.Test;

/*
 * This class tests the string substitutor, which is used in all of the inheriting template classes.
 */
public class StringSubstitutorTest {
	@Test
	public void testStringSubstitution() throws MalformedURLException {
		Map<String, String> subMap = new HashMap<>();
		String afterChange = "afterChange";
		subMap.put(SlrLatexTemplate.SLRVARIABLE_ABSTRACT, afterChange);

		String dontChangeMeString = "dontChangeMe";
		String oldString = "${" + SlrLatexTemplate.SLRVARIABLE_ABSTRACT + "}" + "," + dontChangeMeString;

		StrSubstitutor sub = new StrSubstitutor(subMap);
		String newString = sub.replace(oldString);

		assertFalse(newString.contains(SlrLatexTemplate.SLRVARIABLE_ABSTRACT));
		assertTrue(newString.contains(dontChangeMeString));
	}

}
