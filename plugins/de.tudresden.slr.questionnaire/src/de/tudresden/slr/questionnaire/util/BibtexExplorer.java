package de.tudresden.slr.questionnaire.util;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.domain.EditingDomain;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;

public class BibtexExplorer {
	private EditingDomain domain;
	private IProject project;
	private List<IFile> bibtexFiles = new LinkedList<>();

	private static final String BIBTEXT_FILE_EXTENSION = "bib";

	public BibtexExplorer(IProject project) {
		if (project == null)
			throw new NullPointerException();
		this.project = project;
	}

	public List<Document> getDocuments() {
		bibtexFiles.clear();
		EclipseUtils.listProjectFiles(project).stream()
				.filter(file -> BIBTEXT_FILE_EXTENSION.equals(file.getFileExtension())).forEach(bibtexFiles::add);
		List<Document> documents = new LinkedList<>();
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent(d -> domain = d);
		if (domain == null)
			throw new NullPointerException();
		for (IFile file : bibtexFiles) {
			URI uri = URI.createURI(file.getFullPath().toString());
			BibtexResourceImpl o = (BibtexResourceImpl) domain.getResourceSet().getResource(uri, true);
			for (Object obj : o.getContents()) {
				if (obj instanceof Document)
					documents.add((Document) obj);
			}
		}
		documents.sort(new Comparator<Document>() {
			@Override
			public int compare(Document o1, Document o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		return documents;
	}
}