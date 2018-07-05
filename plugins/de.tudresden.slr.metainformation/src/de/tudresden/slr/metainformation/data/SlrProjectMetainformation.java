package de.tudresden.slr.metainformation.data;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.impl.TermImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SlrProjectMetainformation {
	@XmlElement
	private String title = "";
	@XmlElement
	private String authors = "";
	@XmlElement
	private String keywords = "";
	@XmlElement
	private String projectAbstract = "";
	@XmlElement
	private String taxonomyDescription = "";
	@XmlElement
	private Map<TermImpl, String> dimensionDescriptions = new HashMap<TermImpl, String>();
	@XmlElement
	private List<Author> authorsList = new ArrayList<Author>();


	public List<Author> getAuthorsList() {
		return authorsList;
	}

	public void setAuthorsList(List<Author> authorsList) {
		this.authorsList = authorsList;
	}

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

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
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
		return this.authors + "; " +  this.keywords + "; " + this.projectAbstract + "; " + this.taxonomyDescription;
	}

	public Map<TermImpl, String> getDimensionDescriptions() {
		return dimensionDescriptions;
	}

	public void setDimensionDescriptions(Map<TermImpl, String> dimensionDescriptions) {
		this.dimensionDescriptions = dimensionDescriptions;
	}
	
	public void addDimensionDescription(TermImpl term, String description) {
		dimensionDescriptions.put(term, description);
	}

}