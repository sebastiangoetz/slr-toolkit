package de.tudresden.slr.ui.chart.settings.parts;

import org.eclipse.swt.graphics.RGB;

public class PlotSettings {

	private PlotSettings() {
	}
	
	public static PlotSettings get() {
		return PLOTSETTINGS;
	}

	private static PlotSettings PLOTSETTINGS= new PlotSettings();
	

	//Background
	private RGB plotBackgroundRGB = new RGB(255,255,255);
	//Shadow
	private RGB plotShadowRGB = new RGB(255,255,255);
	//Insets
	private double plotInsetTop = 000;
	private double plotInsetLeft = 0;
	private double plotInsetBottom = 0;
	private double plotInsetRight = 0;
	//Spacing
	private int plotHorizontalSpacing = 0;
	private int plotVerticalSpacing = 0;
	
	

//Plotvariables Getter + Setter

	
	

	public RGB getPlotBackgroundRGB() {
		return plotBackgroundRGB;
	}

	public RGB getPlotShadowRGB() {
		return plotShadowRGB;
	}

	public void setPlotShadowRGB(RGB plotShadowRGB) {
		this.plotShadowRGB = plotShadowRGB;
	}

	public void setPlotBackgroundRGB(RGB plotBackgroundRGB) {
		this.plotBackgroundRGB = plotBackgroundRGB;
	}

	public double getPlotInsetTop() {
		return plotInsetTop;
	}

	public void setPlotInsetTop(double plotInsetTop) {
		this.plotInsetTop = plotInsetTop;
	}

	public double getPlotInsetLeft() {
		return plotInsetLeft;
	}

	public void setPlotInsetLeft(double plotInsetLeft) {
		this.plotInsetLeft = plotInsetLeft;
	}

	public double getPlotInsetBottom() {
		return plotInsetBottom;
	}

	public void setPlotInsetBottom(double plotInsetBottom) {
		this.plotInsetBottom = plotInsetBottom;
	}

	public double getPlotInsetRight() {
		return plotInsetRight;
	}

	public void setPlotInsetRight(double plotInsetRight) {
		this.plotInsetRight = plotInsetRight;
	}

	public int getPlotHorizontalSpacing() {
		return plotHorizontalSpacing;
	}

	public void setPlotHorizontalSpacing(int plotHorizontalSpacing) {
		this.plotHorizontalSpacing = plotHorizontalSpacing;
	}

	public int getPlotVerticalSpacing() {
		return plotVerticalSpacing;
	}

	public void setPlotVerticalSpacing(int plotVerticalSpacing) {
		this.plotVerticalSpacing = plotVerticalSpacing;
	}
	
	

	
	
}