package de.tudresden.slr.latexexport.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.util.ULocale;

import de.tudresden.slr.latexexport.helpers.FileHelper;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.BarChartGenerator;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;

public class LatexExportChartGenerator {

	private static final String FILEEXTENSION = ".JPG";

	/**
	 * Generates barcharts (format: see member variable FILEEXTENSION) of the main dimensions of an slr project
	 * 
	 * @param filepath
	 *            Path of the LaTex-document which was generated
	 * @param dataProvider
	 *            DataProvider object which contains the dimensions
	 * @return Mapping from Term to String which maps a Term to a relative path to
	 *         an image which is to be used in a LaTex document
	 */
	public static Map<Term, String> generatePDFOutput(String filepath, DataProvider dataProvider) {
		EList<Term> dimensions = dataProvider.getMainDimensions();
		ChartDataProvider chartData = new ChartDataProvider();
		Map<Term, String> toReturn = new HashMap<Term, String>();
		PlatformConfig config = new PlatformConfig();
		String folder = FileHelper.extractFolderFromFilepath(filepath);

		String filepathNewChart;
		String imagesFolderName = "images";
		new File(folder + File.separator + imagesFolderName).mkdir();

		for (Term term : dimensions) {
			filepathNewChart = folder;

			if (term.getSubclasses().size() != 0)
			{
				try {
					String newFileName = term.getName().replaceAll("\\s+", "") + FILEEXTENSION;
					filepathNewChart = filepathNewChart + File.separator + imagesFolderName + File.separator
							+ newFileName;
					SortedMap<String, Integer> myValues = chartData.calculateNumberOfPapersPerClass(term);

					Chart myChart = new BarChartGenerator().createBar(myValues);

					IDeviceRenderer idr = null;
					idr = ChartEngine.instance(config).getRenderer("dv.JPG");
					RunTimeContext rtc = new RunTimeContext();
					rtc.setULocale(ULocale.getDefault());

					Generator gr = Generator.instance();
					GeneratedChartState gcs = null;
					Bounds bo = BoundsImpl.create(0, 0, 600, 400);
					gcs = gr.build(idr.getDisplayServer(), myChart, bo, null, rtc, null);

					idr.setProperty(IDeviceRenderer.FILE_IDENTIFIER, filepathNewChart);
					// idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, new
					// EmptyUpdateNotifier(chart, gcs.getChartModel()));

					gr.render(idr, gcs);

					// Separator "/" for LaTex syntag
					toReturn.put(term, imagesFolderName + "/" + newFileName);
				} catch (ChartException gex) {
					gex.printStackTrace();
				}
			}
		}
		return toReturn;
	}

}
