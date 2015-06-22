package de.tudresden.slr;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;

import de.tudresden.slr.model.bibtex.Document;

/**
 * Helpful functions.
 * 
 * @author Manuel Brauer
 *
 */
public class Utils {
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
		if (doc == null || doc.eResource() == null) {
			return null;
		}
		URI uri = doc.eResource().getURI();
		uri = doc.eResource().getResourceSet().getURIConverter().normalize(uri);
		String scheme = uri.scheme();
		if ("platform".equals(scheme) && uri.segmentCount() > 1
				&& "resource".equals(uri.segment(0))) {
			StringBuffer platformResourcePath = new StringBuffer();
			for (int j = 1, size = uri.segmentCount(); j < size; ++j) {
				platformResourcePath.append('/');
				platformResourcePath.append(uri.segment(j));
			}
			IResource parent = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(new Path(platformResourcePath.toString()));
			if (parent instanceof IFile) {
				return (IFile) parent;
			}
		}
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
}
