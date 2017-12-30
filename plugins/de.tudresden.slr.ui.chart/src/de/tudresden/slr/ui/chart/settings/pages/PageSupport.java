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
	
	protected static int setFontSize(int size) {
		switch (size){
		case 12: return 0;
		case 14: return 1;
		case 16: return 2;
		case 18: return 3;
		case 20: return 4;
		case 22: return 5;
		case 24: return 6;
		case 26: return 7;
		case 28: return 8;
		case 36: return 9;
		case 48: return 10;
		case 72: return 11;
		default: return -1;
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
		default:
			return null;
		}		
	}
	
	protected static int setLineStyle(LineStyle style) {
		if(style == null)
			return 0;
		
		switch (style) {		
		case DOTTED_LITERAL:
			return 1;		
		case DASH_DOTTED_LITERAL:
			return 2;		
		case DASHED_LITERAL:
			return 3;		
		case SOLID_LITERAL:
			return 4;
		default:
			return -1;
		}		
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
		default:
			return null;
		}
	}
	
	protected static int setPosition(Position position) {
		switch(position) {
		case RIGHT_LITERAL:
			return 0;
		case LEFT_LITERAL:
			return 1;
		case BELOW_LITERAL:
			return 2;
		case ABOVE_LITERAL:
			return 3;
		default:
			return -1;
		}
	}
}
