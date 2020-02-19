package de.tudresden.slr.latexexport.latexgeneration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SlrLatexGeneratorTest {
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	SlrLatexGenerator generator;
		
	@Test
	public void testGetResourceContentAsString() throws IOException {
		String fileContent = "hello there";
		
		File file = tempFolder.newFile();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(fileContent.getBytes());
		
		generator = new SlrLatexGenerator();
		
		String fileRead = generator.getResourceContentAsString(file.toURI().toURL());
		
		assertEquals(System.getProperty("line.separator")+fileContent, fileRead);
	}

}
