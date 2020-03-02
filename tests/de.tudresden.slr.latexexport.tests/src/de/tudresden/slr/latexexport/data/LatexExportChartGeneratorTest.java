package de.tudresden.slr.latexexport.data;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public class LatexExportChartGeneratorTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	private DataProvider dataProvider;

	@Before
	public void setUp() throws Exception {		
		EList<Term> mainDimensions = new BasicEList<>();
		EList<Term> subDimensions = new BasicEList<>();
		
		Term termA = Mockito.mock(Term.class);
		Mockito.when(termA.getName()).thenReturn("termA");
		Mockito.when(termA.getSubclasses()).thenReturn(subDimensions);
		mainDimensions.add(termA);
		
		Term termB = Mockito.mock(Term.class);
		Mockito.when(termB.getName()).thenReturn("termB");
		//subDimensions.add(termB);
		
		dataProvider = Mockito.mock(DataProvider.class);
		Mockito.when(dataProvider.getMainDimensions()).thenReturn(mainDimensions);
	}

	@Test
	public void testTexAndImageFolderAreCreated() throws IOException {
		// renderer dv.JPG not available in jenkins build
		File targetFile = tempFolder.newFile("output.texFileEnding");
		LatexExportChartGenerator.generatePDFOutput(targetFile.toString(), dataProvider);
		
		File texFile = null;
		File imagesFolder = null;

		for(File f : tempFolder.getRoot().listFiles()) {
			if(f.getName().contains("texFileEnding")) {
				texFile = f;
			}
			
			if(f.getName().contains("images")) {
				imagesFolder = f;
			}
		}
		
		// assert that there are the tex file, the images folder and exactly one image
		assertNotNull(texFile);
		assertNotNull(imagesFolder);
	}

}
