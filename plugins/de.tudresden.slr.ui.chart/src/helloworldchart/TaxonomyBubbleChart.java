package helloworldchart;

import java.util.HashMap;

import javax.script.ScriptEngine;

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

public class TaxonomyBubbleChart {

	private HashMap<Integer, String> axisMapping = null;
	private JavaScriptEvaluator evaluator = null;
	private ScriptEngine engine = null;

	public TaxonomyBubbleChart() {

	}

	/**
	 * 
	 * @param kat11
	 *            The first x - Axis Label
	 * @param kat12
	 *            The second x - Axis Label
	 * @param kat21
	 *            The first y - Axis Label
	 * @param kat22
	 *            The second y - Axis Label
	 * @return a new TaxonomyBubbleChart
	 */

	public final Chart createBubble(String kat11, String kat12, String kat21,
			String kat22) {

		axisMapping = new HashMap<>();
		axisMapping.put(10, kat21);
		axisMapping.put(20, kat22);

		ChartWithAxes cwaBubble = ChartWithAxesImpl.create();

		evaluator = new JavaScriptEvaluator();
		engine = evaluator.getEngine();
		engine.put("kat21", 10);
		engine.put("kat22", 20);

		cwaBubble
				.setScript("function beforeDrawAxisLabel(axis, label, scriptContext)" //$NON-NLS-1$
						+ "{if (label.getCaption( ).getValue( ) == 10)"
						+ "{label.getCaption().setValue("
						+ formatForJS(kat21)
						+ " )}"
						+ "if (label.getCaption( ).getValue( ) == 20)"
						+ "{label.getCaption().setValue("
						+ formatForJS(kat22)
						+ " )}"
						+ "if (label.getCaption( ).getValue( ) == 0)"
						+ "{label.getCaption().setValue(\"\")}"
						+ "if (label.getCaption( ).getValue( ) == 30)"
						+ "{label.getCaption().setValue(\"\")}"
						// + "label.getCaption( ).getFont( ).setSize(7);"
						+ "}\n");

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

		TextDataSet categoryValues = TextDataSetImpl.create(new String[] {
				kat11, kat12 });
		BubbleDataSet values1 = BubbleDataSetImpl.create(new BubbleEntry[] {
				new BubbleEntry(Integer.valueOf(10), Integer.valueOf(100)),
				new BubbleEntry(Integer.valueOf(20), Integer.valueOf(200))

		});

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
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BubbleSeries bs1 = (BubbleSeries) BubbleSeriesImpl.create();
		bs1.setDataSet(values1);
		bs1.getLabel().setVisible(false);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().shift(-1);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);

		return cwaBubble;
	}

	public String formatForJS(String toFormat) {
		return "\"" + toFormat + "\"";
	}

}
