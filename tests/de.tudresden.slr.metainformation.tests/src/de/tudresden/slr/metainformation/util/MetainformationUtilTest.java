package de.tudresden.slr.metainformation.util;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import de.tudresden.slr.metainformation.data.Author;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;

public class MetainformationUtilTest {

	@Test
	public void testGetMetainformationFromFile() throws JAXBException {
		File metainformationFile = new File("resources"+File.separator+"metainformationTestfile.slrproject");
		SlrProjectMetainformation metainformation = MetainformationUtil.getMetainformationFromFile(metainformationFile);

		assertEquals("myKeywords", metainformation.getKeywords());
		assertEquals("myDescription", metainformation.getTaxonomyDescription());
		assertEquals("myTitle", metainformation.getTitle());
		assertEquals("myAbstract", metainformation.getProjectAbstract());
		Author author = metainformation.getAuthorsList().get(0);
		assertEquals("myName", author.getName());
		assertEquals("myMail", author.getEmail());
		assertEquals("myOrganisation", author.getOrganisation());
	}

}
