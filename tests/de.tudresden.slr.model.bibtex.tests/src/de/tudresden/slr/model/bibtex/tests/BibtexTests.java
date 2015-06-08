package de.tudresden.slr.model.bibtex.tests;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexResourceFactoryImpl;
import de.tudresden.slr.model.taxonomy.Term;

public class BibtexTests {

	@BeforeClass
	public static void registerResourceFactory() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"bib", new BibtexResourceFactoryImpl());
	}

	@Test
	public void loadSingleDimensionWithoutSubTerms() throws Exception {
		final String bib = "@INPROCEEDINGS{Test01, classes = {test}}";

		ResourceSet resourceSet = new ResourceSetImpl();

		Resource resource = resourceSet.createResource(URI
				.createURI("test01.bib"));
		resource.load(new URIConverter.ReadableInputStream(bib, "UTF-8"), null);

		assertThat(resource.getContents().get(0), instanceOf(Document.class));
		Document document = (Document) resource.getContents().get(0);
		Term term = document.getTaxonomy().getDimensions().get(0);
		assertThat(term.getName(), is("test"));
	}
} // BibtexTests
