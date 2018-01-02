package de.tudresden.slr.ui.chart.settings.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.swt.graphics.RGB;

import de.tudresden.slr.ui.chart.logic.BarDataTerm;

public class SeriesSettings {
	
	private SeriesSettings() {
	}
	
	public static SeriesSettings get() {
		return SERIESSETTINGS;
	}

	private static SeriesSettings SERIESSETTINGS= new SeriesSettings();

	
	
	//Y-Series
	private boolean seriesTranslucent = false;
	private boolean seriesStacked = false; //Als Stacked rendern
	private boolean seriesShowLabels = true; //Zeigt Labels der Balken a
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

	public void setSeriesColor(List<BarDataTerm> barTermList) {
		ArrayList<Fill> fillList = new ArrayList<>();
		for(BarDataTerm entry: barTermList) {
			if(entry.isDisplayed()) {
				RGB series = entry.getRgb();
				fillList.add(ColorDefinitionImpl.create(series.red, series.green, series.blue));				
				
			}
		}
		this.seriesColor = fillList;


	}

	public boolean isSeriesUseCustomColors() {
		return seriesUseCustomColors;
	}

	public void setSeriesUseCustomColors(boolean seriesUseCustomColors) {
		this.seriesUseCustomColors = seriesUseCustomColors;
	
	}
	
	
}
