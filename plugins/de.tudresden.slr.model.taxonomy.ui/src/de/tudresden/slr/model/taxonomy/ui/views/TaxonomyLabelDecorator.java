package de.tudresden.slr.model.taxonomy.ui.views;

import java.util.Map;

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;

public class TaxonomyLabelDecorator implements ILabelDecorator {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image decorateImage(Image image, Object element) {
		return null;
	}

	@Override
	public String decorateText(String text, Object element) {
		int count = 0;
		if(element instanceof Term) {
			Map<Document, Term> termsInDocuments = SearchUtils.findDocumentsWithTerm((Term)element);
			return text + " ("+termsInDocuments.size()+")";
		}
		return text + " ("+count+")";
	}

}
