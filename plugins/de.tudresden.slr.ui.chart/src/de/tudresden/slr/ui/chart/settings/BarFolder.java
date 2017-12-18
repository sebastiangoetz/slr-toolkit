package de.tudresden.slr.ui.chart.settings;

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class BarFolder extends ChartFolder{
	
	protected Composite composite, composite_1, composite_2;
	protected Button btnNewButton;
	protected List list;
	protected java.util.List<RGB> colorList = new ArrayList<>();
	protected Label lblNewLabel;
	protected Button btnNewButton_1;
	
	protected Button btnRadioButton;
	protected Button btnRadioButton_1;
	protected Button btnRadioButton_2;
	
	Random random = new Random();

	@Override
	protected void build(TabFolder tabFolder) {
		composite =  new Composite(tabFolder, SWT.NONE);		
		buildGeneralItem(composite, tabFolder.getItem(0));
		
		composite_1 =  new Composite(tabFolder, SWT.NONE);		
		buildLegendItem(composite_1, tabFolder.getItem(1));
		
		composite_2 =  new Composite(tabFolder, SWT.NONE);
		buildXItem(composite_2, tabFolder.getItem(2));
		
		
		
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		
		if(e.getSource() == buttonTitleColor) {
			ColorDialog dlg = new ColorDialog(composite.getShell());
			dlg.setRGB(labelTitleColor.getBackground().getRGB());
			dlg.setText("Choose a Color");
			RGB rgb_1 = dlg.open();
			
	        if (rgb_1 != null) {
	        	colorTitle = rgb_1;
	        	labelTitleColor.setBackground(new Color(composite.getShell().getDisplay(), rgb_1));
	        }
        }
		
		if(e.getSource() == buttonBlockColor) {
			ColorDialog dlg = new ColorDialog(composite.getShell());
			dlg.setRGB(labelBlockColor.getBackground().getRGB());
			dlg.setText("Choose a Color");
			RGB rgb_2 = dlg.open();
			
	        if (rgb_2 != null) {
	        	colorBackground = rgb_2;
	        	labelBlockColor.setBackground(new Color(composite.getShell().getDisplay(), rgb_2));
	        }
		}
		
		if(e.getSource() == buttonSetColorBackground) {
			ColorDialog dlg = new ColorDialog(composite_1.getShell());
			dlg.setRGB(labelLegendColor.getBackground().getRGB());
			dlg.setText("Choose a Color");
			RGB rgb_3 = dlg.open();
			
	        if (rgb_3 != null) {
	        	colorLegend = rgb_3;
	        	labelLegendColor.setBackground(new Color(composite_1.getShell().getDisplay(), rgb_3));
	        }
		}
		
		if(e.getSource() == btnNewButton) {
			list.removeAll();
			colorList.clear();
			for(int i = 0; i < 10; i++) {
				list.add(Integer.toString(i));
				colorList.add(new RGB(255,255,255));
			}
		}
		
		if(e.getSource() == list) {
			int index = list.getSelectionIndex();
			lblNewLabel.setBackground(new Color(list.getDisplay(), colorList.get(index)));			
		}
		
		if(e.getSource() == btnNewButton_1) {
			ColorDialog dlg = new ColorDialog(btnNewButton_1.getShell());
			dlg.setText("Choose a Color");
			RGB rgb = dlg.open();
			
	        if (rgb != null) {
	        	colorList.set(list.getSelectionIndex(), rgb);	        	
	        	lblNewLabel.setBackground(new Color(btnNewButton_1.getShell().getDisplay(), rgb));
	        }
		}
		
		if(e.getSource() == btnRadioButton) {
			btnNewButton_1.setEnabled(false);
		}
		
		if(e.getSource() == btnRadioButton_1) {
			btnNewButton_1.setEnabled(true);
		}

		if(e.getSource() == btnRadioButton_2) {
			btnNewButton_1.setEnabled(false);
			for(int i = 0; i< colorList.size(); i++) {
				colorList.set(i, new RGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			}
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
	
	private void buildXItem(Composite parent, TabItem tabItem) {
		
		tabItem.setText("X Achse");
		tabItem.setControl(parent);
		
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		composite_1.setSize(365, 489);
		
		btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setText("Select Items");
		btnNewButton.addSelectionListener(this);
		
		btnRadioButton = new Button(composite_1, SWT.RADIO);		
		btnRadioButton.setText("Standard Colors");
		btnRadioButton.addSelectionListener(this);
		
		btnRadioButton_1 = new Button(composite_1, SWT.RADIO);
		btnRadioButton_1.setText("Custom Colors");
		btnRadioButton_1.setSelection(true);
		btnRadioButton_1.addSelectionListener(this);
		
		btnRadioButton_2 = new Button(composite_1, SWT.RADIO);
		btnRadioButton_2.setText("Random Colors");
		btnRadioButton_2.addSelectionListener(this);
		
		list = new List(parent, SWT.BORDER);
		GridData gd_list = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_list.heightHint = 300;
		gd_list.widthHint = 300;
		list.setLayoutData(gd_list);
		list.addSelectionListener(this);
		
		Composite composite_2 = new Composite(parent, SWT.NONE);
		composite_2.setLayout(new GridLayout(3, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		Button btnCheckButton = new Button(composite_2, SWT.CHECK);
		btnCheckButton.setText("Show");
		
		lblNewLabel = new Label(composite_2, SWT.BORDER);
		lblNewLabel.setBackground(new Color(parent.getShell().getDisplay(), new RGB(255,255,255)));
		lblNewLabel.setText("                            ");
		
		btnNewButton_1 = new Button(composite_2, SWT.NONE);
		btnNewButton_1.setText("Set Color");
		btnNewButton_1.addSelectionListener(this);
	}
}
