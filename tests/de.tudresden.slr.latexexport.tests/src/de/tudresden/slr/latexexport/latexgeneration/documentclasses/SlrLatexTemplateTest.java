package de.tudresden.slr.latexexport.latexgeneration.documentclasses;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SlrLatexTemplateTest {

	private SlrLatexTemplate template;
	
	@Before
	public void setUp() throws Exception {
		template = Mockito.mock(SlrLatexTemplate.class, Mockito.CALLS_REAL_METHODS);
	}

	@Test
	public void testGenerateSectionTemplate() {
		assertEquals("\\section{name}", template.generateSectionTemplate("name"));
	}

	@Test
	public void testGenerateImageFigure() {
		String expected = "\\begin{figure}[!htb]\r\n" + 
				"\\centering\r\n" + 
				"\\includegraphics[width = 0.8\\textwidth]{name}\r\n" + 
				"\\caption{caption}\r\n" + 
				"\\end{figure}\r\n";
		assertEquals(expected, template.generateImageFigure("name", "caption", 0.8));
	}
}
