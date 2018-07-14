package de.tudresden.slr.latexexport.latexgeneration.documentclasses;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MetaEventListener;

import org.apache.commons.lang.text.StrSubstitutor;

import de.tudresden.slr.metainformation.data.Author;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public class TemplateACMSigplanConf extends SlrLatexTemplate {

	public TemplateACMSigplanConf() throws MalformedURLException {
		URL testCopy = new URL(resourcePrefix + "latexTemplates/acmsigplan/sigplanconf.cls");

		this.filesToCopy = new URL[] { testCopy };
		this.name = "ACM SIGPLAN conf";
		this.templatePath = new URL(resourcePrefix + "latexTemplates/acmsigplan/sigplanconf-template.tex");
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
		valuesMap.put("SLR_DIMENSIONCHARTS", this.generateDimensionCharts(mainDimensions));

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(document);

		return resolvedString;
	}
	
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