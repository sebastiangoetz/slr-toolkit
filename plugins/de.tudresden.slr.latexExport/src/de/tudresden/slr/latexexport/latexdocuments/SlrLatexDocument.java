package de.tudresden.slr.latexexport.latexdocuments;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

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
		return "\\begin{figure}[ht!]\r\n" + 
				"\\centering\r\n" + 
				"\\includegraphics[width = 1\\textwidth]{"+path+"}\r\n" + 
				"\\caption{"+caption+"}\r\n" + 
				"\\end{figure}\r\n";
	}
}
