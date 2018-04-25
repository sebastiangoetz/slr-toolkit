package de.tudresden.slr.model.mendeley.ui;
import java.util.ArrayList;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jbibtex.TokenMgrException;

import de.tudresden.slr.model.mendeley.api.client.MendeleyClient;
import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;
import de.tudresden.slr.model.mendeley.util.MendeleyTreeLabelProvider;
import de.tudresden.slr.model.mendeley.util.TreeContentProvider;

/**
 * This class implements the folder page of the MSyncWizard which is used to
 * select a MendeleyFolder in order to synchronize its content with a Bib-File.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 * @see org.eclipse.jface.wizard.WizardPage
 */
public class MSyncWizardFolderPage extends WizardPage {

	public boolean isSelectionValidated;

	private MendeleyClient mc;
	
	private MendeleyFolder folder_selected = null;
	
	private ArrayList<String>folder_list;
	
	private String folders_str;
	
	protected AdapterFactoryEditingDomain editingDomain;
	
	public MSyncWizardFolderPage() {
		super("FolderPage");
		folders_str = "";
		setTitle("Mendeley Folder Selection");
		setDescription("Choose a Folder from your Mendeley Profile");
		folder_list = new ArrayList();
		isSelectionValidated = false;
		setPageComplete(false);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		mc = MendeleyClient.getInstance();
		
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		TreeViewer treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeViewer.setContentProvider(new TreeContentProvider());
		treeViewer.setLabelProvider(new MendeleyTreeLabelProvider());
		
        try {
        	// TreeViewer takes Array of MendeleyFolders
			treeViewer.setInput(mc.getMendeleyFolders());
		} catch (TokenMgrException e) {
			e.printStackTrace();
		}  
        
        // add a Listener that makes sure that only Folders can be selected
        // if a Folder is selected, the page will be set to complete
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection selection = ((TreeSelection)event.getSelection());
				if(selection.getFirstElement() instanceof MendeleyDocument){
					
					TreeItem[] item = treeViewer.getTree().getSelection();
					tree.select(item[0].getParentItem());
					folder_selected = (MendeleyFolder)item[0].getParentItem().getData();
					setPageComplete(true);
				}
				if(selection.getFirstElement() instanceof MendeleyFolder){
					folder_selected = (MendeleyFolder)selection.getFirstElement();
					setPageComplete(true);
				}
				isSelectionValidated = false;
			}
		});	
        
	}
	
	public MendeleyFolder getFolder_selected() {
		return folder_selected;
	}
}
