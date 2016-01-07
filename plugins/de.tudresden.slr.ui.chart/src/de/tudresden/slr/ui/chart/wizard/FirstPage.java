package de.tudresden.slr.ui.chart.wizard;

import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexResourceFactoryImpl;
import de.tudresden.slr.model.taxonomy.Term;

public class FirstPage extends WizardPage {
	private Composite container;
	private Button button2;
	private Button button1;

	private ResourceSet resourceSet;
	private Resource resource;

	public FirstPage() {
		super("First Page");
		setTitle("First Page");
		setDescription("Chart Wizard: First page");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		Label label1 = new Label(container, SWT.NONE);
		label1.setText("Which type of Diagram do you want?");

		Group buttonGroup = new Group(container, SWT.NONE);
		buttonGroup.setLayout(layout);
		// buttonGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		SelectionListener selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Button button = ((Button) event.widget);
				if (button.getSelection() == true)
					setPageComplete(true);
			};
		};

		button1 = new Button(buttonGroup, SWT.RADIO);
		button1.setText("BarChart");
		button1.addSelectionListener(selectionListener);
		button2 = new Button(buttonGroup, SWT.RADIO);
		button2.setText("BubbleChart");
		button2.addSelectionListener(selectionListener);

		// required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
		try {
			setUpExampleBibtex();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Button getButton2() {
		return button2;
	}

	public Button getButton1() {
		return button1;
	}

	private void setUpExampleBibtex() throws Exception {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"bib", new BibtexResourceFactoryImpl());

		final String bib = "@INPROCEEDINGS{Test01, classes = {test}}";
		resourceSet = new ResourceSetImpl();
		resource = resourceSet.createResource(URI.createURI("test.bib"));
		resource.load(new URIConverter.ReadableInputStream(bib, "UTF-8"),
				Collections.EMPTY_MAP);

		Document document = (Document) resource.getContents().get(0);
		Term term = document.getTaxonomy().getDimensions().get(0);
	}

}
