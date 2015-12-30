package de.tudresden.slr.model.bibtex.ui.presentation;

import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.ui.serialization.DocumentStorage;
import de.tudresden.slr.model.bibtex.ui.serialization.DocumentStorageEditorInput;

/**
 * This Listener opens an editor for {@link DocumentImpl}. The editor is focused
 * when it is not reusable (like {@link BibtexOverviewEditor}) and there was an
 * {@link OpenEvent}, otherwise it is opened or updated without gaining focus.
 * 
 * @author Manuel Brauer
 *
 */
public class BibtexOpenListener implements IOpenListener,
		ISelectionChangedListener {
	private String id;
	private int match_flags;

	/**
	 * Constructor
	 * 
	 * @param editorId
	 *            - of the editor which shall be opened
	 * @param match_flags
	 *            - @see {@link IWorkbenchPage}, recommended: MATCH_ID for
	 *            {@link IReusableEditor}; otherwise MATCH_ID | MATCH_INPUT.
	 */
	public BibtexOpenListener(String editorId, int match_flags) {
		if (editorId == null || "".equals(editorId.trim())) {
			throw new IllegalArgumentException(
					"editorId must be the ID of an editor.");
		}
		this.id = editorId;
		this.match_flags = match_flags;

	}

	@Override
	public void open(OpenEvent event) {
		openEditor(event.getSelection(), true);
	}

	/**
	 * opens or reuses the editor which is set in the constructor.
	 * 
	 * @param selection
	 *            {@link StructuredSelection} of {@link DocumentImpl}
	 * @param activate
	 *            {@literal true} for gaining focus on the opened editor
	 */
	public void openEditor(ISelection selection, boolean activate) {
		if (!(selection instanceof StructuredSelection)) {
			return;
		}
		Object element = ((StructuredSelection) selection).getFirstElement();
		if (element == null || !(element instanceof DocumentImpl)) {
			return;
		}
		DocumentImpl document = (DocumentImpl) element;
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		DocumentStorage storage = new DocumentStorage(document);
		DocumentStorageEditorInput input = new DocumentStorageEditorInput(
				storage);
		IWorkbenchPage page = window.getActivePage();
		if (page != null) {
			IEditorReference[] editorRef = page.findEditors(null, id,
					IWorkbenchPage.MATCH_ID);
			IEditorPart editor;
			if (editorRef.length > 0) {
				editor = editorRef[0].getEditor(false);
				// if (editor != null)
				// page.closeEditor(editor, false);
				if (editor instanceof IReusableEditor) {
					page.reuseEditor((IReusableEditor) editor, input);
					page.bringToTop(editor);
					return;
				}
			}
			try {
				editor = page.openEditor(input, id, activate, match_flags);
				if (activate) {
					page.activate(editor);
				}
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		openEditor(event.getSelection(), false);
	}
}
