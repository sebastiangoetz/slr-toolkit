package de.tudresden.slr.ui.chart.handlers;

import java.util.Iterator;
import java.util.List;

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
import de.tudresden.slr.ui.chart.logic.BubbleDataContainer;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;
import de.tudresden.slr.ui.chart.logic.ChartGenerator;
import de.tudresden.slr.ui.chart.settings.BubbleChartConfiguration;
import de.tudresden.slr.ui.chart.views.ICommunicationView;

public class CreateBubbleChartHandler implements IHandler {

	private final String chartViewId = "chart.view.chartview";
	private ICommunicationView view;
	private String noDataToDisplay = "Could not create a bubble chart. \n Try to select two Terms with subclasses.";

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
		if (currentSelection.size() == 2) {
			view.getPreview().setDataPresent(true);
			ChartDataProvider provider = new ChartDataProvider();
			@SuppressWarnings("unchecked")
			Iterator<Term> selectionIterator = currentSelection.iterator();
			Term first = selectionIterator.next();
			Term second = selectionIterator.next();
			List<BubbleDataContainer> bubbleChartData = provider.calculateBubbleChartData(first, second);
			BubbleChartConfiguration.get().getGeneralSettings().setChartTitle("Intersection of " + first.getName() + " and " + second.getName());
			Chart bubbleChart = ChartGenerator.createBubble(bubbleChartData,first,second);
			view.setAndRenderChart(bubbleChart);
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

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
