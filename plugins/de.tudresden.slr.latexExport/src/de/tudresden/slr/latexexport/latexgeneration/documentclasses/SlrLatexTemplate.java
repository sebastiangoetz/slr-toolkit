package de.tudresden.slr.latexexport.latexgeneration.documentclasses;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public abstract class SlrLatexTemplate {
	//Strings representing the template's names. Used in the wizard's dialog.
	public static final String TEMPLATE_PLAIN = "Plain Article";
	public static final String TEMPLATE_TUDSCR = "tudscr";
	public static final String TEMPLATE_IEEE = "IEEEconf";
	public static final String TEMPLATE_ACM = "ACM SIGPLAN conf";
	public static final String TEMPLATE_SPRINGER_LNCS = "Springer LNCS";
	
	public static final String[] documentTypes = { TEMPLATE_PLAIN , TEMPLATE_TUDSCR , TEMPLATE_IEEE , TEMPLATE_SPRINGER_LNCS, TEMPLATE_ACM};
	
	//variables in LaTex templates, use this strings for string replacement
	protected static final String SLRVARIABLE_TITLE = "SLR_TITLE";
	protected static final String SLRVARIABLE_ABSTRACT = "SLR_ABSTRACT";
	protected static final String SLRVARIABLE_KEYWORDS = "SLR_KEYWORDS";
	protected static final String SLRVARIABLE_AUTHORS = "SLR_AUTHORS";
	protected static final String SLRVARIABLE_STATISTICS = "SLR_STATISTICS";
	protected static final String SLRVARIABLE_TAXONOMYDESCRIPTION = "SLR_TAXONOMYDESCRIPTION";
	protected static final String SLRVARIABLE_DIMENSIONCHARTS = "SLR_DIMENSIONCHARTS";
	
	protected final String resourcePrefix = "platform:/plugin/de.tudresden.slr.latexExport/resources/";
	
	/**
	 * .cls and auxiliary files of the LaTex template. Specify in the constructors of the subclasses!
	 */
	protected URL[] filesToCopy = {};
	/**
	 * LaTex template. Specify in the constructors of the subclasses!
	 */
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
	
	/**
	 * Fills LaTex template with information.
	 * @param document LaTex template, represented as String
	 * @param metainformation Metainformation object from project
	 * @param dataProvider DataProvider from project
	 * @param mainDimensions Mapping from Term/it's chart to relative path
	 * @return LaTex code for the filled out document
	 */
	public abstract String fillDocument(String document, SlrProjectMetainformation metainformation, DataProvider dataProvider, Map<Term, String> mainDimensions);

	/**
	 * Generates a block for a section in a LaTex-Template.
	 * @param title Title of the secion
	 * @return LaTex-Code for a section
	 */
	public String generateSectionTemplate(String title) {
		String sectionPreTitle = "\\section{";
		String sectionPostTitle = "}";

		return sectionPreTitle + title + sectionPostTitle;
	}

	/**
	 * Generates a figure (consisting of an image and a caption) for a image in LaTex.
	 * @param path Path to the picture. By default, images are exported to /images in the LaTex document's folder
	 * @param caption Caption for the picture
	 * @param imageToTextWidthFactor Scaling from picture to textwidth
	 * @return LaTex-Code for a figure
	 */
	public String generateImageFigure(String path, String caption, double imageToTextWidthFactor) {
		return "\\begin{figure}[!htb]\r\n" + "\\centering\r\n" + "\\includegraphics[width = "+imageToTextWidthFactor+"\\textwidth]{" + path
				+ "}\r\n" + "\\caption{" + caption + "}\r\n" + "\\end{figure}\r\n";
	}
	
	/**
	 * Generates LaTex figures for the main dimensions.
	 * @param mainDimensionsMap Mapping from Term/it's Chart to the relative path
	 * @param imageToTextWidthFactor Scaling from picture to textwidth
	 * @return LaTex code for the figures of the main dimensions and captions.
	 */
	public String generateDimensionCharts(Map<Term, String> mainDimensionsMap, double imageToTextWidthFactor) {
		String toReturn = "";
		
		//ensure that order of main dimensions is the same as in the taxonomy
		DataProvider dataProvider = new DataProvider();
		for(int i = 0; i<dataProvider.getMainDimensions().size(); i++) {
			Term currentTerm = dataProvider.getMainDimensions().get(i);
			String path = mainDimensionsMap.get(currentTerm);
			if(path != null) {
				toReturn = toReturn + generateImageFigure(path, currentTerm.getName(),imageToTextWidthFactor);
			}
		}
		
//		for (Map.Entry<Term, String> entry : mainDimensionsMap.entrySet()) {
//			toReturn = toReturn + generateImageFigure(entry.getValue(), entry.getKey().getName(), imageToTextWidthFactor);
//		}
		
		return toReturn;
	}
	
	/**
	 * Generates statistics analyzing the documents and the taxonomy which are used in the project.
	 * @param dataProvider
	 * @return LaTex code for statistics section
	 */
	public String generateStatistics(DataProvider dataProvider) {
		int dimensionsCounWithoutMainDimensions = dataProvider.getAllDimensionsOrdered().size()-dataProvider.getMainDimensions().size();
		String toReturn = "During this systematic literature review, "
				+ dataProvider.getDocuments().size() 
				+ " documents were analyzed. They were mapped to a taxonomy of " 
				+ dataProvider.getMainDimensions().size() 
				+ " main dimensions which themselves are subcategorised in a total of "
				+ dimensionsCounWithoutMainDimensions
				+ " dimensions. \\\\";
		return toReturn;
	}
	
	/**
	 * Generates an author section for a LaTex Documents. Due to the different syntax styles regarding
	 * the author section of different LaTex templates, this has to be implemented in every subclass.
	 * IMPORTANT: the generated String will be placed <b>INSIDE</b> the \authors{ ... } tag in the filled
	 * template.
	 * @param metainformation Metainformation object containing the list of authors
	 * @return String which is to be put in authors tag in template
	 */
	public abstract String generateAuthorSection(SlrProjectMetainformation metainformation);
	
}
