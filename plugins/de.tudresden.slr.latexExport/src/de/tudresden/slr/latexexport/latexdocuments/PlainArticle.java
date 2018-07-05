package de.tudresden.slr.latexexport.latexdocuments;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public class PlainArticle extends SlrLatexDocument {
	

	public PlainArticle(SlrProjectMetainformation metainformation, DataProvider dataProvider, String filename) {
		super(metainformation, dataProvider, filename);
		// TODO Auto-generated constructor stub
	}

	String preamble = "\\documentclass{article}\r\n" + 
			"\\usepackage{graphicx}\r\n" +
			"\\providecommand{\\keywords}[1]{\\textbf{\\textit{Keywords---}} #1}\r\n" + 
			"\r\n" + 
			"\\begin{document}\r\n" + 
			"\r\n";
	
	String titlePre = "\\title{";
	String titlePost = "}\r\n";
	
	String authorPre = "\\author{";
	String authorPost = "}\r\n";
	
	String abstractPre = " \r\n" + 
			"\\maketitle\r\n" + 
			"\\begin{abstract}\r\n";
	
	String abstractPost = "\\end{abstract}\r\n";
	
	String keywordsPre = "\\keywords{";
	String keywordsPost = "asd,asd,asd}\r\n";
	
	String documentEnd = "\\end{document}";


	@Override
	public void performExport() throws FileNotFoundException, UnsupportedEncodingException {
		Map<Term, String> mainDimensions = exportCharts();
				
		PrintWriter writer = new PrintWriter(new FileOutputStream(this.filename, false));
		
		writer.println(preamble);
		writer.println(titlePre + metainformation.getTitle() + titlePost);
		writer.println(authorPre + metainformation.getAuthors() + authorPost);
		writer.println(abstractPre + metainformation.getProjectAbstract() + abstractPost);
		writer.println(keywordsPre + metainformation.getKeywords() + keywordsPost);
		writer.println(generateSectionTemplate("Statistics"));
			writer.println("Number of documents: " + dataProvider.getNumberOfDocuments() + "\r\n");
			
		writer.println("\\begin{itemize} \r\n");
		EList<Term> allDimensions = dataProvider.getMainDimensions();
		for(Term t : allDimensions) {
			writer.println("\\item " + t.getName() + ": " + dataProvider.getNumberOfElementsInDimension(t) + "\r\n");
		}
		writer.println("\\end{itemize}");
		
		for(Map.Entry<Term, String> entry : mainDimensions.entrySet()) {
			writer.println(generateImageFigure(entry.getValue(), entry.getKey().getName()));
		}
		
		writer.println(documentEnd);
		writer.close();
	}
}
