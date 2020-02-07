package de.tudresden.slr.latexexport.helpers;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileHelperTest {


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void extractFolderFromFilepathTest() {
		String invalidFilepath = "randomStringWithoutFilepathSeparator";
		
		String validFileName = "file.end";
		String validPathWithoutFile = "home" + File.separator + "user";
		String validFilepath = validPathWithoutFile + File.separator + validFileName;
		
		// null should return null
		assertNull(FileHelper.extractFolderFromFilepath(null));
		
		// String which does not represent valid filepath (no file separators in path)
		assertNull(FileHelper.extractFolderFromFilepath(invalidFilepath));
		
		// String which represents filepath should return file
		assertEquals(validPathWithoutFile, FileHelper.extractFolderFromFilepath(validFilepath));
	}
	
	@Test
	public void extractFileNameFromURLTest() throws MalformedURLException {
		// null should return null
		assertNull(FileHelper.extractFileNameFromURL(null));
		
		// test with valid url
		String validFileName = "file.end";
		String urlWithFilename = "http://domain.tld/" + validFileName;
		URL validUrl = new URL(urlWithFilename);
		assertEquals(validFileName, FileHelper.extractFileNameFromURL(validUrl));
	}

}
