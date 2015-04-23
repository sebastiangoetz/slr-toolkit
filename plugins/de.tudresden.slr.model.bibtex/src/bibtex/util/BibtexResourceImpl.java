/**
 */
package bibtex.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;

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

		Reader reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			BibTeXParser parser = new BibTeXParser();
			BibTeXDatabase db = parser.parse(reader);

			List<BibTeXObject> objects = db.getObjects();
			for (BibTeXObject bto : objects) {
				if (bto instanceof BibTeXEntry) {
					BibTeXEntry entry = (BibTeXEntry) bto;
					Document document = BibtexFactory.eINSTANCE
							.createDocument();

					if (entry.getField(BibTeXEntry.KEY_TITLE) != null) {
						document.setTitle(entry.getField(BibTeXEntry.KEY_TITLE)
								.toUserString());
					}
					if (entry.getField(BibTeXEntry.KEY_YEAR) != null) {
						document.setYear(entry.getField(BibTeXEntry.KEY_YEAR)
								.toUserString());
					}
					if (entry.getField(BibTeXEntry.KEY_MONTH) != null) {
						document.setMonth(entry.getField(BibTeXEntry.KEY_MONTH)
								.toUserString());
					}
					if (entry.getField(BibTeXEntry.KEY_MONTH) != null) {
						document.setDay(entry.getField(BibTeXEntry.KEY_MONTH)
								.toUserString());
					}
					getContents().add(document);
				}
				// TODO Check for more types later
			}

		} catch (TokenMgrException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options)
			throws IOException {

		File f = new File(uri.toString());
		FileInputStream inputStream = new FileInputStream(f);
		Reader reader = new BufferedReader(new InputStreamReader(inputStream));
		BibTeXParser parser;
		try {
			parser = new BibTeXParser();
			BibTeXDatabase db = parser.parse(reader);

			Map<Key, BibTeXEntry> entryMap = db.getEntries();

			List<BibTeXObject> objects = db.getObjects();
			for (BibTeXObject bto : objects) {
				if (bto instanceof BibTeXString) {

				} else if (bto instanceof BibTeXEntry) {
					BibTeXEntry bte = (BibTeXEntry) bto;
				}
			}

		} catch (TokenMgrException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

} // BibtexResourceImpl
