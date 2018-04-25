package de.tudresden.slr.ui.chart.logic;

import java.util.Random;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;

import de.tudresden.slr.model.taxonomy.Term;

public class BubbleDataTerm {
	
	
	private static Random random;
	static {
		random = new Random();
	}
	
	private Term term;
	private RGB rgb;
	private boolean displayed = true;
	
	
	public BubbleDataTerm (Term term) {
		this.term = term;
		this.rgb = new RGB(random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}
	
	
	
	public Term getTerm() {
		return term;
	}
	public void setTerm(Term term) {
		this.term = term;
	}
	public RGB getRGB() {
		return rgb;
	}
	public void setRGB(RGB color) {
		this.rgb = color;
	}
	public boolean isDisplayed() {
		return displayed;
	}
	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}
	public Color getColor(Device device) {
		return new Color(device, rgb);
	}
	public void setRGBRandom() {
		this.rgb = new RGB(random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}

}
