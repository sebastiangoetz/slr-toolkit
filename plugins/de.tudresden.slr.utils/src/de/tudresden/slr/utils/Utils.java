package de.tudresden.slr.utils;

import de.tudresden.slr.model.bibtex.Document;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

public class Utils {

	protected static Document lastDoc = null;
	protected static StringBuffer logBuffer = new StringBuffer();

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

	public static void mark(Document document, String message, String path, String path2, String sourceId) {
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
		map.put(IMarker.LINE_NUMBER, document.getLine()); 
		map.put(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
		map.put(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		map.put(IMarker.LOCATION, document.getKey());
		map.put(IMarker.MESSAGE, message);

		map.put("PATH", path);
		map.put("PATH2", path2);

		if (sourceId != null) {
			map.put(IMarker.SOURCE_ID, sourceId);
		}

		try {
			marker.setAttributes(map);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
		

	public static void unmark(Document document) {
		IFile file = Utils.getIFilefromDocument(document);
		try {
			file.deleteMarkers(null, true, 42); //TODO
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
}
