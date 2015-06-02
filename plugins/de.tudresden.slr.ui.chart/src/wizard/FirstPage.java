package wizard;

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
import org.eclipse.swt.widgets.Text;

public class FirstPage extends WizardPage {
	private Text text1;
	private Composite container;
	private Button button2;
	private Button button1;

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

	}

	public Button getButton2() {
		return button2;
	}

	public Button getButton1() {
		return button1;
	}

}
