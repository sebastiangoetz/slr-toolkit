package de.tudresden.slr.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		defineLayout(layout);
	}

	private void defineLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		//layout.setEditorAreaVisible(true);

		IFolderLayout left = layout.createFolder("slr.left", IPageLayout.LEFT,
				0.25f, editorArea);
		left.addView(IPageLayout.ID_PROJECT_EXPLORER);
		
		IFolderLayout bottom = layout.createFolder("slr.bottom",
				IPageLayout.BOTTOM, 0.65f, editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);

		IFolderLayout middle = layout.createFolder("slr.middle", IPageLayout.TOP,
				0.3f, "slr.bottom");
		middle.addView(IPageLayout.ID_EDITOR_AREA);
//		middle.addPlaceholder("de.tudresden.slr.ui.bibtex.view");

		IFolderLayout right = layout.createFolder("slr.right", IPageLayout.RIGHT,
				0.75f, editorArea);
		right.addView(IPageLayout.ID_OUTLINE);
	}
}
