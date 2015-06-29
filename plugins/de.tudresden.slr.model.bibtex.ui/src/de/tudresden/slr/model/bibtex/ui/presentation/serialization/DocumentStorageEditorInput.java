package de.tudresden.slr.model.bibtex.ui.presentation.serialization;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public class DocumentStorageEditorInput implements IStorageEditorInput {

	private DocumentStorage storage;

	public DocumentStorageEditorInput(DocumentStorage ds) {
		storage = ds;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return storage.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "BibTeX entry for document " + getName();
	}

	@SuppressWarnings(value = { "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == DocumentStorageEditorInput.class) {
			DocumentStorageEditorInput adaptedStorage = new DocumentStorageEditorInput(
					storage);
			return adaptedStorage;
		}
		return null;
	}

	@Override
	public DocumentStorage getStorage() throws CoreException {
		return storage;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof DocumentStorageEditorInput) {
			return getName().equals(
					((DocumentStorageEditorInput) obj).getName());
		}
		return false;
	}
}
