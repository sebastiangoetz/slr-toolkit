package de.tudresden.slr.metainformation.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SlrProjectMetainformation {
	private String title = "";
	private String authors = "";
	private String keywords = "";
	private String projectAbstract = "";
	private String taxonomyDescription = "";


	public SlrProjectMetainformation(String authors, String keywords, String projectAbstract,
			String taxonomyDescription) {
		super();
		this.authors = authors;
		this.keywords = keywords;
		this.projectAbstract = projectAbstract;
		this.taxonomyDescription = taxonomyDescription;
	}

	public SlrProjectMetainformation() {
		// TODO Auto-generated constructor stub
	}
	
	public String getTitle() {
		return title;
	}

	@XmlElement
	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthors() {
		return authors;
	}

	@XmlElement
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getKeywords() {
		return keywords;
	}

	@XmlElement
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getProjectAbstract() {
		return projectAbstract;
	}

	@XmlElement
	public void setProjectAbstract(String projectAbstract) {
		this.projectAbstract = projectAbstract;
	}

	public String getTaxonomyDescription() {
		return taxonomyDescription;
	}

	@XmlElement
	public void setTaxonomyDescription(String taxonomyDescription) {
		this.taxonomyDescription = taxonomyDescription;
	}

	public String toString() {
		return this.authors + "; " +  this.keywords + "; " + this.projectAbstract + "; " + this.taxonomyDescription;
	}

}