package view;

import java.util.SortedMap;
import java.util.TreeMap;

import logic.BarChartGenerator;
import logic.ChartDataProvider;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.EmptyUpdateNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.ibm.icu.util.ULocale;

import de.tudresden.slr.model.taxonomy.Term;

public class ChartView extends ViewPart implements ICommunicationView {

	public ChartView() {

	}

	private IDeviceRenderer idr = null;
	private Canvas paintCanvas;
	private Chart myChart = null;
	private ChartPreview preview;
	private Composite _parent;
	private Term previousTerm = null;
	private final String noDataToDisplay = "There is no Data to display at the moment.\n Try clicking a Term with subclasses.";

	/***
	 * This listener handles the reaction to the selection of an element in the
	 * TaxonomyView
	 */
	ISelectionListener listener = new ISelectionListener() {

		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection sel) {
			if (!(sel instanceof IStructuredSelection))
				return;
			IStructuredSelection ss = (IStructuredSelection) sel;
			Object o = ss.getFirstElement();

			// TODO: there is still some flickering when changing the selection
			if (o instanceof Term && !(o.equals(previousTerm))) {

				SortedMap<String, Integer> myValues = new TreeMap<>();
				Term termToPresent = (Term) o;

				myValues = getNumberOfPapersPerClass(termToPresent);
				if (myValues.size() > 0) {
					myChart = BarChartGenerator.createBar(myValues);
					preview.setDataPresent(true);
				} else {
					preview.setTextToShow(noDataToDisplay);
					preview.setDataPresent(false);
				}
				setAndRenderChart(myChart);
				previousTerm = termToPresent;

			}

		}
	};

	@Override
	public void createPartControl(Composite parent) {
		_parent = parent;
		getSite().getPage().addSelectionListener(listener);
		// noDataToShowText = new Text(_parent, SWT.CENTER);
		// noDataToShowText
		// .setText("There is no Data to display. Try selecting a Term with subclasses.");
		setUpDrawing(parent);

	}

	private SortedMap<String, Integer> getNumberOfPapersPerClass(Term inputTerm) {

		ChartDataProvider chartDataProvider = new ChartDataProvider();
		SortedMap<String, Integer> myValues = chartDataProvider
				.calculateNumberOfPapersPerClass(inputTerm);
		return myValues;
	}

	@Override
	public void setFocus() {

	}

	private void setUpDrawing(Composite parent) {
		// preview canvas

		paintCanvas = new Canvas(parent, SWT.NO_REDRAW_RESIZE);
		paintCanvas.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_BLACK));
		preview = new ChartPreview();
		paintCanvas.addPaintListener(preview);
		paintCanvas.addControlListener(preview);
		preview.setPreview(paintCanvas);
		preview.setDataPresent(false);
		preview.setTextToShow(noDataToDisplay);
	}

	public void generatePDFForCurrentChart(String output) {
		generatePDFOutput(myChart, output);
		// here I call the render method again to show the chart after
		// outputting it
		setAndRenderChart(myChart);
	}

	/***
	 * 
	 * @param chart
	 *            The chart that is to be printed to pdf
	 * @param output
	 *            The system specific output path of the pdf
	 */

	private void generatePDFOutput(Chart chart, String output) {
		// Example output string for Windows
		// "C:\\pdf\\output.pdf"
		PlatformConfig config = new PlatformConfig();
		try {
			// idr = ChartEngine.instance(config).getRenderer("dv.SWT");
			idr = ChartEngine.instance(config).getRenderer("dv.PDF");

			RunTimeContext rtc = new RunTimeContext();
			rtc.setULocale(ULocale.getDefault());

			Generator gr = Generator.instance();
			GeneratedChartState gcs = null;
			Bounds bo = BoundsImpl.create(0, 0, 600, 400);
			gcs = gr.build(idr.getDisplayServer(), chart, bo, null, rtc, null);

			idr.setProperty(IDeviceRenderer.FILE_IDENTIFIER, output);
			idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER,
					new EmptyUpdateNotifier(chart, gcs.getChartModel()));

			gr.render(idr, gcs);

		} catch (ChartException gex) {
			gex.printStackTrace();
		}

	}

	@Override
	public void setAndRenderChart(Chart parameter) {

		this.myChart = parameter;
		preview.renderModel(parameter);
		// renderChart(_parent, myChart);

	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(listener);
	}

	@Override
	public void redraw() {
		_parent.redraw();

	}

	@Override
	public ChartPreview getPreview() {
		return preview;
	}
}
