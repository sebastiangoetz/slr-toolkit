package de.tudresden.slr.googlescholar;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class WizardGSImportPage extends WizardPage {
	
	private Composite container;
	
	public Text as_q, as_epq, as_oq, as_eq, as_sauthors, as_publication, as_ylo, as_yhi;
	private String as_occt;

	public WizardGSImportPage() {
		super("Import from Google Scholar");
		
		setTitle("Google Scholar Import Wizard");
		setDescription("Define your Google Scholar Search");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		
		Label label1 = new Label(container, SWT.NONE);
		label1.setText("mit allen Wörtern");
		
		as_q = new Text(container, SWT.BORDER | SWT.SINGLE);
		as_q.setText("");
		as_q.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label2 = new Label(container, SWT.NONE);
		label2.setText("mit ger genauen Worgruppe");
		
		as_epq = new Text(container, SWT.BORDER | SWT.SINGLE);
		as_epq.setText("");
		as_epq.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label3 = new Label(container, SWT.NONE);
		label3.setText("mit irgendeinem der Wörter");
		
		as_oq = new Text(container, SWT.BORDER | SWT.SINGLE);
		as_oq.setText("");
		as_oq.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label4 = new Label(container, SWT.NONE);
		label4.setText("ohne die Wörter");
		
		as_eq = new Text(container, SWT.BORDER | SWT.SINGLE);
		as_eq.setText("");
		as_eq.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label5 = new Label(container, SWT.NONE);
		label5.setText("die meine Wörter enthalten");
		
		Group occt = new Group(container, SWT.NONE);
		GridLayout occt_layout = new GridLayout();
		occt_layout.numColumns = 1;
		occt.setLayout(occt_layout);
		occt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		SelectionListener occt_selectionListener = new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				Button button = ((Button) event.widget);
				as_occt = (String) button.getData("value");
			};
		};
		Button occt_any = new Button(occt, SWT.RADIO);
		occt_any.setText("irgendwo im Artikel");
		occt_any.addSelectionListener(occt_selectionListener);
		occt_any.setData("value", "any");
		
		Button occt_title = new Button(occt, SWT.RADIO);
		occt_title.setText("im Titel des Artikels");
		occt_title.addSelectionListener(occt_selectionListener);
		occt_any.setData("value", "title");
		
		Label label6 = new Label(container, SWT.NONE);
		label6.setText("Artikel zurückgeben, die von folgendem Autor verfasst wurden");
		
		as_sauthors = new Text(container, SWT.BORDER | SWT.SINGLE);
		as_sauthors.setText("");
		as_sauthors.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label7 = new Label(container, SWT.NONE);
		label7.setText("Artikel zurückgeben, die hier veröffentlicht wurden");
		
		as_publication = new Text(container, SWT.BORDER | SWT.SINGLE);
		as_publication.setText("");
		as_publication.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label8 = new Label(container, SWT.NONE);
		label8.setText("Artikel zurückgeben, die in folgendem Zeitraum geschrieben wurden");
		
		Composite years = new Composite(container, SWT.NONE);
		years.setLayout(new GridLayout(3, false));
		
		as_ylo = new Text(years, SWT.BORDER | SWT.SINGLE);
		as_ylo.setText("");
		
		Label label9 = new Label(years, SWT.NONE);
		label9.setText("--");
		
		as_yhi = new Text(years, SWT.BORDER | SWT.SINGLE);
		as_yhi.setText("");
		
		setControl(container);
		setPageComplete(true);
	}
	
	public String getOcct() {
		return this.as_occt;
	}

}
