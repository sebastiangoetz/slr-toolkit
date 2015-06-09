package de.tudresden.slr.model.bibtex.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tudresden.slr.model.bibtex.BibtexFactory;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexResourceFactoryImpl;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.TaxonomyFactory;
import de.tudresden.slr.model.taxonomy.Term;

public class BibtexTest {

	private ResourceSet resourceSet;
	private Resource resource;

	@BeforeClass
	public static void registerResourceFactory() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"bib", new BibtexResourceFactoryImpl());
	}

	@Before
	public void setUpFixtures() {
		resourceSet = new ResourceSetImpl();
		resource = resourceSet.createResource(URI.createURI("test.bib"));
	}

	@Test
	public void loadSingleDimensionWithoutSubTerms() throws Exception {
		final String bib = "@INPROCEEDINGS{Test01, classes = {test}}";

		resource.load(new URIConverter.ReadableInputStream(bib, "UTF-8"),
				Collections.EMPTY_MAP);

		assertThat(resource.getContents().get(0), instanceOf(Document.class));
		Document document = (Document) resource.getContents().get(0);
		Term term = document.getTaxonomy().getDimensions().get(0);
		assertThat(term.getName(), is("test"));
	}

	@Test
	public void storeSingleDimensionWithoutSubTerms() throws Exception {

		Model model = TaxonomyFactory.eINSTANCE.createModel();
		Term term = TaxonomyFactory.eINSTANCE.createTerm();

		term.setName("test");
		model.getDimensions().add(term);

		Document document = BibtexFactory.eINSTANCE.createDocument();
		document.setTaxonomy(model);
		document.setKey("test02");
		document.setType("article");

		StringWriter writer = new StringWriter();
		resource.getContents().add(document);
		resource.save(new URIConverter.WriteableOutputStream(writer, "UTF-8"),
				Collections.EMPTY_MAP);

		String bib = writer.toString();
		assertThat(bib, containsString("test02"));
	}

	@Test
	public void loadChangeAndStoreABibtexResource() throws Exception {
		final String bibIn = "@INPROCEEDINGS{Test01, classes = {test}}";
		StringWriter writer = new StringWriter();
		resource.load(new URIConverter.ReadableInputStream(bibIn, "UTF-8"),
				Collections.EMPTY_MAP);

		Term subTerm1 = TaxonomyFactory.eINSTANCE.createTerm();
		subTerm1.setName("sub-test1");
		Term subTerm2 = TaxonomyFactory.eINSTANCE.createTerm();
		subTerm2.setName("sub-test2");

		Document document = (Document) resource.getContents().get(0);
		Term term = document.getTaxonomy().getDimensions().get(0);
		term.getSubclasses().add(subTerm1);
		term.getSubclasses().add(subTerm2);

		resource.save(new URIConverter.WriteableOutputStream(writer, "UTF-8"),
				Collections.EMPTY_MAP);
		final String bibOut = writer.toString();
		assertThat(bibOut, containsString("sub-test1"));
		assertThat(bibOut, containsString("sub-test2"));
	}
} // BibtexTests
