package de.tudresden.slr.model.mendeley.api.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.CharacterFilterReader;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.StringValue;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.tudresden.slr.model.mendeley.Activator;
import de.tudresden.slr.model.mendeley.api.model.MendeleyAnnotation;
import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;
import de.tudresden.slr.model.mendeley.preferences.PreferenceConstants;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;
import de.tudresden.slr.model.mendeley.ui.MendeleyOAuthDialog;

/**
 * The MendeleyClient class implements a client that communicates with the Mendeley API. 
 * 
 * Tokens will be obtained and transmitted to the Preference Store. 
 * There is only a single instance of this class.
 *  
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class MendeleyClient {

	/**
	 * URL for requesting the Mendeley OAuth Dialog.
	 */
	private static final String oauth_request_url = "https://api.mendeley.com/oauth/authorize";
	
	/**
     * URL for requesting OAuth access tokens.
     */
    private static final String token_request_url = "https://api.mendeley.com/oauth/token";

    /**
     * Client ID of client credential.
     */
    private static final String client_id = "4335";

    /**
     * Client secret of client credential.
     */
    private static final String client_secret = "sSFcbUA38RS9Cpm7";

    /**
     * Redirect URI
     */
    private static final String redirect_uri = "https://localhost";
    
    /*
     * Authorization Code
     */
    private String auth_code = "";
    
    /*
     * Access Token
     */
    private String access_token;
    
    /*
     * Refresh Token
     */
    private String refresh_token;
    
    /*
     * Expiration date of the obtained Access Token 
     */
    private Calendar expires_at;
    
    /*
     * WorkspaceManager to access the Resources of the Projects
     */
    private WorkspaceManager wm = WorkspaceManager.getInstance();
    
    /**
     * Latest Mendeley Folders of User that is currently logged into his Mendeley profile
     */
    private MendeleyFolder[] mendeleyFolders = null;
    
    private IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        
    private static final class InstanceHolder {
      static final MendeleyClient INSTANCE = new MendeleyClient();
      
    }

    // Prevent the generation of this object by other methods
    private MendeleyClient () {}
   
    public static MendeleyClient getInstance () {
      return InstanceHolder.INSTANCE;
    }
    
    public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
    
    public String getAccess_token() {
		return access_token;
	}
    
    public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
    
    public String getRefresh_token() {
		return refresh_token;
	}
    
    public String getAuth_code() {
		return auth_code;
	}
    
    public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
   

    /**
     * This method is used to creates a URL to the OAuth authorization service
     * @return String This returns the URL needed to call the Mendeley Login Screen
     */
    public final String getAuthURL(){
    	return 	oauth_request_url + "?" +
    			"&client_id=" + client_id +
    			"&redirect_uri=" + redirect_uri +
    			"&response_type=code&scope=all";
    }
    
    /**
     * This methods is used to obtain all documents via the GET https://api.mendeley.com/documents endpoint.
     * 
     * @return This returns a BibTeX String of all documents from the /documents endpoint
     */
    public String getAllDocumentsBibTex() {
    	refreshTokenIfNecessary();
    	
    	/**
    	 *  The limit parameter of obtainable documents is restricted to a maximum of 500. 
    	 *  The view parameter determines the richness of fields that will be given back.
    	 */
    	String resource_url = "https://api.mendeley.com/documents?&view=bib&limit=500";
    	
    	HttpURLConnection resource_cxn;
		try {
			resource_cxn = (HttpURLConnection)(new URL(resource_url).openConnection());
			resource_cxn.addRequestProperty("Authorization", "Bearer " + this.access_token);
	        resource_cxn.setRequestMethod("GET");
	        resource_cxn.setRequestProperty("Accept","application/x-bibtex");
	        
	        InputStream resource = resource_cxn.getInputStream();
	    	
	        BufferedReader r = new BufferedReader(new InputStreamReader(resource, "UTF-8"));
	        String line = null;
	        String bibtex_str = "";
	        while ((line = r.readLine()) != null) {
	        	bibtex_str = bibtex_str + line;
	        }
	        return bibtex_str;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
    }
    
    /**
     * This methods is used to obtain all documents via the GET https://api.mendeley.com/documents endpoint.
     * 
     * @return This returns a JSON String of all documents from the /documents endpoint
     */
    public String getAllDocumentsJSON() throws MalformedURLException, IOException, TokenMgrException, ParseException{
    	refreshTokenIfNecessary();
    	
    	String resource_url = "https://api.mendeley.com/documents?&view=bib&limit=500";
    	
    	HttpURLConnection resource_cxn = (HttpURLConnection)(new URL(resource_url).openConnection());
        resource_cxn.addRequestProperty("Authorization", "Bearer " + this.access_token);
        resource_cxn.setRequestMethod("GET");
        
        InputStream resource = resource_cxn.getInputStream();
    	
        BufferedReader r = new BufferedReader(new InputStreamReader(resource, "UTF-8"));
        String line = null;
        String json_str = "";
        while ((line = r.readLine()) != null) {
        	json_str = json_str + line;
        }
        
    	return json_str;
    }
    
    /**
     * This methods is used to obtain all Annotations of a Mendeley Document via the GET https://api.mendeley.com/annotations endpoint.
     * 
     * @param document Pass a Mendeley Document to get its annotation
     * @return This method returns the annotations stored in Mendeley from the document you pass to this method
     * @throws MalformedURLException
     * @throws IOException
     */
    public MendeleyAnnotation[] getAnnotations(MendeleyDocument document) throws MalformedURLException, IOException{
    	refreshTokenIfNecessary();
    	
    	String result = "";
    	// Use parameter "document_id" to get annoations from a specific document
    	String resource_url = "https://api.mendeley.com/annotations?document_id=" + document.getId();
    	
    	HttpURLConnection resource_cxn = (HttpURLConnection)(new URL(resource_url).openConnection());
        resource_cxn.addRequestProperty("Authorization", "Bearer " + this.access_token);
        resource_cxn.setRequestMethod("GET");
        
        InputStream resource = resource_cxn.getInputStream();
        
        BufferedReader r = new BufferedReader(new InputStreamReader(resource, "UTF-8"));
        String line = null;
        while ((line = r.readLine()) != null) {
        	result = result + line;
        }
        
        Gson gson = new Gson();
        MendeleyAnnotation[] annotations = gson.fromJson(result, MendeleyAnnotation[].class);
        
        return annotations;
    }
    
    /**
     * This methods is used to obtain all BibTeXEntries of a Mendeley Document via the GET https://api.mendeley.com/documents/{id} endpoint.
     * 
     * @param id Document ID tha is stored in Mendeley Folder.
     * @return This Method returns a BibTeXDatabase of the given document id
     * @throws MalformedURLException
     * @throws IOException
     * @throws TokenMgrException
     * @throws ParseException
     */
    public BibTeXDatabase getDocumentBibTex(String id) throws MalformedURLException, IOException, TokenMgrException, ParseException{
    	refreshTokenIfNecessary();
    	
    	String resource_url = "https://api.mendeley.com/documents/" + id + "?view=bib&limit=500";
    	
    	HttpURLConnection resource_cxn = (HttpURLConnection)(new URL(resource_url).openConnection());
        resource_cxn.addRequestProperty("Authorization", "Bearer " + this.access_token);
        resource_cxn.setRequestMethod("GET");
        resource_cxn.setRequestProperty("Accept","application/x-bibtex");
        
        InputStream resource = resource_cxn.getInputStream();
    	
        BufferedReader r = new BufferedReader(new InputStreamReader(resource, "UTF-8"));
        BibTeXDatabase db = new BibTeXDatabase();
    	try(Reader reader = r){
    		CharacterFilterReader filterReader = new CharacterFilterReader(reader);
    		BibTeXParser parser = new BibTeXParser();
    		db = parser.parse(filterReader);
    		
    		/*
    		 * Mendeley API returns a parsing error concerning the 'title' field
    		 * 	- 	The additional characters '{' at the beginning of a Title and '}' at the end of a title
    		 * 		must be removed
    		 */
    		for(BibTeXEntry entry: db.getEntries().values()){
    			String fixedTitleStr = getFixedString(entry.getField(new Key("title")).toUserString());
    			StringValue fixedTitleValue = new StringValue(fixedTitleStr, StringValue.Style.BRACED);
    			entry.removeField(new Key("title"));
    			entry.addField(new Key("title"), fixedTitleValue);
    		}
    	}catch (TokenMgrException | IOException |ParseException e) {
			e.printStackTrace();
		}
 
    	return db;
    }
    
    /**
     * This methods is used to obtain all Folder from a Mendeley profile via the GET https://api.mendeley.com/folders endpoint.
     * The response model of this endpoint only includes name, creation date and id of a folder.
     * 
     * @return This Method returns a JSON String of all Mendeley Folders
     * @throws MalformedURLException
     * @throws IOException
     * @throws TokenMgrException
     * @throws ParseException
     */
    public String getAllFolders() throws MalformedURLException, IOException, TokenMgrException, ParseException{
    	refreshTokenIfNecessary();
    	
    	String resource_url = "https://api.mendeley.com/folders";
    	
    	HttpURLConnection resource_cxn = (HttpURLConnection)(new URL(resource_url).openConnection());
        resource_cxn.addRequestProperty("Authorization", "Bearer " + this.access_token);
        resource_cxn.setRequestMethod("GET");
        
        InputStream resource = resource_cxn.getInputStream();
    	
        BufferedReader r = new BufferedReader(new InputStreamReader(resource, "UTF-8"));
        String line = null;
        String json_str = "";
        while ((line = r.readLine()) != null) {
        	json_str = json_str + line;
        }
        
    	return json_str;
    }
    
    /**
     * This methods is used to update the Mendeley Folder that are stored in this client class 
     * by obtaining all Folders of a Mendeley profile including the respective Mendeley Documents.
     * The updated Mendeley Folders are a requirement for starting the Mendeley Sync Wizard
     * 
     * @param monitor 
     * @return Returns the IStatus of the Job that is used to inform the User about the current state
     * @throws MalformedURLException
     * @throws TokenMgrException
     * @throws IOException
     * @throws ParseException
     */
    public IStatus updateMendeleyFolders(IProgressMonitor monitor) throws MalformedURLException, TokenMgrException, IOException, ParseException{
    	refreshTokenIfNecessary();
    	
    	// Get all Folder IDs and Folder Names of your Mendeley profile
        String folders_str = this.getAllFolders();
        Gson gson = new Gson();
        
        // Parse JSON String of Folders into MendeleyFolder objects
        MendeleyFolder[] m_folders = gson.fromJson(folders_str, MendeleyFolder[].class);
        
        // Number of tasks equals number of Folder that are present in your Mendeley profile
        SubMonitor subMonitor = SubMonitor.convert(monitor, m_folders.length);
		
        for(int i = 0; i < m_folders.length;i++){
			subMonitor.setTaskName("Working on Folder " + String.valueOf(i+1) + " of " + String.valueOf(m_folders.length) +  ": " + m_folders[i].getName() );
			
			String folder_content_str;
			try {
				// get all documents of a specific folder
				folder_content_str = this.getDocumentsByFolderJSON(m_folders[i].getId());
				
				// get all BibTeXEntries of a specific folder
				BibTeXDatabase bibdb = this.getDocumentsByFolderBibTex(m_folders[i]);
				m_folders[i].setBibtexDatabase(bibdb);
				
				MendeleyDocument[] folder_content = gson.fromJson(folder_content_str, MendeleyDocument[].class);
				m_folders[i].setDocuments(folder_content);
				m_folders[i].setType("Folder");
				
				// get the notes of all Documents to find the 'classes' field that is missing in the Mendeley BibTeX response model
				for(MendeleyDocument md : m_folders[i].getDocuments()){
					subMonitor.subTask("Download Document \"" + md.getTitle() + "\"");
					String notes = md.getNotes();
					MendeleyAnnotation[] annotations = getAnnotations(md);
					for(MendeleyAnnotation annotation: annotations){
						if(annotation.getType().equals("note")){
							notes = annotation.getText();
						}
					}
					// add notes to the 'notes' variable
					md.setNotes(notes);
				}
			} catch (TokenMgrException | IOException | ParseException e) {
				e.printStackTrace();
			}
			subMonitor.worked(1);
			if (subMonitor.isCanceled()) return Status.CANCEL_STATUS;
        }
        mendeleyFolders = m_folders;
		return Status.OK_STATUS;
    }
    
    /**
     * This methods is used to obtain a Mendeley Folder from a specific Folder via the GET https://api.mendeley.com/documents endpoint.
     * All Mendeley Documents included in this folder and the respective BibTeXDatabase will be included.
     * 
     * @param folderId The ID of a Folder that is stored in your Mendeley profile
     * @return This method returns a Mendeley Folder containing all respective documents
     * @throws MalformedURLException
     * @throws TokenMgrException
     * @throws IOException
     * @throws ParseException
     */
    public MendeleyFolder getMendeleyFolder(String folderId) throws MalformedURLException, TokenMgrException, IOException, ParseException{
    	refreshTokenIfNecessary();
    	
    	// The 'folder_id' parameter is used to specify the folder you want to retrieve.
    	String resource_url = "https://api.mendeley.com/documents?folder_id=" + folderId + "&view=bib&limit=500";
    	
    	HttpURLConnection resource_cxn = (HttpURLConnection)(new URL(resource_url).openConnection());
        resource_cxn.addRequestProperty("Authorization", "Bearer " + this.access_token);
        resource_cxn.setRequestMethod("GET");
        InputStream resource = resource_cxn.getInputStream();
        
        BufferedReader r = new BufferedReader(new InputStreamReader(resource, "UTF-8"));
        String line = null;
        String json_str = "";
        while ((line = r.readLine()) != null) {
        	json_str = json_str + line;
        }
        
        Gson gson = new Gson();
        MendeleyDocument[] documents = gson.fromJson(json_str, MendeleyDocument[].class);
        
	    MendeleyFolder m_folder = new MendeleyFolder();
	    m_folder.setId(folderId);
	    m_folder.setDocuments(documents);
	    
        try {
			String folder_content_str = this.getDocumentsByFolderJSON(m_folder.getId());
			
			MendeleyDocument[] folder_content = gson.fromJson(folder_content_str, MendeleyDocument[].class);
			m_folder.setDocuments(folder_content);
			m_folder.setType("Folder");
			
			for(MendeleyDocument md : m_folder.getDocuments()){
				String notes = md.getNotes();
				MendeleyAnnotation[] annotations = getAnnotations(md);
				for(MendeleyAnnotation annotation: annotations){
					if(annotation.getType().equals("note")){
						notes = annotation.getText();
					}
				}
				md.setNotes(notes);
			}
			BibTeXDatabase bibdb = this.getDocumentsByFolderBibTex(m_folder);
			m_folder.setBibtexDatabase(bibdb);
			
		} catch (TokenMgrException | IOException | ParseException e) {
			e.printStackTrace();
		}
        return m_folder;
        
    }
 
    /**
     * This methods is used to obtain all Mendeley documents that are stored in a specific Folder
     * via the GET https://api.mendeley.com/documents endpoint.
     * 
     * @param id Pass the ID of a Mendeley Folder to get its documents
     * @return This Method returns a JSON String of all documents stored in a specific Mendeley Folder
     * @throws MalformedURLException
     * @throws IOException
     * @throws TokenMgrException
     * @throws ParseException
     */
    public String getDocumentsByFolderJSON(String id) throws MalformedURLException, IOException, TokenMgrException, ParseException{
    	refreshTokenIfNecessary();
    	
    	String resource_url = "https://api.mendeley.com/documents?folder_id=" + id + "&view=bib&limit=500";
    	
    	HttpURLConnection resource_cxn = (HttpURLConnection)(new URL(resource_url).openConnection());
        resource_cxn.addRequestProperty("Authorization", "Bearer " + this.access_token);
        resource_cxn.setRequestMethod("GET");
        InputStream resource = resource_cxn.getInputStream();
        
        BufferedReader r = new BufferedReader(new InputStreamReader(resource, "UTF-8"));
        String line = null;
        String json_str = "";
        while ((line = r.readLine()) != null) {
        	json_str = json_str + line;
        }
        
    	return json_str;
    }
    
    /**
     * This methods is used to obtain a BibTeXDatabase containing all documents of a specific Mendeley Folder
     * via the GET https://api.mendeley.com/documents endpoint.
     * 
     * @param mf You can pass a specific MendeleyFolder object to get its BibTeXDatabase
     * @return This Method returns a BibTeXDatabase 
     * @throws MalformedURLException
     * @throws IOException
     * @throws TokenMgrException
     * @throws ParseException
     */
    public BibTeXDatabase getDocumentsByFolderBibTex(MendeleyFolder mf) throws MalformedURLException, IOException, TokenMgrException, ParseException{
    	refreshTokenIfNecessary();
    	
    	String resource_url = "https://api.mendeley.com/documents?folder_id=" + mf.getId() + "&view=bib&limit=500";
    	
    	HttpURLConnection resource_cxn = (HttpURLConnection)(new URL(resource_url).openConnection());
        resource_cxn.addRequestProperty("Authorization", "Bearer " + this.access_token);
        resource_cxn.setRequestMethod("GET");
        resource_cxn.setRequestProperty("Accept","application/x-bibtex");
        InputStream resource = resource_cxn.getInputStream();
            	
        BufferedReader r = new BufferedReader(new InputStreamReader(resource, "UTF-8"));

        BibTeXDatabase db = new BibTeXDatabase();
    	try(Reader reader = r){
    		CharacterFilterReader filterReader = new CharacterFilterReader(reader);
    		BibTeXParser parser = new BibTeXParser();
    		db = parser.parse(filterReader);
    		
    		/*
    		 * Mendeley API returns a parsing error concerning the 'title' field
    		 * 	- 	The additional characters '{' at the beginning of a Title and '}' at the end of a title
    		 * 		must be removed
    		 */
    		for(BibTeXEntry entry: db.getEntries().values()){
    			String fixedTitleStr = getFixedString(entry.getField(new Key("title")).toUserString());
    			StringValue fixedTitleValue = new StringValue(fixedTitleStr, StringValue.Style.BRACED);
    			entry.removeField(new Key("title"));
    			entry.addField(new Key("title"), fixedTitleValue);
    			
    			Value value = entry.getField(new Key("title"));
    			if(value != null){
    				MendeleyDocument document = mf.getDocumentByTitle(value.toUserString());
    				if(document != null){
    					// get the Note containing the additional classes field
    					String notes = document.getNotes();
    					if(notes != null){
    						// Specify regex pattern for classes
    						Pattern pattern = Pattern.compile("\\[classes\\](.*?)\\[/classes\\]");
    						Matcher matcher = pattern.matcher(notes);
    						String classes_str = "";
    						
    						// Extract only classes string inside of [classes] tags
    						if(matcher.find()){
    							classes_str = matcher.group(1);
    							StringValue classes = new StringValue(classes_str, StringValue.Style.BRACED);
    							
    							// Add classes to BibTeXDatabase
    							entry.addField(new Key("classes"), classes);
    						}
    					}
    				}
    			}
    		}
    		
    	}catch (TokenMgrException | IOException |ParseException e) {
			e.printStackTrace();
		}
    	return db;
    }
    
    /**
     * This Methods opens the MendeleyOAuthDialog which sisplay the authorization user interface.
     * 
     * @param shell
     * @return This returns if Login was successful
     */
    public boolean displayAuthorizationUserInterface(Shell shell){
    	
    	MendeleyOAuthDialog oauth_D = new MendeleyOAuthDialog(shell);
		oauth_D.create();
		
		if (oauth_D.open() == Window.OK) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * This Method exchanges the authorization code for an access token. 
     * If successful the Tokens and Expiration Date will be stored.
     * 
     * @param code The authorization code from the user interface response has to be passed
     * @throws IOException
     * @throws TokenMgrException
     * @throws ParseException
     */
    public void requestAccessToken(String code) throws IOException, TokenMgrException, ParseException {
	    try {
	      TokenResponse response =
	          new AuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),
	              new GenericUrl("https://api.mendeley.com/oauth/token"),code)
	              .setRedirectUri("https://localhost")
	              .setGrantType("authorization_code")
	              .setClientAuthentication(
	                  new BasicAuthentication("4335", "sSFcbUA38RS9Cpm7")).execute();
	      
	      this.access_token = response.getAccessToken();
	      this.refresh_token = response.getRefreshToken();
	      this.expires_at = this.generateExpiresAtFromExpiresIn(response.getExpiresInSeconds().intValue());
	      
	      updatePreferenceStore();
	      refreshTokenIfNecessary();
	    } catch (TokenResponseException e) {
	      if (e.getDetails() != null) {
	        System.err.println("Error: " + e.getDetails().getError());
	        if (e.getDetails().getErrorDescription() != null) {
	          System.err.println(e.getDetails().getErrorDescription());
	        }
	        if (e.getDetails().getErrorUri() != null) {
	          System.err.println(e.getDetails().getErrorUri());
	        }
	      } else {
	        System.err.println(e.getMessage());
	      }
	    }
    }
    
    /**
     * This Methods uses the refresh Token to retrieve a renewed access token
     * 
     * @param code Refresh Token
     * @return This returns if the request was successful.
     * @throws IOException
     * @throws TokenMgrException
     * @throws ParseException
     */
    public boolean requestRefreshAccessToken(String code) throws IOException, TokenMgrException, ParseException {
	    try {
	      RefreshTokenRequest request =
	          new RefreshTokenRequest(new NetHttpTransport(), new JacksonFactory(),
	              new GenericUrl("https://api.mendeley.com/oauth/token"),code)
	          	  .setRefreshToken(code)
	          	  .set("redirect_uri", "https://localhost")
	              .setGrantType("refresh_token")
	              .setClientAuthentication(
	                  new BasicAuthentication("4335", "sSFcbUA38RS9Cpm7"));
	      
	      TokenResponse response = request.execute();
	      
	      this.access_token = response.getAccessToken();
	      this.refresh_token = response.getRefreshToken();
	      this.expires_at = this.generateExpiresAtFromExpiresIn(response.getExpiresInSeconds().intValue());
	      
	      updatePreferenceStore();
	      refreshTokenIfNecessary();
	      
	      return true;
	    } catch (TokenResponseException e) {
	      if (e.getDetails() != null) {
	        System.err.println("Error: " + e.getDetails().getError());
	        if (e.getDetails().getErrorDescription() != null) {
	          System.err.println(e.getDetails().getErrorDescription());
	        }
	        if (e.getDetails().getErrorUri() != null) {
	          System.err.println(e.getDetails().getErrorUri());
	        }
	      } else {
	        System.err.println(e.getMessage());
	      }
	      return false;
	    }
    }
    
    /**
     * This method takes the milliseconds left until the access token expires and
     * generates a Calendar 
     * 
     * @param expiresIn Milliseconds until Token expires
     * @return This return a Calender object for the expiration date
     */
    private Calendar generateExpiresAtFromExpiresIn(int expiresIn) {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, expiresIn);
        return c;
    }
    
    /**
     * This method is called to check if the token is this valid.
     * The authorization interface will be called if the token is missing
     * or expired.
     * 
     * @return This returns if authorization is valid
     */
    public boolean refreshTokenIfNecessary(){
    	
    	// check if date is in already preference store
    	long date = store.getLong(PreferenceConstants.P_EXPIRE_DATE);
    	if(date != 0) {
    		Calendar preference_date = convertCalender(date);
    		if(expires_at == null) {
    			expires_at = preference_date;
    			access_token = store.getString(PreferenceConstants.P_TOKEN);
    			refresh_token = store.getString(PreferenceConstants.P_REFRESH_TOKEN);
    		}
    		else {
    			// check if expiration date from store is from latest token
    			if(preference_date.before(expires_at)) {
        			expires_at = preference_date;
        			access_token = store.getString(PreferenceConstants.P_TOKEN);
        			refresh_token = store.getString(PreferenceConstants.P_REFRESH_TOKEN);
        		}
    		}
    		
    	}
    	
    	// call auth interface when there is still no expiration date
    	if(expires_at == null) {
    		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    		return displayAuthorizationUserInterface(shell);
    	}
    	else {
    		final Calendar now = Calendar.getInstance();
        	
        	if(now.before(expires_at)){
        		final Calendar refresh_due = Calendar.getInstance();
        		refresh_due.setTimeInMillis(expires_at.getTimeInMillis());
        		refresh_due.add(Calendar.SECOND, -1200);
        		
        		// Refresh Token will be used 20 min before token expires
        		if(now.after(refresh_due)){
        			try {
    					return this.requestRefreshAccessToken(this.refresh_token);
    				} catch (TokenMgrException | IOException | ParseException e) {
    					e.printStackTrace();
    				}
        		} 		
        	}
        	
        	// call auth interface when token expired
        	if(now.after(expires_at)){
        		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        		return displayAuthorizationUserInterface(shell);
        	}
        	return true;
    	}
    }
    
    /**
     * This methods is used to update a Mendeley documents via the PATCH https://api.mendeley.com/documents/{id} endpoint. 
     * @param document Pass the document that needs to be updated
     */
    public void updateDocument(MendeleyDocument document ){
    	refreshTokenIfNecessary();
    	
    	HttpRequestFactory requestFactory = new ApacheHttpTransport().createRequestFactory();
    	HttpRequest request;
    	HttpRequest patch_request;
    	
    	Gson gson = new GsonBuilder().create();
    	String json_body = gson.toJson(document);
    	String document_id = document.getId();
    	String resource_url = "https://api.mendeley.com/documents/" + document_id;
    	GenericUrl gen_url = new GenericUrl(resource_url);
    	
		try {
			final HttpContent content = new ByteArrayContent("application/json", json_body.getBytes("UTF8") );
			patch_request = requestFactory.buildPatchRequest(gen_url, content);
			patch_request.getHeaders().setAuthorization("Bearer " + access_token);
			
			patch_request.getHeaders().setContentType("application/vnd.mendeley-document.1+json");
			String rawResponse = patch_request.execute().parseAsString();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * This methods is used to delete a Mendeley documents via the DELETE https://api.mendeley.com/documents/{id} endpoint. 
     * @param document Pass the document that needs to be deleted from Mendeley
     */
    public void deleteDocument(MendeleyDocument document ){
    	refreshTokenIfNecessary();
    	
    	HttpRequestFactory requestFactory = new ApacheHttpTransport().createRequestFactory();
    	HttpRequest request;
    	HttpRequest delete_request;
    	
    	Gson gson = new GsonBuilder().create();
    	String json_body = gson.toJson(document);
    	String document_id = document.getId();
    	String resource_url = "https://api.mendeley.com/documents/" + document_id;
    	GenericUrl gen_url = new GenericUrl(resource_url);
    	
		try {
			final HttpContent content = new ByteArrayContent("application/json", json_body.getBytes("UTF8") );
			delete_request = requestFactory.buildDeleteRequest(gen_url);
			delete_request.getHeaders().setAuthorization("Bearer " + access_token);
			
			delete_request.getHeaders().setContentType("application/vnd.mendeley-document.1+json");
			String rawResponse = delete_request.execute().parseAsString();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * This methods is used to add a new document to Mendeley via the POST https://api.mendeley.com/documents endpoint.
     * A successful response contains the newly added MendeleyDocument.
     * 
     * @param entry A BibTeXEntry is needed to add MendeleyDocument
     * @return This returns a MendeleyDocument if request was successful
     */
    public MendeleyDocument addDocument(BibTeXEntry entry ){
    	refreshTokenIfNecessary();
    	
    	HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
    	HttpRequest request;
    	HttpRequest patch_request;
    	MendeleyDocument document = null;
    	
    	// create a new MendeleyDocument and add the fields from BibTeXEntry that should be added
    	MendeleyDocument document_to_add = new MendeleyDocument(entry);
    	
    	Gson gson = new GsonBuilder().create();
    	String json_body = gson.toJson(document_to_add);
    	
    	String resource_url = "https://api.mendeley.com/documents";
    	GenericUrl gen_url = new GenericUrl(resource_url); 	
    	
		try {
			final HttpContent content = new ByteArrayContent("application/json", json_body.getBytes("UTF8") );
			
			patch_request = requestFactory.buildPostRequest(gen_url, content);
			patch_request.getHeaders().setAuthorization("Bearer " + access_token);
			patch_request.getHeaders().setContentType("application/vnd.mendeley-document.1+json");
			String rawResponse = patch_request.execute().parseAsString();
			document = gson.fromJson(rawResponse, MendeleyDocument.class);
		} catch (IOException e ) {
			e.printStackTrace();
		}

		return document;
    }
    
    /**
     * This methods is adds a document that exists in Mendeley to a specific folder
     * via the POST https://api.mendeley.com/folders/{id}/documents endpoint.
     * @param document This methods needs a dkocument to add
     * @param folder_id This passes the folder where to document is added to
     */
    public void addDocumentToFolder(MendeleyDocument document, String folder_id){
    	refreshTokenIfNecessary();
    	
    	HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
    	HttpRequest request;
    	HttpRequest patch_request;
    	
    	Gson gson = new GsonBuilder().create();
    	String json_body = "{\"id\": \""+ document.getId() + "\" }";
    	String resource_url = "https://api.mendeley.com/folders/"+ folder_id + "/documents";
    	GenericUrl gen_url = new GenericUrl(resource_url);
    		
		try {
			final HttpContent content = new ByteArrayContent("application/json", json_body.getBytes("UTF8") );
			patch_request = requestFactory.buildPostRequest(gen_url, content);
			patch_request.getHeaders().setAuthorization("Bearer " + access_token);
			patch_request.getHeaders().setContentType("application/vnd.mendeley-document.1+json");
			String rawResponse = patch_request.execute().parseAsString();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * This methods updates the Preference store when a new token is obtained 
     */
    private void updatePreferenceStore() {
    	store.setValue(PreferenceConstants.P_TOKEN, access_token);
		store.setValue(PreferenceConstants.P_REFRESH_TOKEN, refresh_token);
		store.setValue(PreferenceConstants.P_EXPIRE_DATE, expires_at.getTimeInMillis());
		store.setValue(PreferenceConstants.P_MENDELEY, "mendeley_on");
		try {
			if(store instanceof ScopedPreferenceStore) {
				((ScopedPreferenceStore) store).save();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private Calendar convertCalender(long milliseconds) {
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(milliseconds);
		return c;
    }
    
    /**
     * This methods removes tokens and expiration date from class and preference store
     * in order to logout the user from mendeley
     * 
     */
    public void logout() {
    	access_token = "";
    	refresh_token = "";
    	expires_at = null;
   
    	store.setValue(PreferenceConstants.P_TOKEN, access_token);
		store.setValue(PreferenceConstants.P_REFRESH_TOKEN, refresh_token);
		store.setValue(PreferenceConstants.P_EXPIRE_DATE, 0L);
		store.setValue(PreferenceConstants.P_MENDELEY, "mendeley_off");
		try {
			if(store instanceof ScopedPreferenceStore) {
				((ScopedPreferenceStore) store).save();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Remove links between Mendeley Folders and Files after logout
		wm = WorkspaceManager.getInstance();
		for(WorkspaceBibTexEntry entry : wm.getWorkspaceEntries()) {
			entry.setMendeleyFolder(null);
		}
		
		// Remove text decorations from Project Navigation after logout
		IDecoratorManager decoratorManager = PlatformUI.getWorkbench().getDecoratorManager();
		decoratorManager.update("de.tudresden.slr.model.mendeley.decorators.MendeleyOverlayDecorator");
    }
    
    
    public MendeleyFolder[] getMendeleyFolders() {
		return mendeleyFolders;
	}
    
    /**
     * Creates a new String without brackets at the start and end of a string.
     * Method is needed because Mendeley returns the 'title' field of its BibTeXEntries with additional brackets
     * 
     * @param string String that needs to be fixed
     * @return This returns string without additional brackets
     */
    private String getFixedString(String string) {
    	if(string.startsWith("{") & string.endsWith("}")) {
    		return string.substring(1, string.length() - 1);
    	}
    	return string;
    }
}
