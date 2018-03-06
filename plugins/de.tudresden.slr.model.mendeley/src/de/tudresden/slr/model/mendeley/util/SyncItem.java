package de.tudresden.slr.model.mendeley.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.StringValue;
import org.jbibtex.Value;

import de.tudresden.slr.model.mendeley.api.model.MendeleyAnnotation;
import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;

public class SyncItem {
	private String id;
	private Key type;
	private String title;
	private MendeleyDocument mendeley;
	private BibTeXEntry mendeleyDoc;
	private BibTeXEntry workspaceDoc;
	private Map<Key, ArrayList<Value>> fields;
	private Map<Key, Value> selectedFields;
	
	public SyncItem(MendeleyDocument mendeley, BibTeXEntry mendeleyDoc, BibTeXEntry workspaceDoc) {
		// TODO Auto-generated constructor stub
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
	
	public void addToolkitFields(){
		
		String notes = mendeley.getNotes();
		if(notes != null){
			Pattern pattern = Pattern.compile("\\[classes\\](.*?)\\[/classes\\]");
			Matcher matcher = pattern.matcher(notes);
			String classes_str = "";
			
			if(matcher.find()){
				classes_str = matcher.group(1);
				System.out.println(classes_str);
				StringValue classes = new StringValue(classes_str, StringValue.Style.BRACED);
				mendeleyDoc.addField(new Key("classes"), classes);
			}
		}
		
		
	}
	
	private void initTitle(){
		Value title = workspaceDoc.getFields().get(new Key("title"));
		this.title = title.toUserString();
	}
	
	private void initFields(){
		for(Key key : mendeleyDoc.getFields().keySet()){
			Value valueMendeley = mendeleyDoc.getFields().get(key);
			Value valueWorkspace = workspaceDoc.getFields().get(key);
			ArrayList<Value> values = new ArrayList<>();
			values.add(valueMendeley);
			values.add(valueWorkspace);
			this.fields.put(key, values);	
		}
		
		for(Key key : workspaceDoc.getFields().keySet()){
			Value valueWorkspace = workspaceDoc.getFields().get(key);
			Value valueMendeley = mendeleyDoc.getFields().get(key);
			ArrayList<Value> values = new ArrayList<>();
			values.add(valueMendeley);
			values.add(valueWorkspace);
			this.fields.put(key, values);		
		}
		
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
