package helloworldchart;

import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.emf.common.util.EList;
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
	// private final String chartViewId = "chart.view.helloworld";
	// private IViewPart chartView = null;

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
			// TODO: Diagram is redrawn for new selection, but only if I switch
			// focus to another
			// view back and forth
			if (o instanceof Term && !(o.equals(previousTerm))) {
				// IWorkbenchPage page = PlatformUI.getWorkbench()
				// .getActiveWorkbenchWindow().getActivePage();
				// chartView = page.findView(chartViewId);
				// try {
				// page.showView(chartViewId);
				// } catch (PartInitException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// page.hideView(chartView);

				Term termToPresent = (Term) o;
				EList<Term> subclassList = termToPresent.getSubclasses();
				SortedMap<String, Integer> myValues = new TreeMap<>();
				myValues.put(subclassList.get(0).getName(), 10);
				myValues.put(subclassList.get(1).getName(), 20);
				myChart = BarChartGenerator.createBar(myValues);
				setChart(myChart);
				_parent.redraw();
				_parent.update();
				previousTerm = termToPresent;
			}
		}
	};

	@Override
	public void createPartControl(Composite parent) {
		_parent = parent;
		getSite().getPage().addSelectionListener(listener);

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
				Bounds bo = BoundsImpl.create(re.x, re.y, re.width, re.height);
				// BOUNDS MUST BE SPECIFIED IN POINTS
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
