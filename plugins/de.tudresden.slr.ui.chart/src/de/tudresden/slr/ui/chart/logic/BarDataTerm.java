package de.tudresden.slr.ui.chart.logic;

import java.util.Random;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;

public class BarDataTerm {
	
	private static Random random;
	
	static {
		random = new Random();
	}

	private RGB rgb;	
	private boolean displayed  = true;
	private String term;
	private int size = 0;
	
	public BarDataTerm(String term, int size) {
		this.term = term;
		this.size = size;
		this.rgb = new RGB(random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}

	public RGB getRgb() {
		return rgb;
	}

	public void setRgb(RGB rgb) {
		this.rgb = rgb;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}

	public String getTerm() {
		return term;
	}

	public int getSize() {
		return size;
	}
	
	public Color getColor(Device device) {
		return new Color(device, rgb);
	}
	
	public void setRGBRandom() {
		this.rgb = new RGB(random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}

}
