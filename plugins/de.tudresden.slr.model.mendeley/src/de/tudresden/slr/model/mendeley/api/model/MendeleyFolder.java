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

public class MendeleyFolder {
	private String id;
    private String name;
    private String created;
    private String modified;
    private String type;
    //private MendeleyDocument[] documents;
    private List<MendeleyDocument> documents = new ArrayList<MendeleyDocument>();
    private BibTeXDatabase bibtexDatabase;
    // Getters and setters are not required for this example.
    // GSON sets the fields directly using reflection.
    

    @Override
    public String toString() {
        return id + " - " + name;
    }
    
    public String getCreated() {
		return created;
	}
    
    public String getId() {
		return id;
	}
    
    public String getModified() {
		return modified;
	}
    
    public String getName() {
		return name;
	}
    /*
    public MendeleyDocument[] getDocuments() {
		return documents;
	}
	*/
    public void setDocuments(MendeleyDocument[] folder_content) {
    	List<MendeleyDocument> temp_list = new ArrayList<MendeleyDocument>(Arrays.asList(folder_content));
		this.documents = temp_list;
	}
    
    public List<MendeleyDocument> getDocuments() {
		return this.documents;
	}
    
    public void setDocuments(ArrayList<MendeleyDocument> folder_content) {
		this.documents = folder_content;
	}
    
    public String getType() {
		return type;
	}
    
    public void setType(String type) {
		this.type = type;
	}
    /*
    public void addDocument(MendeleyDocument md){
    	if(md != null){
    		List<MendeleyDocument> temp_list = new ArrayList<MendeleyDocument>();
    		if(this.documents != null){
    			temp_list = new ArrayList<MendeleyDocument>(Arrays.asList(this.documents));
    		}
    		temp_list.add(md);
            MendeleyDocument[] new_docs = new MendeleyDocument[temp_list.size()];
            new_docs = temp_list.toArray(new_docs);
            setDocuments(new_docs);
    		System.out.println(this.documents);
        	
    	}
    	*/
    public void addDocument(MendeleyDocument md){
    	documents.add(md);
    }
    
    public void setName(String name) {
		this.name = name;
	}
    public void setBibtexDatabase(BibTeXDatabase bibtexDatabase) {
		this.bibtexDatabase = bibtexDatabase;
		List<BibTeXObject> bibObjList = this.bibtexDatabase.getObjects();
		Map<Key, BibTeXEntry> bibEntryMap = this.bibtexDatabase.getEntries();
		Map<Key, BibTeXString> bibStringMap = this.bibtexDatabase.getStrings();
		for(BibTeXObject bib : bibObjList){
			System.out.println(bib);
		}
		System.out.println("ok");
	}
    
    public MendeleyDocument getDocumentById(String id){
    	for(MendeleyDocument md : this.getDocuments()){
    		if(md.getId().equals(id)){
    			return md;
    		}
    	}
    	return null;
    }
    
    public MendeleyDocument getDocumentByTitle(String title){
    	for(MendeleyDocument md : this.getDocuments()){
    		if(md.getTitle().equals(title)){
    			return md;
    		}
    	}
    	return null;
    }
    
    
    
    
    
    
}
