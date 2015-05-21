/**
 */
package bibtex.util;

import static org.jbibtex.BibTeXEntry.*;

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
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.LaTeXObject;
import org.jbibtex.LaTeXParser;
import org.jbibtex.LaTeXPrinter;
import org.jbibtex.ParseException;
import org.jbibtex.StringValue;
import org.jbibtex.StringValue.Style;
import org.jbibtex.TokenMgrException;

import bibtex.BibtexFactory;
import bibtex.Document;

/**
 * <!-- begin-user-doc --> The <b>Resource </b> associated with the package.
 * <!-- end-user-doc -->
 * 
 * @see bibtex.util.BibtexResourceFactoryImpl
 * @generated
 */
public class BibtexResourceImpl extends ResourceImpl {

	private static final Key KEY_ABSTRACT = new Key("abstract");

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

					getContents().add(document);
				}
			}
		} catch (TokenMgrException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options)
			throws IOException {

		ExtensibleURIConverterImpl converter = new ExtensibleURIConverterImpl();
		try (Reader reader = new InputStreamReader(converter.createInputStream(
				uri, options))) {

			BibTeXParser parser = new BibTeXParser();
			BibTeXDatabase db = parser.parse(reader);
			Map<String, BibTeXEntry> entries = new HashMap<>();

			for (BibTeXObject bto : db.getObjects()) {
				if (bto instanceof BibTeXEntry) {
					BibTeXEntry entry = (BibTeXEntry) bto;
					entries.put(entry.getKey().toString(), entry);
				}
			}

			List<Document> dirtyDocuments = getContents()
					.stream()
					.filter(p -> {
						if (p instanceof Document) {
							Document document = (Document) p;
							return entries.keySet().contains(document.getKey());
						}
						return false;
					}).map(d -> (Document) d).collect(Collectors.toList());

			for (Document document : dirtyDocuments) {
				BibTeXEntry entry = entries.get(document.getKey());
				db.removeObject(entry);
				db.addObject(updateDocument(document, entry));
			}

			try (OutputStreamWriter out = new OutputStreamWriter(outputStream)) {
				BibTeXFormatter formatter = new BibTeXFormatter();
				formatter.format(db, out);
			}
		} catch (TokenMgrException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		if (!doc.getAuthors().isEmpty()) {
			result.addField(KEY_AUTHOR,
					new StringValue(doc.getUnparsedAuthors(), Style.BRACED));
		}
		return result;
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
