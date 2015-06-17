package de.tudresden.slr.model.bibtex.ui.presentation.serialization;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;

public class DocumentStorage implements IStorage {
	private DocumentImpl document;

	public DocumentStorage(DocumentImpl document) {
		this.document = document;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public InputStream getContents() throws CoreException {
		return new ByteArrayInputStream(document.getKey().getBytes());
	}

	@Override
	public IPath getFullPath() {
		return null;
	}

	@Override
	public String getName() {
		return document.getKey();
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	public DocumentImpl getDocument() {
		return document;
	}
}
