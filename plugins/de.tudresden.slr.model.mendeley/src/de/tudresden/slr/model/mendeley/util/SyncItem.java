package de.tudresden.slr.model.mendeley.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.StringValue;
import org.jbibtex.Value;

import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;

/**
 * This class implements a model that is used to store the combined fields of
 * a Mendeley BibTeXEntry and a Bib-File BibTeXEntry. 
 * 
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class SyncItem {
	
	/**
	 * Mendeley Document id
	 */
	private String id;
	
	/**
	 * Mendeley Document Key
	 */
	private Key type;
	
	/**
	 * Mendeley Document title
	 */
	private String title;
	
	/**
	 * Mendeley Document
	 */
	private MendeleyDocument mendeley;
	
	/**
	 * BibTeXEntry of the Mendeley Document
	 */
	private BibTeXEntry mendeleyDoc;
	
	/**
	 * BibTeXEntry of the Bib-File document
	 */
	private BibTeXEntry workspaceDoc;
	
	/**
	 * Map of the combined Key/Value-Pairs of the Mendeley and Bib-File Documents
	 */
	private Map<Key, ArrayList<Value>> fields;
	
	/**
	 * Map of the combined Key/Value-Pairs of the Mendeley and Bib-File Documents
	 * A single Value for each Key
	 */
	private Map<Key, Value> selectedFields;
	
	public SyncItem(MendeleyDocument mendeley, BibTeXEntry mendeleyDoc, BibTeXEntry workspaceDoc) {
		this.id = mendeley.getId();
		this.mendeley = mendeley;
		this.mendeleyDoc = mendeleyDoc;
		this.workspaceDoc = workspaceDoc;
		this.type = mendeleyDoc.getType();
		this.fields = new HashMap<>();
		this.selectedFields = new HashMap<>();
		addToolkitFields();
		initTitle();
		initFields();
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	/**
	 * This method is used to set the fields that are espacially needed by the SLR Toolkit.
	 * At the moment this is the 'classes' field which will be extracted from the private
	 * note of a Mendeley Document
	 */
	public void addToolkitFields(){
		
		String notes = mendeley.getNotes();
		if(notes != null){
			Pattern pattern = Pattern.compile("\\[classes\\](.*?)\\[/classes\\]");
			Matcher matcher = pattern.matcher(notes);
			String classes_str = "";
			
			if(matcher.find()){
				classes_str = matcher.group(1);
				StringValue classes = new StringValue(classes_str, StringValue.Style.BRACED);
				mendeleyDoc.addField(new Key("classes"), classes);
			}
		}
	}
	
	/**
	 * This methods sets the Title of this SyncItem to the field Value of
	 * the Document from the given Bib-File
	 */
	private void initTitle(){
		Value title = workspaceDoc.getFields().get(new Key("title"));
		this.title = title.toUserString();
	}
	
	/**
	 * This method combines field from both the Mendeley Document BibTeXEntry
	 * and the Bib-File Document BibTeXEntry
	 */
	private void initFields(){
		//get all fields owned by the Mendeley Document BibTeXEntry
		for(Key key : mendeleyDoc.getFields().keySet()){
			Value valueMendeley = mendeleyDoc.getFields().get(key);
			Value valueWorkspace = workspaceDoc.getFields().get(key);
			ArrayList<Value> values = new ArrayList<>();
			values.add(valueMendeley);
			values.add(valueWorkspace);
			this.fields.put(key, values);	
		}
		
		//get all fields owned by the Bib-File Document BibTeXEntry
		for(Key key : workspaceDoc.getFields().keySet()){
			Value valueWorkspace = workspaceDoc.getFields().get(key);
			Value valueMendeley = mendeleyDoc.getFields().get(key);
			ArrayList<Value> values = new ArrayList<>();
			values.add(valueMendeley);
			values.add(valueWorkspace);
			this.fields.put(key, values);		
		}
		
		//set all selected field value to the Mendeley Document values by Default
		for(Key key : this.fields.keySet()){
			this.selectedFields.put(key, fields.get(key).get(0));
		}
	}
	
	public Map<Key, ArrayList<Value>> getFields() {
		return fields;
	}
	
	public Map<Key, Value> getSelectedFields() {
		return selectedFields;
	}
	
	public void addSelectedField(Key key, Value value){
		selectedFields.put(key, value);
	}
	
	/**
	 * This methods checks if there are conflicts between any field of
	 * this combined BibTeXEntry.
	 * 
	 * @return true if there is at least one conflict of values for a specific key
	 */
	public boolean hasConflicts(){
		for(Key key : selectedFields.keySet()){
			Value v1 = mendeleyDoc.getField(key);
			Value v2 = workspaceDoc.getField(key);
			
			if((v1 == null) | (v2 == null))	return true;
			if(!v1.toUserString().equals(v2.toUserString())) return true;
		}
		return false;
	}
	 
}
