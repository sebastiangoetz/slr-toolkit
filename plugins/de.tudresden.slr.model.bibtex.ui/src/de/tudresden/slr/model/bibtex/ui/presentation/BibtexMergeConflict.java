package de.tudresden.slr.model.bibtex.ui.presentation;

import org.apache.commons.lang.StringUtils;

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
		if (existsButNotEquals(resource2.getDoi(), resource1.getDoi()))
			result += "\tdoi={" + resource2.getDoi() + "}\n";
		if (resource1.getTitle() != null)
			result += "\ttitle={" + resource1.getTitle() + "}\n";
		if (existsButNotEquals(resource2.getTitle(), resource1.getTitle()))
			result += "\ttitle={" + resource2.getTitle() + "}\n";
		if (resource1.getAuthors() != null)
			result += "\tauthors={" + StringUtils.join(resource1.getAuthors(), ",") + "}\n";
		if (existsButNotEquals(StringUtils.join(resource2.getAuthors(), ","), StringUtils.join(resource1.getAuthors(), ",")))
			result += "\tauthors={" + StringUtils.join(resource2.getAuthors(), ",") + "}\n";
		if (resource1.getUrl() != null)
			result += "\turl={" + resource1.getUrl() + "}\n";
		if (existsButNotEquals(resource2.getUrl(), resource1.getUrl()))
			result += "\turl={" + resource2.getUrl() + "}\n";
		if (resource1.getMonth() != null)
			result += "\tmonth={" + resource1.getMonth() + "}\n";
		if (existsButNotEquals(resource2.getMonth(), resource1.getMonth()))
			result += "\tmonth={" + resource2.getMonth() + "}\n";
		if (resource1.getYear() != null)
			result += "\tyear={" + resource1.getYear() + "}\n";
		if (existsButNotEquals(resource2.getYear(), resource1.getYear()))
			result += "\tyear={" + resource2.getYear() + "}\n";
		if (resource1.getAbstract() != null)
			result += "\tabstract={" + resource1.getAbstract() + "}\n";
		if (existsButNotEquals(resource2.getAbstract(), resource1.getAbstract()))
			result += "\tabstract={" + resource2.getAbstract() + "}\n";
		if (resource1.getKey() != null)
			result += "\tkey=={" + resource1.getKey() + "}\n";
		if (existsButNotEquals(resource2.getKey(), resource1.getKey()))
			result += "\tkey=={" + resource2.getKey() + "}\n";
		if (resource1.getType() != null)
			result += "\ttype={" + resource1.getType() + "}\n";
		if (existsButNotEquals(resource2.getType(), resource1.getType()))
			result += "\ttype={" + resource2.getType() + "}\n";
		result += "}";
		return result;
	}
	
	private boolean existsButNotEquals(String s1, String s2) {
		return (s1 != null && (s2 != null || !s1.equals(s2)));
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
	
	public boolean isConflicted() {
		return !((resource1.getTitle() != null && resource1.getTitle().equals(resource2.getTitle()) || resource1.getTitle() == null && resource2.getTitle() == null)
				&& (resource1.getAuthors() != null && resource1.getAuthors().equals(resource2.getAuthors()) || resource1.getAuthors() == null && resource2.getAuthors() == null)
				&& (resource1.getDoi() != null && resource1.getDoi().equals(resource2.getDoi()) || resource1.getDoi() == null && resource2.getDoi() == null)
				&& (resource1.getUrl() != null && resource1.getUrl().equals(resource2.getUrl()) || resource1.getUrl() == null && resource2.getUrl() == null)
				&& (resource1.getMonth() != null && resource1.getMonth().equals(resource2.getMonth()) || resource1.getMonth() == null && resource2.getMonth() == null)
				&& (resource1.getYear() != null && resource1.getYear().equals(resource2.getYear()) || resource1.getYear() == null && resource2.getYear() == null)
				&& (resource1.getType() != null && resource1.getType().equals(resource2.getType()) || resource1.getType() == null && resource2.getType() == null)
				&& (resource1.getAbstract() != null && resource1.getAbstract().equals(resource2.getAbstract()) || resource1.getAbstract() == null && resource2.getAbstract() == null)
				&& (resource1.getKey() != null && resource1.getKey().equals(resource2.getKey()) || resource1.getKey() == null && resource2.getKey() == null));
	}
}
