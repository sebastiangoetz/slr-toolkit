package de.tudresden.slr.googlescholar;

import java.util.Iterator;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
		GSWorker worker = new GSWorker(_pageOne.as_q.getText(), _pageOne.as_eq.getText(), _pageOne.as_epq.getText(), _pageOne.as_eq.getText(), _pageOne.getOcct(), _pageOne.as_sauthors.getText(), _pageOne.as_publication.getText(), _pageOne.as_ylo.getText(), _pageOne.as_yhi.getText());
		String filepath = _pageTwo.createNewFile().getLocation().toOSString();
		
		Job job = new Job("My Job") {
			@Override
		    protected IStatus run(IProgressMonitor monitor) {
				// convert to SubMonitor
				SubMonitor subMonitor = SubMonitor.convert(monitor);
				
				subMonitor.setTaskName("Preparing Google Scholar Session");
				
				worker.init();
				
				int i = 1;
				subMonitor.setTaskName("Loading Google Scholar Entry " + String.valueOf(i));
				while(worker.work()) {
					i++;
					subMonitor.setTaskName("Loading Google Scholar Entry " + String.valueOf(i));
				}
				
				subMonitor.setTaskName("Writing Entries to File");
				PrintWriter out;
				try {
					out = new PrintWriter(filepath);
					List<String> results = worker.getResults();
					Iterator<String> iterator = results.iterator();
					while(iterator.hasNext()) {
						out.write(iterator.next() + "\n");
					}
					out.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return Status.CANCEL_STATUS;
				}
				subMonitor.done();

				return Status.OK_STATUS;
			}
		};
		job.schedule();
		
		return true;
	}

}
