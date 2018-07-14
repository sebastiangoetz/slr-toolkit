package de.tudresden.slr.latexexport.latexgeneration.documentclasses;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

import de.tudresden.slr.metainformation.data.Author;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public class TemplateIEEEconf extends SlrLatexTemplate {

	/**
	 * Constructor for this template. Initializes which auxiliary files are to be copied and where the template is found in the resources.
	 * @throws MalformedURLException
	 */
	public TemplateIEEEconf() throws MalformedURLException {
		URL resource0 = new URL(resourcePrefix + "latexTemplates/ieee/IEEEtran.cls");

		this.filesToCopy = new URL[] { resource0 };
		this.name = "IEEEconf";
		this.templatePath = new URL(resourcePrefix + "latexTemplates/ieee/ieee-template.tex");
	}
	
	@Override
	public String fillDocument(String document, SlrProjectMetainformation metainformation, DataProvider dataProvider,
			Map<Term, String> mainDimensions) {
		Map<String, String> valuesMap = new HashMap<String, String>();
		valuesMap.put("SLR_TITLE", metainformation.getTitle());
		valuesMap.put("SLR_ABSTRACT", metainformation.getProjectAbstract());
		valuesMap.put("SLR_KEYWORDS", metainformation.getKeywords());
		valuesMap.put("SLR_AUTHORS", this.generateAuthorSection(metainformation));
		valuesMap.put("SLR_STATISTICS", this.generateStatistics(dataProvider));
		
		double imageToTextWidthFactor = 0.4;
		valuesMap.put("SLR_DIMENSIONCHARTS", this.generateDimensionCharts(mainDimensions, imageToTextWidthFactor));

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
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
		int authorNumber = 1;
		
		for(Author a : metainformation.getAuthorsList()) {
			authors = authors + "\\IEEEauthorblockN{"+
					authorNumber +
					"\\textsuperscript{st} " +
					a.getName() +
					"}\r\n" + 
					"\\IEEEauthorblockA{\\textit{ "+
					a.getOrganisation() +
					" } \\\\\r\n" + 
					"\\textit{name of organization (of Aff.)}\\\\\r\n" + 
					"City, Country \\\\\r\n" + 
					a.getEmail() +
					"}\r\n";
			
			if(authorNumber != metainformation.getAuthorsList().size()) {
				authors = authors + "\\and\r\n";
			}
					
			
			authorNumber++;
		}
		
		return authors;
	}

}
