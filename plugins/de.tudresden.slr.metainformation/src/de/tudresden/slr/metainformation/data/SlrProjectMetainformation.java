package de.tudresden.slr.metainformation.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SlrProjectMetainformation {
	private String authors;
	private String keywords;
	private String projectAbstract;
	private String taxonomyDescription;
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public SlrProjectMetainformation(String authors, String keywords, String projectAbstract,
			String taxonomyDescription, PropertyChangeSupport propertyChangeSupport) {
		super();
		this.authors = authors;
		this.keywords = keywords;
		this.projectAbstract = projectAbstract;
		this.taxonomyDescription = taxonomyDescription;
		this.propertyChangeSupport = propertyChangeSupport;
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void setAuthors(String authors) {
		propertyChangeSupport.firePropertyChange("authors", this.authors, this.authors = authors);
	}
	
	public void setKeywords(String keywords) {
		propertyChangeSupport.firePropertyChange("keywords", this.keywords, this.keywords = keywords);
	}
	
	public void setProjectAbstract(String projectAbstract) {
		propertyChangeSupport.firePropertyChange("projectAbstract", this.projectAbstract, this.projectAbstract = projectAbstract);
	}
	
	public void setTaxonomDescription(String taxonomyDescription) {
		propertyChangeSupport.firePropertyChange("taxonomyDescription", this.taxonomyDescription, this.taxonomyDescription = taxonomyDescription);
	}

	public String getAuthors() {
		return authors;
	}

	public String getKeywords() {
		return keywords;
	}

	public String getProjectAbstract() {
		return projectAbstract;
	}

	public String getTaxonomyDescription() {
		return taxonomyDescription;
	}

	@Override
	public String toString() {
		//TODO implement
		return null;
	}

}