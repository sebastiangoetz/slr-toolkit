package de.tudresden.slr.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import bibtex.presentation.BibtexEditor;
import bibtex.presentation.BibtexEntryEditor;
import de.tudresden.slr.model.taxonomy.ui.views.TaxonomyCheckboxListView;

public class PerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		defineLayout(layout);
	}

	private void defineLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		// layout.setEditorAreaVisible(true);

		IFolderLayout left = layout.createFolder("slr.left", IPageLayout.LEFT,
				0.25f, editorArea);
		left.addView(IPageLayout.ID_PROJECT_EXPLORER);
		left.addView(BibtexEditor.ID);

		IFolderLayout bottom = layout.createFolder("slr.bottom",
				IPageLayout.BOTTOM, 0.65f, editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);

		IFolderLayout middle = layout.createFolder("slr.middle",
				IPageLayout.TOP, 0.3f, "slr.bottom");
		// middle.addPlaceholder(IPageLayout.ID_EDITOR_AREA);
		middle.addPlaceholder(BibtexEntryEditor.ID);

		IFolderLayout right = layout.createFolder("slr.right",
				IPageLayout.RIGHT, 0.75f, editorArea);
		right.addView(TaxonomyCheckboxListView.ID);
	}
}
