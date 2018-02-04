package de.tudresden.slr.ui.chart.settings.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.swt.graphics.RGB;

import de.tudresden.slr.ui.chart.logic.BarDataTerm;
import de.tudresden.slr.ui.chart.logic.PieDataTerm;

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
	private boolean seriesUseCustomColors = true; //false=no Custom Color, true=custom Colors
	private ArrayList<Fill> seriesColor = new ArrayList<Fill>();
	private int seriesExplosion = 5;
	private Position seriesLabelPosition = Position.OUTSIDE_LITERAL;
	
	


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
	public void setPieSeriesColor(List<PieDataTerm> pieTermList) {
		ArrayList<Fill> fillList = new ArrayList<>();
		for(PieDataTerm entry: pieTermList) {
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

	public int getSeriesExplosion() {
		return seriesExplosion;
	}

	public void setSeriesExplosion(int seriesExplosion) {
		this.seriesExplosion = seriesExplosion;
	}

	public Position getSeriesLabelPosition() {
		return seriesLabelPosition;
	}

	public void setSeriesLabelPosition(Position seriesLabelPosition) {
		this.seriesLabelPosition = seriesLabelPosition;
	}
	
	
}
