package de.tudresden.slr.ui.chart.settings.parts;

import java.util.ArrayList;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;

public class SeriesSettings {
	
	private SeriesSettings() {
	}
	
	public static SeriesSettings get() {
		return SERIESSETTINGS;
	}

	private static SeriesSettings SERIESSETTINGS= new SeriesSettings();

	
	
	//Y-Series
	private boolean seriesTranslucent = false;
	private boolean seriesStacked = false; //Alle Werte abbilden
	private boolean seriesShowLabels = true; //Zeigt Labels der Balken an
	private boolean seriesUseCustomColors = true; //false=no Custom Color, true=custom Colors
	private ArrayList<Fill> seriesColor = new ArrayList<Fill>();
	
	


	public boolean isSeriesTranslucent() {
		return seriesTranslucent;
	}

	public void setSeriesTranslucent(boolean seriesTranslucent) {
		this.seriesTranslucent = seriesTranslucent;
	}

	public boolean isSeriesStacked() {
		return seriesStacked;
	}

	public void setSeriesStacked(boolean seriesStacked) {
		this.seriesStacked = seriesStacked;
	}

	public boolean isSeriesShowLabels() {
		return seriesShowLabels;
	}

	public void setSeriesShowLabels(boolean seriesShowLabels) {
		this.seriesShowLabels = seriesShowLabels;
	}

	public ArrayList<Fill> getSeriesColor() {
		return seriesColor;
	}

	public void setSeriesColor(ArrayList<Fill> seriesColor) {
		/*
		col1.add(ColorDefinitionImpl.create(0, 0, 255));
		col1.add(ColorDefinitionImpl.create(0, 255, 0));
		col1.add(ColorDefinitionImpl.create(255, 0, 0));
		 */
		this.seriesColor = seriesColor;
	}

	public boolean isSeriesUseCustomColors() {
		return seriesUseCustomColors;
	}

	public void setSeriesUseCustomColors(boolean seriesUseCustomColors) {
		this.seriesUseCustomColors = seriesUseCustomColors;
	
	}
	
	
}
