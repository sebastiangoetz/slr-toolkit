package de.tudresden.slr.metainformation.data;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.impl.TermImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SlrProjectMetainformation {
	private String title = "";
	private String authors = "";
	private String keywords = "";
	private String projectAbstract = "";
	private String taxonomyDescription = "";
	private Map<TermImpl, String> dimensionDescriptions = new HashMap<TermImpl, String>();
	//private List<Author> authors = new ArrayList<Author>();


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

	public Map<TermImpl, String> getDimensionDescriptions() {
		return dimensionDescriptions;
	}

	@XmlElement
	public void setDimensionDescriptions(Map<TermImpl, String> dimensionDescriptions) {
		this.dimensionDescriptions = dimensionDescriptions;
	}
	
	public void addDimensionDescription(TermImpl term, String description) {
		dimensionDescriptions.put(term, description);
	}

}