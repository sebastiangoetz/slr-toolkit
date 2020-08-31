package de.tudresden.slr.classification.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.tudresden.slr.classification.validators.ITermNameValidator;
import de.tudresden.slr.classification.validators.TermNameValidatorImpl;

public class TermNameValidatorTests {

	@Test
	public void ValidNamesTest() {
		ITermNameValidator validator = new TermNameValidatorImpl();
		List<String> names = new ArrayList<String>();
		names.add("Term");
		names.add("Term with spaces");
		names.add("TermNumber1");
		names.add("Term with numbers like 1 and spaces");
		
		for(String name: names) {
			if(!validator.isTermNameValid(name)) fail();
		}
	}
	@Test
	public void InvalidNamesTest() {
		ITermNameValidator validator = new TermNameValidatorImpl();
		List<String> names = new ArrayList<String>();
		names.add("1 starts with a number");
		names.add("Inv@lid name");
		names.add("%$&=?");
		names.add("valid start but %&=?");
		
		
		for(String name: names) {
			if(validator.isTermNameValid(name)) fail();
		}
	}

}
