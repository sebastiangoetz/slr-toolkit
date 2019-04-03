package de.tudresden.slr.model.bibtex.ui.presentation;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;

public class BibtexMergeConflict {
	private DocumentImpl resource1;
	private DocumentImpl resource2;
	private DocumentImpl finalResource;
	
	public BibtexMergeConflict(DocumentImpl resource1, DocumentImpl resource2) {
		this.resource1 = resource1;
		this.resource2 = resource2;
	}
	
	public String printConflict() {
		String result = "{\n";
		if (resource1.getDoi() != null)
			result += "\tdoi={" + resource1.getDoi() + "}\n";
		if (resource2.getDoi() != null && (resource1.getDoi() == null || !resource1.getDoi().equals(resource2.getDoi())))
			result += "\tdoi={" + resource2.getDoi() + "}\n";
		if (resource1.getTitle() != null)
			result += "\ttitle={" + resource1.getTitle() + "}\n";
		if (resource2.getTitle() != null && (resource1.getTitle() == null || !resource1.getTitle().equals(resource2.getTitle())))
			result += "\ttitle={" + resource2.getTitle() + "}\n";
		if (resource1.getAuthors() != null)
			result += "authors={" + resource1.getAuthors() + "}\n";
		if (resource2.getAuthors() != null && (resource1.getAuthors() == null || !resource1.getAuthors().equals(resource2.getAuthors())))
			result += "authors={" + resource2.getAuthors() + "}\n";
		if (resource1.getUrl() != null)
			result += "url={" + resource1.getUrl() + "}\n";
		if (resource2.getUrl() != null && (resource1.getUrl() == null || !resource1.getUrl().equals(resource2.getUrl())))
			result += "url={" + resource2.getUrl() + "}\n";
		if (resource1.getMonth() != null)
			result += "month={" + resource1.getMonth() + "}\n";
		if (resource2.getMonth() != null && (resource1.getMonth() == null || !resource1.getMonth().equals(resource2.getMonth())))
			result += "month={" + resource2.getMonth() + "}\n";
		if (resource1.getYear() != null)
			result += "year={" + resource1.getYear() + "}\n";
		if (resource2.getYear() != null && (resource1.getYear() == null || !resource1.getYear().equals(resource2.getYear())))
			result += "year={" + resource2.getYear() + "}\n";
		if (resource1.getAbstract() != null)
			result += "abstract={" + resource1.getAbstract() + "}\n";
		if (resource2.getAbstract() != null && (resource1.getAbstract() == null || !resource1.getAbstract().equals(resource2.getAbstract())))
			result += "abstract={" + resource2.getAbstract() + "}\n";
		if (resource1.getKey() != null)
			result += "key=={" + resource1.getKey() + "}\n";
		if (resource2.getKey() != null && (resource1.getKey() == null || !resource1.getKey().equals(resource2.getKey())))
			result += "key=={" + resource2.getKey() + "}\n";
		if (resource1.getType() != null)
			result += "type={" + resource1.getType() + "}\n";
		if (resource2.getType() != null && (resource1.getType() == null || !resource1.getType().equals(resource2.getType())))
			result += "type={" + resource2.getType() + "}\n";
		result += "}";
		return result;
	}
	
	public void useResource1() {
		finalResource = resource1;
	}

	public void useResource2() {
		finalResource = resource2;
	}
	
	public DocumentImpl getResource1() {
		return resource1;
	}
	
	public void setResource1(DocumentImpl resource1) {
		this.resource1 = resource1;
	}
	
	public DocumentImpl getResource2() {
		return resource2;
	}
	
	public void setResource2(DocumentImpl resource2) {
		this.resource2 = resource2;
	}
	
	public DocumentImpl getFinalResource() {
		return finalResource;
	}
	
	public void setFinalResource(DocumentImpl finalResource) {
		this.finalResource = finalResource;
	}
}
