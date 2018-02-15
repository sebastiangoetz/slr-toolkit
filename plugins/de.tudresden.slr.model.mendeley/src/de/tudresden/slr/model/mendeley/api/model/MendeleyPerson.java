package de.tudresden.slr.model.mendeley.api.model;

public class MendeleyPerson {

	private String first_name;
	private String last_name;
	private String scopus_author_id;
	
	MendeleyPerson(){
		
	}
	
	MendeleyPerson(String first_name, String last_name){
		this.first_name = first_name;
		this.last_name = last_name;
		scopus_author_id = null;
	}
}
