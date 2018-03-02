package de.tudresden.slr.model.mendeley.api.model;

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
	
	public String getDocument_id() {
		return document_id;
	}
	
	public String getText() {
		return text;
	}
	
	public String getType() {
		return type;
	}
	
	public void setDocument_id(String document_id) {
		this.document_id = document_id;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
