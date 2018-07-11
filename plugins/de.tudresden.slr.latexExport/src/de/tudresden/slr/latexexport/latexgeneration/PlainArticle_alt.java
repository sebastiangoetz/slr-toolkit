//package de.tudresden.slr.latexexport.latexgeneration;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.lang.text.StrSubstitutor;
//import org.eclipse.emf.common.util.EList;
//import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
//import de.tudresden.slr.metainformation.util.DataProvider;
//import de.tudresden.slr.model.taxonomy.Term;
//
//public class PlainArticle_alt {
//	
//	public final String templatePath = "/resources/latexTemplates/plain/plainArticle.tex";
//
//	
//
////	public PlainArticle(SlrProjectMetainformation metainformation, DataProvider dataProvider, String filename) {
////		super(metainformation, dataProvider, filename);
////	}
//
////	String preamble = "\\documentclass{article}\r\n" + 
////			"\\usepackage{graphicx}\r\n" +
////			"\\providecommand{\\keywords}[1]{\\textbf{\\textit{Keywords---}} #1}\r\n" + 
////			"\r\n" + 
////			"\\begin{document}\r\n" + 
////			"\r\n";
////	
////	String titlePre = "\\title{";
////	String titlePost = "}\r\n";
////	
////	String authorPre = "\\author{";
////	String authorPost = "}\r\n";
////	
////	String abstractPre = " \r\n" + 
////			"\\maketitle\r\n" + 
////			"\\begin{abstract}\r\n";
////	
////	String abstractPost = "\\end{abstract}\r\n";
////	
////	String keywordsPre = "\\keywords{";
////	String keywordsPost = "asd,asd,asd}\r\n";
////	
////	String documentEnd = "\\end{document}";
//	
//	String document = "\\documentclass{article}\r\n" + 
//			"\\usepackage{graphicx}\r\n" + 
//			"\\providecommand{\\keywords}[1]{\\textbf{\\textit{Keywords---}} #1}\r\n" + 
//			"\r\n" + 
//			"\\begin{document}\r\n" + 
//			"\r\n" + 
//			"\r\n" + 
//			"\\title{ ${SLR_TITLE} }\r\n" + 
//			"\r\n" + 
//			"\\author{ ${SLR_AUTHORS} }\r\n" + 
//			"\r\n" + 
//			" \r\n" + 
//			"\\maketitle\r\n" + 
//			"\\begin{abstract}\r\n" + 
//			"${SLR_ABSTRACT}\r\n" + 
//			"\r\n" + 
//			"\\keywords{ ${SLR_KEYWORDS} }\r\n" + 
//			"\r\n" + 
//			"\\section{Statistics}\r\n" + 
//			"${SLR_STATISTICS}\r\n" + 
//			"${SLR_DIMENSIONCHARTS}\r\n" + 
//			"\r\n" + 
//			"\\end{document}\r\n";
//
//
//	@Override
//	public void performExport() throws FileNotFoundException, UnsupportedEncodingException {
//				
//		PrintWriter writer = new PrintWriter(new FileOutputStream(this.filename, false));
//		
////		writer.println(preamble);
////		writer.println(titlePre + metainformation.getTitle() + titlePost);
////		writer.println(authorPre + metainformation.getAuthors() + authorPost);
////		writer.println(abstractPre + metainformation.getProjectAbstract() + abstractPost);
////		writer.println(keywordsPre + metainformation.getKeywords() + keywordsPost);
////		writer.println(generateSectionTemplate("Statistics"));
////			writer.println("Number of documents: " + dataProvider.getNumberOfDocuments() + "\r\n");
////			
////		writer.println("\\begin{itemize} \r\n");
////		EList<Term> allDimensions = dataProvider.getMainDimensions();
////		for(Term t : allDimensions) {
////			writer.println("\\item " + t.getName() + ": " + dataProvider.getNumberOfElementsInDimension(t) + "\r\n");
////		}
////		writer.println("\\end{itemize}");
////		
////		for(Map.Entry<Term, String> entry : mainDimensions.entrySet()) {
////			writer.println(generateImageFigure(entry.getValue(), entry.getKey().getName()));
////		}
////		
////		writer.println(documentEnd);
////		writer.close();
//		
//		Map<String, String> valuesMap = new HashMap<String, String>();
//		valuesMap.put("SLR_TITLE", metainformation.getTitle());
//		//valuesMap.put("SLR_AUTHORS", metainformation.getAuthors());
//		valuesMap.put("SLR_ABSTRACT", metainformation.getProjectAbstract());
//		valuesMap.put("SLR_KEYWORDS", metainformation.getKeywords());
//		//valuesMap.put("SLR_STATISTICS", );
//		//valuesMap.put("SLR_DIMENSIONCHARTS", gen);
//
////		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
////		InputStream is = classloader.getResourceAsStream("latexTemplates/plain/plainArticle.tex");
////
////		StrSubstitutor sub = new StrSubstitutor(valuesMap);
////		String resolvedString = sub.replace(document);
////		// System.out.println(resolvedString);
////
////		// parse("asd");
//		System.out.println(getResourceContentAsString("plainArticle.tex").toString());
//	}
//}
