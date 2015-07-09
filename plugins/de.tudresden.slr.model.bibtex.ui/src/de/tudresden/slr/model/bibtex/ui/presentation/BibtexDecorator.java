package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.HashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.tudresden.slr.Utils;
import de.tudresden.slr.model.bibtex.Document;

/**
 * This Decorator is added to {@link IFile}s where the persisted property with
 * the {@link BibtexDecorator#QUALIFIER} is set with any local name but with
 * {@link BibtexDecorator#ERROR} as value. It also decorates {@link Document}s
 * whose key is used as local name in the persisted property of the parental
 * {@link IFile}.
 * 
 * @author Manuel Brauer
 *
 */
public class BibtexDecorator implements ILightweightLabelDecorator {
	private static final ImageDescriptor DECORATOR;
	public static final String QUALIFIER = "de.tudresden.slr";
	public static final String ERROR = "ERROR";
	private HashSet<ILabelProviderListener> listeners;

	// load imagedescriptor only once
	static {
		DECORATOR = AbstractUIPlugin.imageDescriptorFromPlugin(
				"de.tudresden.slr.model.bibtex.ui", "icons/decorator.gif");
	}

	public BibtexDecorator() {
		this.listeners = new HashSet<ILabelProviderListener>();
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		listeners.add(listener);
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		System.out.println(element + "\t" + property);
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof Resource) {
			IFile file = Utils.getIFilefromEMFResource((Resource) element);
			if (file == null) {
				return;
			}
			IMarker[] markers = null;
			try {
				markers = file.findMarkers(IMarker.PROBLEM, true,
						IResource.DEPTH_ONE);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (markers == null || markers.length == 0) {
				return;
			}
			for (IMarker marker : markers) {
				String location = marker.getAttribute(IMarker.LOCATION, null);
				if (location != null) {
					decoration.addOverlay(DECORATOR, IDecoration.BOTTOM_RIGHT);
					return;
				}
			}
		} else if (element instanceof Document) {
			Document doc = (Document) element;
			IFile parent = Utils.getIFilefromDocument(doc);
			if (parent == null) {
				return;
			}
			IMarker[] markers = null;
			try {
				markers = parent.findMarkers(IMarker.PROBLEM, true,
						IResource.DEPTH_ONE);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (markers == null || markers.length == 0) {
				return;
			}
			for (IMarker marker : markers) {
				if (doc.getKey().equals(
						marker.getAttribute(IMarker.LOCATION, null))) {
					decoration.addOverlay(DECORATOR, IDecoration.BOTTOM_RIGHT);
					return;
				}
			}
		}
	}
}
