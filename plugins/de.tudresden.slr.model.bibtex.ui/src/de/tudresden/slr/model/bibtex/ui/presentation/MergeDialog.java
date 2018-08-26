package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

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
    private Text filename;
    private CheckboxTableViewer ctv;

    public MergeDialog(Shell parentShell, BibtexMergeData d) {
        super(parentShell);
    	this.data = d;
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
        BibtexResourceImpl ele;
        List<Object> resourceList = data.getResourceList();
        for(int i = 0; i < resourceList.size(); i++) {
        	ele = (BibtexResourceImpl) resourceList.get(i);
            ctv.add(ele.getURI());
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
        data.setArgs(new boolean[] {b0.getSelection(), b1.getSelection(), b2.getSelection()});
        b0.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
            	boolean chk = b0.getSelection();
                data.setSingleArg(0, chk);
                b2.setEnabled(chk);
            }
        });
        b1.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                data.setSingleArg(1, b1.getSelection());
            }
        });   
        b2.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                data.setSingleArg(2, b2.getSelection());
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
		if(merge() == null) {
			MessageDialog.openError(getShell(), "Error", "The filename " + getFilename() + " is taken. Please choose another one!");
			return;
		}
		if(!data.getArgs()[0]) {
			// merge with no duplicate handling - display stats
			String info = "The files have been merged into " + data.getFilename() + ".";
			ListIterator<String> i = data.getStats().listIterator();
			while(i.hasNext()) {
				info = info + "\n" + data.getStats().iterator().next();
			}
			MessageDialog.openInformation(getShell(), "Merge successful", info);
    		
    	}
    	else{
    		if(data.getArgs()[2]) {
        		// merge with manual duplicate handling
    			
        	}
    		else {
        		// merge with automatic duplicate handling
    			
    		}
    	}
        super.okPressed();
    }


	public IFile merge() {
		IFile result = null;
		if(!data.getArgs()[0]) {
			// merge with no duplicate handling
			BibtexResourceImpl res = null;
			for(ListIterator<Object> i = data.getResourceList().listIterator(); i.hasNext();) {
				res = (BibtexResourceImpl) i.next();
				if(!data.toMergeContains(res.getURI())) {
					i.remove();
				}
			}
			IPath filePath = Utils.getIFilefromEMFResource((BibtexResourceImpl) data.getResourceList().get(0)).getLocation();
			filePath = filePath.removeLastSegments(1);
			filePath = new Path(filePath.toString() + "/" + getFilename());
			System.out.println(filePath);
			result = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(filePath)[0];
			if(result.exists()) {
				// if file already exists, signalise that
				return null;
			}
			try {
				InputStream source = Utils.getIFilefromEMFResource((BibtexResourceImpl) data.getResourceList().get(0)).getContents();
			    result.create(source, IResource.NONE, null);
			    for(int i = 1; i < data.getResourceList().size(); i++) {
			    	source = Utils.getIFilefromEMFResource((BibtexResourceImpl) data.getResourceList().get(i)).getContents();
			    	result.appendContents(source, IResource.NONE, null);
			    }
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			if(data.getArgs()[2]) {
				// merge with manual duplicate handling
				
			}
			else {
				// merge with automatic duplicate handling
				
			}
		}
		return result;
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