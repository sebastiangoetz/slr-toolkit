package de.tudresden.slr.model.mendeley.api.model;

/**
 * This class implements a additional attribute that is needed by the Document Class Model retrieved 
 * via the https://api.mendeley.com/document endpoint. It is used to convert the raw JSON response of 
 * the 'folder_uuids' attribute in MendeleyDocument to an appropriate Object.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class MendeleyUUID {
	// attribute names have to resemble JSON Model of the 'folder_uuids' attribute of a Document in Mendeley
	private int leastSignificantBits;
	private int mostSignificantBits;
}
