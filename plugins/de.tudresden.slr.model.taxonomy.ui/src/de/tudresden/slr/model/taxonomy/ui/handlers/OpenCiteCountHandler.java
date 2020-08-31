package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;

public class OpenCiteCountHandler extends OpenCiteHandler {

	public OpenCiteCountHandler() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected Map<String, Integer> processSelectionData(IStructuredSelection selection) {
		IStructuredSelection currentSelection = (IStructuredSelection) selection;

		ChartDataProvider provider = new ChartDataProvider();
		Term input = (Term) currentSelection.getFirstElement();
		Map<String, Integer> citeChartData = provider.calculateCitesPerYear();
		return citeChartData;
	}

}
