package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.model.bibtex.ui.presentation.BibtexMergeData.Criteria;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class BibtexMergeDialogReloaded extends Dialog {
	
    private BibtexMergeData mergeData;
    private CheckboxTableViewer ctv;
    private Text filename;

    public BibtexMergeDialogReloaded(Shell parentShell, BibtexMergeData d) {
        super(parentShell);
        // make non modal
    	setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
    	setBlockOnOpen(false);
    	this.mergeData = d;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);
        
        buildFilesPart(container);        
        buildOptions(container);
        buildCriteria(container);
        buildWeighting(container);
        buildPreview(container);
        buildSavePart(container);
        
        return container;
    }

	private void buildPreview(Composite container) {
        GridData gridData = new GridData();
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 3;
        
		Composite preview = new Composite(container, SWT.NONE);
        preview.setLayout(new GridLayout(2, false));
        preview.setLayoutData(gridData);

        Label label = new Label(preview, SWT.NONE);
        label.setText("Preview: ");
        label.setLayoutData(new GridData (SWT.END, SWT.END, false, false));
        Text previewText = new Text(preview, SWT.BORDER | SWT.SINGLE);
        previewText.setText("---- Preview ----");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalAlignment = SWT.END;
        previewText.setLayoutData(gd);
	}

	private void buildCriteria(Composite container) {
		Group options = new Group(container, SWT.SHADOW_NONE);
        options.setText("Criteria");
        options.setLayout(new RowLayout(SWT.VERTICAL));
        
        // 3 buttons: doi, authors, title
        final Button b0 = new Button(options, SWT.CHECK);
        b0.setText("DoI");
        b0.setSelection(mergeData.getCriteria().get(Criteria.doi) > 0);
        final Button b1 = new Button(options, SWT.CHECK);
        b1.setText("Authors");  
        b1.setSelection(mergeData.getCriteria().get(Criteria.authors) > 0);
        final Button b2 = new Button(options, SWT.CHECK);
        b2.setText("Title");  
        b2.setSelection(mergeData.getCriteria().get(Criteria.title) > 0);
        
        // add listeners for updates
        b0.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mergeData.getCriteria().put(Criteria.doi, b0.getSelection() ? 95 : 0);
            }
        });
        b1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mergeData.getCriteria().put(Criteria.doi, b1.getSelection() ? 95 : 0);
            }
        });   
        b2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mergeData.getCriteria().put(Criteria.doi, b2.getSelection() ? 95 : 0);
            }
        });	
	}

	private void buildWeighting(Composite container) {
		Group scaleGroup = new Group(container, SWT.SHADOW_NONE);
		scaleGroup.setText("Criteria");
		scaleGroup.setLayout(new RowLayout(SWT.VERTICAL));
        
		for (Criteria criteria : Criteria.values() ) {
			createScale(scaleGroup, container, criteria);			
		}
	}

	private void createScale(Group scaleGroup, Composite container, Criteria criteria) {
		Scale scale = new Scale(scaleGroup, SWT.HORIZONTAL);
	    //scale.setBounds(0, 0, 20, 200);
	    scale.setMaximum(100);
	    scale.setMinimum(0);
	    scale.setIncrement(1);
	    
	    Text value = new Text(container, SWT.BORDER | SWT.SINGLE);
		value.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
	    scale.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	        mergeData.getCriteria().put(criteria, scale.getSelection());
	        value.setText(Integer.toString(scale.getSelection()));
	      }
	    });
	}

	private void buildFilesPart(Composite container) {
		Group ctvGroup = new Group(container, SWT.SHADOW_NONE);
        ctvGroup.setText("Files to merge");
        ctvGroup.setLayout(new FillLayout());
        ctv = CheckboxTableViewer.newCheckList(ctvGroup, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        List<BibtexResourceImpl> resourceList = mergeData.getResourceList();
        for(int i = 0; i < resourceList.size(); i++) {
            ctv.add(resourceList.get(i).getURI());
        }
        ctv.setAllChecked(true);
        ctv.getTable().addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                	System.out.println("Checked " + event.item.toString().split("\\{")[1].split("\\}")[0]);
//                	if(validateDialog()) {
//                		mergeData.setToMerge(new HashSet<Object>(Arrays.asList(ctv.getCheckedElements())));
//                	}
                }
              }
            });
	}

	private void buildSavePart(Composite container) {
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
        //data.setFilename(getFilename());
        filename.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
//        		if(validateDialog()) {
//        			data.setFilename(getFilename());
//        		}
            }
        });
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalAlignment = SWT.END;
        filename.setLayoutData(gd);
	}

	private void buildOptions(Composite container) {
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
        b0.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
            	boolean chk = b0.getSelection();
                b2.setEnabled(chk);
            }
        });
        b1.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                
            }
        });   
        b2.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                
            }
        });
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
}
