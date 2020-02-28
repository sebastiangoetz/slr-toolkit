package de.tudresden.slr.latexexport.wizard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.latexexport.latexgeneration.SlrLatexGenerator;
import de.tudresden.slr.metainformation.MetainformationActivator;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;

public class LatexExportWizard extends Wizard implements INewWizard {

    protected LatexExportWizardPageOne wizardPageOne;
    private DataProvider dataprovider = new DataProvider();
    SlrProjectMetainformation metainformation = MetainformationActivator.getMetainformation();

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
        wizardPageOne = new LatexExportWizardPageOne();
        addPage(wizardPageOne);
    }

    @Override
    /**
     * When wizard is finished/ok clicked, this method tries to get the filename from it.
     * If the filename is valid, perform export method is called.
     */
    public boolean performFinish() {
    	if(wizardPageOne.getFilename() == null || wizardPageOne.getFilename().equals("")) {
    		popupError("No path was specified.");
    	}
    	else {
    		try {
				performExport();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
        return true;
    }

    /**
     * Gets the template, which is selected in the wizard, creates a new LatexGenerator and calls the export method.
     * @throws IOException
     */
	private void performExport() throws IOException {
		String templateSelection = wizardPageOne.getTemplate();

		SlrLatexGenerator document = new SlrLatexGenerator(metainformation, dataprovider, wizardPageOne.getFilename(), templateSelection);
		document.performExport();
	}
	

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}
	
	/**
	 * Generates a popup with an error message
	 * @param message Error message which is to be displayed
	 */
	public void popupError(String message) {
		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageDialog.openError(activeShell, "Error", message);
	}
}