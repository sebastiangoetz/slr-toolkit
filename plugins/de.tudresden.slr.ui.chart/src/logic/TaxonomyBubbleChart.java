package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.birt.chart.extension.datafeed.BubbleEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.BubbleDataSet;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.BubbleDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;

import de.tudresden.slr.model.taxonomy.Term;

public class TaxonomyBubbleChart {

	private Map<Term, Integer> scriptMappings;
	Map<Term, List<BubbleDataContainer>> ySeriesMap;

	public TaxonomyBubbleChart() {

	}

	/**
	 * 
	 * @param dataContainer
	 *            TODO
	 * @return a new TaxonomyBubbleChart
	 */

	private String createScriptString(Map<Term, Integer> scriptMappings) {
		// This affects the size of the bubbles, have to try this out

		String bubbleSize = "function afterComputations( chart, plotComputation ) "
				+ "{ importPackage(Packages.java.lang); var xAxis = chart.getAxes().get(0);"
				+ "var yAxis = xAxis.getAssociatedAxes().get(0);"
				+ "var xSerieDef = xAxis.getSeriesDefinitions().get(0);"
				+ "var ySerieDef = yAxis.getSeriesDefinitions().get(0);"
				+ "var ySerie = ySerieDef.getRunTimeSeries().get(0);"
				+ "var dpa = plotComputation.getSeriesRenderingHints(ySerieDef,ySerie).getDataPoints();"
				+ "for( ii =0; ii<dpa.length; ii++ ){var csize = dpa[ii].getOrthogonalValue().getSize();"
				+ "dpa[ii].getOrthogonalValue().setSize( Double.parseDouble( csize)*.3);}}";

		// here i get rid of 0 and also the highest value in the script mapping
		// + 1 so no numbers are displayed on the y-axis
		int max = Collections.max(scriptMappings.values()) + 1;
		String output = "function beforeDrawAxisLabel(axis, label, scriptContext){";
		output += "if (label.getCaption( ).getValue( ) == 0) {label.getCaption().setValue(\"\")}"
				+ "if (label.getCaption( ).getValue( ) == "
				+ max
				+ ") {label.getCaption().setValue(\"\")}";
		for (Term t : scriptMappings.keySet()) {
			output += "if (label.getCaption( ).getValue( ) == "
					+ formatForJS(String.valueOf(scriptMappings.get(t))) + ")"
					+ "{label.getCaption().setValue("
					+ formatForJS(t.getName()) + " )}";
		}
		output += "}\n"; // bubbleSize;
		return output;
	}

	public final Chart createBubble(List<BubbleDataContainer> input) {

		ChartWithAxes cwaBubble = ChartWithAxesImpl.create();

		cwaBubble.setType("Bubble Chart"); //$NON-NLS-1$
		cwaBubble.setSubType("Standard Bubble Chart"); //$NON-NLS-1$
		// Plot
		cwaBubble.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBubble.getBlock().getOutline().setVisible(true);
		Plot p = cwaBubble.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwaBubble.getTitle().getLabel().getCaption()
				.setValue("Sub class comparison"); //$NON-NLS-1$

		// Legend
		Legend lg = cwaBubble.getLegend();
		lg.setItemType(LegendItemType.SERIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBubble.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MAX_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBubble.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		// yAxisPrimary.
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.setLabelPosition(Position.RIGHT_LITERAL);
		// yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 90 );
		// yAxisPrimary.getLabel().getCaption().getFont().setWordWrap(true);

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE
				.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		cwaBubble.setSampleData(sd);

		// X-Series

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		scriptMappings = new HashMap<Term, Integer>();
		createScriptMappingsForYAxis(input);

		// Set script for Chart

		cwaBubble.setScript(createScriptString(scriptMappings));

		List<String> xValues = createXLabels(input);

		Series seCategory = SeriesImpl.create();
		TextDataSet categoryValues = TextDataSetImpl.create(xValues);
		seCategory.setDataSet(categoryValues);
		sdX.getSeriesPalette().shift(0);
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		ySeriesMap = createYSeriesMap(input);
		addSeries(ySeriesMap, sdY);
		return cwaBubble;
	}

	private List<String> createXLabels(List<BubbleDataContainer> input) {
		SortedSet<String> xValues = new TreeSet<>();
		for (BubbleDataContainer b : input) {
			xValues.add(b.getxTerm().getName());
		}
		return new ArrayList<String>(xValues);

	}

	private Map<Term, List<BubbleDataContainer>> createYSeriesMap(
			List<BubbleDataContainer> input) {
		Map<Term, List<BubbleDataContainer>> yMap = new HashMap<>();
		for (BubbleDataContainer b : input) {
			if (yMap.get(b.getyTerm()) == null) {
				yMap.put(b.getyTerm(), new ArrayList<BubbleDataContainer>());
			}
			if (yMap.containsKey(b.getyTerm())) {
				yMap.get(b.getyTerm()).add(b);

			}
		}

		return yMap;
	}

	private void addSeries(Map<Term, List<BubbleDataContainer>> yMap,
			SeriesDefinition sdY) {
		for (Term t : yMap.keySet()) {
			List<BubbleEntry> yValues = new ArrayList<>();
			for (BubbleDataContainer b : yMap.get(t)) {
				// I need to retrieve the Mapping from yTerm to Integer here
				yValues.add(new BubbleEntry(Integer.valueOf(scriptMappings
						.get(b.getyTerm())), Integer.valueOf(b.getBubbleSize())));
			}
			BubbleDataSet values = BubbleDataSetImpl.create(yValues);
			BubbleSeries bs = (BubbleSeries) BubbleSeriesImpl.create();
			bs.getLabel().setVisible(true);
			bs.setDataSet(values);
			sdY.getSeries().add(bs);

		}
	}

	private void createScriptMappingsForYAxis(List<BubbleDataContainer> input) {
		int i = 1;
		for (BubbleDataContainer b : input) {
			Term yCandidate = b.getyTerm();
			int mapping = scriptMappings.containsKey(yCandidate) ? scriptMappings
					.get(yCandidate) : i;
			i++;
			scriptMappings.put(yCandidate, mapping);
		}
	}

	public String formatForJS(String toFormat) {
		return "\"" + toFormat + "\"";
	}

}
