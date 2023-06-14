package de.tudresden.slr.latexexport.latexgeneration.documentclasses;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.tudresden.slr.metainformation.data.Author;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public class TemplateACMSigplanConf extends SlrLatexTemplate {

	/**
	 * Constructor for this template. Initializes which auxiliary files are to be copied and where the template is found in the resources.
	 * @throws MalformedURLException
	 */
	public TemplateACMSigplanConf() throws MalformedURLException {
		URL resource0 = new URL(resourcePrefix + "latexTemplates/acmsigplan/sigplanconf.cls");

		this.filesToCopy = new URL[] { resource0 };
		this.name = "ACM SIGPLAN conf";
		this.templatePath = new URL(resourcePrefix + "latexTemplates/acmsigplan/sigplanconf-template.tex");
	}

	@Override
	public String fillDocument(String document, SlrProjectMetainformation metainformation, DataProvider dataProvider,
			Map<Term, String> mainDimensions) {
		Map<String, String> valuesMap = new HashMap<String, String>();
		valuesMap.put(SLRVARIABLE_TITLE, metainformation.getTitle());
		valuesMap.put(SLRVARIABLE_ABSTRACT, metainformation.getProjectAbstract());
		valuesMap.put(SLRVARIABLE_KEYWORDS, metainformation.getKeywords());
		valuesMap.put(SLRVARIABLE_AUTHORS, this.generateAuthorSection(metainformation));
		valuesMap.put(SLRVARIABLE_STATISTICS, this.generateStatistics(dataProvider));
		valuesMap.put(SLRVARIABLE_TAXONOMYDESCRIPTION, metainformation.getTaxonomyDescription());
		
		double imageToTextWidthFactor = 0.4;
		valuesMap.put(SLRVARIABLE_DIMENSIONCHARTS, this.generateDimensionCharts(mainDimensions, imageToTextWidthFactor));

		StringSubstituter sub = new StringSubstituter(valuesMap);
		String resolvedString = sub.replace(document);

		return resolvedString;
	}
	
	/**
	 * Generates the author section
	 * @param metainformation Metainformation project
	 * @return LaTex code for the author section
	 */
	public String generateAuthorSection(SlrProjectMetainformation metainformation) {
		String authors = "";
		for(Author a : metainformation.getAuthorsList()) {
			authors = authors + "\\authorinfo{"+ a.getName() +"\\thanks{with optional author note}}\r\n" + 
					"           {"+ a.getOrganisation() +"}\r\n" + 
					"           {"+ a.getEmail() +"}\r\n";
		}
		
		return authors;
	}

}
