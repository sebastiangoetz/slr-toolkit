package de.tudresden.slr.model.bibtex.ui.presentation;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;

/** This View shows a filtered list of Bibtex Entries for the currently selected term in the taxonomy.
  * Additionally, an export action for the keys of the currently shown list of documents is provided.
  *
  * @author Sebastian Götz 
  */
public class BibtexSearchView extends ViewPart {
	
	public static final String ID = "de.tudresden.slr.model.bibtex.ui.presentation.BibtexSearchView";
	
	protected AdapterFactoryEditingDomain editingDomain;
	
	protected TableViewer viewer;
	protected Action exportKeysAction;
	
	/***
	 * This listener handles the reaction to the selection of an element in the TaxonomyView
	 */
	ISelectionListener listener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection sel) {
			if (!(sel instanceof IStructuredSelection)) {
				return;
			}
			IStructuredSelection ss = (IStructuredSelection) sel;
			Object o = ss.getFirstElement();

			if (o instanceof Term && ss.size() == 1) {
				viewer.setInput(getSearchResult((Term)o));
				viewer.refresh();
			}
		}
	};

	

	public BibtexSearchView() {
		//initialize editing domain (to load the bibtex entries)
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent((domain) -> editingDomain = domain);		
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent);
		
		//add listener to react to selections in the taxonomy view
		getSite().getPage().addSelectionListener(listener);
		
		//create columns: key, title and authors		
		TableViewerColumn colKey = createColumn(viewer,100,"Key");
		colKey.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        Document d = (Document) element;
		        return d.getKey();
		    }
		});
		TableViewerColumn colAuthors = createColumn(viewer,250,"Authors");
		colAuthors.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        Document d = (Document) element;
		        return d.getAuthors().toString();
		    }
		});
		TableViewerColumn colTitle = createColumn(viewer,250,"Title");
		colTitle.setLabelProvider(new ColumnLabelProvider() {
		    @Override
		    public String getText(Object element) {
		        Document d = (Document) element;
		        return d.getTitle();
		    }
		});
		
		//content is provided as simple list
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		
		//set input
		viewer.setInput(getSearchResult(null));
		
		//set header and lines visible
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		viewer.refresh();
		
		makeActions();
	}

	private void makeActions() {
		exportKeysAction = new Action() {
			@Override
			public void run() {
				List<Document> docs = (List<Document>)viewer.getInput();
				String ret = "";
				for(Document d : docs) {
					ret += d.getKey()+",";
				}
				ret = ret.substring(0,ret.length()-1);
				
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection s = new StringSelection(ret);
				clipboard.setContents(s, null);
				System.out.println(ret);
			}
		};
		exportKeysAction.setText("Copy Keys");
		exportKeysAction.setToolTipText("Copy Bibtex Keys to Clipboard");
		exportKeysAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(exportKeysAction);
	}

	private TableViewerColumn createColumn(TableViewer v, int width, String name) {
		TableViewerColumn col = new TableViewerColumn(v, SWT.NONE);
		col.getColumn().setWidth(width);
		col.getColumn().setText(name);
		return col;
	}
	
	private List<Document> getSearchResult(Term term) {
		List<Document> filteredInput = new ArrayList<>();
		for(Resource r : editingDomain.getResourceSet().getResources()) {
			for(EObject o : r.getContents()) {
				Document d = (Document)o;
				//if no term is given, no filtering shall take place
				if(term == null) {
					filteredInput.add(d);
				} else {
					if(SearchUtils.findTermInDocument(d, term) != null) {
						filteredInput.add(d);
					}
				}
			}
		}
		return filteredInput;
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();	
	}

	@Override
	public void dispose() {
		super.dispose();
		getSite().getPage().removeSelectionListener(listener);
	}
}
