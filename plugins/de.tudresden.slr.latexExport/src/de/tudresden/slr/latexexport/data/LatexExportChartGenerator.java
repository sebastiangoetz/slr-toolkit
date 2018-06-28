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

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.BarChartGenerator;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;

public class LatexExportChartGenerator {
	
	
	public static Map<Term, String> generatePDFOutput(String folder, EList<Term> dimensions) {
		ChartDataProvider chartData = new ChartDataProvider();
		Map<Term, String> toReturn = new HashMap<Term, String>();
		PlatformConfig config = new PlatformConfig();
		folder = extractFolderFromFilepath(folder);
		
		String folderCopy = folder;
		String filepath;
		
		for(Term term : dimensions) {
			filepath = folderCopy;
			try {
				
				filepath = filepath+File.separator+term.getName()+".svg";
				SortedMap<String, Integer> myValues = chartData.calculateNumberOfPapersPerClass(term);
				//TODO delete
				//SortedMap<String, Integer> myValues = new TreeMap<String, Integer>();
				//myValues.put("asd", 1);
				
				Chart myChart = new BarChartGenerator().createBar(myValues);
				
				IDeviceRenderer idr = null;
				idr = ChartEngine.instance(config).getRenderer("dv.SVG");
				RunTimeContext rtc = new RunTimeContext();
				rtc.setULocale(ULocale.getDefault());

				Generator gr = Generator.instance();
				GeneratedChartState gcs = null;
				Bounds bo = BoundsImpl.create(0, 0, 600, 400);
				gcs = gr.build(idr.getDisplayServer(), myChart, bo, null, rtc, null);

				idr.setProperty(IDeviceRenderer.FILE_IDENTIFIER, filepath);
				//idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, new EmptyUpdateNotifier(chart, gcs.getChartModel()));

				gr.render(idr, gcs);
				
				toReturn.put(term, filepath);
			} catch (ChartException gex) {
				gex.printStackTrace();
			}
		}
		return toReturn;
	}
	
	public static String extractFolderFromFilepath(String fileName) {
		return fileName = fileName.substring(0, fileName.lastIndexOf(File.separator));
	}
	
	public static String extractRelativePathFromFilepath(String filename) {
		return filename.substring(filename.lastIndexOf((File.separator)));
	}
}
