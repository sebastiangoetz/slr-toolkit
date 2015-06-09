package de.tudresden.slr.model.bibtex.ui.presentation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.model.bibtex.Document;

class ViewLabelProvider extends LabelProvider{

	public ViewLabelProvider() {
		super();
	}

	@Override
	public String getText(Object obj) {
		if (obj instanceof IProject) {
			return ((IProject)obj).getName();
		}
		if (obj instanceof IFile) {
			return ((IFile)obj).getName();
		}
		if (obj instanceof Document) {
			return ((Document) obj).getKey();
		}
		return obj.toString();
	}

	@Override
	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof IProject) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		} else if (obj instanceof IFile) {
			imageKey = ISharedImages.IMG_OBJ_FILE;
		}

		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}