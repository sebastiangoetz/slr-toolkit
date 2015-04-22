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
import java.util.Collection;
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
import org.jbibtex.LaTeXObject;
import org.jbibtex.LaTeXParser;
import org.jbibtex.LaTeXPrinter;
import org.jbibtex.ParseException;
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

		Reader reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			BibTeXParser parser = new BibTeXParser();
			BibTeXDatabase db = parser.parse(reader);

			Map<Key, BibTeXEntry> entryMap = db.getEntries();

			Collection<BibTeXEntry> entries = entryMap.values();
			for (BibTeXEntry entry : entries) {
				org.jbibtex.Value value = entry.getField(BibTeXEntry.KEY_TITLE);
				if (value == null) {
					continue;
				}

				// Do something with the title value
				String plainTextString = "";
				String string = value.toUserString();
				if (string.indexOf('\\') > -1 || string.indexOf('{') > -1) {
					// LaTeX string that needs to be translated to plain text
					// string
					LaTeXParser latexParser = new LaTeXParser();

					List<LaTeXObject> latexObjects = latexParser.parse(string);

					LaTeXPrinter latexPrinter = new LaTeXPrinter();

					plainTextString = latexPrinter.print(latexObjects);
				}
				Document doc = BibtexFactory.eINSTANCE.createDocument();
				doc.getAuthors().add(plainTextString);
				this.getContents().add(doc);
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
			for(BibTeXObject bto : objects) {
				if(bto instanceof BibTeXString) {
					
				} else if(bto instanceof BibTeXEntry) {
					BibTeXEntry bte = (BibTeXEntry) bto;
				}
			}
			
		} catch (TokenMgrException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

} // BibtexResourceImpl
