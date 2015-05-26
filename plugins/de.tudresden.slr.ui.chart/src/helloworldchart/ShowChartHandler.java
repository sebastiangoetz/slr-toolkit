package helloworldchart;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import wizard.ChartWizard;

public class ShowChartHandler extends AbstractHandler {

@Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
//    MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "Info", "Info for you");
	  WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), new ChartWizard());
	  dialog.open(); 
	  return null;
  }

}
