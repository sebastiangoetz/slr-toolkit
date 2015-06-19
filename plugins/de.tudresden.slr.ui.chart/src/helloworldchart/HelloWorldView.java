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
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.tudresden.slr.model.taxonomy.Term;

public class HelloWorldView extends ViewPart implements ICommunicationView {

	public HelloWorldView() {
	}

	private IDeviceRenderer idr = null;
	private Chart myChart = null;
	private Composite _parent;
	private final String chartViewId = "chart.view.helloworld";

	/***
	 * This listener handles the reaction to the selection of an element in the @BibtexEntryView
	 */
	ISelectionListener listener = new ISelectionListener() {

		public void selectionChanged(IWorkbenchPart part, ISelection sel) {
			if (!(sel instanceof IStructuredSelection))
				return;
			IStructuredSelection ss = (IStructuredSelection) sel;
			Object o = ss.getFirstElement();
			// TODO: Why can't I check here for Term interface?
			// It just doesn't get triggered that way. :o
			// TODO: I have to reset the diagram somehow
			if (o instanceof BasicEObjectImpl) {
				// _parent.dispose();

				Term termToPresent = (Term) o;
				EList<Term> subclassList = termToPresent.getSubclasses();
				SortedMap<String, Integer> myValues = new TreeMap<>();
				myValues.put(subclassList.get(0).getName(), 10);
				myValues.put(subclassList.get(1).getName(), 20);
				myChart = BarChartGenerator.createBar(myValues);
				setChart(myChart);
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().showView(chartViewId);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}
	};

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

		parent.addPaintListener(new PaintListener() {

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

	public void setChart(Chart parameter) {

		this.myChart = parameter;
		renderChart(_parent, myChart);

	}

	public void dispose() {
		getSite().getPage().removeSelectionListener(listener);
	}
}
