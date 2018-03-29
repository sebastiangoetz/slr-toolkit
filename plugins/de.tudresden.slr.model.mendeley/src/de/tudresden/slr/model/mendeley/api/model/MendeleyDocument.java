package de.tudresden.slr.model.mendeley.api.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.LaTeXObject;
import org.jbibtex.LaTeXParser;
import org.jbibtex.LaTeXPrinter;
import org.jbibtex.ParseException;
import org.jbibtex.StringValue;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

/**
 * This class implements the Document Class Model retrieved via the https://api.mendeley.com/documents endpoint.
 * It is used to convert the raw JSON response of a GET/POST Request to an appropriate Object.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class MendeleyDocument {
	// attribute names have to resemble JSON Model of Documents in Mendeley
	private String profile_id;
	private String group_id;
	private String last_modified;
	private String[] tags;
	private boolean read;
	private boolean starred;
	private boolean authored;
	private boolean confirmed;
	private boolean hidden;
	private String citation_key;
	private String source_type;
	private String language;
	private String short_title;
	private String reprint_edition;
	private String genre;
	private String country;
	private MendeleyPerson[] translators;
	private String series_editor;
	private String code;
	private String medium;
	private String user_context;
	private String patent_owner;
	private String patent_application_number;
	private String patent_legal_status;
	private String notes;
	private MendeleyUUID[] folder_uuids;
	private boolean private_publication;
	private String created;
	private String accessed;
	private boolean file_attached;
	private String id;
	private int year;
	private int month;
	private int day;
	private String source;
	private String title;
	private String revision;
	private Map<String, String> identifiers;
	
	@SerializedName("abstract") private String mAbstract;
	private MendeleyPerson[] authors;
	private String[] keywords;
	private String pages;
	private String volume;
	private String issue;
	private String[] websites;
	private String city;
	private String edition;
	private String institution;
	private String series;
	private String series_number;
	private String chapter;
	private MendeleyPerson[] editors;
	private String department;
	private String publisher;
	private String type;
	
	/**
	 * A constructor that offers to create a MendeleyDocument from a BibTeXEntry
	 * @param entry
	 */
	public MendeleyDocument(BibTeXEntry entry){
		this.citation_key = entry.getKey().toString();
		this.source_type = entry.getType().toString();
		this.assignEntryType(entry.getType().toString());
		updateFields(entry.getFields());
	}
	
	public MendeleyDocument(){
		
	}
	
	/**
	 * This method is used to update the object attributes by passing a Map of Key-Value-Pairs found in
	 * a BibTeXEntry. The richness of the Mendley Document Model is not sufficient to to cover all
	 * possible Keys of a BibTeXEntry, so that information might get discarded.
	 * 
	 * @param fields This method takes Key-Value-Pairs of a BibTeXEntry
	 */
	public void updateFields(Map<Key, Value> fields){

		for(Key key : fields.keySet()){
			String key_str = key.toString();
			Value value = fields.get(key);
			String value_str = "";
			
			if(value != null){
				// JBibTex can't handle escape Sequences
				value_str = value.toUserString().replaceAll("(\\r|\\n)", " ");
				// the classes field contains bracket that shouldn't be removed
				if(!key_str.equals("classes")){
					try {
						// the LaTeXParser resolves special characters
						LaTeXParser latexParser = new LaTeXParser();
						List<LaTeXObject> latexObjects = latexParser.parse(value_str);
						LaTeXPrinter latexPrinter = new org.jbibtex.LaTeXPrinter();
						value_str = latexPrinter.print(latexObjects);
						
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (TokenMgrException e) {
						e.printStackTrace();
					} 
				}
			}
			
			/*
			 * This is the manual mapping of BibTeXEntries to the Mendeley Document Model.
			 */
			
			if(	key_str.toLowerCase().equals("author") || 
				key_str.toLowerCase().equals("bookauthor")){
				this.authors = this.parseAuthorsBibTeX(value_str);
			}
			
			if(key_str.toLowerCase().equals("editor") )
				this.editors = this.parseAuthorsBibTeX(value_str);
			
			if(key_str.toLowerCase().equals("keywords") ){
				this.keywords = value_str.split(", ");
				if(keywords.length < 2){
					this.keywords = value_str.split(";");
				}
				if(keywords.length < 2){
					this.keywords = value_str.split("; ");
				}
			}
			
			if(key_str.toLowerCase().equals("holder") ){
				// Could be List of Names. Mendeley doesn't support Name List for 'patent_owner'
				this.patent_owner = value_str;
			}
			
			if(	key_str.toLowerCase().equals("annotator") || 
				key_str.toLowerCase().equals("commentator")){
				// Not yet supported by Mendeley
			}
			
			if(key_str.toLowerCase().equals("translator") ){
				// Could be List of Names. Mendeley doesn't support Name List for 'patent_owner'
				this.translators = this.parseAuthorsBibTeX(value_str);
			}
			
			if(	key_str.toLowerCase().equals("afterword") || 
				key_str.toLowerCase().equals("foreword") || 
				key_str.toLowerCase().equals("introduction")){
				// Not yet supported by Mendeley
			}
			
			if(key_str.toLowerCase().equals("title") ){
				this.title = value_str;
			}
			
			if(	key_str.toLowerCase().equals("titleaddon") || 
				key_str.toLowerCase().equals("subtitle")){
				// Not yet supported by Mendeley
			}
			
			if(	key_str.toLowerCase().equals("maintitle") || 
				key_str.toLowerCase().equals("mainsubtitle") || 
				key_str.toLowerCase().equals("maintitleaddon")){
				// Not yet supported by Mendeley
			}
			
			if(	key_str.toLowerCase().equals("booktitle") ){
				this.source = value_str;
			}
			
			if(	key_str.toLowerCase().equals("booksubtitle") || 
				key_str.toLowerCase().equals("booktitleaddon")){
				// Not yet supported by Mendeley
			}
			
			if(	key_str.toLowerCase().equals("journalsubtitle") || 
				key_str.toLowerCase().equals("journaltitle")){
				this.source = value_str;
			}
			
			if(	key_str.toLowerCase().equals("journal")){
					this.source = value_str;
			}
			
			if(	key_str.toLowerCase().equals("abstract")){
				this.mAbstract = value_str;
			}
			
			if(	key_str.toLowerCase().equals("eventdate") || 
				key_str.toLowerCase().equals("eventtitle") || 
				key_str.toLowerCase().equals("eventtitleaddon")){
				// Not yet supported by Mendeley
			}
			
			if(key_str.toLowerCase().equals("date") || key_str.equals("year") ){
				try{
					this.year = Integer.parseInt(value_str);
				}catch(NumberFormatException e){
					this.year = 0;
				}
			}
			
			if(key_str.toLowerCase().equals("month") ){
				try{
					this.month = Integer.parseInt(value_str);
				}catch(NumberFormatException e){
					switch(value_str.toLowerCase()){
						case "january": 	this.month=1;
											break;
						case "february": 	this.month=2;
											break;
						case "march": 		this.month=3;
											break;
						case "april": 		this.month=4;
											break;
						case "may": 		this.month=5;
											break;
						case "june": 		this.month=6;
											break;
						case "juli": 		this.month=7;
											break;
						case "august": 		this.month=8;
											break;
						case "september": 	this.month=9;
											break;
						case "october": 	this.month=10;
											break;
						case "november": 	this.month=11;
											break;
						case "december": 	this.month=12;
											break;
						default: 			this.month=0;
											break;
					}
				}
			}
			
			if(key_str.toLowerCase().equals("day") ){
				try{
					this.day = Integer.parseInt(value_str);
				}catch(NumberFormatException e){
					this.day = 0;
				}
			}
			
			if(key_str.toLowerCase().equals("edition") ){
				this.edition = value_str;
			}
			
			if(	key_str.toLowerCase().equals("issue") || 
				key_str.toLowerCase().equals("issuetitle") || 
				key_str.toLowerCase().equals("issuesubtitle") ){
				
				this.issue = value_str;
			}
			
			if(key_str.toLowerCase().equals("number") ){
				this.series_number = value_str;
			}
			
			if(key_str.toLowerCase().equals("series") ){
				this.series = value_str;
			}
			
			if(key_str.toLowerCase().equals("chapter") ){
				this.chapter = value_str;
			}
			
			if(key_str.toLowerCase().equals("part") ){
				//Not yet supported by Mendeley
			}
			
			if(	key_str.toLowerCase().equals("volume") ||
					key_str.toLowerCase().equals("volumes") ){
				this.volume = value_str;
			}
			
			if(key_str.toLowerCase().equals("version") ){
				//Not yet supported by Mendeley
			}
			
			if(	key_str.toLowerCase().equals("doi") || 
				key_str.toLowerCase().equals("eprint") || 
				key_str.toLowerCase().equals("eprintclass") || 
				key_str.toLowerCase().equals("eprinttype") ){
				
				if(this.identifiers == null){
					this.identifiers = new LinkedTreeMap<>();
				}
				identifiers.put("doi", value_str);					
			}
			
			if(	key_str.toLowerCase().equals("eid")){
					
					if(this.identifiers == null){
						this.identifiers = new LinkedTreeMap<>();
					}
					identifiers.put("eid", value_str);					
			}
			
			if(	key_str.toLowerCase().equals("isbn")){
				if(this.identifiers == null){
					this.identifiers = new LinkedTreeMap<>();
				}
				identifiers.put("isbn", value_str);					
			}
			
			if(	key_str.toLowerCase().equals("isrn")){
				if(this.identifiers == null){
					this.identifiers = new LinkedTreeMap<>();
				}
				identifiers.put("isrn", value_str);					
			}
				
			if(	key_str.toLowerCase().equals("issn")){	
				if(this.identifiers == null){
					this.identifiers = new LinkedTreeMap<>();
				}
				identifiers.put("issn", value_str);					
			}
			
			if(	key_str.toLowerCase().equals("url")){
				this.websites = value_str.split(" and ");
			}
			
			if(	key_str.toLowerCase().equals("urldate") ){
				//Not yet supported by Mendeley
			}
			
			if(	key_str.toLowerCase().equals("location") ){
				// locations can be separated into city and country if split is possible
				String[] locations = value_str.split(", ");
				switch(locations.length){
					case 1:
						this.country = value_str;
						break;
					case 2:
						this.city = locations[0];
						this.country = locations[1];
						break;
					default:
						break;
				}
			}
			
			if(	key_str.toLowerCase().equals("publisher") ){
				this.publisher = value_str;
			}
			
			if(	key_str.toLowerCase().equals("organization") ){
				//Not yet supported by Mendeley
			}
			
			if(	key_str.toLowerCase().equals("institution") ){
				this.institution = value_str;
			}	

			if(key_str.toLowerCase().equals("type") ){
				//Not yet supported by Mendeley
			}
			
			if(key_str.toLowerCase().equals("howpublished") ){
				//Not yet supported by Mendeley
			}
			
			if(key_str.toLowerCase().equals("pages") ){
				this.pages = value_str;
			}
			
			if(key_str.toLowerCase().equals("pagestotal") ){
				//Not yet supported by Mendeley
			}	
			
			/*
			 *  classes are used by the taxonomy plugin but have no place in the Mendeley Document Model.
			 *  So classes will be extracted from this field and will then be put in the notes attribute.
			 *  By updating the 'notes' attribute via the UPDATE /documents/{id} enpoint, the private
			 *  Annotation of this document will automatically be overwritten. 
			 */
			if(key_str.toLowerCase().equals("classes") ){
				
				// get current notes
				String text = this.notes;
				if(text == null){
					text = "";
				}
				
				// get content of [classes]
				Pattern pattern = Pattern.compile("\\[classes\\](.*?)\\[/classes\\]");
				Matcher matcher = pattern.matcher(text);
				if(matcher.find()){
					// replace old content of [classes] with new values
					text = text.replace(matcher.group(0), "");
					text = text + "\n[classes]" + value_str + "[/classes]";
				}
				else{
					text = "\n[classes]" + value_str + "[/classes]";
				}
				this.notes = text;
			}	
		}
	}
	
    @Override
    public String toString() {
        return title + " - ";
    }
    
    /**
     * 
     * @return title of Document (String)
     */
    public String getTitle() {
		return title;
	}
    
    /**
     * 
     * @return ID of Mendeley Document (String)
     */
    public String getId() {
		return id;
	}
    
    /**
     * 
     * @param title set title of document
     */
    public void setTitle(String title) {
		this.title = title;
	}
    
    /**
     * This method takes a String of Persons (Authors, Contributers etc.) and converts
     * these into MendeleyPerson objects. Persons should be devided by ' and '.
     * First name and last name should be devided by ', '
     * 
     * @param authors_str This takes the a String of authors and other persons
     * @return Returns MendeleyPerson array
     */
    private MendeleyPerson[] parseAuthorsBibTeX(String authors_str){
    	String authors[] = authors_str.split(" and ");
    	ArrayList<MendeleyPerson> m_authors= new ArrayList<>();
    	for(String author : authors){
    		String names[] = author.split(", ");
    		if(names.length > 1){
    			m_authors.add(new MendeleyPerson(names[1], names[0]));
    		}
    		else{
    			m_authors.add(new MendeleyPerson(null, names[0]));
    		}
    		
    	}
    	MendeleyPerson[] m_authors_array = new MendeleyPerson[m_authors.size()];
    	m_authors_array = m_authors.toArray(m_authors_array);
    	return m_authors_array;
    }
    
    /**
     * This method is used to manually map the known Types of BibTeXEntries (ARTICLE, INPROCEEDING, etc) to
     * a type that is compatible with the Mendeley Document Model.
     * 
     * @param type_str Type of BibTeXEntry
     * @return This returns the type of Document that is used in Mendeley Documents
     */
    private String parseMendeleyType(String type_str){
    	//TODO: Improve Parsing BibTeXEntry types to Mendeley Types; Fallback solution for now is 'journal'
    	String return_type = "journal";
    	String[] mendeley_types = new String[] { 	"journal", "book", "generic", "book_section", "conference_proceedings", 
    												"working_paper", "report", "web_page", "thesis", "encyclopedia_article",
    												"statute", "patent", "newspaper_article", "computer_program", "hearing", 
    												"television_broadcast", "magazine_article", "case", "film", "bill"};
    	
    	String pattern = ".*" + type_str.toLowerCase() + ".*";
    	
    	for(String m_type : mendeley_types){
    		if(m_type.toLowerCase().matches(pattern)){
    			return_type = m_type;
    		}
    	}
    	
    	return return_type;
    }
    
    /**
     * This is a Helper Function of parseMendeleyType. It is used to map a certain type of
     * BibTeXEntry to a Mendeley Document Type
     *     
     * @param type BibTeXEntry type
     */
    public void assignEntryType(String type){
    	switch(type.toLowerCase()){
    		case "article": 
    			this.type = "magazine_article";
    			break;
    		case "inbook": 
    			this.type = "book_section";
    			break;
    		case "bookinbook": 
    			this.type = "book_section";
    			break;
    		case "suppbook": 
    			this.type = "book_section";
    			break;
    		default:
    			this.type = this.parseMendeleyType(type);
    		
    	}
    }
    
    /**
     * 
     * @return get private note of Mendeley Document
     */
    public String getNotes() {
		return notes;
	}
    
    /**
     * 
     * @param notes set private note of Mendeley Document
     */
    public void setNotes(String notes) {
		this.notes = notes;
	}
    
    /**
     * This method is used to get the [classes] field of this document
     * @return This returns a StringValue of the classes field
     */
    public StringValue getClasses(){
    	return new StringValue(this.notes, StringValue.Style.BRACED);
    }
}
