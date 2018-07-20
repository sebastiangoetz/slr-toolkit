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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.latexexport.data.LatexExportChartGenerator;
import de.tudresden.slr.latexexport.helpers.FileHelper;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.SlrLatexTemplate;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.TemplateACMSigplanConf;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.TemplateIEEEconf;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.TemplatePlainArticle;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.TemplateSpringerLncs;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.TemplateTUDSCR;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;

public class SlrLatexGenerator {
	/**
	 * Metainformation, which is to be used
	 */
	protected SlrProjectMetainformation metainformation;
	/**
	 * DataProvider for infromation about documents, dimensions,..
	 */
	protected DataProvider dataProvider;
	protected String filename;
	/**
	 * Concrete type of document, which is to be generated.
	 */
	protected SlrLatexTemplate concreteDocument;
	Map<Term, String> mainDimensions;

	public SlrLatexGenerator(SlrProjectMetainformation metainformation, DataProvider dataProvider, String filename,
			String templateName) throws MalformedURLException {
		super();
		this.metainformation = metainformation;
		this.dataProvider = dataProvider;
		this.filename = filename;

		SlrLatexTemplate conreteDocumentTemplate = null;

		// check manually, whether the template name matches an existing one
		if (templateName.equals(SlrLatexTemplate.TEMPLATE_PLAIN))
			conreteDocumentTemplate = new TemplatePlainArticle();
		if (templateName.equals(SlrLatexTemplate.TEMPLATE_ACM))
			conreteDocumentTemplate = new TemplateACMSigplanConf();
		if (templateName.equals(SlrLatexTemplate.TEMPLATE_IEEE))
			conreteDocumentTemplate = new TemplateIEEEconf();
		if (templateName.equals(SlrLatexTemplate.TEMPLATE_SPRINGER_LNCS))
			conreteDocumentTemplate = new TemplateSpringerLncs();
		if (templateName.equals(SlrLatexTemplate.TEMPLATE_TUDSCR))
			conreteDocumentTemplate = new TemplateTUDSCR();

		this.concreteDocument = conreteDocumentTemplate;
	}

	/**
	 * Triggers the generation of the charts for the main dimensions.
	 * 
	 * @return Mapping from Term to String which contains for every Term in the main
	 *         dimensions the relative path in the exported LaTex folder
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public Map<Term, String> exportCharts() throws FileNotFoundException, UnsupportedEncodingException {
		return LatexExportChartGenerator.generatePDFOutput(filename, dataProvider);
	}

	/**
	 * Tries to read a certain file, addressed by it's URL, and read it's contents
	 * into a String, which is later on used for the templates.
	 * 
	 * @param url
	 *            URL to the file
	 * @return File content as String
	 */
	public String getResourceContentAsString(URL url) {
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

	/**
	 * Copies resources given by an array of URLs to the LaTex document's folder.
	 * 
	 * @param resources
	 *            Array of URLs, which point to files which are to be
	 *            exported/copied to the LaTex document's folder.
	 * @throws IOException
	 */
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

	/**
	 * Tempalte method encapsuling the LaTex export. Copies the resources to the
	 * export's folder, generates the charts for the main dimensions and triggers
	 * the input of data in the LaTex templates
	 * 
	 * @throws IOException
	 */
	public final void performExport() throws IOException {
		if (this.concreteDocument != null) {
			copyResources(this.concreteDocument.getFilesToCopy());
			mainDimensions = exportCharts();
			String filledDocument = fillDocument(concreteDocument);

			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(filledDocument);
			writer.close();
			
			String informationMessage = 
					"Please be advised - check the generated LaTex template for parts which are still to be filled out. "
					+ "\r\n\r\n"
					+ "Due to the nature of the bar charts, charts were just generated for dimensions which have subdimensions as children.";
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", informationMessage);
		} else {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error",
					"No valid template selected.");
		}

	}

	/*
	 * Fills a given template with information from the metainformation object and
	 * DataProvider.
	 */
	public String fillDocument(SlrLatexTemplate template) {
		String document = getResourceContentAsString(template.getTemplatePath());
		return template.fillDocument(document, metainformation, dataProvider, mainDimensions);
	}
}
