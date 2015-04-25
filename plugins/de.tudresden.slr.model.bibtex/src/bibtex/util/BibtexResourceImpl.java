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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;
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
		if (doc.getMonth() != null)
			result.addField(KEY_MONTH, new StringValue(doc.getMonth(),
					Style.QUOTED));
		return result;
	}
} // BibtexResourceImpl
