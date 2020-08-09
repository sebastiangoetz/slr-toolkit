package de.tudresden.slr.classification.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class EntryExcludeSelectionPage extends WizardPage {

	protected static final String entryTypes[] = {"article","book","booklet","conference","inbook","incollection","inproceedings","manual","mastersthesis","misc","phdthesis","proceedings","techreport","unpublished"};
	protected Map<String,Boolean> checkedMap;
	
	public EntryExcludeSelectionPage(String pageName) {
		super(pageName);
		checkedMap = new HashMap<String,Boolean>();
	}

	public EntryExcludeSelectionPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		checkedMap = new HashMap<String,Boolean>();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        Label labelExclude = new Label(container, SWT.NONE);
        labelExclude.setText("Exclude Entries");
        Label labelEmpty = new Label(container, SWT.NONE);
        labelEmpty.setText("");
        
        ArrayList<Button> checkBoxes = new ArrayList<Button>();
        
        for(String entryType:entryTypes) {
        	Button checkBox = new Button(container, SWT.CHECK);
        	checkBox.setText(entryType);
        	checkedMap.put(entryType,false);
        	checkBox.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button check = ((Button) e.widget);
					checkedMap.put(check.getText(),check.getSelection()); 
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});
            checkBox.setSelection(false);
            checkBoxes.add(checkBox);
        }
        
        setControl(container);
        //setPageComplete(false);
	}
	
	public Map<String,Boolean> getCheckedMap() {
		return checkedMap;
	}
	
	public List<String> getExclusionList() {
		List<String> exclusionList = new LinkedList<String>();
		checkedMap.forEach((s,b) -> {
			if(b) exclusionList.add(s);
		});
		
		return exclusionList;
	}

}
