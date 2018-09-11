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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
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

import de.tudresden.slr.model.bibtex.BibtexFactory;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.TaxonomyFactory;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.util.TaxonomyStandaloneParser;

/**
 * <!-- begin-user-doc --> The <b>Resource </b> associated with the package.
 * <!-- end-user-doc -->
 * 
 * @see de.tudresden.slr.model.bibtex.util.BibtexResourceFactoryImpl
 * @generated
 */
public class BibtexResourceImpl extends ResourceImpl {
	private static final Key KEY_ABSTRACT = new Key("abstract");
	private static final Key KEY_FILE = new Key("howpublished");
	private static final Key KEY_CITES = new Key("citations");
	private static final Key KEY_CLASSES = new Key("classes");

	/**
	 * Creates an instance of the resource. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param uri the URI of the new resource.
	 * @generated
	 */
	public BibtexResourceImpl(URI uri) {
		super(uri);
	}

	private String safeGetField(BibTeXEntry entry, Key key) {
		if (entry.getField(key) != null) {
			return entry.getField(key).toUserString();
		}
		return "";
	}

	@Override
	protected void doLoad(InputStream in, Map<?, ?> options) throws IOException {

		Map<Key, BibTeXEntry> entryMap = Collections.emptyMap();
		try (Reader reader = new BufferedReader(new InputStreamReader(in))) {
			BibTeXParser parser = new BibTeXParser();
			BibTeXDatabase db = parser.parseFully(reader);
			entryMap = db.getEntries();
		} catch (TokenMgrException | ParseException e) {
			System.err.println(e.getMessage());
		}
		for (BibTeXEntry entry : entryMap.values()) {
			Document document = BibtexFactory.eINSTANCE.createDocument();

			document.setKey(entry.getKey().toString());
			document.setType(entry.getType().toString());
			document.setTitle(safeGetField(entry, KEY_TITLE));
			try {
				int cites = Integer.parseInt(safeGetField(entry, KEY_CITES));
				document.setCites(cites);
			} catch (NumberFormatException e) {
			}
			document.setYear(safeGetField(entry, KEY_YEAR));
			document.setMonth(safeGetField(entry, KEY_MONTH));
			String unparsedAuthors = safeGetField(entry, KEY_AUTHOR).replaceAll("\r", "");
			document.setUnparsedAuthors(unparsedAuthors);
			String authors = parseLaTeX(unparsedAuthors);
			for (String author : authors.split(" and ")) {
				if (!author.isEmpty()) {
					document.getAuthors().add(author);
				}
			}
			document.setDoi(safeGetField(entry, KEY_DOI));
			document.setUrl(safeGetField(entry, KEY_URL));
			document.setAbstract(safeGetField(entry, KEY_ABSTRACT));
			String url = safeGetField(entry, KEY_FILE);
			if (url.contains("url{")) {
				url = url.substring("\\url{".length());
				url = url.substring(0, url.length() - 1);
			}
			document.setFile(url);
			document.setUrl(url);
			document.setTaxonomy(parseClasses(safeGetField(entry, KEY_CLASSES)));

			String path = "/home/x/u/kp_literature/slr-toolkit/examples/Bachelorarbeit_Kevin_Horst/Library.bib";

			File f = new File(path);
			Scanner fileScanner = new Scanner(f);

			int lineNumber = 0;
			while (fileScanner.hasNextLine()) {
				lineNumber++;
				if (fileScanner.nextLine().contains(entry.getKey().toString())) {
					break;
				}
			}
			fileScanner.close();
			document.setLine(lineNumber);

			getContents().add(document);
		}
	}

	private Model parseClasses(String taxonomyString) {
		if (taxonomyString == null || taxonomyString.isEmpty()) {
			return TaxonomyFactory.eINSTANCE.createModel();
		}
		TaxonomyStandaloneParser tsp = new TaxonomyStandaloneParser();
		return tsp.parseTaxonomyText(taxonomyString);

	}

	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
		BibTeXDatabase db = new BibTeXDatabase();
		ExtensibleURIConverterImpl converter = new ExtensibleURIConverterImpl();
		try (Reader reader = new InputStreamReader(converter.createInputStream(uri, options))) {
			BibTeXParser parser = new BibTeXParser();
			db = parser.parse(reader);
		} catch (IOException e) {
			// do nothing
		} catch (ObjectResolutionException | TokenMgrException | ParseException e) {
			e.printStackTrace();
		}

		Map<Key, BibTeXEntry> entryMap = new HashMap<>(db.getEntries());
		List<Document> processedDocuments = new ArrayList<Document>();
		for (EObject e : getContents()) {
			if (e instanceof Document) {
				Document document = (Document) e;
				Key key = new Key(document.getKey());
				if (entryMap.containsKey(key)) {
					BibTeXEntry entry = entryMap.get(key);
					updateDocument(document, entry);
				} else {
					Key type = new Key(document.getType());
					BibTeXEntry entry = new BibTeXEntry(type, key);
					updateDocument(document, entry);
					db.addObject(entry);
				}
				processedDocuments.add(document);
			}
		}

		for (BibTeXEntry entry : entryMap.values()) {
			Iterator<Document> iter = processedDocuments.iterator();
			boolean found = false;
			while (iter.hasNext() && !found) {
				Document document = iter.next();
				Key key = new Key(document.getKey());
				if (entry.getKey().equals(key)) {
					found = true;
					iter.remove();
				}
			}
			if (!found) {
				db.removeObject(entry);
			}
		}

		try (OutputStreamWriter out = new OutputStreamWriter(outputStream)) {
			BibTeXFormatter formatter = new BibTeXFormatter();
			formatter.format(db, out);
		}
	}

	private void updateDocument(Document document, BibTeXEntry entry) {

		if (document.getMonth() != null && !document.getMonth().isEmpty()) {
			entry.addField(KEY_MONTH, new StringValue(document.getMonth(), Style.QUOTED));
		}

		if (document.getAbstract() != null && !document.getAbstract().isEmpty()) {
			entry.addField(KEY_ABSTRACT, new StringValue(document.getAbstract(), Style.BRACED));
		}
		if (document.getCites() > 0) {
			entry.addField(KEY_CITES, new StringValue(Integer.toString(document.getCites()), Style.BRACED));
		}

		if (document.getFile() != null && !document.getFile().isEmpty()) {
			entry.addField(KEY_FILE, new StringValue(document.getFile(), Style.BRACED));
		}

		if (!document.getAuthors().isEmpty()) {
			entry.addField(KEY_AUTHOR, new StringValue(document.getUnparsedAuthors(), Style.BRACED));
		}

		if (!document.getTaxonomy().getDimensions().isEmpty()) {
			entry.addField(KEY_CLASSES,
					new StringValue(serializeTaxonomy(document.getTaxonomy().getDimensions()), Style.BRACED));
		}
	}

	private String serializeTaxonomy(EList<Term> dimensions) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < dimensions.size(); ++i) {
			Term term = dimensions.get(i);
			if (i > 0 && term instanceof Term) {
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
