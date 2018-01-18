package de.tudresden.slr.ui.chart.settings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.withaxes.Grid;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.BarDataTerm;
import de.tudresden.slr.ui.chart.logic.TermSort;
import de.tudresden.slr.ui.chart.settings.parts.AxisSettings;
import de.tudresden.slr.ui.chart.settings.parts.BlockSettings;
import de.tudresden.slr.ui.chart.settings.parts.GeneralSettings;
import de.tudresden.slr.ui.chart.settings.parts.LegendSettings;
import de.tudresden.slr.ui.chart.settings.parts.PlotSettings;
import de.tudresden.slr.ui.chart.settings.parts.SeriesSettings;

public final class ChartConfiguration {
	
	private ChartConfiguration() {
	}
	public PlotSettings getPlotSettings() {
		return PlotSettings.get();
	}
	public GeneralSettings getGeneralSettings() {
		return GeneralSettings.get();
	}
	public LegendSettings getLegendSettings() {
		return LegendSettings.get();
	}
	public BlockSettings getBlockSettings() {
		return BlockSettings.get();
	}
	public AxisSettings getAxisSettings() {
		return  AxisSettings.get();
	}
	public SeriesSettings getSeriesSettings() {
		return SeriesSettings.get();
	}
	
	public static ChartConfiguration BARCHARTCONFIG = new ChartConfiguration();
	public static ChartConfiguration BUBBLECHARTCONFIG = new ChartConfiguration();
	
	private List<BarDataTerm> barTermList = new ArrayList<>();
	private Term selectedTerm = null;
	private TermSort termSort = TermSort.YEAR;
	
	
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

	public List<BarDataTerm> getBarTermList() {
		return barTermList;
	}

	public void setBarTermList(List<BarDataTerm> barTermList) {
		this.barTermList = barTermList;
		SeriesSettings.get().setSeriesColor(barTermList);
	}

	public Term getSelectedTerm() {
		return selectedTerm;
	}

	public void setSelectedTerm(Term selectedTerm) {
		this.selectedTerm = selectedTerm;
	}

	public TermSort getTermSort() {
		return termSort;
	}

	public void setTermSort(TermSort termSort) {
		this.termSort = termSort;
	}

	
	
}
