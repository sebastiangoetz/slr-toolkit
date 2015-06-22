/**
 */
package de.tudresden.slr.model.bibtex.util;

import static org.jbibtex.BibTeXEntry.KEY_AUTHOR;
import static org.jbibtex.BibTeXEntry.KEY_DOI;
import static org.jbibtex.BibTeXEntry.KEY_MONTH;
import static org.jbibtex.BibTeXEntry.KEY_TITLE;
import static org.jbibtex.BibTeXEntry.KEY_URL;
import static org.jbibtex.BibTeXEntry.KEY_YEAR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.LaTeXObject;
import org.jbibtex.LaTeXParser;
import org.jbibtex.LaTeXPrinter;
import org.jbibtex.ObjectResolutionException;
import org.jbibtex.ParseException;
import org.jbibtex.StringValue;
import org.jbibtex.StringValue.Style;
import org.jbibtex.TokenMgrException;

import com.google.inject.Injector;

import de.tudresden.slr.model.TaxonomyStandaloneSetupGenerated;
import de.tudresden.slr.model.bibtex.BibtexFactory;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.TaxonomyFactory;
import de.tudresden.slr.model.taxonomy.Term;

/**
 * <!-- begin-user-doc --> The <b>Resource </b> associated with the package.
 * <!-- end-user-doc -->
 * 
 * @see de.tudresden.slr.model.bibtex.util.BibtexResourceFactoryImpl
 * @generated
 */
public class BibtexResourceImpl extends ResourceImpl {

	private static final Key KEY_ABSTRACT = new Key("abstract");
	private static final Key KEY_FILE = new Key("file");

	private static final Key KEY_CLASSES = new Key("classes");

	/**
	 * Creates an instance of the resource. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param uri
	 *            the URI of the new resource.
	 * @generated
	 */
	public BibtexResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options)
			throws IOException {

		try (Reader reader = new BufferedReader(new InputStreamReader(
				inputStream))) {
			BibTeXParser parser = new BibTeXParser();
			BibTeXDatabase db = parser.parse(reader);

			for (BibTeXObject bto : db.getObjects()) {
				if (bto instanceof BibTeXEntry) {
					BibTeXEntry entry = (BibTeXEntry) bto;
					Document document = BibtexFactory.eINSTANCE
							.createDocument();

					document.setKey(entry.getKey().toString());
					document.setType(entry.getType().toString());

					// TODO refactor, cleaner smaller code

					if (entry.getField(KEY_TITLE) != null) {
						document.setTitle(entry.getField(KEY_TITLE)
								.toUserString());
					}
					if (entry.getField(KEY_YEAR) != null) {
						document.setYear(entry.getField(KEY_YEAR)
								.toUserString());
					}
					if (entry.getField(KEY_MONTH) != null) {
						document.setMonth(entry.getField(KEY_MONTH)
								.toUserString());
					}
					if (entry.getField(KEY_AUTHOR) != null) {
						String unparsedAuthors = entry.getField(KEY_AUTHOR)
								.toUserString().replaceAll("\r", "");
						String authors = parseLaTeX(unparsedAuthors);

						document.setUnparsedAuthors(unparsedAuthors);
						for (String author : authors.split(" and ")) {
							document.getAuthors().add(author);
						}
					}
					if (entry.getField(KEY_DOI) != null) {
						document.setDoi(entry.getField(KEY_DOI).toUserString());
					}
					if (entry.getField(KEY_URL) != null) {
						document.setUrl(entry.getField(KEY_URL).toUserString());
					}
					if (entry.getField(KEY_ABSTRACT) != null) {
						document.setAbstract(entry.getField(KEY_ABSTRACT)
								.toUserString());
					}
					if (entry.getField(KEY_FILE) != null) {
						document.setFile(entry.getField(KEY_FILE)
								.toUserString());
					}
					if (entry.getField(KEY_CLASSES) != null) {
						Model model = parseClasses(entry.getField(KEY_CLASSES)
								.toUserString());
						document.setTaxonomy(model);
					} else {
						document.setTaxonomy(TaxonomyFactory.eINSTANCE
								.createModel());
					}
					getContents().add(document);
				}
			}
		} catch (TokenMgrException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Model parseClasses(String string) {
		TaxonomyStandaloneSetupGenerated setup = new TaxonomyStandaloneSetupGenerated();
		Injector injector = setup.createInjectorAndDoEMFRegistration();
		ResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		Resource resource = resourceSet.createResource(URI
				.createURI("tmp.taxonomy"));
		try {
			resource.load(
					new URIConverter.ReadableInputStream(string, "UTF-8"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!resource.getContents().isEmpty()
				&& resource.getContents().get(0) instanceof Model) {
			return (Model) resource.getContents().get(0);
		}
		return TaxonomyFactory.eINSTANCE.createModel();
	}

	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options)
			throws IOException {

		// check if resource is available and load it
		// check if elements in the resource already exist and renew them
		// store everything else
		BibTeXDatabase db = new BibTeXDatabase();
		ExtensibleURIConverterImpl converter = new ExtensibleURIConverterImpl();
		try (Reader reader = new InputStreamReader(converter.createInputStream(
				uri, options))) {
			BibTeXParser parser = new BibTeXParser();
			db = parser.parse(reader);
		} catch (IOException e) {
			// do nothing
		} catch (ObjectResolutionException | TokenMgrException | ParseException e) {
			e.printStackTrace();
		}

		Map<String, BibTeXEntry> entries = new HashMap<>();
		for (BibTeXObject bto : db.getObjects()) {
			if (bto instanceof BibTeXEntry) {
				BibTeXEntry entry = (BibTeXEntry) bto;
				entries.put(entry.getKey().toString(), entry);
			}
		}

		for (EObject e : getContents()) {
			if (e instanceof Document) {
				Document document = (Document) e;
				if (entries.containsKey(document.getKey())) {
					BibTeXEntry entry = entries.get(document.getKey());
					db.removeObject(entry);
					db.addObject(updateDocument(document, entry));
				} else {
					BibTeXEntry entry = new BibTeXEntry(new Key(
							document.getType()), new Key(document.getKey()));
					db.addObject(updateDocument(document, entry));
				}
			}
		}

		try (OutputStreamWriter out = new OutputStreamWriter(outputStream)) {
			BibTeXFormatter formatter = new BibTeXFormatter();
			formatter.format(db, out);
		}
	}

	private BibTeXEntry updateDocument(Document doc, BibTeXEntry entry) {
		BibTeXEntry result = new BibTeXEntry(entry.getType(), entry.getKey());
		result.addAllFields(entry.getFields());

		if (doc.getMonth() != null) {
			result.addField(KEY_MONTH, new StringValue(doc.getMonth(),
					Style.QUOTED));
		}

		if (doc.getAbstract() != null) {
			result.addField(KEY_ABSTRACT, new StringValue(doc.getAbstract(),
					Style.BRACED));
		}

		if (doc.getFile() != null) {
			result.addField(KEY_FILE, new StringValue(doc.getFile(),
					Style.BRACED));
		}

		if (!doc.getAuthors().isEmpty()) {
			result.addField(KEY_AUTHOR,
					new StringValue(doc.getUnparsedAuthors(), Style.BRACED));
		}

		if (doc.getTaxonomy() != null) {
			result.addField(KEY_CLASSES, new StringValue(serializeTaxonomy(doc
					.getTaxonomy().getDimensions()), Style.BRACED));
		}

		return result;
	}

	private String serializeTaxonomy(EList<Term> dimensions) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < dimensions.size(); ++i) {
			Term term = dimensions.get(i);
			if (i > 0) {
				result.append(", ");
			}
			result.append(term.getName());
			if (term.getSubclasses().size() > 0) {
				result.append("{");
				result.append(serializeTaxonomy(term.getSubclasses()));
				result.append("}");
			}
		}
		return result.toString();
	}

	private String parseLaTeX(String latexString) {
		String plainString = "";
		try {
			LaTeXParser parser = new LaTeXParser();
			List<LaTeXObject> latexObjects = parser.parse(latexString);
			LaTeXPrinter printer = new LaTeXPrinter();
			plainString = printer.print(latexObjects);
		} catch (TokenMgrException | ParseException e) {
			System.out.println(e.getMessage());
			return latexString;
		}
		return plainString;
	}

} // BibtexResourceImpl
