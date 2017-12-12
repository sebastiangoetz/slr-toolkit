package de.tudresden.slr.ui.chart.settings.parts;

public class AxisSettings {
	
	private AxisSettings() {
	}
	
	public static AxisSettings get() {
		return AXISSETTINGS;
	}

	private static AxisSettings AXISSETTINGS= new AxisSettings();

	private float axisFontSize = 12;
	private int xAxisRotation = 45;
	
	public float getAxisFontSize() {
		return axisFontSize;
	}

	public void setAxisFontSize(float axisFontSize) {
		this.axisFontSize = axisFontSize;
	}

	public int getxAxisRotation() {
		return xAxisRotation;
	}

	public void setxAxisRotation(int xAxisRotation) {
		this.xAxisRotation = xAxisRotation;
	}
	
	
}
