package de.tudresden.slr.classification.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tudresden.slr.classification.validators.MalformedTermNameHandlerImpl;

public class MalformedTermNameHandlerTest {

	@Test
	public void transformInvalidTermNamesTest() {
		MalformedTermNameHandlerImpl handler = new MalformedTermNameHandlerImpl();
		handler.setUseDefault(true);
		
		String[] testNames = {
				"1Starts with a number",
				"!Special @Characters@",
				"1Starts with a %number$ and special characters()?/.:",
				"Valid Name"
		};
		
		String[] expectedNames = {
				"T 1Starts with a number",
				"T Special Characters",
				"T 1Starts with a number and special characters",
				"Valid Name"
		};
		
		for(int i = 0; i<testNames.length;i++) {
			if(!handler.handleMalformedTermName(testNames[i]).equals(expectedNames[i])) fail();
		}
		
	}

}
