package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;
import de.tudresden.slr.ui.chart.logic.ChartGenerator;
import de.tudresden.slr.ui.chart.logic.TermSort;
import de.tudresden.slr.ui.chart.settings.PieChartConfiguration;
import de.tudresden.slr.ui.chart.views.ICommunicationView;

public class CreatePieChartHandler implements IHandler {

	
	private final String chartViewId = "chart.view.chartview";
	private ICommunicationView view;
	private String noDataToDisplay = "Could not create a pie chart. \n Try to select a Term with subclasses.";
	
	public CreatePieChartHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO: seems to be there is some refactoring needed in here
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		IViewPart part = null;
		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					part = page.showView(chartViewId);
				}
			}
		} catch (PartInitException e) {
			e.printStackTrace();
			return null;
		}
		if (part instanceof ICommunicationView) {
			view = (ICommunicationView) part;
		} else {
			return null;
		}

		view.getPreview().setTextToShow(noDataToDisplay);
		if (currentSelection.size() == 1) {
			view.getPreview().setDataPresent(true);
			ChartDataProvider provider = new ChartDataProvider();
			Term input = (Term) currentSelection.getFirstElement();
			Map<String, Integer> citeChartData = provider.calculateNumberOfPapersPerClass(input);
			PieChartConfiguration.get().getGeneralSettings().setChartTitle("Number of cites per subclass of " + input.getName());
			PieChartConfiguration.get().setPieTermSort(TermSort.SUBCLASS);
			Chart citeChart = ChartGenerator.createPie(citeChartData);
			view.setAndRenderChart(citeChart);
		} else {
			view.setAndRenderChart(null);
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
		
	}

}
