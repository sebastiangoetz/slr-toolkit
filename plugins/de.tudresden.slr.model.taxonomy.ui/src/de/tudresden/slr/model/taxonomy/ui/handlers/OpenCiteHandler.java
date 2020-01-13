package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

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

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.ui.views.Activator;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;

public class OpenCiteHandler implements IHandler {

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
		Map<String, Integer> data = processSelectionData((IStructuredSelection) selection);

		boolean dataWrittenSuccessfully = false;
		try {
			// overwrite csv with new data
			dataWrittenSuccessfully = overwriteCSVFile(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// call website on writen file
		if (dataWrittenSuccessfully) {
			Program.launch(Activator.getUrl() + "bar.index.html");
		}

		return null;
	}

	private boolean overwriteCSVFile(Map<String, Integer> selectionData) throws IOException {
		if (null == selectionData) {
			return false;
		}
	    File writeToFile = new File(Activator.getWebAppWorkspace() + "/bar.data.csv");
	    PrintWriter writer = new PrintWriter(new FileWriter(writeToFile));
	    writer.println("category,amount");
	    for (Map.Entry<String, Integer> entry : selectionData.entrySet()) {
	    	writer.println(entry.getKey() + "," + Integer.toString(entry.getValue()));
	    }
	    writer.close();
		return true;
	}

	private Map<String, Integer> processSelectionData(IStructuredSelection selection) {
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() == 1) {
			ChartDataProvider provider = new ChartDataProvider();
			Term input = (Term) currentSelection.getFirstElement();
			Map<String, Integer> citeChartData = provider.calculateNumberOfPapersPerClass(input);
			return citeChartData;
		} else {
			String message = "Please choose exactly one taxonomy from the taxonomy list.";
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
