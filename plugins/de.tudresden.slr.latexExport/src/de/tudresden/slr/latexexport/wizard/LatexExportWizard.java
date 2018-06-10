package de.tudresden.slr.latexexport.wizard;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.latexexport.logic.DataProvider;


public class LatexExportWizard extends Wizard implements INewWizard {

    protected LatexExportWizardPageOne one;
    DataProvider dataprovider = new DataProvider();

    public LatexExportWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    @Override
    public String getWindowTitle() {
        return "LaTex Export";
    }

    @Override
    public void addPages() {
        one = new LatexExportWizardPageOne();
        addPage(one);
    }

    @Override
    public boolean performFinish() {
        // Print the result to the console

    	if(one.getFilename() == null || one.getFilename().equals("")) {
    		popupError("No path was specified.");
    	}
    	else {
    		performExport();
    	}
    	
        return true;
    }

	private void performExport() {
		System.out.println(dataprovider.getAllDimensionsAndMetainformation(true));
		
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		
	}
	
	public void popupError(String message) {
		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageDialog.openError(activeShell, "Error", message);
	}
}