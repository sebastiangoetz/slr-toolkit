package de.tudresden.slr.questionnaire;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;

import de.tudresden.slr.model.bibtex.Document;

public class BibtexEntryWatcher {

	private ISelectionService selectionService;
	private ISelectionListener selectionListener;
	private List<Consumer<Document>> listeners = new LinkedList<>();

	public BibtexEntryWatcher(ISelectionService selectionService) {
		this.selectionService = selectionService;
		this.selectionListener = new ISelectionListener() {
			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				List<Document> documents = new LinkedList<Document>();

				if (selection instanceof IStructuredSelection)
					for (Object element : ((IStructuredSelection) selection).toList())
						if (element instanceof Document)
							documents.add((Document) element);

				final Document doc;
				if (documents.size() == 1)
					doc = documents.get(0);
				else
					doc = null;
				listeners.stream().forEach(it -> it.accept(doc));
			}
		};
		selectionService.addPostSelectionListener(selectionListener);
	}

	public void dispose() {
		selectionService.removePostSelectionListener(selectionListener);
	}

	public void addDocumentListener(Consumer<Document> listener) {
		listeners.add(listener);
	}

}
