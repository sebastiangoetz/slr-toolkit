package de.tudresden.slr.latexexport.latexgeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.latexexport.data.LatexExportChartGenerator;
import de.tudresden.slr.latexexport.helpers.FileHelper;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.SlrLatexTemplate;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.TemplateACMSigplanConf;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.TemplatePlainArticle;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public class SlrLatexGenerator {
	protected SlrProjectMetainformation metainformation;
	protected DataProvider dataProvider;
	protected String filename;
	protected SlrLatexTemplate concreteDocument;
	Map<Term, String> mainDimensions;

	public SlrLatexGenerator(SlrProjectMetainformation metainformation, DataProvider dataProvider, String filename,
			String templateName) throws MalformedURLException {
		super();
		this.metainformation = metainformation;
		this.dataProvider = dataProvider;
		this.filename = filename;

		SlrLatexTemplate conreteDocumentTemplate = null;

		if (templateName.equals(SlrLatexTemplate.TEMPLATE_PLAIN)) 
			conreteDocumentTemplate = new TemplatePlainArticle();
		if (templateName.equals(SlrLatexTemplate.TEMPLATE_ACM))
			conreteDocumentTemplate = new TemplateACMSigplanConf();

		this.concreteDocument = conreteDocumentTemplate;
	}

	public Map<Term, String> exportCharts() throws FileNotFoundException, UnsupportedEncodingException {
		return LatexExportChartGenerator.generatePDFOutput(filename, dataProvider);
	}

	public String getResourceContentAsString(URL url) {
		// URL fileURL =
		// bundle.getEntry("resources/latexTemplates/plain/plainArticle.tex");
		String toReturn = "";
		try {
			InputStream inputStream = url.openConnection().getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				toReturn = toReturn + "\r\n" + inputLine;
			}

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return toReturn;
	}

	public void copyResources(URL[] resources) throws IOException {
		String folder = FileHelper.extractFolderFromFilepath(filename);
		for (URL url : resources) {
			InputStream inputStream = url.openConnection().getInputStream();

			String filenameToCopy = FileHelper.extractFileNameFromURL(url);
			File dest = new File(folder + File.separator + filenameToCopy);
			Files.copy(inputStream, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

			inputStream.close();
		}
	}

	public final void performExport() throws IOException {
		if (this.concreteDocument != null) {
			copyResources(this.concreteDocument.getFilesToCopy());
			mainDimensions = exportCharts();
			String filledDocument = fillDocument(concreteDocument);

			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(filledDocument);
			writer.close();
		} else {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error",
					"No valid template selected.");
		}

	}

	public String fillDocument(SlrLatexTemplate template) {
		String document = getResourceContentAsString(template.getTemplatePath());
		return template.fillDocument(document, metainformation, dataProvider, mainDimensions);
	}
}
