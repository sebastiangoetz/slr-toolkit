package helloworldchart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;

public interface ICommunicationView extends IViewPart {
	public void setAndRenderChart(Chart parameter);

	public Text getNoDataToShowText();

	public void redraw();
}