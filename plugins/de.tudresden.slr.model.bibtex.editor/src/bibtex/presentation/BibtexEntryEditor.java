package bibtex.presentation;

import java.io.StringWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.ide.IDE;

import bibtex.impl.DocumentImpl;
import bibtex.presentation.serialization.DocumentStorageEditorInput;
import bibtex.provider.BibtexItemProviderAdapterFactory;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class BibtexEntryEditor extends MultiPageEditorPart implements
		IResourceChangeListener {

	/** The text editor used in page 0. */
	private TextEditor editor;

	/** The font chosen in page 1. */
	private Font font;

	/** The text widget used in page 2. */
	private StyledText text;

	private DocumentImpl document;

	private ComposedAdapterFactory adapterFactory;

	/**
	 * Creates a multi-page editor example.
	 */
	public BibtexEntryEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory
				.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new BibtexItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
	}

	private static String getString(String key) {
		return BibtexEditorPlugin.INSTANCE.getString(key);
	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	void createPage0() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = true;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		composite.setLayout(layout);

		// Title
		Label label = new Label(composite, SWT.CENTER);
		label.setText(document.getTitle());
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(
				label.getFont()).setStyle(SWT.BOLD);
		Font boldFont = boldDescriptor.createFont(label.getDisplay());
		label.setFont(boldFont);
		label.setLayoutData(gridData);

		// Authors
		label = new Label(composite, SWT.CENTER);
		label.setText(document.getAuthors().get(0));
		label.setLayoutData(gridData);

		// Date
		label = new Label(composite, SWT.CENTER);
		String labelText = "";
		if (document.getMonth() != null) {
			labelText += document.getMonth() + " ";
		}
		if (document.getYear() != null) {
			labelText += document.getYear();
		}
		label.setText(labelText.trim());
		FontDescriptor italicDescriptor = FontDescriptor.createFrom(
				label.getFont()).setStyle(SWT.ITALIC);
		Font italicFont = italicDescriptor.createFont(label.getDisplay());
		label.setFont(italicFont);
		label.setLayoutData(gridData);

		// Abstract
		text = new StyledText(composite, SWT.V_SCROLL
				| SWT.READ_ONLY | SWT.WRAP);
		text.setEditable(false);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		text.setLayoutData(gridData);
		if (document.getAbstract() != null) {
			text.setText(document.getAbstract());
		}

		int index = addPage(composite);
		setPageText(index, getString("_UI_Bibtex_Abstract"));
	}

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the
	 * font used in page 2.
	 */
	void createPropertyPage() {
		PropertySheetPage property = new PropertySheetPage();
		property.createControl(getContainer());
		property.setPropertySourceProvider(new AdapterFactoryContentProvider(
				adapterFactory));
		int index = addPage(property.getControl());
		setPageText(index, "Properties");
		property.selectionChanged(BibtexEntryEditor.this,
				new IStructuredSelection() {

					@Override
					public boolean isEmpty() {
						return false;
					}

					@Override
					public List<DocumentImpl> toList() {
						List<DocumentImpl> list = new ArrayList<DocumentImpl>(1);
						list.add(document);
						return list;
					}

					@Override
					public Object[] toArray() {
						return toList().toArray();
					}

					@Override
					public int size() {
						return toList().size();
					}

					@Override
					public Iterator<DocumentImpl> iterator() {
						return toList().iterator();
					}

					@Override
					public Object getFirstElement() {
						return toList().get(0);
					}
				});
	}

	/**
	 * Creates page 2 of the multi-page editor, which shows the sorted text.
	 */
	void createWebpage() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		Browser browser = new Browser(composite, SWT.NONE);
		String url ="";
		if (document.getUrl()!= null){
			url = document.getUrl();
		} else if (document.getDoi() != null){
			url = "http://doi.org/"+document.getDoi();
		} else {
			return;
		}
		browser.setUrl(url);
		int index = addPage(composite);
		setPageText(index, "Webpage");
	}

	/**
	 * Creates page 3 of the multi-page editor, which shows the sorted text.
	 */
	void createPdfPage() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);

		int index = addPage(composite);
		setPageText(index, "PDF");
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		// TODO: create abstract view
		createPage0();
		// TODO: create webpage
		createPropertyPage();
		if (document.getUrl()!= null || document.getDoi()!= null){
			createWebpage();
		}
		if(document.getAbstract() != null){
			createPdfPage();
		}
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		if (!(editorInput instanceof DocumentStorageEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be DocumentStorageEditorInput");
		super.init(site, editorInput);
		this.setPartName(editorInput.getName());
		try {
			this.document = ((DocumentStorageEditorInput) editorInput)
					.getStorage().getDocument();
		} catch (CoreException e) {
			throw new PartInitException(
					"Invalid Input: Cannot find DocumentImpl object", e);
		}
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		if (newPageIndex == 3) return;
		super.pageChange(newPageIndex);
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow()
							.getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput())
								.getFile().getProject()
								.equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor
									.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	/**
	 * Sets the font related data to be applied to the text in page 2.
	 */
	void setFont() {
		FontDialog fontDialog = new FontDialog(getSite().getShell());
		fontDialog.setFontList(text.getFont().getFontData());
		FontData fontData = fontDialog.open();
		if (fontData != null) {
			if (font != null)
				font.dispose();
			font = new Font(text.getDisplay(), fontData);
			text.setFont(font);
		}
	}

	/**
	 * Sorts the words in page 0, and shows them in page 2.
	 */
	void sortWords() {

		String editorText = editor.getDocumentProvider()
				.getDocument(editor.getEditorInput()).get();

		StringTokenizer tokenizer = new StringTokenizer(editorText,
				" \t\n\r\f!@#\u0024%^&*()-_=+`~[]{};:'\",.<>/?|\\");
		ArrayList editorWords = new ArrayList();
		while (tokenizer.hasMoreTokens()) {
			editorWords.add(tokenizer.nextToken());
		}

		Collections.sort(editorWords, Collator.getInstance());
		StringWriter displayText = new StringWriter();
		for (int i = 0; i < editorWords.size(); i++) {
			displayText.write(((String) editorWords.get(i)));
			displayText.write(System.getProperty("line.separator"));
		}
		text.setText(displayText.toString());
	}
}
