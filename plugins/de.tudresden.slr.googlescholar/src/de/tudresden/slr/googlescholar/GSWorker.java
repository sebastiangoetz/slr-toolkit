package de.tudresden.slr.googlescholar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

public class GSWorker {

	private WebClient webClient;
	private String Scholar = "http://scholar.google.de/";
	private String Base;
	private boolean ready = false;
	private List<String> results;
	private int start = 0;
	private Set<String> Links;
	private Iterator<String> current;
	
	public GSWorker(String as_q, String as_epq, String as_oq, String as_eq, String as_occt, String as_sauthors, String as_publication, String as_ylo, String as_yhi) {
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
	}
	
	public void init() {
		// load main page and do timing
		HtmlPage page;
		try {
			page = webClient.getPage(Scholar);
			waitLikeUser(3000, 10000);
			waitForJs(page);
			
			// select link to settings form, load settings form and do timing
			HtmlAnchor configLink = page.getFirstByXPath("//a[contains(@href, '/scholar_settings')]");
			HtmlPage configFormPage = webClient.getPage(configLink.getBaseURI() + configLink.getHrefAttribute().substring(1));
			waitLikeUser(3000, 10000);
			waitForJs(configFormPage);
			
			// select settings form and enable bibtex
			HtmlForm configForm = configFormPage.getFirstByXPath("//form[@action='/scholar_setprefs']");
			for(HtmlInput r : configForm.getInputsByName("scis")) {
				if(r.getAttribute("value").equals("yes")) {
					r.setChecked(true);
					break;
				}
			}

			// click save and do timing
			HtmlButton configFormSave = configForm.getButtonByName("save");
			HtmlPage readyForSearch = configFormSave.click();
			waitLikeUser(3000, 10000);
			waitForJs(readyForSearch);
			
			results = new ArrayList<String>();
			loadPage();
			ready = true;
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadPage() {
		Links = new HashSet<String>();
		try {
			HtmlPage Results = webClient.getPage((start > 0) ? Base + "&start=" + start : Base);
			waitLikeUser(3000, 10000);
			waitForJs(Results);
			
			for(HtmlAnchor a : Results.getAnchors()) {
				if(a.getHrefAttribute().contains("scholar.bib")) {
					Links.add(a.getHrefAttribute());
					
				}
			}
			start += 10;
			current = Links.iterator();
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean work() {
		if(ready) {
			if(!current.hasNext()) {
				loadPage();
			}
			if(!current.hasNext()) {
				ready = false;
				return false;
			} else {
				try {
					String url = current.next();
					current.remove();
				
					TextPage Result;
				
					Result = webClient.getPage(url);
					results.add(Result.getContent());
					waitLikeUser(10000, 30000);
				} catch (FailingHttpStatusCodeException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}
	
	public void waitForJs(HtmlPage page) {
		JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
		while (manager.getJobCount() > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void waitLikeUser(int min, int max) {
		try {
			Thread.sleep((long) (min + Math.random()*(max-min)));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> getResults() {
		return this.results;
	}
}