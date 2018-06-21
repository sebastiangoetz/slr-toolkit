package de.tudresden.slr.metainformation.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.*;

import de.tudresden.slr.metainformation.data.SlrProjectMetainformation_NONXML;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import javax.inject.Inject;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class MetainformationView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.tudresden.slr.metainformation.views.MetainformationView";

	@Inject
	IWorkbench workbench;

	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;

	private FormToolkit toolkit;
	private ScrolledForm form;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			// return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
			return null;
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		// GridLayout layout = new GridLayout(2, false);
		// parent.setLayout(layout);
		//
		// Label authorsLabel = new Label(parent, SWT.NONE);
		// authorsLabel.setText("Authors: ");
		//
		// viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//
		// viewer.setContentProvider(ArrayContentProvider.getInstance());
		// viewer.setInput(new String[] { "One", "Two", "Three" });
		// viewer.setLabelProvider(new ViewLabelProvider());
		//
		// // Create the help context id for the viewer's control
		//// workbench.getHelpSystem().setHelp(viewer.getControl(),
		// "de.tudresden.slr.metainformation.viewer");
		// getSite().setSelectionProvider(viewer);
		// makeActions();
		// hookContextMenu();
		// hookDoubleClickAction();
		// contributeToActionBars();

		// SlrProjectMetainformation data = new SlrProjectMetainformation();

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		// form.setText("Hello, Eclipse Forms");

		GridLayout layout = new GridLayout();
		form.getBody().setLayout(layout);

		// Hyperlink link = toolkit.createHyperlink(form.getBody(),
		// "Click here.", SWT.WRAP);
		// link.addHyperlinkListener(new HyperlinkAdapter() {
		// public void linkActivated(HyperlinkEvent e) {
		// System.out.println("Link activated!");
		// }
		// });

		firePropertyChange(IEditorPart.PROP_DIRTY);

		layout.numColumns = 2;
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		// link.setLayoutData(gd);

		Label label = new Label(form.getBody(), SWT.NULL);
		label.setText("Title");
		Text text = new Text(form.getBody(), SWT.BORDER | SWT.H_SCROLL);
		text.insert("Title of the paper");
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		text.setBounds(100, 100, 100, 100);

		Label label2 = new Label(form.getBody(), SWT.NULL);
		label2.setText("Authors");
		Text text2 = new Text(form.getBody(), SWT.BORDER | SWT.H_SCROLL);
		text2.insert("John Doe (University of XYZ), Maximiliane Musterfrau (Technische Universität Dresden");
		text2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label3 = new Label(form.getBody(), SWT.NULL);
		label3.setText("Keywords");
		Text text3 = new Text(form.getBody(), SWT.BORDER | SWT.H_SCROLL);
		text3.insert("a, b, c, d");
		text3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label4 = new Label(form.getBody(), SWT.NULL);
		label4.setText("Abstract");
		Text text4 = new Text(form.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		text4.insert("Abstract of the paper.");
		text4.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label5 = new Label(form.getBody(), SWT.NULL);
		label5.setText("Description of\nthe Taxonomy");
		Text text5 = new Text(form.getBody(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		text5.insert("Description of the taxonomy of the project.");
		text5.setLayoutData(new GridData(GridData.FILL_BOTH));


		text5.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				// Get the widget whose text was modified
				Text text = (Text) event.widget;
				System.out.println(text.getText());
			}
		});

		// Button button = new Button(form.getBody(), SWT.CHECK);
		// button.setText("An example of a checkbox in a form");
		//
		// gd = new GridData();
		// gd.horizontalSpan = 2;
		// button.setLayoutData(gd);

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				MetainformationView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		// action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
		// getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		// action2.setImageDescriptor(workbench.getSharedImages().
		// getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				Object obj = selection.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Metainformation", message);
	}

	@Override
	public void setFocus() {
		// viewer.getControl().setFocus();
	}
}
