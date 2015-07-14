package handlers;

import java.util.Iterator;
import java.util.List;

import logic.BubbleDataContainer;
import logic.ChartDataProvider;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import view.ChartGenerator;
import view.ICommunicationView;
import de.tudresden.slr.model.taxonomy.Term;

public class CreateBubbleChartHandler implements IHandler {

	private final String chartViewId = "chart.view.chartview";

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
			IViewPart part = null;
			try {
				part = HandlerUtil.getActiveWorkbenchWindow(event)
						.getActivePage().showView(chartViewId);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ICommunicationView view = (ICommunicationView) part;
			Iterator<Term> selectionIterator = currentSelection.iterator();
			List<BubbleDataContainer> bubbleChartData = provider
					.calculateBubbleChartData(selectionIterator.next(),
							selectionIterator.next());
			Chart bubbleChart = ChartGenerator.createBubble(bubbleChartData);
			view.setAndRenderChart(bubbleChart);
			view.getNoDataToShowText().setVisible(false);
			view.redraw();
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
