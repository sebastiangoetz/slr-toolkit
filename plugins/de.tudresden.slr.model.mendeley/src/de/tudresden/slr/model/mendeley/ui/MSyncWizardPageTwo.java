package de.tudresden.slr.model.mendeley.ui;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;

import de.tudresden.slr.model.mendeley.api.authentication.MendeleyClient;
import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;
import de.tudresden.slr.model.mendeley.util.MendeleyTreeLabelProvider;
import de.tudresden.slr.model.mendeley.util.SyncItem;
import de.tudresden.slr.model.mendeley.util.TreeContentProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

public class MSyncWizardPageTwo extends WizardPage {
    private Composite container;
    protected MSyncWizardPageOne folder_page;
    private MendeleyFolder folder_selected;
    private List<MendeleyFolder> tree_list;
    private TreeViewer treeViewer;
    private MendeleyFolder conflicts;
    private MendeleyFolder[] treeInput;
    private BibTeXDatabase workspaceBib;
    private MendeleyClient mc;
    private List<SyncItem> syncItems;
    private List<BibTeXEntry> missingInMendeley;
    private List<BibTeXEntry> missingInWorkspace;
    
    public MSyncWizardPageTwo() {
        super("overviewPage");
        setTitle("Processing Overview");
        setDescription("Overview of pending tasks");
        mc = mc.getInstance();
        syncItems = new ArrayList<>();
        missingInMendeley = new ArrayList();
        missingInWorkspace = new ArrayList();
    }

    @Override
    public void createControl(Composite parent) {
    	
        container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        // required to avoid an error in the system
        setControl(container);
        
        treeViewer = new TreeViewer(container, SWT.BORDER);
        treeViewer.setContentProvider(new TreeContentProvider());
        treeViewer.setLabelProvider(new MendeleyTreeLabelProvider());
        
        Tree tree = treeViewer.getTree();
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        container.setEnabled(true);
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				treeViewer.getTree().deselect(treeViewer.getTree().getSelection()[0]);
			}
		});
        
    }
    
    @Override
    public void setVisible(boolean visible) {
    	System.out.println(getName() + " is set " + visible );
    	
    	if(visible && !((MSyncWizardPageOne)this.getPreviousPage()).isSelectionValidated){
    		
    		syncItems = new ArrayList<>();
            missingInMendeley = new ArrayList();
            missingInWorkspace = new ArrayList();
    		
	    	this.syncFolderSelection();
	    	this.initWorkspaceBib();
	    	this.findDocumentsToSync();
	    	this.initDiscription();
	    	this.createTreeInput();
	    	((MSyncWizardPageOne)this.getPreviousPage()).isSelectionValidated = true;
    	}
    	
    	super.setVisible(visible);
    }
    
    private void syncFolderSelection(){
    	MSyncWizard myWiz = (MSyncWizard) this.getWizard();
    	IWizardPage[] wpages = myWiz.getPages();
    	this.folder_page = (MSyncWizardPageOne)wpages[1];
    	this.folder_selected = folder_page.getFolder_selected();
    	((MSyncWizardPage)getWizard().getStartingPage()).getResourceSelected().setMendeleyFolder(folder_selected);
    }
    
    private void initDiscription(){
        if(folder_selected != null){	
        	setDescription("Synchronisation of Folder: " + this.folder_selected.getName());
        }
    }
    
    private void createTreeInput(){
    	this.tree_list = new ArrayList<MendeleyFolder>();
    	
    	MendeleyFolder add_to_mendeley_folder = new MendeleyFolder();
    	add_to_mendeley_folder.setName("Upload Documents from Workspace to Mendeley (automatically)");
    	add_to_mendeley_folder.setType("AddToMendeley");
    	  	
    	BibTeXDatabase dbToM = new BibTeXDatabase();
    	for(BibTeXEntry bib : this.missingInMendeley){
    		MendeleyDocument md = new MendeleyDocument();
    		md.setTitle(bib.getField(new Key("title")).toUserString().replaceAll("\\{", "").replaceAll("\\}", ""));
    		dbToM.addObject(bib);
    		add_to_mendeley_folder.addDocument(md);
    	}
    	
    	add_to_mendeley_folder.setBibtexDatabase(dbToM);
    	
    	if(!dbToM.getEntries().isEmpty()){
    		this.tree_list.add(add_to_mendeley_folder);
    	}
    	
    	
    	MendeleyFolder add_to_workspace_folder = new MendeleyFolder();
    	add_to_workspace_folder.setName("Download Documents from Mendeley to Workspace (automatically)");
    	add_to_workspace_folder.setType("AddToWorkspace");
    	
    	BibTeXDatabase dbToW = new BibTeXDatabase();
    	for(BibTeXEntry bib : this.missingInWorkspace){
    		MendeleyDocument md = new MendeleyDocument();
    		md.setTitle(bib.getField(new Key("title")).toUserString().replaceAll("\\{", "").replaceAll("\\}", ""));
    		dbToW.addObject(bib);
    		add_to_workspace_folder.addDocument(md);
    	}
    	
    	add_to_workspace_folder.setBibtexDatabase(dbToW);
    	
    	if(!dbToW.getEntries().isEmpty()){	
    		this.tree_list.add(add_to_workspace_folder);
    	}
    	
    	MendeleyFolder ignore_folder = new MendeleyFolder();
    	ignore_folder.setName("Documents with identical Content");
    	ignore_folder.setType("Comparison");
    	
    	MendeleyFolder comparison_folder = new MendeleyFolder();
    	comparison_folder.setName("Documents with Conflicts");
    	comparison_folder.setType("Comparison");
    	
    	for(SyncItem si : this.syncItems){
    		if(comparison_folder.getDocumentById(si.getId()) == null){
    			if(si.hasConflicts()){
    				comparison_folder.addDocument(this.folder_selected.getDocumentById(si.getId()));
    			}
    			else{
    				ignore_folder.addDocument(this.folder_selected.getDocumentById(si.getId()));
    			}
    		}
    	}
    	
    	if(!comparison_folder.getDocuments().isEmpty()){
    		this.tree_list.add(comparison_folder);
    	}
    	else {
    		comparison_folder.setName("There are no Documents with Conflict");
    	}
    	
    	if(!ignore_folder.getDocuments().isEmpty()){
    		this.tree_list.add(ignore_folder);
    	}
    	
    	this.conflicts = comparison_folder;
    	
    	this.treeInput = new MendeleyFolder[tree_list.size()];
    	treeInput = tree_list.toArray(treeInput);
    	this.treeViewer.setInput(treeInput);
    	this.treeViewer.expandAll();
    	
    }
    
    public MendeleyFolder getConflicts() {
		return conflicts;
	}
    
    public MendeleyFolder[] getTreeInput() {
		return treeInput;
	}
    
    public List<SyncItem> getSyncItems() {
		return syncItems;
	}
    
    private void initWorkspaceBib(){
    	this.workspaceBib = ((MSyncWizardPage) this.getWizard().getPages()[0]).getResourceSelected().getBibTexDB();
    }
    
    public void findDocumentsToSync(){
    	for(BibTeXEntry bib : workspaceBib.getEntries().values()){
    		Value titleBib = bib.getField(new Key("title"));
			String workspaceTitle = titleBib.toUserString();
			workspaceTitle = workspaceTitle.replaceAll("\\{", "");
			workspaceTitle = workspaceTitle.replaceAll("\\}", "");
			
    		MendeleyDocument md = folder_selected.getDocumentByTitle(workspaceTitle);
    		
    		if(md == null){
    			this.missingInMendeley.add(bib);
    		}
    		else{
				BibTeXDatabase new_db;
				try {
					new_db = mc.getDocumentBibTex(md.getId());
					for(BibTeXEntry mendeley_bib : new_db.getEntries().values()){
						SyncItem syncItem = new SyncItem(md, mendeley_bib , bib);
						this.syncItems.add(syncItem);
					}
					
				} catch (TokenMgrException | IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
    		
    		}
    	}
    	
    	for(MendeleyDocument md : this.folder_selected.getDocuments()){
    		boolean exists = false;
    		for(BibTeXEntry bib : this.workspaceBib.getEntries().values()){
    			Value titleBib = bib.getField(new Key("title"));
    			String workspaceTitle = titleBib.toUserString().replaceAll("\\{", "").replaceAll("\\}", "");
    			
    			if(md.getTitle().equals(workspaceTitle)){
    				exists = true;
    			}
    		}
    		
    		if(!exists){
    			BibTeXDatabase new_db;
				try {
					new_db = mc.getDocumentBibTex(md.getId());
					for(BibTeXEntry bibEntry : new_db.getEntries().values()){
						this.missingInWorkspace.add(bibEntry);
					}
				} catch (TokenMgrException | IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
    		}
    	}
    }
    
    public List<BibTeXEntry> getMissingInMendeley() {
		return missingInMendeley;
	}
    
    public List<BibTeXEntry> getMissingInWorkspace() {
		return missingInWorkspace;
	}
    
    
    

}