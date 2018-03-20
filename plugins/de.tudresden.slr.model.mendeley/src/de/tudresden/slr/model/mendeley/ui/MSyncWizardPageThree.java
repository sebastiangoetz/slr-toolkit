package de.tudresden.slr.model.mendeley.ui;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.Value;

import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;
import de.tudresden.slr.model.mendeley.util.MSyncWizardTableEntry;
import de.tudresden.slr.model.mendeley.util.MendeleyTableEditingSupport;
import de.tudresden.slr.model.mendeley.util.MendeleyTreeLabelProvider;
import de.tudresden.slr.model.mendeley.util.SyncItem;
import de.tudresden.slr.model.mendeley.util.TreeContentProvider;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.custom.StyledText;

import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.INPUT_STREAM;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class MSyncWizardPageThree extends WizardPage {
    private Composite container;
    private Table table;
	private MSyncWizardPageOne folder_page;
	private MSyncWizardPageTwo overview_page;
	private MendeleyFolder folder_selected;
	private TreeViewer treeViewer;
	private TableViewer tableViewer;
	private MendeleyFolder treeInput[];
	private List<SyncItem> syncItems;

    public MSyncWizardPageThree() {
        super("conflictPage");
        setTitle("Resolve Document Conflicts");
        setDescription("Select a document to see if conflicts between Mendeley and your project have occured");
        System.out.println(getName());
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        setControl(container);
        container.setLayout(new GridLayout(1, false));
        
        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setText("Select a Document");
         
        treeViewer = new TreeViewer(container, SWT.BORDER);
        treeViewer.setContentProvider(new TreeContentProvider());
        treeViewer.setLabelProvider(new MendeleyTreeLabelProvider());
        
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection selection = ((TreeSelection)event.getSelection());
				if(selection.getFirstElement() != null){
					if(selection.getFirstElement().getClass() == MendeleyFolder.class){
						treeViewer.getTree().deselect(treeViewer.getTree().getSelection()[0]);
					}
					if(selection.getFirstElement().getClass() == MendeleyDocument.class){
						MendeleyDocument mSelection = (MendeleyDocument)selection.getFirstElement();
						fillTable(mSelection);
					}
				}
			}
		});
        
        Tree tree = treeViewer.getTree();
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        
        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setText("Attributes of selected document:");
        
        tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
        table = tableViewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        
        TableColumn tColumnLabel = new TableColumn(table, SWT.NONE);
        tColumnLabel.setWidth(142);
        TableColumn tColumnValue = new TableColumn(table, SWT.NONE);
        tColumnValue.setWidth(410);
        TableViewerColumn labelColumn = new TableViewerColumn(tableViewer, tColumnLabel);
        
        labelColumn.getColumn().setText("Label");
        TableViewerColumn valueColumn = new TableViewerColumn(tableViewer, tColumnValue);
        labelColumn.setLabelProvider(new ColumnLabelProvider(){
        	@Override
            public String getText(Object element) {
        		MSyncWizardTableEntry entry = (MSyncWizardTableEntry) element;
                return entry.getKey().getValue();
            }
        	
        	@Override
        	public Color getBackground(Object element) {
        		MSyncWizardTableEntry entry = (MSyncWizardTableEntry) element;
				if(entry.getValue1() != null && entry.getValue2() != null){
					if(entry.getValue1().toUserString().equals(entry.getValue2().toUserString()) ){
						return super.getBackground(element);
					}
				}
				return new Color(Display.getDefault(), 0xD8, 0xF6, 0xCE);	
        	}
        });
        
        valueColumn.getColumn().setText("Value");
        valueColumn.setLabelProvider(new ColumnLabelProvider(){
        	@Override
            public String getText(Object element) {
        		MSyncWizardTableEntry entry = (MSyncWizardTableEntry) element;
        		
        		if(entry.getSelected()!= null){
        			return entry.getSelected().toUserString();
        		}
        		else{
        			return "-- [empty] --";
        		}
            }
        	
        	@Override
        	public Color getBackground(Object element) {
        		MSyncWizardTableEntry entry = (MSyncWizardTableEntry) element;
				if(entry.getValue1() != null && entry.getValue2() != null){
					if(entry.getValue1().toUserString().equals(entry.getValue2().toUserString()) ){
						return super.getBackground(element);
					}
				}
				return new Color(Display.getDefault(), 0xD8, 0xF6, 0xCE);	
        	}
        });
        
        valueColumn.setEditingSupport(new MendeleyTableEditingSupport(tableViewer));
        
       
        tableViewer.setContentProvider(new IStructuredContentProvider() {
        	@Override
        	public Object[] getElements(Object inputElement) {
        		SyncItem si = (SyncItem) inputElement;
        		List<MSyncWizardTableEntry> fields = new ArrayList<>();
        		
        		for(Key key : si.getFields().keySet()){
        			Value v1 = si.getFields().get(key).get(0);
        			Value v2 = si.getFields().get(key).get(1);
        			Value vSelected = si.getSelectedFields().get(key);
        			MSyncWizardTableEntry entry = new MSyncWizardTableEntry(key, v1, v2, vSelected);
        			entry.setSyncItem(si);
        			fields.add(entry);
        		}
	
        		MSyncWizardTableEntry[] si_array = new MSyncWizardTableEntry[fields.size()];
            	si_array = fields.toArray(si_array);
        		return si_array;
        	}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub
				
			}
		});
        setPageComplete(false);   
    }
    
    @Override
    public void setVisible(boolean visible) {
    	System.out.println(getName() + " is set " + visible );
    	
    	if(visible){
    		syncFolderSelection();
    		setPageComplete(visible);
    	}
    	//this.tableViewer.refresh();
    	// TODO Auto-generated method stub
    	super.setVisible(visible);
    }   
    
    public void syncFolderSelection(){
    	MSyncWizard myWiz = (MSyncWizard) this.getWizard();
    	IWizardPage[] wpages = myWiz.getPages();
    	this.folder_page = (MSyncWizardPageOne)this.getWizard().getPages()[1];
    	this.folder_selected = folder_page.getFolder_selected();
    	this.overview_page = ((MSyncWizardPageTwo)wpages[2]);
    	this.syncItems = overview_page.getSyncItems();
    	
    	List<MendeleyFolder> inputList = new ArrayList<MendeleyFolder>();
    	
    	
    	inputList.add(this.overview_page.getConflicts());

    	this.treeInput = new MendeleyFolder[1];
    	this.treeInput = inputList.toArray(this.treeInput);
    	this.treeViewer.setInput(treeInput);
    	this.treeViewer.expandAll();
    }
    
    public void fillTable(MendeleyDocument md){
    	SyncItem siSelected = null;
    	for(SyncItem si : this.syncItems){
    		String title = si.getTitle().replaceAll("\\{", "").replaceAll("\\}", "");
    		if(title.equals(md.getTitle())){
    			siSelected = si;
    		}
    	}
    	this.tableViewer.setInput(siSelected);
    }
    
    public List<SyncItem> getSyncItems() {
		return syncItems;
	}
    
}