package de.tudresden.slr.classification.dialog;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

import de.tudresden.slr.model.taxonomy.Term;

public class TermLabelProvider extends BaseLabelProvider implements ILabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Term) {
			return ((Term) element).getName();
		} else {
			return "";
		}
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

}
