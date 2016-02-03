package de.tudresden.slr.ui.chart.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.ui.chart.views.ICommunicationView;

public class SaveAsPDFHandler implements IHandler {

	private final String chartViewId = "chart.view.chartview";

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart part = null;
		part = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().findView(chartViewId);
		FileDialog dialog = new FileDialog(HandlerUtil.getActiveShell(event), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.pdf" });
		try {
			String result = dialog.open();
			if (result != null) {
				ICommunicationView view = (ICommunicationView) part;
				view.generatePDFForCurrentChart(result);
				view.redraw();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
