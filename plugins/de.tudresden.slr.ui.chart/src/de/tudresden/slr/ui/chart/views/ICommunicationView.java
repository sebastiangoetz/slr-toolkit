package de.tudresden.slr.ui.chart.views;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.ui.IViewPart;

public interface ICommunicationView extends IViewPart {
	public void setAndRenderChart(Chart parameter);

	public void redraw();

	public void generatePDFForCurrentChart(String output);

	public ChartPreview getPreview();
}