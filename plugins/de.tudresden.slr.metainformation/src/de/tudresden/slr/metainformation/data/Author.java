package de.tudresden.slr.metainformation.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Author {
	private String name, email, organisation;
	
	public Author(String name, String email, String organisation) {
		super();
		this.name = name;
		this.email = email;
		this.organisation = organisation;
	}
	
	public Author() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	
	public String toString() {
		String mailString = email.equals("") ? "" : "; "+email;
		String organisationString = organisation.equals("") ? "" : "; "+organisation;
		return name + mailString + organisationString;
	}

}
