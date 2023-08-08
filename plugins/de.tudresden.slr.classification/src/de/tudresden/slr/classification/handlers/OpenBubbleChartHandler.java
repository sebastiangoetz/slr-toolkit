package de.tudresden.slr.classification.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.classification.Activator;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.BubbleDataContainer;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;

public class OpenBubbleChartHandler implements IHandler {

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

		// get selection
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		List<BubbleDataContainer> data = processSelectionData((IStructuredSelection) selection);

		boolean dataWrittenSuccessfully = false;
		try {
			// overwrite csv with new data
			dataWrittenSuccessfully = overwriteCSVFile(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// open website in default browser
		if (dataWrittenSuccessfully) {
			Program.launch(Activator.getUrl() + "bubble.index.html");
		}

		return null;
	}

	private boolean overwriteCSVFile(List<BubbleDataContainer> selectionData) throws IOException {
		if (null == selectionData) {
			return false;
		}

	    File writeToFile = new File(Activator.getWebAppWorkspace() + "/bubble.data.csv");
	    PrintWriter writer = new PrintWriter(new FileWriter(writeToFile));
	    writer.println("category1,category2,amount");
	    for (BubbleDataContainer entry : selectionData) {
	    	writer.println(entry.getxTerm().getName() + "," + entry.getyTerm().getName() + "," + Integer.toString(entry.getBubbleSize()));
	    }
	    writer.close();
		return true;
	}

	private List<BubbleDataContainer> processSelectionData(IStructuredSelection selection) {		
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() == 2) {
			ChartDataProvider provider = new ChartDataProvider();
			@SuppressWarnings("unchecked")
			Iterator<Term> selectionIterator = currentSelection.iterator();
			Term first = selectionIterator.next();
			Term second = selectionIterator.next();
			List<BubbleDataContainer> bubbleChartData = provider.calculateBubbleChartData(first, second);
			return bubbleChartData;
		} else {
			String message = "Please choose exactly two taxonomies from the taxonomy list.\n\r"
					+ "Do this by choosing one taxonomy and then hold CTRL and pick the second taxonomy.";
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Taxonomy Values Incorrect", message);
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
	}
}
