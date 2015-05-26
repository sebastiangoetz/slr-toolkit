package helloworldchart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.ui.IViewPart;

public interface ICommunicationView extends IViewPart{
	   public void setChart(Chart parameter);
	}