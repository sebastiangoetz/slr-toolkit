package de.tudresden.slr.googlescholar;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
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

	private static final String PREFERENCE_NAME = "de.tudresden.slr.googlescholar";
	private static final String ENCODING = "UTF-8";

	private WebClient webClient;
	private String scholar = "http://scholar.google.de/";
	private String base;

	private PrintWriter out;
	private SubMonitor monitor;

	private static final IPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, PREFERENCE_NAME);
	private static final int MIN_WAIT = store.getInt(PreferenceConstants.P_MIN_WAIT);
	private static final int MAX_WAIT = store.getInt(PreferenceConstants.P_MAX_WAIT);

	public GSWorker(PrintWriter output, String asQ, String asEpq, String asOq, String asEq, String asOcct,
			String asSauthors, String asPublication, String asYlo, String asYhi) {
		webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.setAjaxController(new AjaxController() {
			@Override
			public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
				return true;
			}
		});
		webClient.getOptions().setScreenHeight(768);
		webClient.getOptions().setScreenWidth(1024);
		webClient.getCookieManager().setCookiesEnabled(true);

		base = scholar + "scholar?";
		try {
			if (asQ == null) {
				asQ = "";
			}
			base += "as_q=" + URLEncoder.encode(asQ.replace(" ", "+"), ENCODING) + "&";

			if (asEpq == null) {
				asEpq = "";
			}
			base += "as_epq=" + URLEncoder.encode(asEpq.replace(" ", "+"), ENCODING) + "&";

			if (asOq == null) {
				asOq = "";
			}
			base += "as_oq=" + URLEncoder.encode(asOq.replace(" ", "+"), ENCODING) + "&";

			if (asEq == null) {
				asEq = "";
			}
			base += "as_eq=" + URLEncoder.encode(asEq.replace(" ", "+"), ENCODING) + "&";

			if (asOcct == null) {
				asOcct = "";
			}
			base += "as_occt=" + URLEncoder.encode(asOcct.replace(" ", "+"), ENCODING) + "&";

			if (asSauthors == null) {
				asSauthors = "";
			}
			base += "as_sauthors=" + URLEncoder.encode(asSauthors.replace(" ", "+"), ENCODING) + "&";

			if (asPublication == null) {
				asPublication = "";
			}
			base += "as_publication=" + URLEncoder.encode(asPublication.replace(" ", "+"), ENCODING) + "&";

			if (asYlo == null) {
				asYlo = "";
			}
			base += "as_ylo=" + URLEncoder.encode(asYlo.replace(" ", "+"), ENCODING) + "&";

			if (asYhi == null) {
				asYhi = "";
			}
			base += "as_yhi=" + URLEncoder.encode(asYhi.replace(" ", "+"), ENCODING) + "&";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		base += "hl=de&as_sdt=0%2C5";

		this.out = output;
		out.write("@Comment { Base-URL: " + base + " }\n\n");
		out.flush();
	}

	public IStatus work(SubMonitor mon) {
		this.monitor = mon;
		IStatus s;

		mon.setTaskName("Preparing Google Scholar Session");
		s = this.initSession();
		if (!s.isOK()) {
			out.print("@Comment { Status: " + s.getMessage() + " }\n");
			out.close();
			return s;
		}

		// set monitor to a maximum of 100 pages x 10 entries
		mon.beginTask("Loading Entries", 1000);

		int last = 10;
		int start = 0;
		while (last > 0) {
			// Update Monitor
			mon.subTask("Loading Page " + ((start / 10) + 1));

			try {
				// Load Page
				out.print("@Comment { PageLoad: Offset " + start + " } \n");
				HtmlPage Results = webClient.getPage((start > 0) ? base + "&start=" + start : base);

				// Timeouts
				s = waitLikeUser();
				if (!s.isOK()) {
					out.print("@Comment { Status: " + s.getMessage() + " }\n");
					out.close();
					return s;
				}
				s = waitForJs(Results);
				if (!s.isOK()) {
					out.print("@Comment { Status: " + s.getMessage() + " }\n");
					out.close();
					return s;
				}

				// preparing sub task monitor for entries
				SubMonitor entries = monitor.newChild(10);

				// search for bib links
				last = 0;
				for (HtmlAnchor a : Results.getAnchors()) {
					// update submonitor
					entries.subTask("Loading Page " + ((start / 10) + 1) + " - Entry " + (last + 1));

					if (a.getHrefAttribute().contains("scholar.bib")) {
						// Load entry
						TextPage Result = webClient.getPage(a.getHrefAttribute());

						// Write entry
						out.print(Result.getContent() + "\n");

						// Timeouts
						s = waitLikeUser();
						if (!s.isOK()) {
							out.print("@Comment { Status: " + s.getMessage() + " }\n");
							out.close();
							return s;
						}
						s = waitForJs(Results);
						if (!s.isOK()) {
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

	protected IStatus initSession() {
		IStatus s;
		try {
			// load main page
			HtmlPage page = webClient.getPage(scholar);

			// Timeouts
			s = waitLikeUser();
			if (!s.isOK())
				return s;
			s = waitForJs(page);
			if (!s.isOK())
				return s;

			// select link to settings form, load settings form
			HtmlAnchor configLink = page.getFirstByXPath("//a[contains(@href, '/scholar_settings')]");
			HtmlPage configFormPage = webClient
					.getPage(configLink.getBaseURI() + configLink.getHrefAttribute().substring(1));

			// Timeouts
			s = waitLikeUser();
			if (!s.isOK())
				return s;
			s = waitForJs(page);
			if (!s.isOK())
				return s;

			// select settings form and enable bibtex
			HtmlForm configForm = configFormPage.getFirstByXPath("//form[@action='/scholar_setprefs']");
			for (HtmlInput r : configForm.getInputsByName("scis")) {
				if (r.getAttribute("value").equals("yes")) {
					r.setChecked(true);
					break;
				}
			}

			// click save
			HtmlButton configFormSave = configForm.getButtonByName("save");
			HtmlPage readyForSearch = configFormSave.click();

			// Timeouts
			s = waitLikeUser();
			if (!s.isOK())
				return s;
			s = waitForJs(page);
			if (!s.isOK())
				return s;
		} catch (FailingHttpStatusCodeException | IOException e) {
			return new Status(Status.ERROR, "de.tudresden.slr.googlescholar",
					"Could not initialize Google Scholar session", e);
		}

		return Status.OK_STATUS;
	}

	protected IStatus waitForJs(HtmlPage page) {
		JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
		while (manager.getJobCount() > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return new Status(Status.ERROR, "de.tudresden.slr.googlescholar",
						"Failed to wait for Javascript processes", e);
			}

			if (this.monitor != null && monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
		}
		return Status.OK_STATUS;
	}

	protected IStatus waitLikeUser() {
		int time = (MIN_WAIT + ((int) Math.random() * (MAX_WAIT - MIN_WAIT))) / 1000;
		for (int i = 0; i < time; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return new Status(Status.ERROR, "de.tudresden.slr.googlescholar", "Timeout Failure", e);
			}

			if (this.monitor != null && monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
		}
		return Status.OK_STATUS;
	}
}