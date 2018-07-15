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

public class TemplateSpringerLncs extends SlrLatexTemplate{

	/**
	 * Constructor for this template. Initializes which auxiliary files are to be copied and where the template is found in the resources.
	 * @throws MalformedURLException
	 */
	public TemplateSpringerLncs() throws MalformedURLException {
		URL resource0 = new URL(resourcePrefix + "latexTemplates/springerlncs/llncs.cls");
		URL resource1 = new URL(resourcePrefix + "latexTemplates/springerlncs/splncs04.bst");


		this.filesToCopy = new URL[] { resource0 , resource1 };
		this.name = "Springer LNCS";
		this.templatePath = new URL(resourcePrefix + "latexTemplates/springerlncs/lncs-template.tex");
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
		
		double imageToTextWidthFactor = 0.8;
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
		String authors = "\\author{";

		int authorNumber = 1;
		
		for(Author a : metainformation.getAuthorsList()) {
			authors = authors + 
					a.getName() +
					"\\inst{"+
					authorNumber +
					"}\\orcidID{INSERT OrcidID}\r\n";
			
			authorNumber++;
			
		}	
		authors = authors + "} \r\n%\r\n" + 
				"\\authorrunning{F. Author et al.}\r\n" + 
				"% First names are abbreviated in the running head.\r\n" + 
				"% If there are more than two authors, 'et al.' is used.\r\n" + 
				"%\r\n";
		
		authorNumber = 1;
		
		authors = authors +"\\institute{";
		for(int i = 0; i<metainformation.getAuthorsList().size();i++) {
			Author a = metainformation.getAuthorsList().get(i);
			String organisation = a.getOrganisation();
			String mail = a.getEmail();
			if(organisation.isEmpty()) organisation = "no organisation specified";
			if(mail.isEmpty()) mail = "no mail specified";

			 authors = authors +
					 organisation + 
					 "\r\n" +
					 "\\email{" +
					 mail +
					 "} \\\\" + 
					 "\\url{http://www.inserturlhere.com}\r\n";
			 
				if(i+1 != metainformation.getAuthorsList().size()) {
					authors = authors + "\\and\r\n";
				}
		}
		
		return authors+"}";
	}
}
