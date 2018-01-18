package de.tudresden.slr.ui.chart.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.ui.chart.settings.SettingsDialog;

public class SettingsHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SettingsDialog settingsDialog = new SettingsDialog(HandlerUtil.getActiveShell(event), SWT.DIALOG_TRIM);
		try {
			IViewPart part = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView("chart.view.chartview");
			settingsDialog.setViewPart(part);
		} catch (PartInitException e) {
			e.printStackTrace();
		}		
		settingsDialog.open();
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
