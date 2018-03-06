package de.tudresden.slr.googlescholar;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

import de.tudresden.slr.googlescholar.preferences.PreferenceConstants;

public class GSWorker {

	private WebClient webClient;
	private String Scholar = "http://scholar.google.de/";
	private String Base;
	
	private PrintWriter out;
	private SubMonitor monitor;
	
	private final static IPreferenceStore store = new ScopedPreferenceStore( InstanceScope.INSTANCE, "de.tudresden.slr.googlescholar");
	private final static int MIN_WAIT =  store.getInt(PreferenceConstants.P_MIN_WAIT);
	private final static int MAX_WAIT =  store.getInt(PreferenceConstants.P_MAX_WAIT);
	
	public GSWorker(PrintWriter output, String as_q, String as_epq, String as_oq, String as_eq, String as_occt, String as_sauthors, String as_publication, String as_ylo, String as_yhi) {
		webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.setAjaxController(new AjaxController(){
			@Override
			public boolean processSynchron(HtmlPage page, WebRequest request, boolean async)
			{
				return true;
			}
		});
		webClient.getOptions().setScreenHeight(768);
		webClient.getOptions().setScreenWidth(1024);
		webClient.getCookieManager().setCookiesEnabled(true);
		
		Base = Scholar + "scholar?";
		try {
			if(as_q == null) {
				as_q = "";
			}
			Base += "as_q=" + URLEncoder.encode(as_q.replace(" ", "+"), "UTF-8") + "&";
			
			if(as_epq == null) {
				as_epq = "";
			}
			Base += "as_epq=" + URLEncoder.encode(as_epq.replace(" ", "+"), "UTF-8") + "&";
			
			if(as_oq == null) {
				as_oq = "";
			}
			Base += "as_oq=" + URLEncoder.encode(as_oq.replace(" ", "+"), "UTF-8") + "&";
			
			if(as_eq == null) {
				as_eq = "";
			}
			Base += "as_eq=" + URLEncoder.encode(as_eq.replace(" ", "+"), "UTF-8") + "&";
			
			if(as_occt == null) {
				as_occt = "";
			}
			Base += "as_occt=" + URLEncoder.encode(as_occt.replace(" ", "+"), "UTF-8") + "&";
			
			if(as_sauthors == null) {
				as_sauthors = "";
			}
			Base += "as_sauthors=" + URLEncoder.encode(as_sauthors.replace(" ", "+"), "UTF-8") + "&";
			
			if(as_publication == null) {
				as_publication = "";
			}
			Base += "as_publication=" + URLEncoder.encode(as_publication.replace(" ", "+"), "UTF-8") + "&";
			
			if(as_ylo == null) {
				as_ylo = "";
			}
			Base += "as_ylo=" + URLEncoder.encode(as_ylo.replace(" ", "+"), "UTF-8") + "&";
			
			if(as_yhi == null) {
				as_yhi = "";
			}
			Base += "as_yhi=" + URLEncoder.encode(as_yhi.replace(" ", "+"), "UTF-8") + "&";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Base += "hl=de&as_sdt=0%2C5";
		
		this.out = output;
		out.write("@Comment { Base-URL: " + Base + " }\n\n");
		out.flush();
	}
	
	public IStatus work(SubMonitor mon) {
		this.monitor = mon;
		IStatus s;
		
		mon.setTaskName("Preparing Google Scholar Session");
		s = this.initSession();
		if(!s.isOK()) {
			out.print("@Comment { Status: " + s.getMessage() + " }\n");
			out.close();
			return s;
		}
		
		// set monitor to a maximum of 100 pages x 10 entries
		mon.beginTask("Loading Entries", 1000);
		
		int last = 10;
		int start = 0;
		while(last > 0) {
			// Update Monitor
			mon.subTask("Loading Page " + ((start/10)+1));
			
			try {
				// Load Page
				out.print("@Comment { PageLoad: Offset " + start + " } \n");
				HtmlPage Results = webClient.getPage((start > 0) ? Base + "&start=" + start : Base);
				
				// Timeouts
				s = waitLikeUser();
				if(!s.isOK()) {
					out.print("@Comment { Status: " + s.getMessage() + " }\n");
					out.close();
					return s;
				}
				s = waitForJs(Results);
				if(!s.isOK()) {
					out.print("@Comment { Status: " + s.getMessage() + " }\n");
					out.close();
					return s;
				}
				
				// preparing sub task monitor for entries
				SubMonitor entries = monitor.newChild(10);
				
				// search for bib links
				last = 0;
				for(HtmlAnchor a : Results.getAnchors()) {
					// update submonitor
					entries.subTask("Loading Page " + ((start/10)+1) + " - Entry " + (last + 1));
					
					if(a.getHrefAttribute().contains("scholar.bib")) {
						// Load entry
						TextPage Result = webClient.getPage(a.getHrefAttribute());
						
						// Write entry
						out.print(Result.getContent() + "\n");
						
						// Timeouts
						s = waitLikeUser();
						if(!s.isOK()) {
							out.print("@Comment { Status: " + s.getMessage() + " }\n");
							out.close();
							return s;
						}
						s = waitForJs(Results);
						if(!s.isOK()) {
							out.print("@Comment { Status: " + s.getMessage() + " }\n");
							out.close();
							return s;
						}
						
						// Increment Counter
						last++;
						
						// increment monitor
						entries.worked(1);
					}
				}
				entries.done();
			} catch (FailingHttpStatusCodeException | IOException e) {
				out.print("@Comment { Error: " + e.getLocalizedMessage() + " }");
				out.close();
				return new Status(Status.ERROR, "de.tudresden.slr.googlescholar", "Could not load page or entry", e);
			}
			
			// Increment Offset
			start += 10;
		}
		
		out.print("@Comment { Finished }");
		out.close();
		monitor.done();
		return Status.OK_STATUS;
	}
	
	private IStatus initSession() {
		IStatus s;
		try {
			// load main page
			HtmlPage page = webClient.getPage(Scholar);
			
			// Timeouts
			s = waitLikeUser();
			if(!s.isOK()) return s;
			s = waitForJs(page);
			if(!s.isOK()) return s;
			
			// select link to settings form, load settings form
			HtmlAnchor configLink = page.getFirstByXPath("//a[contains(@href, '/scholar_settings')]");
			HtmlPage configFormPage = webClient.getPage(configLink.getBaseURI() + configLink.getHrefAttribute().substring(1));
			
			// Timeouts
			s = waitLikeUser();
			if(!s.isOK()) return s;
			s = waitForJs(page);
			if(!s.isOK()) return s;
			
			// select settings form and enable bibtex
			HtmlForm configForm = configFormPage.getFirstByXPath("//form[@action='/scholar_setprefs']");
			for(HtmlInput r : configForm.getInputsByName("scis")) {
				if(r.getAttribute("value").equals("yes")) {
					r.setChecked(true);
					break;
				}
			}

			// click save
			HtmlButton configFormSave = configForm.getButtonByName("save");
			HtmlPage readyForSearch = configFormSave.click();
			
			// Timeouts
			s = waitLikeUser();
			if(!s.isOK()) return s;
			s = waitForJs(page);
			if(!s.isOK()) return s;
		} catch (FailingHttpStatusCodeException | IOException e) {
			return new Status(Status.ERROR, "de.tudresden.slr.googlescholar", "Could not initialize Google Scholar session", e);
		}
		
		return Status.OK_STATUS;
	}
	
	private IStatus waitForJs(HtmlPage page) {
		JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
		while (manager.getJobCount() > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return new Status(Status.ERROR, "de.tudresden.slr.googlescholar", "Failed to wait for Javascript processes", e);
			}
			
			if(this.monitor != null && monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
		}
		return Status.OK_STATUS;
	}
	
	private IStatus waitLikeUser() {
		int time = (MIN_WAIT + ((int)Math.random()*(MAX_WAIT-MIN_WAIT))) / 1000;
		for(int i = 0; i < time; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return new Status(Status.ERROR, "de.tudresden.slr.googlescholar", "Timeout Failure", e);
			}
			
			if(this.monitor != null && monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
		}
		return Status.OK_STATUS;
	}
}