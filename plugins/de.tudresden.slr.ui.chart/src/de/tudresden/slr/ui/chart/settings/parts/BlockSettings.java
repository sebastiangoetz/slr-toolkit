package de.tudresden.slr.ui.chart.settings.parts;

import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.swt.graphics.RGB;


public class BlockSettings {
	
	public BlockSettings() {
	}
	
	public static BlockSettings get() {
		return BLOCKSETTINGS;
	}

	private static BlockSettings BLOCKSETTINGS= new BlockSettings();

	private RGB blockBackgroundRGB = new RGB(255,255,255);

	//Outline
	private boolean blockShowOutline = true;
	private LineStyle blockOutlineStyle = LineStyle.SOLID_LITERAL;
	private int blockOutlineThickness = 3;
	private RGB blockOutlineRGB = new RGB(0,0,0);

	
	//Blockvariables Get + Setter
	
	
	public boolean isBlockShowOutline() {
		return blockShowOutline;
	}

	public RGB getBlockBackgroundRGB() {
		return blockBackgroundRGB;
	}

	public void setBlockBackgroundRGB(RGB blockBackgroundRGB) {
		this.blockBackgroundRGB = blockBackgroundRGB;
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

	public RGB getBlockOutlineRGB() {
		return blockOutlineRGB;
	}

	public void setBlockOutlineRGB(RGB blockOutlineRGB) {
		this.blockOutlineRGB = blockOutlineRGB;
	}
	
	


}
