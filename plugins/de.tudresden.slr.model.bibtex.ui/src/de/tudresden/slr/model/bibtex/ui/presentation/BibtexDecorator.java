package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
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

	// load imagedescriptor only once
	static {
		DECORATOR = AbstractUIPlugin.imageDescriptorFromPlugin(
				"de.tudresden.slr.model.bibtex.ui", "icons/decorator.gif");
	}

	public BibtexDecorator() {
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
			IFile file = (IFile) element;
			Set<QualifiedName> names = null;
			try {
				names = file.getPersistentProperties().keySet();
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (names == null || names.isEmpty()) {
				return;
			}
			for (QualifiedName qName : names) {
				if (QUALIFIER.equals(qName.getQualifier())) {
					String content = null;
					try {
						content = file.getPersistentProperty(qName);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
					if (ERROR.equals(content)) {
						decoration.addOverlay(DECORATOR,
								IDecoration.BOTTOM_RIGHT);
						return;
					}
				}
			}
		} else if (element instanceof Document) {
			Document doc = (Document) element;
			IFile parent = Utils.getIFilefromDocument(doc);
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
			if (property != null && ERROR.equals(property)) {
				decoration.addOverlay(DECORATOR, IDecoration.BOTTOM_RIGHT);
				return;
			}
		}
	}

}
