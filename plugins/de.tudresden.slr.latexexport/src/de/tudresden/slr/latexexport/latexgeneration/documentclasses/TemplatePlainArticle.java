package de.tudresden.slr.latexexport.latexgeneration.documentclasses;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.tudresden.slr.metainformation.data.Author;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public class TemplatePlainArticle extends SlrLatexTemplate {

	/**
	 * Constructor for this template. Initializes which auxiliary files are to be copied and where the template is found in the resources.
	 * @throws MalformedURLException
	 */
	public TemplatePlainArticle() throws MalformedURLException {
		this.name = "Plain Article";
		this.templatePath = new URL(resourcePrefix + "latexTemplates/plain/plainArticle.tex");
	}

	@Override
	public String fillDocument(String document, SlrProjectMetainformation metainformation, DataProvider dataProvider,
			Map<Term, String> mainDimensions) {
		Map<String, String> valuesMap = new HashMap<String, String>();
		valuesMap.put(SLRVARIABLE_TITLE, metainformation.getTitle());
		valuesMap.put(SLRVARIABLE_ABSTRACT, metainformation.getProjectAbstract());
		valuesMap.put(SLRVARIABLE_KEYWORDS, metainformation.getKeywords());
		valuesMap.put(SLRVARIABLE_AUTHORS, this.generateAuthorSection(metainformation));
		valuesMap.put(SLRVARIABLE_TAXONOMYDESCRIPTION, metainformation.getTaxonomyDescription());
		valuesMap.put(SLRVARIABLE_STATISTICS, this.generateStatistics(dataProvider));
		
		double imageToTextWidthFactor = 1;
		valuesMap.put(SLRVARIABLE_DIMENSIONCHARTS, this.generateDimensionCharts(mainDimensions, imageToTextWidthFactor));

		StringSubstituter sub = new StringSubstituter(valuesMap);
		String resolvedString = sub.replace(document);

		return resolvedString;
	}

	@Override
	public String generateAuthorSection(SlrProjectMetainformation metainformation) {
		String authors = "";
		for(Author a : metainformation.getAuthorsList()) {
			authors = authors + a.toString() + "\r\n\\\\";
		}
		
		return authors;
	}

}
