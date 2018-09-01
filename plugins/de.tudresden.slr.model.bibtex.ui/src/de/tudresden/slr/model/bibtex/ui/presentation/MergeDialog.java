package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.model.bibtex.ui.util.Utils;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class MergeDialog extends Dialog {
	
    private BibtexMergeData data;
	private boolean[] args;
    private Text filename;
    private CheckboxTableViewer ctv;
    private WizardDialog wizardDialog;

    public MergeDialog(Shell parentShell, BibtexMergeData d) {
        super(parentShell);
    	this.data = d;
		this.args = new boolean[3];
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);
        Group ctvGroup = new Group(container, SWT.SHADOW_NONE);
        ctvGroup.setText("Files to merge");
        ctvGroup.setLayout(new FillLayout());
        ctv = CheckboxTableViewer.newCheckList(ctvGroup, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        List<BibtexResourceImpl> resourceList = data.getResourceList();
        for(int i = 0; i < resourceList.size(); i++) {
            ctv.add(resourceList.get(i).getURI());
        }
        ctv.setAllChecked(true);
        ctv.getTable().addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                	System.out.println("Checked " + event.item.toString().split("\\{")[1].split("\\}")[0]);
                	if(validateDialog()) {
                		data.setToMerge(new HashSet<Object>(Arrays.asList(ctv.getCheckedElements())));
                	}
                }
              }
            });
        Group options = new Group(container, SWT.SHADOW_NONE);
        options.setText("Options");
        options.setLayout(new RowLayout(SWT.VERTICAL));
        final Button b0 = new Button(options, SWT.CHECK);
        b0.setText("Eliminate duplicates");
        b0.setSelection(true);
        final Button b1 = new Button(options, SWT.CHECK);
        b1.setText("Delete original files after merge");  
        b1.setSelection(false);
        final Button b2 = new Button(options, SWT.CHECK);
        b2.setText("Manually resolve conflicts");  
        b2.setSelection(false);
        setArgs(new boolean[] {b0.getSelection(), b1.getSelection(), b2.getSelection()});
        b0.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
            	boolean chk = b0.getSelection();
                setSingleArg(0, chk);
                b2.setEnabled(chk);
            }
        });
        b1.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                setSingleArg(1, b1.getSelection());
            }
        });   
        b2.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                setSingleArg(2, b2.getSelection());
            }
        });
        Composite savePart = new Composite(container, SWT.NONE);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = GridData.FILL;
        savePart.setLayout(new GridLayout(2, false));
        savePart.setLayoutData(gridData);
        Label save = new Label(savePart, SWT.NONE);
        save.setText("Save as: ");
        save.setLayoutData(new GridData (SWT.END, SWT.END, false, false));
        filename = new Text(savePart, SWT.BORDER | SWT.SINGLE);
        filename.setText("mergeResult.bib");
        data.setFilename(getFilename());
        filename.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
        		if(validateDialog()) {
        			data.setFilename(getFilename());
        		}
            }
        });
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalAlignment = SWT.END;
        filename.setLayoutData(gd);
        return container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Merge BibTeX files");
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        createButton(parent, IDialogConstants.OK_ID, "Merge", true);
    }
    
    @Override
    protected void okPressed() {
    	if(!validateDialog()) {
    		return;
    	}
    	if(fileExists()) {
    		MessageDialog.openError(getShell(), "Error", "The filename " + getFilename() + " is taken. Please choose another one!");
			return;
    	}
    	if(args[1] && !MessageDialog.openQuestion(getShell(), "Confirm file deletion", "This will delete the selected files. Are you sure?")) {
    		return;
    	}
    	boolean success = false;
    	try {
    		if(!getArgs()[0]) {
    			// merge with no duplicate handling - display stats
        		if(merge("")) {
        			String info = "The files have been merged into " + data.getFilename() + ".\n" + data.getStats();
        			MessageDialog.openInformation(getShell(), "Merge successful", info);
        			success = true;
        		}

    		}
    		else{
    			if(getArgs()[2]) {
    				// merge with manual duplicate handling
    				if(data.getConflicts().isEmpty()) {
    					if(merge("")) {
        	    			MessageDialog.openInformation(getShell(), "Merge successful", "No conflicts detected, " + data.getSimpleDuplicateTitles().size() + " duplicate(s) eliminated.");
        	    			success = true;
    					}
    				}
    				else {
    					BibtexManualMergeWizard mergeWizard = new BibtexManualMergeWizard(data);
    					wizardDialog = new WizardDialog(getShell(), mergeWizard);
    					if(wizardDialog.open() == 0) {
    						if(merge(mergeWizard.getResults())) {
    							MessageDialog.openInformation(getShell(), "Manual merge successful", data.getConflicts().size() + " conflict(s) solved, " + data.getSimpleDuplicateTitles().size() + " duplicate(s) eliminated.");
    							success = true;
    						}
    	    			}
    	    		}
    			}
    			else {
    				// merge with automatic duplicate handling
    				if(merge("")) {
    					MessageDialog.openInformation(getShell(), "Automatic merge successful", data.getConflicts().size() + " conflict(s) solved, " + data.getSimpleDuplicateTitles().size() + " duplicate(s) eliminated.");
    					success = true;
    				}

    			}
    		}
    	} 
    	catch (CoreException | IOException e) {
    		e.printStackTrace();
    		if(e.getMessage().contains("deleting")) {
    			MessageDialog.openError(getShell(), "Error", "An exception occured while deleting files.");
    		}
    		else if(e.getMessage().contains("creating")) {
    			MessageDialog.openError(getShell(), "Error", "An exception occured while creating the new file.");
    		}
    		else {
    			MessageDialog.openError(getShell(), "Error", "An exception occured while manipulating files.");
    		}
    	}
    	if(success) {
        	super.okPressed();
    	}
    	else {
    		MessageDialog.openError(getShell(), "Error", "Something went wrong while merging.");
    	}
    }


	private boolean merge(String manualResult) throws CoreException, IOException {
		boolean[] args = getArgs();
		if(fileExists()) {
			return false;
		}
		data.removeUnselectedResources();
		IPath filePath = Utils.getIFilefromEMFResource(data.getResourceList().get(0)).getLocation().removeLastSegments(1);
		filePath = new Path(filePath.toString() + "/" + getFilename());
		IFile result = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(filePath)[0];
		if(!args[0]) {
			// merge with no duplicate handling
			InputStream source = Utils.getIFilefromEMFResource(data.getResourceList().get(0)).getContents();
			result.create(source, IResource.NONE, null);
			for(int i = 1; i < data.getResourceList().size(); i++) {
				source = Utils.getIFilefromEMFResource(data.getResourceList().get(i)).getContents();
				result.appendContents(source, IResource.NONE, null);
			}
		}
		else {
			// merge with duplicate handling
			String conflictResult = "";
			if(!manualResult.isEmpty()) {
				// merge with manual duplicate handling
				conflictResult = manualResult;
			}
			else {
				// merge with automatic duplicate handling
				String chosen;
				String checked;
				for(BibtexMergeConflict c : data.getConflicts()) {
					chosen = c.getEntry(0);
					if(chosen.endsWith("}")) {
						chosen += System.lineSeparator();
					}
					for(int i = 0; i < c.amountOfEntries(); i++) {
						checked = c.getEntry(i);
						if(checked.endsWith("}")) {
							checked += System.lineSeparator();
						}
						// TODO: more detailed decision making: parse with BibTeXParser and check which entry has more fields / more info
						// TODO: even more detailed decision making: per-field evaluation of all entries and construction of chosen result
						if(checked.length() > chosen.length()) {
							chosen = c.getEntry(i);
						}
					}
					conflictResult += chosen;
				}
			}
			// common part: write resolved conflicts to newly created file, then iterate through all merged files and write entries if necessary
			InputStream source = new ByteArrayInputStream(conflictResult.getBytes());
			result.create(source, IResource.NONE, null);
			Set<String> dupTitles = data.getSimpleDuplicateTitles();
			Set<String> remainingDupTitles = new HashSet<String>(dupTitles);
			Set<String> conflictTitles = data.getConflictTitles();
			for(int i = 0; i < data.getResourceList().size(); i++) {
				IFile f = Utils.getIFilefromEMFResource((BibtexResourceImpl) data.getResourceList().get(i));
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				InputStream inputStream = f.getContents();
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, length);
				}
				inputStream.close();
				for(String snippet : outputStream.toString().split("@")) {
					if(snippet.contains("{")) {
						String title = snippet.split("title = \\{")[1].split("\\}")[0];
						String toWrite = "@" + snippet;
						if(toWrite.endsWith("}")) {
							toWrite += System.lineSeparator();
						}
						// The following statements assure that:
						// - if the snippet contains a simple duplicate's title, it is written to the result only when found for the first time
						// - else if snippet does not contain a conflict's title, it is written to the result
						if(dupTitles.remove(title)) {
							dupTitles.add(title);
							if(remainingDupTitles.remove(title)) {
								source = new ByteArrayInputStream(toWrite.getBytes());
								result.appendContents(source, IResource.NONE, null);
							}
						}
						else if(conflictTitles.add(title)) {
							conflictTitles.remove(title);
							source = new ByteArrayInputStream(toWrite.getBytes());
							result.appendContents(source, IResource.NONE, null);
						}
					}
				}
			}
		}
		if(args[1]) {
			// if source files should be deleted
			for(ListIterator<BibtexResourceImpl> i = data.getResourceList().listIterator(); i.hasNext();) { 
				Utils.getIFilefromEMFResource(i.next()).delete(true, null);
			}
		}
		return true;
	}

	private boolean fileExists() {
		BibtexResourceImpl res = data.getResourceList().get(0);
		for(int i = 0; i < data.getResourceList().size(); i++) {
			res = data.getResourceList().get(i);
			if(data.toMergeContains(res.getURI())) {
				break;
			}
		}
		IPath filePath = Utils.getIFilefromEMFResource(res).getLocation().removeLastSegments(1);
		filePath = new Path(filePath.toString() + "/" + getFilename());
		IFile result = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(filePath)[0];
		return result.exists();
	}
	
	public boolean[] getArgs() {
		return args;
	}

	public void setArgs(boolean[] args) {
		this.args = args;
	}

	public void setSingleArg(int index, boolean arg) {
		this.args[index] = arg;
	}
	
	public String getFilename() {
        return filename.getText();
    }
    
    private boolean validateDialog() {
    	boolean valid = getFilename().matches("[-_. A-Za-z0-9]+\\.(bib)") && ctv.getCheckedElements().length > 1;
        getButton(IDialogConstants.OK_ID).setEnabled(valid);
    	return valid;
    }
    
}