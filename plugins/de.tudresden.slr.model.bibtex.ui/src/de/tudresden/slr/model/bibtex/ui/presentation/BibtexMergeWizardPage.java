package de.tudresden.slr.model.bibtex.ui.presentation;


import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.Value;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.util.TaxonomyStandaloneParser;

public class BibtexMergeWizardPage  extends WizardPage {
	private Composite container;
    private BibtexMergeConflict conflict;
    private int index;
    private int amount;
    private String result;
    private String resultName;
    private Label hint;
    private TabFolder folder;
    private TabItem editTab;
    private StyledText editedText;
    private Button select;
    private Button edit;

    public BibtexMergeWizardPage(BibtexMergeConflict conflict, int i, int a) {
        super("Step 2: Manual conflict solving");
        setTitle("Step 2: Manual conflict solving");
    	this.conflict = conflict;
    	this.index = i;
    	this.amount = a;
    	this.result = "";
    	this.resultName = "";
    	this.editTab = null;
    }
    
    @Override
    public void createControl(Composite parent) {
        setDescription("Conflict " + index + " of " + amount);
        container = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout(1, false);
        gl.marginHeight = 0;
        container.setLayout(gl);
        folder = new TabFolder(container, SWT.NONE);
        for(int i = 0; i < conflict.amountOfEntries(); i++) {
            TabItem tab = new TabItem(folder, SWT.NONE);
    	    tab.setText(conflict.getFileName(i));
    	    Composite c = new Composite(folder, SWT.NONE);
    	    c.setLayoutData(new FillLayout());
    	    StyledText text = new StyledText(c, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    	    text.setText(conflict.getEntry(i));
    	    text.setEditable(false);
    	    text.setBounds(0, 2, 480, 240);
			text.setAlwaysShowScrollBars(false);
    	    tab.setControl(c);
        }
        folder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean onEditTab = folder.getSelectionIndex() == conflict.amountOfEntries();
				edit.setEnabled(!onEditTab);
	    		select.setEnabled((!onEditTab || validateString(editedText.getText())) && folder.getItem(folder.getSelectionIndex()).getText() != resultName);
			}
		});
		Composite bottom = new Composite(container, SWT.BOTTOM);
		bottom.setLayout(new GridLayout(2, true));
		GridData but = new GridData();
		but.horizontalAlignment = SWT.CENTER;
		but.heightHint = 25;
		but.widthHint = 90;
		edit = new Button(bottom, SWT.PUSH);
		edit.setLayoutData(but);
		edit.setText("Edit");
		edit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addEditTab();
			}
		});
		select = new Button(bottom, SWT.PUSH);
		select.setLayoutData(but);
		select.setText("Select");
		select.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		            if(folder.getSelectionIndex() < conflict.amountOfEntries()) {
		            	setResult(conflict.getFileName(folder.getSelectionIndex()), conflict.getEntry(folder.getSelectionIndex()));
		            }
		            else {
			            setResult("Edited result", editedText.getText());
		            }
	                setPageComplete(true);
	                select.setEnabled(false);
		        }
		      });
		hint = new Label(bottom, SWT.CENTER);
		hint.setText("No solution selected.");
		GridData span = new GridData();
		span.horizontalSpan = 2;
		span.horizontalAlignment = SWT.CENTER;
		span.grabExcessHorizontalSpace = true;
		span.widthHint = 480;
		hint.setLayoutData(span);
		
        // required to avoid an error in the system
        setControl(container);
        setPageComplete(false);
    }
    
    private boolean validateString(String toValidate) {
    	Map<Key, BibTeXEntry> entryMap = Collections.emptyMap();
		try (Reader reader = new BufferedReader(new StringReader(toValidate))) {
			BibTeXParser parser = new BibTeXParser();
			BibTeXDatabase db = parser.parseFully(reader);
			entryMap = db.getEntries();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		if(!entryMap.isEmpty()) {
			BibTeXEntry entry = entryMap.values().iterator().next();
			if(entry.getField(BibTeXEntry.KEY_TITLE) != null) {
				// validate taxonomy classification
				Key classKey = new Key("classes");
				Value classes = entry.getField(classKey);
				if(classes != null) {
					TaxonomyStandaloneParser tsp = new TaxonomyStandaloneParser();
					EList<Term> newDims;
					try {
						newDims = tsp.parseTaxonomyText(classes.toUserString()).getDimensions();
					}
					catch(Exception e){
						System.err.println(e.getMessage());
						return false;
					}
					EList<Term> dims = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy().get().getDimensions();
					if(!validateTerms(newDims, dims)) {
						return false;
					}
				}
				// check for required fields
				switch (entry.getType().getValue()) {
				case "article": 
					if(entry.getField(BibTeXEntry.KEY_AUTHOR) != null && entry.getField(BibTeXEntry.KEY_JOURNAL) != null 
					&& entry.getField(BibTeXEntry.KEY_YEAR) != null) 
						return true;
				case "book":
					if((entry.getField(BibTeXEntry.KEY_AUTHOR) != null || entry.getField(BibTeXEntry.KEY_EDITOR) != null)
					&& entry.getField(BibTeXEntry.KEY_PUBLISHER) != null && entry.getField(BibTeXEntry.KEY_YEAR) != null) 
						return true;
				case "inbook":
					if((entry.getField(BibTeXEntry.KEY_AUTHOR) != null || entry.getField(BibTeXEntry.KEY_EDITOR) != null)
					&& entry.getField(BibTeXEntry.KEY_PUBLISHER) != null && entry.getField(BibTeXEntry.KEY_YEAR) != null 
					&& (entry.getField(BibTeXEntry.KEY_CHAPTER) != null || entry.getField(BibTeXEntry.KEY_PAGES) != null)) 
						return true;
				case "incollection":
					if(entry.getField(BibTeXEntry.KEY_AUTHOR) != null && entry.getField(BibTeXEntry.KEY_BOOKTITLE) != null 
					&& entry.getField(BibTeXEntry.KEY_PUBLISHER) != null && entry.getField(BibTeXEntry.KEY_YEAR) != null)
						return true;
				case "inproceedings":
					if(entry.getField(BibTeXEntry.KEY_AUTHOR) != null && entry.getField(BibTeXEntry.KEY_BOOKTITLE) != null 
					&& entry.getField(BibTeXEntry.KEY_YEAR) != null) 
						return true;
				case "mastersthesis":
					if(entry.getField(BibTeXEntry.KEY_AUTHOR) != null && entry.getField(BibTeXEntry.KEY_SCHOOL) != null 
					&& entry.getField(BibTeXEntry.KEY_YEAR) != null) 
						return true;
				case "phdthesis":
					if(entry.getField(BibTeXEntry.KEY_AUTHOR) != null && entry.getField(BibTeXEntry.KEY_SCHOOL) != null 
					&& entry.getField(BibTeXEntry.KEY_YEAR) != null) 
						return true;
				case "proceedings":
					if(entry.getField(BibTeXEntry.KEY_YEAR) != null) 
						return true;
				case "techreport":
					if(entry.getField(BibTeXEntry.KEY_AUTHOR) != null && entry.getField(BibTeXEntry.KEY_INSTITUTION) != null 
					&& entry.getField(BibTeXEntry.KEY_YEAR) != null) 
						return true;
				case "unpublished":
					if(entry.getField(BibTeXEntry.KEY_AUTHOR) != null && entry.getField(BibTeXEntry.KEY_NOTE) != null) 
						return true;
				default: 
					return false;
				}
			}
		}
		return false;
    }
    
    private boolean validateTerms(EList<Term> toValidate, EList<Term> taxonomy) {
    	if(toValidate.isEmpty()) {
    		return true;
    	}
    	else if(taxonomy.isEmpty()) {
    		return false;
    	}
		boolean result = true;
		for(int i = 0; i < toValidate.size(); i++) {
			boolean match = false;
			for(int j = 0; j < taxonomy.size(); j++) {
				if(toValidate.get(i).getName().equals(taxonomy.get(j).getName())) {
					match = true;
					result = result && validateTerms(toValidate.get(i).getSubclasses(), taxonomy.get(j).getSubclasses());
					break;
				}
			}
			if(!match) {
				return false;
			}
		}
		return result;
    }
    
	private void addEditTab() {
		if(editTab == null) {
			editTab = new TabItem(folder, SWT.NONE);
			editTab.setText("Result");
			Composite c = new Composite(folder, SWT.NONE);
			c.setLayoutData(new FillLayout());
			editedText = new StyledText(c, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
			editedText.setBounds(0, 2, 480, 240);
			editedText.setAlwaysShowScrollBars(false);
			editedText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					boolean isValid = validateString(editedText.getText());
					select.setEnabled(isValid);
					refreshHint(isValid);
				}
			});
			editTab.setControl(c);
		}
		editedText.setText(conflict.getEntry(folder.getSelectionIndex()));
		folder.setSelection(editTab);
		select.setEnabled(validateString(editedText.getText()));
		edit.setEnabled(false);
	}
	
	private void setResult(String resultName, String result) {
		this.result = result;
		this.resultName = resultName;
		refreshHint(true);
	}
	
	private void refreshHint(boolean isValid) {
		String newHint = "";
		if(resultName.isEmpty()) {
			newHint += "No result selected";
		}
		else {
			newHint += resultName + " selected";
		}
		if(!isValid) {
			newHint += "; current edit is not valid";
		}
		hint.setText(newHint += ".");
	}
    
	public String getResult() {
		return result;
	}
	
}
