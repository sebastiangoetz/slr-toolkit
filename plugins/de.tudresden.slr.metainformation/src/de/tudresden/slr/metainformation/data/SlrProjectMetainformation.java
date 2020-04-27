package de.tudresden.slr.metainformation.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
/**
 * Represents the project's metainformation
 *
 */
public class SlrProjectMetainformation {
	@XmlElement
	private String title = "";
	@XmlElement
	private String keywords = "";
	@XmlElement
	private String projectAbstract = "";
	@XmlElement
	private String taxonomyDescription = "";
	@XmlElement
	private List<Author> authorsList;


	public List<Author> getAuthorsList() {
		return authorsList;
	}

	public void setAuthorsList(List<Author> authorsList) {
		this.authorsList = authorsList;
	}

	public SlrProjectMetainformation(String keywords, String projectAbstract,
			String taxonomyDescription, List<Author> authorsList) {
		super();
		this.keywords = keywords;
		this.projectAbstract = projectAbstract;
		this.taxonomyDescription = taxonomyDescription;
		this.authorsList = authorsList;
	}

	public SlrProjectMetainformation() {
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getProjectAbstract() {
		return projectAbstract;
	}

	public void setProjectAbstract(String projectAbstract) {
		this.projectAbstract = projectAbstract;
	}

	public String getTaxonomyDescription() {
		return taxonomyDescription;
	}

	public void setTaxonomyDescription(String taxonomyDescription) {
		this.taxonomyDescription = taxonomyDescription;
	}

	public String toString() {
		return this.authorsList + "; " +  this.keywords + "; " + this.projectAbstract + "; " + this.taxonomyDescription;
	}

}