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

public class ChartConfiguration {
	
	private ChartConfiguration() {
	}
	
	public static ChartConfiguration get() {
		return CHARTCONFIGURATION;
	}

	private static ChartConfiguration CHARTCONFIGURATION= new ChartConfiguration();
	//Graph Variables
	private String chartType;
	private String chartSubType;
	private String chartDescription;
	private Interactivity chartInteractivity;
	private Orientation chartOrientation;
	private Rotation3D chartRotation;
	private double chartUnitSpacing;
	//Plot Variables
	private ColorDefinition plotBackground;
	private int horizontalSpacing;
	private int verticalSpacing;
	//Block Variables 
	private Anchor chartAnchor;
	private Fill chartBackground;
	private Bounds chartBounds;
	private int chartColumns;
	private int chartColumnspan;
	private Insets chartInsets;
	private Size chartMinSize;
	private Size chartPrefferedSize;
	private LineAttributes chartOutlines;
	private int chartRows;
	private int chartRowspan;
	//Title Variables
	String chartTitle;
	//Legend Variables
	private ClientArea legendArea;
	private Direction legendDirection;
	private int legendEllipsis;
	private int legendHorizontalSpacing;
	private int legendVerticalSpacing;
	private LegendItemType legendItemType;
	private double legendMaxPercent;
	private Orientation legendOrientation;
	private Position legendPosition;
	private LineAttributes legendSeparator;
	private Text legendText;
	private Label legendTitle;
	private double legendTitleMaxPercent;
	private double legendWrappingSize;
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
