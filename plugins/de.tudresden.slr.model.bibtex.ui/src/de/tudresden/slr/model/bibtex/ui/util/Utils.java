package de.tudresden.slr.model.bibtex.ui.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.util.TaxonomyToStringConverter;

/**
 * Helpful functions.
 * 
 * @author Manuel Brauer
 *
 */
public class Utils {
	private static Document lastDoc = null;
	private static StringBuffer logBuffer = new StringBuffer();

	/**
	 * Returns the resource file which contains the given bibtex document. Bases
	 * on {@link http://www.eclipse.org/forums/index.php?t=msg&th=128695/}
	 * 
	 * @param doc
	 *            {@link Document} whom's resource is wanted
	 * @return {@link IFile} which contains doc. <code>null</code> if nothing
	 *         was found.
	 */
	public static IFile getIFilefromDocument(Document doc) {
		if (doc == null) {
			// TODO: remove syso
			// System.err.println("Document " + doc
			// + " does not exist or has no resource");
			return null;
		}
		if (doc.equals(lastDoc)) {
		} else {
			// System.out.println("Document "
			// + ((lastDoc == null) ? null : lastDoc.getKey())
			// + "'s resource was called " + repetition + " times.");
			lastDoc = doc;
		}
		return getIFilefromEMFResource(doc.eResource());

	}

	/**
	 * Returns the resource file which contains the given bibtex document. Bases
	 * on {@link http://www.eclipse.org/forums/index.php?t=msg&th=128695/}
	 * 
	 * @param doc
	 *            {@link Document} whom's resource is wanted
	 * @return {@link IFile} which contains doc. <code>null</code> if nothing
	 *         was found.
	 */
	public static IFile getIFilefromEMFResource(Resource resource) {
		if (resource == null) {
			return null;
		}
		// TODO: remove logbuffer
		logBuffer = new StringBuffer();

		URI uri = resource.getURI();
		if(resource.getResourceSet() == null){
			return null;
		}
		uri = resource.getResourceSet().getURIConverter().normalize(uri);
		String scheme = uri.scheme();
		logBuffer.append("URI Scheme: " + scheme + "\n");

		if ("platform".equals(scheme) && uri.segmentCount() > 1
				&& "resource".equals(uri.segment(0))) {
			StringBuffer platformResourcePath = new StringBuffer();
			for (int j = 1, size = uri.segmentCount(); j < size; ++j) {
				platformResourcePath.append('/');
				platformResourcePath.append(uri.segment(j));
			}
			logBuffer.append("Platform path " + platformResourcePath.toString()
					+ "\n");

			IResource parent = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(new Path(platformResourcePath.toString()));
			// TODO: remove syso
			logBuffer.append("IResource " + parent);

			if (parent instanceof IFile) {
				return (IFile) parent;
			}
		}
		// System.out.println(logBuffer.toString());
		return null;

	}

	/**
	 * Create a marker concerning a bibtex document.
	 * 
	 * @param document
	 *            the bibtex entry
	 * @param message
	 *            This describes the intention of the marker
	 * @param sourceId
	 *            Optional. ID of the view/editor, where the marker comes from.
	 */
	public static void mark(Document document, String message, String sourceId) {
		IFile file = Utils.getIFilefromDocument(document);
		if (file == null) {
			return;
		}
		IMarker marker = null;
		try {
			marker = file.createMarker(IMarker.PROBLEM);
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(IMarker.LINE_NUMBER, 1);
		map.put(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
		map.put(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		map.put(IMarker.LOCATION, document.getKey());
		map.put(IMarker.MESSAGE, message);
		if (sourceId != null) {
			map.put(IMarker.SOURCE_ID, sourceId);
		}

		try {
			marker.setAttributes(map);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public static boolean isPersisted(Resource resource) {
		if (resource.getURI().isFile()) {
			File f = new File(resource.getURI().toFileString());
			return f.exists();
		}
		return false;
	}
	
	public static void updateBibtexFileForDocument(Document document) {
		IFile iFile = getIFilefromDocument(document);
		File file = iFile.getRawLocation().makeAbsolute().toFile();
		try {
			String content = new String(Files.readAllBytes(file.toPath()));
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String bibRef = document.getKey();
		Model taxonomy = document.getTaxonomy();
		String taxonomyRepresentation = TaxonomyToStringConverter.convertToBib(taxonomy);
		try (Scanner scanner = new Scanner(file)){
			StringBuilder result = new StringBuilder();
			String currentLine = null;
			while (scanner.hasNextLine()) {
				currentLine = scanner.nextLine();
				if (currentLine.contains(bibRef)) { 
					
				} else {
					result.append(currentLine).append("\n");
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
