package de.tudresden.slr.ui.chart.settings.parts;


public class PlotSettings {

	private PlotSettings() {
	}
	
	public static PlotSettings get() {
		return PLOTSETTINGS;
	}

	private static PlotSettings PLOTSETTINGS= new PlotSettings();
	

	//Background
	private int plotBackgroundRed = 255;
	private int plotBackgroundGreen = 255;
	private int plotBackgroundBlue = 255;
	//Shadow
	private int plotShadowRed = 190;
	private int plotShadowGreen = 190;
	private int plotShadowBlue = 190;
	//Insets
	private double plotInsetTop = 000;
	private double plotInsetLeft = 0;
	private double plotInsetBottom = 0;
	private double plotInsetRight = 0;
	//Spacing
	private int plotHorizontalSpacing = 0;
	private int plotVerticalSpacing = 0;
	
	

//Plotvariables Getter + Setter

	public int getPlotBackgroundRed() {
		return plotBackgroundRed;
	}
	public void setPlotBackgroundRed(int plotBackgroundRed) {
		this.plotBackgroundRed = plotBackgroundRed;
	}
	public int getPlotBackgroundGreen() {
		return plotBackgroundGreen;
	}
	public void setPlotBackgroundGreen(int plotBackgroundGreen) {
		this.plotBackgroundGreen = plotBackgroundGreen;
	}
	public int getPlotBackgroundBlue() {
		return plotBackgroundBlue;
	}
	public void setPlotBackgroundBlue(int plotBackgroundBlue) {
		this.plotBackgroundBlue = plotBackgroundBlue;
	}

	public int getPlotShadowRed() {
		return plotShadowRed;
	}

	public void setPlotShadowRed(int plotShadowRed) {
		this.plotShadowRed = plotShadowRed;
	}

	public int getPlotShadowGreen() {
		return plotShadowGreen;
	}

	public void setPlotShadowGreen(int plotShadowGreen) {
		this.plotShadowGreen = plotShadowGreen;
	}

	public int getPlotShadowBlue() {
		return plotShadowBlue;
	}

	public void setPlotShadowBlue(int plotShadowBlue) {
		this.plotShadowBlue = plotShadowBlue;
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