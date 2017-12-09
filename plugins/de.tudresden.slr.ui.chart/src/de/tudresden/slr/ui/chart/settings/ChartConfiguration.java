package de.tudresden.slr.ui.chart.settings;

import org.eclipse.birt.chart.computation.withaxes.Grid;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Rotation3D;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import de.tudresden.slr.ui.chart.settings.parts.BlockSettings;
import de.tudresden.slr.ui.chart.settings.parts.ChartSettings;
import de.tudresden.slr.ui.chart.settings.parts.LegendSettings;
import de.tudresden.slr.ui.chart.settings.parts.PlotSettings;

public class ChartConfiguration {
	
	private ChartConfiguration() {
	}
	
	public static ChartConfiguration get() {
		return CHARTCONFIGURATION;
	}
	public static PlotSettings getPlotSettings() {
		return ps;
	}
	public static ChartSettings getGraphSettings() {
		return gs;
	}
	public static LegendSettings getLegendSettings() {
		return ls;
	}
	public static BlockSettings getBlockSettings() {
		return bs;
	}

	static ChartConfiguration CHARTCONFIGURATION= new ChartConfiguration();
	static PlotSettings ps = PlotSettings.get();
	static ChartSettings gs = ChartSettings.get();
	static LegendSettings ls = LegendSettings.get();
	static BlockSettings bs = BlockSettings.get();
 
	
	//Graph Variables

	//Plot Variables

	
	//Block Variables 
	
	//Title Variables
	
	//Legend Variables
	
	//X-Axis Variables
	private int xAxisMaxPercent;
	private double xAxisGapWidth;
	private int xAxisInterval;
	private Label xAxisLabel;
	private Position xAxisLabelPosition;
	private double xAxisLabelSpan;
	private Grid xAxisMajorGrid;
	private Grid xAxisMinorGrid;
	private EList<MarkerLine> xAxisMarkerLines;
	private EList<MarkerRange> xAxisMarkerRange;
	private Orientation xAxisOrientation;
	private AxisOrigin xAxisOrigin;
	private Scale xAxisScale;
	private Label xAxisTitle;
	private Label xAxisSubTitle;
	private Position xAxisPosition;
	private AxisType xAxisType;
	//Y-Axis Variables
	private int yAxisMaxPercent;
	private double yAxisGapWidth;
	private int yAxisInterval;
	private Label yAxisLabel;
	private Position yAxisLabelPosition;
	private double yAxisLabelSpan;
	private Grid yAxisMajorGrid;
	private Grid yAxisMinorGrid;
	private EList<MarkerLine> yAxisMarkerLines;
	private EList<MarkerRange> yAxisMarkerRange;
	private Orientation yAxisOrientation;
	private AxisOrigin yAxisOrigin;
	private Scale yAxisScale;
	private Label yAxisTitle;
	private Label yAxisSubTitle;
	private Position yAxisPosition;
	//Series Variables
	private CurveFitting seriesCurveFitting;
	private EList<Query> seriesDataDefinition;
	private DataPoint seriesDataPoint;
	private EMap<java.lang.String,DataSet> seriesDataSet;
	private int seriesDataDefinitionIndex;
	
	public int getSeriesDataDefinitionIndex() {
		return seriesDataDefinitionIndex;
	}

	public void setSeriesDataDefinitionIndex(int seriesDataDefinitionIndex) {
		this.seriesDataDefinitionIndex = seriesDataDefinitionIndex;
	}

	private Label seriesLabel;
	private Position seriesPosition;
	
	
	
	
	
	
	
	

	
}
