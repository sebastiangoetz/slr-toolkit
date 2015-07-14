import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;

import view.ICommunicationView;

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
		part = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(chartViewId);
		DirectoryDialog dialog = new DirectoryDialog(
				HandlerUtil.getActiveShell(event), SWT.OPEN);
		String result = dialog.open();
		ICommunicationView view = (ICommunicationView) part;
		view.generatePDFForCurrentChart(result + "\\output.pdf");
		view.redraw();

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
