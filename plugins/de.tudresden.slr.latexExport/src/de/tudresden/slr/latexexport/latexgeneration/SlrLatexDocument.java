package de.tudresden.slr.latexexport.latexgeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import de.tudresden.slr.latexexport.data.LatexExportChartGenerator;
import de.tudresden.slr.latexexport.helpers.LatexDocumentHelper;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public abstract class SlrLatexDocument {
	protected SlrProjectMetainformation metainformation;
	protected DataProvider dataProvider;
	protected String filename;

	public SlrLatexDocument(SlrProjectMetainformation metainformation, DataProvider dataProvider, String filename) {
		super();
		this.metainformation = metainformation;
		this.dataProvider = dataProvider;
		this.filename = filename;
	}

	public Map<Term, String> exportCharts() throws FileNotFoundException, UnsupportedEncodingException {
		return LatexExportChartGenerator.generatePDFOutput(filename, dataProvider);
	}

	public abstract void performExport() throws FileNotFoundException, UnsupportedEncodingException;

	public String generateSectionTemplate(String title) {
		String sectionPreTitle = "\\section{";
		String sectionPostTitle = "}";

		return sectionPreTitle + title + sectionPostTitle;
	}

	public String generateImageFigure(String path, String caption) {
		return "\\begin{figure}[ht!]\r\n" + "\\centering\r\n" + "\\includegraphics[width = 1\\textwidth]{" + path
				+ "}\r\n" + "\\caption{" + caption + "}\r\n" + "\\end{figure}\r\n";
	}

	public URL getFileForResource(String resourceName) {
		//URL fileURL = bundle.getEntry("resources/latexTemplates/plain/plainArticle.tex");

		URL url = null;
		try {
			url = new URL("platform:/plugin/de.tudresden.slr.latexExport/resources/latexTemplates/plain/plainArticle.tex");
			InputStream inputStream = url.openConnection().getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String inputLine;

			System.out.println("accessing file:");
			
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
			}

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return url;
	}
}
