package de.tudresden.slr.latexexport.latexgeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;

import de.tudresden.slr.latexexport.data.LatexExportChartGenerator;
import de.tudresden.slr.latexexport.documentclasses.SlrLatexTemplate;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public class SlrLatexDocument {
	protected SlrProjectMetainformation metainformation;
	protected DataProvider dataProvider;
	protected String filename;
	protected SlrLatexTemplate concreteDocument;
	Map<Term, String> mainDimensions;

	public SlrLatexDocument(SlrProjectMetainformation metainformation, DataProvider dataProvider, String filename, SlrLatexTemplate concreteDocument) {
		super();
		this.metainformation = metainformation;
		this.dataProvider = dataProvider;
		this.filename = filename;
		this.concreteDocument = concreteDocument;
	}

	public Map<Term, String> exportCharts() throws FileNotFoundException, UnsupportedEncodingException {
		return LatexExportChartGenerator.generatePDFOutput(filename, dataProvider);
	}

	public String getResourceContentAsString(URL url) {
		//URL fileURL = bundle.getEntry("resources/latexTemplates/plain/plainArticle.tex");
		String toReturn = "";
		try {
			InputStream inputStream = url.openConnection().getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				toReturn = toReturn + "\r\n" + inputLine;
			}

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return toReturn;
	}
	
	public void copyResources(URL[] resources) {
		
	}
	
	
	public final void performExport() throws IOException {
		copyResources(null);
		mainDimensions = exportCharts();
		String filledDocument = fillDocument(concreteDocument);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    writer.write(filledDocument);
	    writer.close();
	}
	
	
	public String fillDocument(SlrLatexTemplate template) {
		String document = getResourceContentAsString(template.getTemplatePath());
		return template.fillDocument(document, metainformation, dataProvider, mainDimensions);
	}
}
