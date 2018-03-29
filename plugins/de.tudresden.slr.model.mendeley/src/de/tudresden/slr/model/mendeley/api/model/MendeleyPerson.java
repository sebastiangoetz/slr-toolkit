package de.tudresden.slr.model.mendeley.api.model;

/**
 * This class implements a additional attribute that is needed by the Document Class Model retrieved 
 * via the https://api.mendeley.com/document endpoint. It is used to convert the raw JSON response of 
 * the 'person' attribute in MendeleyDocument to an appropriate Object.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class MendeleyPerson {
	// attribute names have to resemble JSON Model of the 'person' attribute of a Document in Mendeley
	private String first_name;
	private String last_name;
	private String scopus_author_id;
	
	MendeleyPerson(){
		
	}
	
	/**
	 * This constructor creates a MendeleyPerson from a given first name and last name
	 * 
	 * @param first_name set first name of a person
	 * @param last_name set last name of a person
	 */
	MendeleyPerson(String first_name, String last_name){
		this.first_name = first_name;
		this.last_name = last_name;
		scopus_author_id = null;
	}
}
