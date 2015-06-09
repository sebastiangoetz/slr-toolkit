package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.tudresden.slr.model.bibtex.Document;

public class BibtexDecorator implements ILightweightLabelDecorator {
	private static final ImageDescriptor DECORATOR;
	public static final String QUALIFIER = "de.tudresden.slr";

	static {
		DECORATOR = AbstractUIPlugin.imageDescriptorFromPlugin(
				"de.tudresden.slr.model.bibtex.ui", "icons/decorator.gif");
	}

	public BibtexDecorator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof IFile) {
			Set<QualifiedName> names = null;
			try {
				names = ((IFile) element).getPersistentProperties().keySet();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (names == null || names.isEmpty()) {
				return;
			}
			for (QualifiedName qName : names) {
				if (QUALIFIER.equals(qName.getQualifier())) {
					decoration.addOverlay(DECORATOR, IDecoration.BOTTOM_RIGHT);
					return;
				}
			}
		} else if (element instanceof Document) {
			Document doc = (Document) element;
			IFile parent = BibtexDecorator.getIFilefromDocument(doc);
			if (parent == null) {
				return;
			}
			// IResource parent = ResourcesPlugin.getWorkspace().getRoot()
			// .findMember(platformString);
			String property = null;
			try {
				property = parent.getPersistentProperty(new QualifiedName(
						QUALIFIER, doc.getKey()));
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (property == null || !("ERROR".equals(property))) {
				return;
			}
			decoration.addOverlay(DECORATOR, IDecoration.BOTTOM_RIGHT);
		}
	}

	public static IFile getIFilefromDocument(Document doc) {
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
}
