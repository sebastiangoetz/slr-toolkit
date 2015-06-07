package de.tudresden.slr.model.bibtex.ui.presentation;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.dialogs.PatternFilter;

import de.tudresden.slr.model.bibtex.Document;

public class BibtexFilter extends PatternFilter {
	/**
	 * Check if the current (leaf) element is a match with the filter text. The
	 * default behavior checks that the label of the element is a match.
	 * 
	 * Subclasses should override this method.
	 * 
	 * @param viewer
	 *            the viewer that contains the element
	 * @param element
	 *            the tree element to check
	 * @return true if the given element's label matches the filter text
	 */
	@Override
	protected boolean isLeafMatch(Viewer viewer, Object element) {
		if (!(element instanceof Document)) {
			return false;
		}
		String labelText = ((ILabelProvider) ((StructuredViewer) viewer)
				.getLabelProvider()).getText(element);

		if (labelText == null) {
			return false;
		}
		return wordMatches(labelText) || contentMatches((Document) element);
	}

	/**
	 * Return whether or not if any property of the document matches the
	 * criteria.
	 * 
	 * @param element
	 * @return
	 */
	private boolean contentMatches(Document element) {
		for (String s : element.getAuthors()) {
			if (wordMatches(s)) {
				return true;
			}
		}
		return wordMatches(element.getAbstract());
	}
}
