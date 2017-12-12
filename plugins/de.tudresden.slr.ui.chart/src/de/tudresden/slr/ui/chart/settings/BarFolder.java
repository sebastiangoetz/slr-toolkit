package de.tudresden.slr.ui.chart.settings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

public class BarFolder extends ChartFolder{

	@Override
	protected void build(TabFolder tabFolder) {
		buildGeneralItem(new Composite(tabFolder, SWT.NONE), tabFolder.getItem(0));
		
	}

}
