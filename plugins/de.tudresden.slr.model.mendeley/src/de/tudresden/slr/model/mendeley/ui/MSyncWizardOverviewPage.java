package de.tudresden.slr.model.mendeley.ui;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.Value;

import de.tudresden.slr.model.mendeley.api.client.MendeleyClient;
import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;
import de.tudresden.slr.model.mendeley.util.MendeleyTreeLabelProvider;
import de.tudresden.slr.model.mendeley.util.SyncItem;
import de.tudresden.slr.model.mendeley.util.TreeContentProvider;

/**
 * This class implements the overview page of the MSyncWizard which is used to
 * display what steps needs to be executed to synchronize the Bib-File with
 * the Mendeley Folder.
 * Documents can automatically be downloaded from an online Mendeley Folder when 
 * they are missing in the Bib-File or uploaded to the online Mendeley Folder.
 * There are also documents with conflicts which needs to be resolved in the
 * next page.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 * @see org.eclipse.jface.wizard.WizardPage
 */
public class MSyncWizardOverviewPage extends WizardPage {
    
	private Composite container;
    
	protected MSyncWizardFolderPage folder_page;
    
	private MendeleyFolder folder_selected;
    
	private List<MendeleyFolder> tree_list;
    
	private TreeViewer treeViewer;
    
	private MendeleyFolder conflicts;
    
	private MendeleyFolder[] treeInput;
    
	private BibTeXDatabase workspaceBib;
    
	private MendeleyClient mc;
    
	/**
	 * List of SyncItems
	 */
	private List<SyncItem> syncItems;
    
	/**
	 * List of BibTexEntries that are Missing in Mendeley and need to be uploaded
	 */
	private List<BibTeXEntry> missingInMendeley;
    
	/**
	 * List of BibTexEntries that are Missing in the Bib-File and need to be downloaded
	 */
	private List<BibTeXEntry> missingInWorkspace;
    
    public MSyncWizardOverviewPage() {
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
    	
    	if(visible && !((MSyncWizardFolderPage)this.getPreviousPage()).isSelectionValidated){
    		
    		syncItems = new ArrayList<>();
            missingInMendeley = new ArrayList();
            missingInWorkspace = new ArrayList();
    		
	    	this.syncFolderSelection();
	    	this.initWorkspaceBib();
	    	this.findDocumentsToSync();
	    	this.initDescription();
	    	this.createTreeInput();
	    	((MSyncWizardFolderPage)this.getPreviousPage()).isSelectionValidated = true;
    	}
    	
    	super.setVisible(visible);
    }
    
    /**
     * This methods gets the chosen Bib-File and MendeleyFolder Selections.
     */
    private void syncFolderSelection(){
    	MSyncWizard myWiz = (MSyncWizard) this.getWizard();
    	IWizardPage[] wpages = myWiz.getPages();
    	this.folder_page = (MSyncWizardFolderPage)wpages[1];
    	this.folder_selected = folder_page.getFolder_selected();
    	((MSyncWizardFilePage)getWizard().getStartingPage()).getResourceSelected().setMendeleyFolder(folder_selected);
    }
    
    /**
     * This method adds the name of the selected mendeley folder to the description of this Wizard
     */
    private void initDescription(){
        if(folder_selected != null){	
        	setDescription("Synchronisation of Folder: " + this.folder_selected.getName());
        }
    }
    
    /**
     * This method creates the Treeinput by dividing documents into different categories: <br />
     * <ul>
     * 	<li>Docs that are exist in MendeleyFolder but are missing in Bib-File</li>
     * 	<li>Docs that are exist in Bib-File but are missing in Mendeley</li>
     * 	<li>Documents that exist in Bib-File and also in Mendeley
     * 		<ul>
     * 			<li>Documents with conflicts that have different fields and/or Values</li>
     * 		<li>Documents with identical fields</li>
     * 		</ul>
     * 	</li>
     * </ul>	
     * 
     */
    private void createTreeInput(){
    	this.tree_list = new ArrayList<MendeleyFolder>();
    	
    	MendeleyFolder add_to_mendeley_folder = new MendeleyFolder();
    	add_to_mendeley_folder.setName("Upload Documents from Workspace to Mendeley (automatically)");
    	add_to_mendeley_folder.setType("AddToMendeley");
    	  	
    	BibTeXDatabase dbToM = new BibTeXDatabase();
    	for(BibTeXEntry bib : this.missingInMendeley){
    		MendeleyDocument md = new MendeleyDocument();
    		md.setTitle(bib.getField(new Key("title")).toUserString());
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
    		md.setTitle(bib.getField(new Key("title")).toUserString());
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
    
    /**
     * this method sets the WorkspaceBibTexEntry that was selected
     */
    private void initWorkspaceBib(){
    	this.workspaceBib = ((MSyncWizardFilePage) this.getWizard().getPages()[0]).getResourceSelected().getBibTexDB();
    }
    
    /**
     * This method finds differences between Bib-File and Mendeley documents and divides them into different categories: <br />
     * <ul>
     * 	<li>Docs that are exist in MendeleyFolder but are missing in Bib-File</li>
     * 	<li>Docs that are exist in Bib-File but are missing in Mendeley</li>
     * 	<li>Documents that exist in Bib-File and also in Mendeley
     * 		<ul>
     * 			<li>Documents with conflicts that have different fields and/or Values</li>
     * 		<li>Documents with identical fields</li>
     * 		</ul>
     * 	</li>
     * </ul>	
     * 
     * If a Document in a Bib-File is basically the same one as in an online Mendeley Folder is decided
     * by its 'title' attribute
     */
    public void findDocumentsToSync(){
    	// find mendeley documents to add or update by looking at the bib-file entries
    	for(BibTeXEntry bib : workspaceBib.getEntries().values()){
    		Value titleBib = bib.getField(new Key("title"));
			String workspaceTitle = titleBib.toUserString();
			
    		MendeleyDocument md = folder_selected.getDocumentByTitle(workspaceTitle);
    		
    		//if there is no Mendeley Document with the same Title as the doc in the bib-file
    		//add it to the mendeley folder
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
					e.printStackTrace();
				}
				
    		
    		}
    	}
    	
    	// find mendeley documents that are missing in the bib-file
    	for(MendeleyDocument md : this.folder_selected.getDocuments()){
    		boolean exists = false;
    		for(BibTeXEntry bib : this.workspaceBib.getEntries().values()){
    			Value titleBib = bib.getField(new Key("title"));
    			//String workspaceTitle = titleBib.toUserString().replaceAll("\\{", "").replaceAll("\\}", "");
    			String workspaceTitle = titleBib.toUserString();
    			
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