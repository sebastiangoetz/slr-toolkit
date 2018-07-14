package de.tudresden.slr.latexexport.latexgeneration.documentclasses;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public abstract class SlrLatexTemplate {
	public static final String TEMPLATE_PLAIN = "Plain Article";
	public static final String TEMPLATE_TUDSCR = "tudscr";
	public static final String TEMPLATE_IEEE = "IEEEconf";
	public static final String TEMPLATE_ACM = "ACM SIGPLAN conf";
	public static final String TEMPLATE_SPRINGER_LNCS = "Springer LNCS";
	
	public static final String[] documentTypes = { TEMPLATE_PLAIN , TEMPLATE_TUDSCR , TEMPLATE_IEEE , TEMPLATE_SPRINGER_LNCS, TEMPLATE_ACM};
	
	protected final String resourcePrefix = "platform:/plugin/de.tudresden.slr.latexExport/resources/";
	
	protected URL[] filesToCopy = {};
	protected URL templatePath;
	protected String name;
	
	public SlrLatexTemplate() throws MalformedURLException {
		
	}

	public URL[] getFilesToCopy() {
		return filesToCopy;
	}

	public URL getTemplatePath() {
		return templatePath;
	}

	public String getName() {
		return name;
	}
	
	public abstract String fillDocument(String document, SlrProjectMetainformation metainformation, DataProvider dataProvider, Map<Term, String> mainDimensions);

	public String generateSectionTemplate(String title) {
		String sectionPreTitle = "\\section{";
		String sectionPostTitle = "}";

		return sectionPreTitle + title + sectionPostTitle;
	}

	public String generateImageFigure(String path, String caption) {
		return "\\begin{figure}[ht!]\r\n" + "\\centering\r\n" + "\\includegraphics[width = 1\\textwidth]{" + path
				+ "}\r\n" + "\\caption{" + caption + "}\r\n" + "\\end{figure}\r\n";
	}
	
	public String generateDimensionCharts(Map<Term, String> mainDimensions) {
		String toReturn = "";
		for (Map.Entry<Term, String> entry : mainDimensions.entrySet()) {
			toReturn = toReturn + generateImageFigure(entry.getValue(), entry.getKey().getName());
		}
		
		return toReturn;
	}
	
	public String generateStatistics(DataProvider dataProvider) {
		int dimensionsCounWithoutMainDimensions = dataProvider.getAllDimensionsOrdered().size()-dataProvider.getMainDimensions().size();
		String toReturn = "During this systematic literature review, "
				+ dataProvider.getDocuments().size() 
				+ " documents were analyzed. They were mapped to a taxonomy of " 
				+ dataProvider.getMainDimensions().size() 
				+ " main dimensions which themselves are subcategorised in a total of "
				+ dimensionsCounWithoutMainDimensions
				+ " dimensions";
		return toReturn;
	}
	
}
