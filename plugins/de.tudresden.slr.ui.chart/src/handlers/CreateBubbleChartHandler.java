package handlers;
import helloworldchart.ChartGenerator;
import helloworldchart.ICommunicationView;

import java.util.Iterator;
import java.util.List;

import logic.BubbleChartDataContainer;
import logic.ChartDataProvider;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.taxonomy.Term;

public class CreateBubbleChartHandler implements IHandler {

	private final String chartViewId = "chart.view.helloworld";

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getCurrentSelectionChecked(event);
		if (!(sel instanceof IStructuredSelection)) {
			return null;
		} else {
			IStructuredSelection currentSelection = (IStructuredSelection) sel;
			if (currentSelection.isEmpty() || currentSelection.size() > 2) {
				return null;
			}
			ChartDataProvider provider = new ChartDataProvider();
			IViewPart part = HandlerUtil.getActiveWorkbenchWindow(event)
					.getActivePage().findView(chartViewId);

			ICommunicationView view = (ICommunicationView) part;
			Iterator<Term> selectionIterator = currentSelection.iterator();
			List<BubbleChartDataContainer> bubbleChartData = provider
					.calculateBubbleChartData(selectionIterator.next(),
							selectionIterator.next());
			Chart bubbleChart = ChartGenerator.createBubble(bubbleChartData);
			view.setChart(bubbleChart);
			return null;
		}
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
