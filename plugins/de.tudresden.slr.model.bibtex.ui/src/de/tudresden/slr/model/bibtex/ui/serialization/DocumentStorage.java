package de.tudresden.slr.model.bibtex.ui.serialization;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import de.tudresden.slr.model.bibtex.Document;

public class DocumentStorage implements IStorage {
	private Document document;

	public DocumentStorage(Document document) {
		this.document = document;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
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

	public Document getDocument() {
		return document;
	}
}
