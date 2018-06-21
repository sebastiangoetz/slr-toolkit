package de.tudresden.slr.latexexport.wizard;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.latexexport.data.DataProvider;
import de.tudresden.slr.latexexport.helpers.LatexDocumentHelper;


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
    		try {
				performExport();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
        return true;
    }

	private void performExport() throws FileNotFoundException, UnsupportedEncodingException {
		System.out.println(dataprovider.getAllDimensionsAndMetainformation(true));
		
		PrintWriter writer = new PrintWriter(one.getFilename(), "UTF-8");
		writer.print(LatexDocumentHelper.getExample1());
		writer.print(dataprovider.getAllDimensionsAndMetainformation(true).size() + "\n");
		writer.print(LatexDocumentHelper.getExample2());
		writer.close();
		
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