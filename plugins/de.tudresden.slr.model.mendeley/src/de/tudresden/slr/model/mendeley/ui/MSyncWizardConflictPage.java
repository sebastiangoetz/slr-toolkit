package de.tudresden.slr.model.mendeley.ui;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;

import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;
import de.tudresden.slr.model.mendeley.util.MSyncWizardTableEntry;
import de.tudresden.slr.model.mendeley.util.MendeleyTableContentProvider;
import de.tudresden.slr.model.mendeley.util.MendeleyTableEditingSupport;
import de.tudresden.slr.model.mendeley.util.MendeleyTreeLabelProvider;
import de.tudresden.slr.model.mendeley.util.SyncItem;
import de.tudresden.slr.model.mendeley.util.TreeContentProvider;


/**
 * This class implements the conflict page of the MSyncWizard which is used to
 * resolve conflicts between documents from a bib-file and an online Mendeley Folder.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 * @see org.eclipse.jface.wizard.WizardPage
 */
public class MSyncWizardConflictPage extends WizardPage {
	
    private Composite container;
    
    private Table table;
	
    private MSyncWizardFolderPage folder_page;
	
    private MSyncWizardOverviewPage overview_page;
	
    private MendeleyFolder folder_selected;
	
    private TreeViewer treeViewer;
	
    /**
     * Table provides a column for field Key and a second column for editable field Value
     */
    private TableViewer tableViewer;
	
    private MendeleyFolder treeInput[];
	
    private List<SyncItem> syncItems;

    public MSyncWizardConflictPage() {
        super("conflictPage");
        setTitle("Resolve Document Conflicts");
        setDescription("Select a document to see if conflicts between Mendeley and your project have occured");
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        setControl(container);
        GridLayout gl_container = new GridLayout(1, false);
        gl_container.verticalSpacing = 4;
        container.setLayout(gl_container);
        
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
						// deselect folder if selected
						treeViewer.getTree().deselect(treeViewer.getTree().getSelection()[0]);
					}
					if(selection.getFirstElement().getClass() == MendeleyDocument.class){
						MendeleyDocument mSelection = (MendeleyDocument)selection.getFirstElement();
						// fill table if Document in Treeviewer is selected
						fillTable(mSelection);
					}
				}
			}
		});
        
        Tree tree = treeViewer.getTree();
        GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_tree.minimumHeight = 125;
        tree.setLayoutData(gd_tree);
        
        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setText("Attributes of selected document:");
        
        tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
        table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_table.minimumHeight = 250;
        table.setLayoutData(gd_table);
        
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
        			return "-- [empty] -- ";
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
        
       
        tableViewer.setContentProvider(new MendeleyTableContentProvider());
        setPageComplete(false);   
    }
    
    @Override
    public void setVisible(boolean visible) {
    	if(visible){
    		syncFolderSelection();
    		setPageComplete(visible);
    	}
    	super.setVisible(visible);
    }   
    
    /**
     * This methods gets the chosen Bib-File and MendeleyFolder Selections
     * and sets the Treeinput that contains the conflicted Documents
     */
    public void syncFolderSelection(){
    	MSyncWizard myWiz = (MSyncWizard) this.getWizard();
    	IWizardPage[] wpages = myWiz.getPages();
    	this.folder_page = (MSyncWizardFolderPage)this.getWizard().getPages()[1];
    	this.folder_selected = folder_page.getFolder_selected();
    	this.overview_page = ((MSyncWizardOverviewPage)wpages[2]);
    	this.syncItems = overview_page.getSyncItems();
    	
    	List<MendeleyFolder> inputList = new ArrayList<MendeleyFolder>();
    	
    	
    	inputList.add(this.overview_page.getConflicts());

    	this.treeInput = new MendeleyFolder[1];
    	this.treeInput = inputList.toArray(this.treeInput);
    	this.treeViewer.setInput(treeInput);
    	this.treeViewer.expandAll();
    }
    
    /**
     * This method takes a Mendeley Document and fills the table
     * with it respective SyncItems
     * @param md MendeleyDocument that needs to be edited
     */
    public void fillTable(MendeleyDocument md){
    	SyncItem siSelected = null;
    	for(SyncItem si : this.syncItems){
    		String title = si.getTitle();
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