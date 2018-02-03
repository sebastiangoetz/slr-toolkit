package de.tudresden.slr.googlescholar;

import java.util.Iterator;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import com.gargoylesoftware.htmlunit.WebClient;

public class GSImportWizard extends Wizard implements INewWizard {

	private IWorkbench _workbench;
	private IStructuredSelection _selection;
	
	private WizardGSImportPage _pageOne;
	private WizardNewFileCreationPage _pageTwo;
	
	public GSImportWizard() {
		setWindowTitle("Import from Google Scholar");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_workbench = workbench;
		_selection = selection;
	}
	
	@Override
	public void addPages() {
		super.addPages();
		
		_pageOne = new WizardGSImportPage();
		addPage(_pageOne);
		
		_pageTwo = new WizardSchemaNewFileCreationPage(_selection);
		addPage(_pageTwo);
	}

	@Override
	public boolean performFinish() {
		IFile file = _pageTwo.createNewFile();
		PrintWriter out;
		try {
			out = new PrintWriter(file.getLocation().toOSString());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		GSWorker worker = new GSWorker(out, _pageOne.as_q.getText(), _pageOne.as_epq.getText(), _pageOne.as_oq.getText(), _pageOne.as_eq.getText(), _pageOne.getOcct(), _pageOne.as_sauthors.getText(), _pageOne.as_publication.getText(), _pageOne.as_ylo.getText(), _pageOne.as_yhi.getText());
		
		Job job = new Job("Google Scholar Import (" + file.getName() + ")") {
			@Override
		    protected IStatus run(IProgressMonitor monitor) {
				SubMonitor subMonitor = SubMonitor.convert(monitor);
				return worker.work(subMonitor);
			}
		};
		job.schedule();
		
		return true;
	}

}
