package helloworldchart;

import java.util.SortedMap;
import java.util.TreeMap;

import logic.ChartDataProvider;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import de.tudresden.slr.model.taxonomy.Term;

public class HelloWorldView extends ViewPart implements ICommunicationView {

	public HelloWorldView() {
	}

	private IDeviceRenderer idr = null;
	private Chart myChart = null;
	private Composite _parent;
	private PaintListener p = null;
	private Term previousTerm = null;

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
				// EList<Term> subclassList;
				// for (Term t : termToPresent.getSubclasses()) {
				// myValues.put(t.getName(), t.getSubclasses().size() == 0 ? 0
				// : t.getSubclasses().size());
				// }
				myValues = getNumberOfPapersPerClass(termToPresent);
				// for(int i : myValues.values()){
				// if()
				// }
				myChart = BarChartGenerator.createBar(myValues);
				_parent.setRedraw(false);
				setChart(myChart);
				_parent.setRedraw(true);
				previousTerm = termToPresent;

			}
		}
	};

	@Override
	public void createPartControl(Composite parent) {
		_parent = parent;
		getSite().getPage().addSelectionListener(listener);

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

	private void renderChart(Composite parent, Chart chart) {
		// INITIALIZE THE SWT RENDERING DEVICE

		PlatformConfig config = new PlatformConfig();
		try {
			idr = ChartEngine.instance(config).getRenderer("dv.SWT");
		} catch (Exception e) {
			e.printStackTrace();
		}

		parent.addPaintListener(p = new PaintListener() {

			@Override
			public void paintControl(PaintEvent pe) {
				idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, pe.gc);
				Composite co = (Composite) pe.getSource();
				Rectangle re = co.getClientArea();
				// BOUNDS MUST BE SPECIFIED IN POINTS
				Bounds bo = BoundsImpl.create(re.x, re.y, re.width, re.height);
				// BUILD AND RENDER THE CHART
				bo.scale(72d / idr.getDisplayServer().getDpiResolution());

				Generator gr = Generator.instance();
				try {
					gr.render(idr,
							gr.build(idr.getDisplayServer(), myChart, bo, null));
				} catch (ChartException gex) {
					gex.printStackTrace();
				}

			}
		});
	}

	@Override
	public void setChart(Chart parameter) {

		this.myChart = parameter;
		renderChart(_parent, myChart);

	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(listener);
		if (p != null)
			_parent.removePaintListener(p);
	}
}
