package de.tudresden.slr.model.mendeley.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import de.tudresden.slr.model.mendeley.Activator;
import de.tudresden.slr.model.mendeley.api.client.MendeleyClient;

/**
 * This class implements a Dialog used for displaying the Mendeley
 * authorization screen.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 * @see org.eclipse.jface.dialogs.Dialog
 */
public class MendeleyOAuthDialog extends Dialog {
	MendeleyClient mendeley_client = MendeleyClient.getInstance();

	/**
	 * Preference Store to update latest login events
	 */
	private IPreferenceStore store;
	
    public MendeleyOAuthDialog(Shell parentShell) {
        super(parentShell);
        this.store = Activator.getDefault().getPreferenceStore();
        
    }

    @Override
    protected Control createDialogArea(Composite parent) {
    	Composite container = (Composite) super.createDialogArea(parent);
    	GridLayout gridLayout = (GridLayout) container.getLayout();
    	gridLayout.marginWidth = 0;
    	gridLayout.marginHeight = 0;
    	Browser browser;
    	
    	// browser will show authorization screen
    	browser = new Browser(container, SWT.NONE);
        
        GridData gd_browser = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
        gd_browser.heightHint = 439;
        gd_browser.widthHint = 644;
        browser.setLayoutData(gd_browser);
        
        browser.addLocationListener(new LocationListener() {
			
			@Override
			public void changing(LocationEvent event) {
				
			}
			
			@Override
			public void changed(LocationEvent event) {
				URL aURL;
				
				try {
					aURL = new URL(event.location);
					// if login was successful the redirection will change the location to 'localhost'
					if(aURL.getHost().equals("localhost")){
						// Capture the authorization code
						String auth_code = aURL.getQuery().replaceAll("code=", "");
						mendeley_client.setAuth_code(auth_code);
						mendeley_client.requestAccessToken(auth_code);
					        
					    close();
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TokenMgrException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});
		
		browser.setUrl("https://api.mendeley.com/oauth/authorize?client_id=4335&redirect_uri=https:%2F%2Flocalhost&response_type=code&scope=all");
		
        return container;
    }

    // overriding this methods allows you to set the
    // title of the custom dialog
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Mendeley Login");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(650, 475);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
    	GridLayout layout = (GridLayout)parent.getLayout();
    	layout.marginHeight = 0;
    }

}