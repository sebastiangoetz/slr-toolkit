package de.tudresden.slr.model.mendeley.ui;

import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;

import de.tudresden.slr.model.mendeley.api.client.MendeleyClient;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;
import de.tudresden.slr.model.mendeley.util.WorkspaceTreeLabelProvider;

/**
 * This class implements the file page of the MSyncWizard which is used to
 * select between Bib-files from different Projects
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 * @see org.eclipse.jface.wizard.WizardPage
 */
public class MSyncWizardFilePage extends WizardPage {

	protected AdapterFactoryEditingDomain editingDomain;
	
	protected AdapterFactory adapterFactory;
	
	private WorkspaceBibTexEntry resourceSelected; 
	
	/**
	 * Project that contains selected Bib-File
	 */
	private IProject selectedProject;
	
	/**
	 * TreeViewer that displays Bib-Files of a selected Project
	 */
	private TreeViewer treeViewer;
	
	private WorkspaceManager wm;
	
	private MendeleyClient mc;
	
	/**
	 * Create the wizard.
	 * @wbp.parser.constructor
	 */
	public MSyncWizardFilePage() {
		super("selectFile");
		setTitle("File Selection");
		setDescription("Select a Bib-File you want to synchronize with Mendeley");
		selectedProject = null;
		wm = WorkspaceManager.getInstance();
		setPageComplete(false);
		createWorkspaceContent();
		resourceSelected = null;
		selectedProject = null;
	}
	
	/**
	 * Creates a Page that already has a Bib-File selected
	 * 
	 * @param uri URI of the Bib-File that needs to be connected
	 */
	public MSyncWizardFilePage(URI uri) {
		super("selectFile");
		setTitle("File Selection");
		setDescription("Select a Bib-File you want to synchronize with Mendeley");
		selectedProject = null;
		wm = WorkspaceManager.getInstance();
		setPageComplete(false);
		createWorkspaceContent();
		WorkspaceBibTexEntry entry = wm.getWorkspaceBibTexEntryByUri(uri);
		if(entry != null) {
			resourceSelected = entry;
			selectedProject = entry.getProject();
		}
	}
	
	/**
	 * Creates contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		
		
		Composite container = new Composite(parent, SWT.NULL);
		
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		ComboViewer comboViewer = new ComboViewer(container, SWT.NONE);
		
		comboViewer.setSorter(new ViewerSorter());
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof IProject) {
					IProject project = (IProject) element;
					return project.getName();
				}
				return super.getText(element);
			}
		});
		comboViewer.addSelectionChangedListener(createProjectListener());
		comboViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.setText("-- Select a Project --");
		
		treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tree.minimumHeight = 350;
		tree.setLayoutData(gd_tree);
		
		treeViewer.setContentProvider(new ITreeContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
				
			}
			
			@Override
			public boolean hasChildren(Object element) {
				if(element instanceof WorkspaceBibTexEntry){
					return true;
				}
				return false;
			}
			
			@Override
			public Object getParent(Object element) {
				return null;
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				ArrayList<WorkspaceBibTexEntry> list = (ArrayList<WorkspaceBibTexEntry>) inputElement;
				WorkspaceBibTexEntry[] array = new WorkspaceBibTexEntry[list.size()];
				array = list.toArray(array);
				return array;
			}
			
			@Override
			public Object[] getChildren(Object parentElement) {
				if(parentElement instanceof WorkspaceBibTexEntry){
					ArrayList<String> list = new ArrayList<String>();
					for(BibTeXEntry entry: ((WorkspaceBibTexEntry)parentElement).getBibEntries().values()){
						list.add(entry.getField(new Key("title")).toUserString());
					}
					String[] array = new String[list.size()];
					array = list.toArray(array);
					return array;
				}
				return null;
			}
		});
		ILabelDecorator decorator = PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator();
		treeViewer.setLabelProvider(new WorkspaceTreeLabelProvider());

		// Set Page Complete if File is selected
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection selection = ((TreeSelection)event.getSelection());
				if(selection.getFirstElement() instanceof String){
					
					TreeItem[] item = treeViewer.getTree().getSelection();
					tree.select(item[0].getParentItem());
					resourceSelected = (WorkspaceBibTexEntry)item[0].getParentItem().getData();
					setPageComplete(true);
				}
				if(selection.getFirstElement() instanceof WorkspaceBibTexEntry){
					resourceSelected = (WorkspaceBibTexEntry) selection.getFirstElement();
					setPageComplete(true);
				}
			}
		});
		
		if(resourceSelected != null) {
			ArrayList<WorkspaceBibTexEntry> wEntries = new ArrayList<WorkspaceBibTexEntry>();
			wEntries.add(resourceSelected);
			treeViewer.setInput(wEntries);
			treeViewer.expandAll();
			comboViewer.getCombo().setVisible(false);
		}
	}
	
	
	/**
	 * Creates a listener for listening the comboviewer.
	 * Sets input of TreeViewer when Project gets selected
	 * @return the listener
	 */
	private ISelectionChangedListener createProjectListener() {
		ISelectionChangedListener result = new ISelectionChangedListener() {
			private IProject lastProject = null;

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() == null || event.getSelection().isEmpty()) {
					return;
				}
				if (event.getSelection() instanceof StructuredSelection) {
					if(((StructuredSelection) event.getSelection()).getFirstElement() instanceof IProject){
						selectedProject = (IProject) ((StructuredSelection) event.getSelection()).getFirstElement();
						treeViewer.setInput(wm.getBibEntriesByProject(selectedProject));
					}
				}
			}
		};
		return result;
	}
	
	/**
	 * This methods searches for all Bib-Files.
	 * For each Bib-File a WorkspaceBibTexEntry will be created/updated.
	 * In order to do that the content of each Bib-File will be parsed
	 * with the JBibTeX Library and put into the WorkspaceBibTexEntry
	 */
	private void createWorkspaceContent(){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		for(IProject project : workspace.getRoot().getProjects()){
			try {
				for(IResource resource :project.members()){
					if(resource.getFileExtension().equals("bib")){
						URI uri = resource.getLocationURI();
						WorkspaceBibTexEntry entry = new WorkspaceBibTexEntry(uri, project);
						wm.updateFileBibTex(entry);
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	public WorkspaceBibTexEntry getResourceSelected() {
		return resourceSelected;
	}

}
