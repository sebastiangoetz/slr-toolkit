package de.tudresden.slr.ui.chart.settings.parts;

import org.eclipse.birt.chart.model.attribute.LineStyle;


public class BlockSettings {
	
	private BlockSettings() {
	}
	
	public static BlockSettings get() {
		return BLOCKSETTINGS;
	}

	private static BlockSettings BLOCKSETTINGS= new BlockSettings();

	
	private int blockBackgroundRed = 255;
	private int blockBackgroundGreen = 255;
	private int blockBackgroundBlue = 255;
	/*
	 * private Anchor chartAnchor;
	 * private Bounds chartBounds;
	private int chartColumns;
	private int chartColumnspan;
	private Insets chartInsets;
	private Size chartMinSize;
	private Size chartPrefferedSize;
	private LineAttributes chartOutlines;
	private int chartRows;
	private int chartRowspan;
	*/
	//Outline
	private boolean blockShowOutline = true;
	private LineStyle blockOutlineStyle = LineStyle.SOLID_LITERAL;
	private int blockOutlineThickness = 1;
	private int blockOutlineRed = 0;
	private int blockOutlineGreen = 0;
	private int blockOutlineBlue = 0;
	
	//Blockvariables Get + Setter
	
	public int getBlockBackgroundRed() {
		return blockBackgroundRed;
	}

	public void setBlockBackgroundRed(int chartBackgroundRed) {
		this.blockBackgroundRed = chartBackgroundRed;
	}

	public int getBlockBackgroundGreen() {
		return blockBackgroundGreen;
	}

	public void setBlockBackgroundGreen(int chartBackgroundGreen) {
		this.blockBackgroundGreen = chartBackgroundGreen;
	}

	public int getBlockBackgroundBlue() {
		return blockBackgroundBlue;
	}

	public void setBlockBackgroundBlue(int chartBackgroundBlue) {

		this.blockBackgroundBlue = chartBackgroundBlue;
	}
	public boolean isBlockShowOutline() {
		return blockShowOutline;
	}

	public void setBlockShowOutline(boolean plotShowOutline) {
		this.blockShowOutline = plotShowOutline;
	}

	public LineStyle getBlockOutlineStyle() {
		return blockOutlineStyle;
	}

	public void setBlockOutlineStyle(LineStyle plotOutlineStyle) {
		this.blockOutlineStyle = plotOutlineStyle;
	}

	public int getBlockOutlineThickness() {
		return blockOutlineThickness;
	}

	public void setBlockOutlineThickness(int plotThickness) {
		this.blockOutlineThickness = plotThickness;
	}
	

	public int getBlockOutlineRed() {
		return blockOutlineRed;
	}

	public void setBlockOutlineRed(int plotOutlineRed) {
		this.blockOutlineRed = plotOutlineRed;
	}

	public int getBlockOutlineGreen() {
		return blockOutlineGreen;
	}

	public void setBlockOutlineGreen(int plotOutlineGreen) {
		this.blockOutlineGreen = plotOutlineGreen;
	}

	public int getBlockOutlineBlue() {
		return blockOutlineBlue;
	}

	public void setBlockOutlineBlue(int plotOutlineBlue) {
		this.blockOutlineBlue = plotOutlineBlue;
	}

}
