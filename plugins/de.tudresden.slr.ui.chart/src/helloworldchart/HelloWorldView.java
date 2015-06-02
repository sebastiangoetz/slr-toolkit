package helloworldchart;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class HelloWorldView extends ViewPart implements ICommunicationView {

	public HelloWorldView() {
	}

	private IDeviceRenderer idr = null;
	private Chart myChart = null;
	private Composite _parent;

	@Override
	public void createPartControl(Composite parent) {
		_parent = parent;

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

	@Override
	public void setChart(Chart parameter) {

		this.myChart = parameter;
		renderChart(_parent, myChart);

	}
}
