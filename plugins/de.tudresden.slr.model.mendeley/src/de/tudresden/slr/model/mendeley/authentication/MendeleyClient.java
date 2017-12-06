package de.tudresden.slr.model.mendeley.authentication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.swt.widgets.Shell;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.http.HttpResponseException;

import de.tudresden.slr.model.mendeley.ui.MendeleyOAuthDialog;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.bibtex.*;
import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class MendeleyClient {
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
    
    protected AdapterFactoryEditingDomain editingDomain;
    
    // Innere private Klasse, die erst beim Zugriff durch die umgebende Klasse initialisiert wird
    private static final class InstanceHolder {
      // Die Initialisierung von Klassenvariablen geschieht nur einmal 
      // und wird vom ClassLoader implizit synchronisiert
      static final MendeleyClient INSTANCE = new MendeleyClient();
      
    }

    // Verhindere die Erzeugung des Objektes über andere Methoden
    private MendeleyClient () {}
    // Eine nicht synchronisierte Zugriffsmethode auf Klassenebene.
    public static MendeleyClient getInstance () {
      return InstanceHolder.INSTANCE;
    }
    
    /*
    public MendeleyClient() {
		this.access_token = "";
		this.refresh_token = "";
				
	}
    
    public MendeleyClient(String access_token, String refresh_token) {
		this.access_token = access_token;
		this.refresh_token = refresh_token;
	}
    */
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
    
    public String getAllDocuments() throws MalformedURLException, IOException, TokenMgrException, ParseException{
    	String resource_url = "https://api.mendeley.com/documents";
    	
    	HttpURLConnection resource_cxn = (HttpURLConnection)(new URL(resource_url).openConnection());
        resource_cxn.addRequestProperty("Authorization", "Bearer " + this.access_token);
        resource_cxn.setRequestMethod("GET");
        resource_cxn.setRequestProperty("Accept","application/x-bibtex");
        
        InputStream resource = resource_cxn.getInputStream();
    	
        BufferedReader r = new BufferedReader(new InputStreamReader(resource, "UTF-8"));
        String line = null;
        String bibtex = "";
        while ((line = r.readLine()) != null) {
        	bibtex = bibtex + line;
            //System.out.println(line);
        }
        
        parseDocumentStream(bibtex);
        
    	return resource.toString();
    }
    
    public void displayAuthorizationUserInterface(Shell shell){
    	MendeleyOAuthDialog oauth_D = new MendeleyOAuthDialog(shell);
		oauth_D.create();
		oauth_D.open();
    }
    
    public void initializeTokenRequest() throws OAuthSystemException, OAuthProblemException{
    	OAuthClient client = new OAuthClient(new URLConnectionClient());
		
		OAuthClientRequest request =
                OAuthClientRequest.tokenLocation("https://api.mendeley.com/oauth/token")
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId("4335")
                .setClientSecret("sSFcbUA38RS9Cpm7")
                .setCode(this.auth_code)
                .setRedirectURI("https://localhost")
                // .setScope() here if you want to set the token scope
                .buildQueryMessage();
		//request.addHeader("Accept", "application/json");
        //request.addHeader("Content-Type", "application/json");
		//request.setHeader(OAuth.HeaderType.CONTENT_TYPE, "multipart/form-data"); 
		
		System.out.println(request.getLocationUri() + request.getBody());
		//OAuthJSONAccessTokenResponse resourceResponse = client.accessToken(request, OAuth.HttpMethod.POST);
		//OAuthClient client = new OAuthClient(new 	new URLConnectionClient());
		//GitHubTokenResponse  resourceResponse = client.accessToken(request, GitHubTokenResponse.class);
		String token = client.accessToken(request, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class).getAccessToken();
		System.out.println(token);
		//System.out.println(resourceResponse.getAccessToken());
		//this.access_token= resourceResponse.getAccessToken();
    }
    
    public void requestAccessToken(String code) throws IOException, TokenMgrException, ParseException {
	    try {
	      //JsonFactory jack = new JacksonFactory();
	      TokenResponse response =
	          new AuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),
	              new GenericUrl("https://api.mendeley.com/oauth/token"),code)
	              .setRedirectUri("https://localhost")
	              .setGrantType("authorization_code")
	              .setClientAuthentication(
	                  new BasicAuthentication("4335", "sSFcbUA38RS9Cpm7")).execute();
	      System.out.println("Access token: " + response.getAccessToken());
	      this.access_token = response.getAccessToken();
	      this.refresh_token = response.getRefreshToken();
	      this.getAllDocuments();
	      
	      compareDocuments();
	      
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
    
    protected void initializeEditingDomain() {
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent((domain) -> editingDomain = domain);
		//contentProvider = new AdapterFactoryContentProvider(editingDomain.getAdapterFactory());
	}
    
    public void parseDocumentStream(String inputAPI){
    	
    	BibTeXDatabase db = new BibTeXDatabase();
    	try(Reader reader = new StringReader(inputAPI)){
    		BibTeXParser parser = new BibTeXParser();
    		db = parser.parse(reader);
    		
    	}catch (TokenMgrException | IOException |ParseException e) {
			e.printStackTrace();
		}
    	
    	
    	Map<Key, BibTeXEntry> entryMap = Collections.emptyMap();
		entryMap = db.getEntries();
		
		System.out.println("-- List of Keys from Mendeley Documens:");
		for(BibTeXEntry entry : entryMap.values()){
			System.out.println(entry.getKey().getValue());
			
		}
    	
    }
    
    public void compareDocuments(){
    	IWorkspace workspace = ResourcesPlugin.getWorkspace();
    	initializeEditingDomain();
    	ResourceSet resource_set = editingDomain.getResourceSet();
    	for(Resource res : resource_set.getResources()){
    		System.out.println("-- List of Keys from " + res.getURI() + ":");
    		for(EObject content : res.getContents()){
    			if(content instanceof DocumentImpl){
    				DocumentImpl doc = (DocumentImpl) content;
    				System.out.println(doc.getKey());
    			}
    			
    		}
    		if(res instanceof BibtexResourceImpl){
    			System.out.println("Yes");
    		}
    	}
    	
    	
    	System.out.println(resource_set.toString());
    }
    
    
    
}
