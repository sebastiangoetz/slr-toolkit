package de.tudresden.slr.model.mendeley.api.model;

/**
 * This class implements the Annotation Class Model retrieved via the https://api.mendeley.com/annotations endpoint.
 * It is used to receive the Annotations for a specific document. The private note of a Mendeley Document is used
 * to store the 'classes' field of BibTeXEntries.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 */
public class MendeleyAnnotation {
	private String id;
	private String type;
	private String previous_id;
	private String text;
	private String profile_id;
	private String created;
	private String last_modified;
	private String privacy_level;
	private String filehash;
	private String document_id;
	
	public MendeleyAnnotation() {
	}
	
	/**
	 * 
	 * @return id of assigned document 
	 */
	public String getDocument_id() {
		return document_id;
	}
	
	/**
	 * 
	 * @return text of the Annotation
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * 
	 * @return Type of Annotation = ['sticky_note' or 'highlight' or 'note']
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * 
	 * @param document_id ID of the document that is assigned to Annotation
	 */
	public void setDocument_id(String document_id) {
		this.document_id = document_id;
	}
	
	/**
	 * 
	 * @param text set Text of Annotation
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * 
	 * @param type Set Type of Annotation = ['sticky_note' or 'highlight' or 'note']
	 */
	public void setType(String type) {
		this.type = type;
	}
}
