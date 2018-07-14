package de.tudresden.slr.latexexport.wizard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Map;

import org.eclipse.core.resources.team.TeamHook;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import de.tudresden.slr.latexexport.data.LatexExportChartGenerator;
import de.tudresden.slr.latexexport.latexgeneration.SlrLatexGenerator;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.SlrLatexTemplate;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.TemplateACMSigplanConf;
import de.tudresden.slr.latexexport.latexgeneration.documentclasses.TemplatePlainArticle;
import de.tudresden.slr.metainformation.MetainformationActivator;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.taxonomy.Term;


public class LatexExportWizard extends Wizard implements INewWizard {

    protected LatexExportWizardPageOne one;
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
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
        return true;
    }

	private void performExport() throws IOException {
		String templateSelection = one.getTemplate();

		SlrLatexGenerator document = new SlrLatexGenerator(metainformation, dataprovider, one.getFilename(), templateSelection);
		document.performExport();
	}
	

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}
	
	public void popupError(String message) {
		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageDialog.openError(activeShell, "Error", message);
	}
}