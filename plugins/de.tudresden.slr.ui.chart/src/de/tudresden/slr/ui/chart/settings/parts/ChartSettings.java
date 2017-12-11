package de.tudresden.slr.ui.chart.settings.parts;

import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Rotation3D;

public class ChartSettings {
	
	private ChartSettings() {
	}
	
	public static ChartSettings get() {
		return GRAPHSETTINGS;
	}

	private static ChartSettings GRAPHSETTINGS= new ChartSettings();

	private String chartType = "Bar Chart";
	private String chartSubType = "Side-By-Side";
	private String chartDescription;
	private Interactivity chartInteractivity;
	private Orientation chartOrientation;
	private Rotation3D chartRotation;
	private double chartUnitSpacing;
	private String chartTitle = "LoLoLo";
	
	//Graphvariables Getter + Setter
	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getChartSubType() {
		return chartSubType;
	}

	public void setChartSubType(String chartSubType) {
		this.chartSubType = chartSubType;
	}
	//Titlevariables Getter + Setter
	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}
}
