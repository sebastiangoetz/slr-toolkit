package de.tudresden.slr.ui.chart.settings.pages;

import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class PageSupport {

	
	protected static RGB openAndGetColor(Composite parent, Label label) {
		
		ColorDialog dlg = new ColorDialog(parent.getShell());
		dlg.setRGB(label.getBackground().getRGB());
		dlg.setText("Choose a Color");
		RGB rgb = dlg.open();
        label.setBackground(new Color(parent.getShell().getDisplay(), rgb));
        
		return rgb;
	}
	
	protected static Color getColor(Composite parent, int color) {
		switch (color) {
		case 0:
			return new Color(parent.getShell().getDisplay(), new RGB(255,255,255));
		case 1:
			return new Color(parent.getShell().getDisplay(), new RGB(245,245,245));
		default:
			return null;
		}
	}
	
	protected static LineStyle getLineStyle(int style) {
		switch (style) {
		case 0:
			return null;
		
		case 1:
			return LineStyle.DOTTED_LITERAL;
		
		case 2:
			return LineStyle.DASH_DOTTED_LITERAL;
		
		case 3:
			return LineStyle.DASHED_LITERAL;		
		
		case 4:
			return LineStyle.SOLID_LITERAL;
			
		}
		return null;
	}
	
	protected static Position getPosition(int i) {
		switch(i) {
		case 0:
			return Position.RIGHT_LITERAL;
		case 1:
			return Position.LEFT_LITERAL;
		case 2:
			return Position.BELOW_LITERAL;
		case 3:
			return Position.ABOVE_LITERAL;
		}
		return null;
	}
}
