package helloworldchart;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class HelloWorldView extends ViewPart{

	public HelloWorldView() {
	}
	    private IDeviceRenderer idr = null;
	    private Chart myChart = null;
	    
	@Override
	public void createPartControl(Composite parent) {
		
		// INITIALIZE THE SWT RENDERING DEVICE
                
		PlatformConfig config = new PlatformConfig( );
				
		myChart = Bubble.createBubble();
				//createMyChart(); 
	    try {
	            idr = ChartEngine.instance( config ).getRenderer("dv.SWT");
	        } catch (Exception e) {
	            e.printStackTrace();
	            //DefaultLoggerImpl.instance().log(e);
	        }
	  
	   parent.addPaintListener(new PaintListener() {
		
		@Override
		public void paintControl(PaintEvent pe) {
			idr.setProperty(
					IDeviceRenderer.GRAPHICS_CONTEXT, pe.gc);
					Composite co = (Composite) pe.getSource();
					Rectangle re = co.getClientArea();
					Bounds bo = BoundsImpl.create(re.x, re.y,
					re.width, re.height);
					bo.scale( 72d /
					idr.getDisplayServer().getDpiResolution()
					); // BOUNDS MUST BE SPECIFIED IN POINTS
					// BUILD AND RENDER THE CHART
			 
					Generator gr = Generator.instance();
					try {
						gr.render(idr, gr.build(idr.getDisplayServer(), myChart, bo, null));
					} catch (ChartException gex)
					{
						gex.printStackTrace();
					}
			
		}
	});



	}

	@Override
	public void setFocus() {


	}

	public static Chart createMyChart() {
		ChartWithAxes cwaEmpty = ChartWithAxesImpl.create();
		 
		cwaEmpty.getTitle().getLabel().getCaption().setValue("Hello Chart");
	 
	    return cwaEmpty;
	}

}
