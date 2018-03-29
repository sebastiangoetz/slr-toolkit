package de.tudresden.slr.model.mendeley.api.model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;

/**
 * This class implements the Folder Class Model retrieved via the https://api.mendeley.com/folders endpoint.
 * It is used to convert the raw JSON response of a GET/POST Request to an appropriate Object.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class MendeleyFolder {
	// attribute names have to resemble JSON Model of Folders in Mendeley
	private String id;
    private String name;
    private String created;
    private String modified;
    private String type;
    private List<MendeleyDocument> documents = new ArrayList<MendeleyDocument>();
    private BibTeXDatabase bibtexDatabase;

    @Override
    public String toString() {
        return id + " - " + name;
    }
    
    /**
     * 
     * @return created date as string
     */
    public String getCreated() {
		return created;
	}
    
    /**
     * 
     * @return ID of Mendeley Folder
     */
    public String getId() {
		return id;
	}
    
    /**
     * 
     * @return latest modify date
     */
    public String getModified() {
		return modified;
	}
    
    /**
     * 
     * @return get name of this folder
     */
    public String getName() {
		return name;
	}


    /**
     * 
     * @param folder_content set Array of MendeleyDocuments that are stored in this folder
     */
    public void setDocuments(MendeleyDocument[] folder_content) {
    	List<MendeleyDocument> temp_list = new ArrayList<MendeleyDocument>(Arrays.asList(folder_content));
		this.documents = temp_list;
	}
    
    /**
     * 
     * @return list of Mendeley Documents that are stored in this folder
     */
    public List<MendeleyDocument> getDocuments() {
		return this.documents;
	}
    
    /**
     * 
     * @param folder_content set ArrayList of MendeleyDocuments that are stored in this folder
     */
    public void setDocuments(ArrayList<MendeleyDocument> folder_content) {
		this.documents = folder_content;
	}
    
    /**
     * 
     * @return type tyxpe of the folder =['conflict', 'folder']
     */
    public String getType() {
		return type;
	}
    
    /**
     * 
     * @param type type of the folder =['conflict', 'folder']
     */
    public void setType(String type) {
		this.type = type;
	}
    
    /**
     * 
     * @param id ID of the Mendeley Folder
     */
    public void setId(String id) {
		this.id = id;
	}
    
    /**
     * 
     * @param md set a document that needs to be added to this folder
     */
    public void addDocument(MendeleyDocument md){
    	documents.add(md);
    }
    
    /**
     * 
     * @param name set name of this folder
     */
    public void setName(String name) {
		this.name = name;
	}
    
    /**
     * This method adds a BibTexDatabase to this Folder
     * @param bibtexDatabase
     */
    public void setBibtexDatabase(BibTeXDatabase bibtexDatabase) {
		this.bibtexDatabase = bibtexDatabase;
		List<BibTeXObject> bibObjList = this.bibtexDatabase.getObjects();
		Map<Key, BibTeXEntry> bibEntryMap = this.bibtexDatabase.getEntries();
		Map<Key, BibTeXString> bibStringMap = this.bibtexDatabase.getStrings();
	}
    
    /**
     * This method searches for a given Mendeley ID and returns the specific
     * Document if it is existing in this folder.
     * 
     * @param id Mendeley Document ID
     * @return Mendeley Document from this Mendeley Folder (null if it is not found)
     */
    public MendeleyDocument getDocumentById(String id){
    	for(MendeleyDocument md : this.getDocuments()){
    		if(md.getId().equals(id)){
    			return md;
    		}
    	}
    	return null;
    }
    
    /**
     * This method searches for a given Mendeley Title and returns the specific
     * Document if it is existing in this folder.
     * 
     * @param title
     * @return Mendeley Document from this Mendeley Folder (null if it is not found)
     */
    public MendeleyDocument getDocumentByTitle(String title){
    	for(MendeleyDocument md : this.getDocuments()){
    		if(md.getTitle().equals(title)){
    			return md;
    		}
    	}
    	return null;
    }
    
    /**
     * TThis method searches for a given Mendeley Title and returns the specific
     * BibTeXEntry of this Document if it is existing in this folder.
     * 
     * @param document
     * @return BibTeXEntry of Mendeley Document from this Mendeley Folder (null if it is not found)
     */
    public BibTeXDatabase getBibTexOfDocument(MendeleyDocument document) {
    	for(BibTeXEntry entry: bibtexDatabase.getEntries().values()) {
    		if(document.getTitle().equals(entry.getField(new Key("title")).toUserString())) {
    			BibTeXDatabase db = new BibTeXDatabase();
    			db.addObject(entry);
    			return db;
    		}
    	}
    	return null;
    }
    
    
    
}
