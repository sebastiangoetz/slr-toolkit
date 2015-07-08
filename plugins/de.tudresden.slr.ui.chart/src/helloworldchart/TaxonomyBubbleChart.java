package helloworldchart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.BubbleChartDataContainer;

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

	public TaxonomyBubbleChart() {

	}

	/**
	 * 
	 * @param dataContainer
	 *            TODO
	 * @return a new TaxonomyBubbleChart
	 */

	public final Chart createBubble(List<BubbleChartDataContainer> input) {

		ChartWithAxes cwaBubble = ChartWithAxesImpl.create();

		// This is not working, maybe i have to do it in javascript?
		// sdX.getGrouping().setEnabled(true);
		// sdX.getGrouping().setAggregateExpression("Sum");
		// sdX.getGrouping().setGroupType(DataType.TEXT_LITERAL);
		// sdX.getGrouping().setGroupingInterval(1);

		// cwaBubble.setScript(
		//				 "function beforeDrawAxisLabel(axis, label, scriptContext)" //$NON-NLS-1$
		// + "{if (label.getCaption( ).getValue( ) == 10)"
		// + "{label.getCaption().setValue("
		// + formatForJS(kat21)
		// + " )}"
		// + "if (label.getCaption( ).getValue( ) == 20)"
		// + "{label.getCaption().setValue("
		// + formatForJS(kat22)
		// + " )}"
		// + "if (label.getCaption( ).getValue( ) == 0)"
		// + "{label.getCaption().setValue(\"\")}"
		// + "if (label.getCaption( ).getValue( ) == 30)"
		// + "{label.getCaption().setValue(\"\")}"
		// // + "label.getCaption( ).getFont( ).setSize(7);"
		// + "}\n");

		cwaBubble.setType("Bubble Chart"); //$NON-NLS-1$
		cwaBubble.setSubType("Standard Bubble Chart"); //$NON-NLS-1$
		// Plot
		cwaBubble.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBubble.getBlock().getOutline().setVisible(true);
		Plot p = cwaBubble.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwaBubble.getTitle().getLabel().getCaption().setValue("Bubble Chart"); //$NON-NLS-1$

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

		scriptMappings = new HashMap<Term, Integer>();
		createScriptMappings(input);
		List<String> xValues = new ArrayList<>();
		List<BubbleEntry> yValues = new ArrayList<>();
		createXandYValues(input, xValues, yValues);

		TextDataSet categoryValues = TextDataSetImpl.create(xValues);
		BubbleDataSet values1 = BubbleDataSetImpl.create(yValues);
		// new BubbleEntry[] {
		// new BubbleEntry(Integer.valueOf(10), Integer.valueOf(100)),
		// new BubbleEntry(Integer.valueOf(20), Integer.valueOf(200))
		//
		// });

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
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);

		// Y-Series
		BubbleSeries bs1 = (BubbleSeries) BubbleSeriesImpl.create();
		bs1.setDataSet(values1);

		bs1.getLabel().setVisible(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);

		sdX.getSeries().add(seCategory);

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		return cwaBubble;
	}

	private void createXandYValues(List<BubbleChartDataContainer> input,
			List<String> xValues, List<BubbleEntry> yValues) {
		for (BubbleChartDataContainer b : input) {
			xValues.add(b.getxTerm().getName());
			yValues.add(new BubbleEntry(Integer.valueOf(scriptMappings.get(b
					.getyTerm())), Integer.valueOf(b.getBubbleSize())));

		}
	}

	private void createScriptMappings(List<BubbleChartDataContainer> input) {
		int i = 1;
		for (BubbleChartDataContainer b : input) {
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
