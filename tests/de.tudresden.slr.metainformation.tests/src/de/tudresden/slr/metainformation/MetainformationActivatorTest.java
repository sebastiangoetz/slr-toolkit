package de.tudresden.slr.metainformation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;

public class MetainformationActivatorTest {
	
	@Mock
	SlrProjectMetainformation metainformation;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetSetMetainformation() {
		// check whether static functions to what they are supposed to
		MetainformationActivator.setMetainformation(metainformation);
		assertEquals(metainformation, MetainformationActivator.getMetainformation());
	}

	@Test
	public void testGetSetCurrentFilePath() {
		// check whether static functions to what they are supposed to
		String filepathString = "/home/tests/file.f";
		MetainformationActivator.setCurrentFilepath(filepathString);
		assertTrue(filepathString.equals(MetainformationActivator.getCurrentFilepath()));
	}
}
