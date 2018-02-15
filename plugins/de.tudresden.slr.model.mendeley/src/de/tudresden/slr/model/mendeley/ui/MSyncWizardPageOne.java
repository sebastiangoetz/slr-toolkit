package de.tudresden.slr.model.mendeley.ui;
import org.eclipse.jface.wizard.IWizardPage;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import com.google.gson.Gson;

import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;
import de.tudresden.slr.model.mendeley.api.authentication.MendeleyClient;
import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;
import de.tudresden.slr.model.mendeley.util.MendeleyTreeLabelProvider;
import de.tudresden.slr.model.mendeley.util.TreeContentProvider;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
public class MSyncWizardPageOne extends WizardPage {

	/**
	 * Create the wizard.
	 */
	public boolean isSelectionValidated;
	private MendeleyClient mc;
	private MendeleyFolder folder_selected = null;
	private ArrayList<String>folder_list;
	private String folders_str;
	protected AdapterFactoryEditingDomain editingDomain;
	
	public MSyncWizardPageOne() {
		super("FolderPage");
		folders_str = "";
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
		folder_list = new ArrayList();
		isSelectionValidated = false;
		setPageComplete(false);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent((domain) -> editingDomain = domain);
		mc = MendeleyClient.getInstance();
		try {
			mc.requestAccessToken("kvQ4pO7zgovBPQaM5bQV0PE8YUY");
			System.out.println(mc.getAllDocumentsBibTex());
			System.out.println(mc.getAllDocumentsJSON());
			System.out.println(mc.getAllFolders());
			folders_str = mc.getAllFolders();
		} catch (TokenMgrException | IOException | ParseException e) {
			e.printStackTrace();
		}
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		TreeViewer treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeViewer.setContentProvider(new TreeContentProvider());
		treeViewer.setLabelProvider(new MendeleyTreeLabelProvider());
		
        try {
			treeViewer.setInput(mc.getAllMendeleyFolders());
		} catch (TokenMgrException | IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection selection = ((TreeSelection)event.getSelection());
				if(selection.getFirstElement() instanceof MendeleyDocument){
					
					TreeItem[] item = treeViewer.getTree().getSelection();
					System.out.print(item[0].getParentItem());
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
