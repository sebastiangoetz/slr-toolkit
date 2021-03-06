package de.tudresden.slr.model.bibtex.ui.presentation;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.PartInitException;

import de.tudresden.slr.model.bibtex.ui.serialization.DocumentStorageEditorInput;

public class BibtexOverviewEditor extends BibtexEditor implements IReusableEditor {
	public static final String ID = "de.tudresden.slr.model.bibtex.presentation.BibtexOverviewEditor";
	public static final String name = "Bibtex Overview";
	private boolean init;

	public BibtexOverviewEditor() {
		super();
		init = true;
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		super.init(site, editorInput);
		this.setPartName(name);
		init = false;
	}

	/*
	 * public void setFocus() { new BibtexOpenListener(BibtexEditor.ID,
	 * IWorkbenchPage.MATCH_INPUT | IWorkbenchPage.MATCH_ID) .openEditor(new
	 * StructuredSelection(this.document), true); }
	 */
	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
		if (!init && input instanceof DocumentStorageEditorInput) {
			extractDocument(input);
			this.setPartName(name);
			int page = this.getActivePage();
			for (int i = this.getPageCount() - 1; i >= 0; i--) {
				this.removePage(i);
			}
			this.createPages();
			this.setActivePage(page);
		}
	}

	@Override
	protected void createPages() {
		createAbstractPage();
	}
}

