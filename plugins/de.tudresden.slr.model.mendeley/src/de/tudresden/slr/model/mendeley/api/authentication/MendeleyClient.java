package de.tudresden.slr.model.mendeley.api.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.ldap.PagedResultsControl;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.apache.http.entity.StringEntity;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.CharacterFilterReader;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.StringValue;
import org.jbibtex.StringValue.Style;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.BrowserClientRequestUrl;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.api.client.http.apache.ApacheHttpTransport;

import de.tudresden.slr.model.mendeley.Activator;
import de.tudresden.slr.model.mendeley.api.model.MendeleyAnnotation;
import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;
import de.tudresden.slr.model.mendeley.preferences.PreferenceConstants;
import de.tudresden.slr.model.mendeley.ui.MSyncWizard;
import de.tudresden.slr.model.mendeley.ui.MendeleyOAuthDialog;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class MendeleyClient {

	private static final String oauth_request_url = "https://api.mendeley.com/oauth/authorize";
	/**
     * URL for requesting OAuth access tokens.
     */
    private static final String token_request_url = "https://api.mendeley.com/oauth/token";

    /**
     * Client ID of your client credential.  Change this to match whatever credential you have created.
     */
    private static final String client_id = "4335";

    /**
     * Client secret of your client credential.  Change this to match whatever credential you have created.
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
     * Expires At
     */
    private Calendar expires_at;
    
    private MendeleyFolder[] mendeleyFolders = null;
    
    private IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    
    protected AdapterFactoryEditingDomain editingDomain;
    
    private static final class InstanceHolder {
      static final MendeleyClient INSTANCE = new MendeleyClient();
      
    }

    // Verhindere die Erzeugung des Objektes über andere Methoden
    private MendeleyClient () {}
    // Eine nicht synchronisierte Zugriffsmethode auf Klassenebene.
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
    
    public final String getAuthURL(){
    	return 	oauth_request_url + "?" +
    			"&client_id=" + client_id +
    			"&redirect_uri=" + redirect_uri +
    			"&response_type=code&scope=all";
    }
    
    public String getAllDocumentsBibTex() {
    	refreshTokenIfNecessary();
    	
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
    
    public MendeleyAnnotation[] getAnnotations(MendeleyDocument document) throws MalformedURLException, IOException{
    	refreshTokenIfNecessary();
    	
    	String result = "";
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
    
    
    public BibTeXDatabase getDocumentBibTex( String id) throws MalformedURLException, IOException, TokenMgrException, ParseException{
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
    		
    		//TODO: Mendeley API parsing error - Parameter: title - additional characters '{' and '}'
    		for(BibTeXEntry entry: db.getEntries().values()){
    			String fixedTitleStr = entry.getField(new Key("title")).toUserString().replaceAll("\\{", "").replaceAll("\\}", "");
    			StringValue fixedTitleValue = new StringValue(fixedTitleStr, StringValue.Style.BRACED);
    			entry.removeField(new Key("title"));
    			entry.addField(new Key("title"), fixedTitleValue);
    			
    		}
    		
    	}catch (TokenMgrException | IOException |ParseException e) {
			e.printStackTrace();
		}
  
        
    	return db;
    }
    
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
    
    public MendeleyFolder[] getAllMendeleyFolders() throws MalformedURLException, TokenMgrException, IOException, ParseException{
    	refreshTokenIfNecessary();
    	
        String folders_str = this.getAllFolders();
        Gson gson = new Gson();
        MendeleyFolder[] m_folders = gson.fromJson(folders_str, MendeleyFolder[].class);
		for(int i = 0; i < m_folders.length;i++){
			String folder_content_str;
			try {
				folder_content_str = this.getDocumentsByFolderJSON(m_folders[i].getId());
			
				BibTeXDatabase bibdb = this.getDocumentsByFolderBibTex(m_folders[i]);
				m_folders[i].setBibtexDatabase(bibdb);
				
				MendeleyDocument[] folder_content = gson.fromJson(folder_content_str, MendeleyDocument[].class);
				m_folders[i].setDocuments(folder_content);
				m_folders[i].setType("Folder");
				
				for(MendeleyDocument md : m_folders[i].getDocuments()){
					String notes = md.getNotes();
					MendeleyAnnotation[] annotations = getAnnotations(md);
					for(MendeleyAnnotation annotation: annotations){
						if(annotation.getType().equals("note")){
							notes = annotation.getText();
						}
					}
					md.setNotes(notes);
					
				}
				
			} catch (TokenMgrException | IOException | ParseException e) {
				e.printStackTrace();
			}
        }
		return m_folders;
    }
    
    public MendeleyFolder getMendeleyFolder(String folderId) throws MalformedURLException, TokenMgrException, IOException, ParseException{
    	refreshTokenIfNecessary();
    	
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
        
        System.out.println("Folder with id: " + id);
        System.out.println(json_str);
    	return json_str;
    }
    
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
    		
    		//TODO: Mendeley API parsing error - Parameter: title - additional characters '{' and '}'
    		for(BibTeXEntry entry: db.getEntries().values()){
    			String fixedTitleStr = entry.getField(new Key("title")).toUserString().replaceAll("\\{", "").replaceAll("\\}", "");
    			StringValue fixedTitleValue = new StringValue(fixedTitleStr, StringValue.Style.BRACED);
    			entry.removeField(new Key("title"));
    			entry.addField(new Key("title"), fixedTitleValue);
    			
    			Value value = entry.getField(new Key("title"));
    			if(value != null){
    				MendeleyDocument document = mf.getDocumentByTitle(value.toUserString());
    				if(document != null){
    					String notes = document.getNotes();
    					if(notes != null){
    						Pattern pattern = Pattern.compile("\\[classes\\](.*?)\\[/classes\\]");
    						Matcher matcher = pattern.matcher(notes);
    						String classes_str = "";
    						
    						if(matcher.find()){
    							classes_str = matcher.group(1);
    							System.out.println(classes_str);
    							StringValue classes = new StringValue(classes_str, StringValue.Style.BRACED);
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
    
    public boolean displayAuthorizationUserInterface(Shell shell){
    	
    	MendeleyOAuthDialog oauth_D = new MendeleyOAuthDialog(shell);
		oauth_D.create();
		
		if (oauth_D.open() == Window.OK) {
            System.out.println("Ok pressed");
            return true;
        } else {
            System.out.println("Cancel pressed");
            return false;
        }
		
    	
		/*
		WizardDialog wizardDialog = new WizardDialog(shell,
	            new MSyncWizard());
        if (wizardDialog.open() == Window.OK) {
            System.out.println("Ok pressed");
        } else {
            System.out.println("Cancel pressed");
        }
        */	
    }
    
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
	      
	      System.out.println("Access Token successfully acquired");
	      
	      initializeEditingDomain();
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
    
    private Calendar generateExpiresAtFromExpiresIn(int expiresIn) {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, expiresIn);
        return c;
    }
    
    public boolean refreshTokenIfNecessary(){
    	
    	long date = store.getLong(PreferenceConstants.P_EXPIRE_DATE);
    	if(date != 0) {
    		Calendar preference_date = convertCalender(date);
    		if(expires_at == null) {
    			expires_at = preference_date;
    			access_token = store.getString(PreferenceConstants.P_TOKEN);
    			refresh_token = store.getString(PreferenceConstants.P_REFRESH_TOKEN);
    		}
    		else {
    			if(preference_date.before(expires_at)) {
        			expires_at = preference_date;
        			access_token = store.getString(PreferenceConstants.P_TOKEN);
        			refresh_token = store.getString(PreferenceConstants.P_REFRESH_TOKEN);
        		}
    		}
    		
    	}
    	
    	
    	if(expires_at == null) {
    		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    		return displayAuthorizationUserInterface(shell);
    	}
    	else {
    		final Calendar now = Calendar.getInstance();
        	System.out.println("It is " + now.getTime());
        	System.out.println("Key will expire at: " + expires_at.getTime() );
        	
        	if(now.before(expires_at)){
        		final Calendar refresh_due = Calendar.getInstance();
        		refresh_due.setTimeInMillis(expires_at.getTimeInMillis());
        		refresh_due.add(Calendar.SECOND, -1200);
        		System.out.print("Refresh Token will be used afer " + refresh_due.getTime());
        		if(now.after(refresh_due)){
        			System.out.println("This Key Must be Refreshed Maaaan");
        			try {
    					return this.requestRefreshAccessToken(this.refresh_token);
    				} catch (TokenMgrException | IOException | ParseException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
        		} 		
        	}
        	
        	if(now.after(expires_at)){
        		// TODO open MendeleyOAUthDialog
        		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        		return displayAuthorizationUserInterface(shell);
        	}
        	
        	return true;
    	}
    }
    
    protected void initializeEditingDomain() {
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent((domain) -> editingDomain = domain);
		AdapterFactoryContentProvider contentProvider = new AdapterFactoryContentProvider(editingDomain.getAdapterFactory());
		for (Resource resource : editingDomain.getResourceSet().getResources()) {
			if(resource instanceof BibtexResourceImpl){
				BibtexResourceImpl bibResource = (BibtexResourceImpl)resource;
				for(EObject econ : bibResource.getContents()){
					DocumentImpl document = (DocumentImpl) econ;
				}
			}
		}
	}
    
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
			System.out.println(rawResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    public MendeleyDocument addDocument(BibTeXEntry entry ){
    	refreshTokenIfNecessary();
    	
    	HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
    	HttpRequest request;
    	HttpRequest patch_request;
    	MendeleyDocument document = null;
    	
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
			System.out.println(rawResponse);
		} catch (IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return document;
    	
    }
    
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
			System.out.println(rawResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private Calendar convertCalender(long milliseconds) {
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(milliseconds);
		return c;
    }
    
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void updateMendeleyFolders() throws MalformedURLException, TokenMgrException, IOException, ParseException {
    	mendeleyFolders = getAllMendeleyFolders();
    }
    
    public MendeleyFolder[] getMendeleyFolders() {
		return mendeleyFolders;
	}
}
