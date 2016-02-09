package de.tudresden.slr.ui.chart.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.birt.chart.extension.datafeed.BubbleEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Scale;
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
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;

import de.tudresden.slr.model.taxonomy.Term;

public class BubbleChartGenerator {

	private Map<Term, Integer> scriptMappings = new HashMap<Term, Integer>();
	Map<Term, List<BubbleDataContainer>> ySeriesMap;

	private String createScriptString(Map<Term, Integer> scriptMappings) {
		String formatSeriesLabels = "\nfunction beforeDrawDataPointLabel( dataPointHint, label, iChartScriptContext){\n"
				+ "\tvar oldLabel = label.getCaption().getValue();\n"
				+ "\tvar newLabel = oldLabel.match(/S(\\d*)\\./);\n"
				+ "\tlabel.getCaption().setValue(newLabel[1]);\n}\n";
		// here i get rid of 0 and also the highest value in the script mapping
		// + 1 so no numbers are displayed on the y-axis
		int max = Collections.max(scriptMappings.values()) + 1;
		String output = "\nfunction beforeDrawAxisLabel(axis, label, scriptContext){\n";
		output += "\tif (label.getCaption().getValue() >= "	+ max + " || label.getCaption().getValue() <= 0) {\n\t\tlabel.getCaption().setValue(\"\");\n\t} ";
		for (Term t : scriptMappings.keySet()) {
			output += "\telse if (label.getCaption( ).getValue( ) == "
					+ formatForJS(String.valueOf(scriptMappings.get(t))) + "){\n"
					+ "\t\tlabel.getCaption().setValue("
					+ formatForJS(t.getName()) + " )\n\t}\n";
		}
		output += "}\n" + formatSeriesLabels;
		return output;
	}

	public final Chart createBubble(List<BubbleDataContainer> input) {
		createScriptMappingsForYAxis(input);
		ChartWithAxes cwaBubble = ChartWithAxesImpl.create();

		cwaBubble.setType("Bubble Chart");
		cwaBubble.setSubType("Standard Bubble Chart");
		// Plot
		cwaBubble.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBubble.getBlock().getOutline().setVisible(false);
		Plot p = cwaBubble.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.WHITE());

		// Title
		cwaBubble.getTitle().getLabel().getCaption().setValue("Sub class comparison");

		// Legend
		cwaBubble.getLegend().setVisible(false);

		// X-Axis
		Axis xAxisPrimary = cwaBubble.getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		Scale xScale = xAxisPrimary.getScale();
		long numberOfXTerms = input.stream().map(x -> x.getxTerm().getName()).distinct().count();
		xScale.setStep(1);
		xScale.setMin(NumberDataElementImpl.create(0));
		xScale.setMax(NumberDataElementImpl.create(numberOfXTerms));
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(45);
		if(numberOfXTerms > 15){
			xAxisPrimary.getLabel().getCaption().getFont().setRotation(90);
		}
		//TODO: Find a more intelligent way to set the rotation...
		//Rotate labels even further if we have many bars
		xAxisPrimary.getLabel().getCaption().getFont().setName("Arial");

		// Y-Axis
		int max = Collections.max(scriptMappings.values()) + 1;
		long numberOfYTerms = input.stream().map(x -> x.getyTerm().getName()).distinct().count();
		Axis yAxisPrimary = cwaBubble.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		Scale yScale = yAxisPrimary.getScale();
		yScale.setStep(1);
		yScale.setMin(NumberDataElementImpl.create(0));
		yScale.setMax(NumberDataElementImpl.create(max));
		if(numberOfYTerms <= 10){
			yAxisPrimary.getLabel().getCaption().getFont().setRotation(45);
			yAxisPrimary.getLabel().getCaption().getFont().setName("Arial");
		}
		
		//Label span defines the margin between axis and page border.
		//Setting a fixed value is far from ideal because it should depend
		//on the size of the labels. Automatic label span doesn't work, since labels are change with scripts.
		//The following is a bad approximation to ensure that labels aren't cut-off
		OptionalInt maxStringLength = input.stream().map(x -> x.getyTerm().getName().length()).mapToInt(Integer::intValue).max();
		if(maxStringLength.isPresent()){
			double factor = yAxisPrimary.getLabel().getCaption().getFont().getRotation() != 0 ? 1 : 1.4;
			yAxisPrimary.setLabelSpan(maxStringLength.getAsInt() * 4.2 * factor);
		} else {
			yAxisPrimary.setLabelSpan(75);
		}
		
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setWordWrap(true);

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		cwaBubble.setSampleData(sd);

		// X-Series
		SeriesDefinition sdX = SeriesDefinitionImpl.create();

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

	private Map<Term, List<BubbleDataContainer>> createYSeriesMap(List<BubbleDataContainer> input) {
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
				yValues.add(new BubbleEntry(Integer.valueOf(scriptMappings.get(b.getyTerm())), Integer.valueOf(b.getBubbleSize())));
			}
			BubbleDataSet values = BubbleDataSetImpl.create(yValues);
			BubbleSeries bs = (BubbleSeries) BubbleSeriesImpl.create();
			bs.getLabel().setVisible(true);
			bs.setLabelPosition(Position.INSIDE_LITERAL);
			bs.setDataSet(values);
			sdY.getSeries().add(bs);

		}
	}

	private void createScriptMappingsForYAxis(List<BubbleDataContainer> input) {
		int i = 1;
		for (BubbleDataContainer b : input) {
			Term yCandidate = b.getyTerm();
			int mapping = scriptMappings.containsKey(yCandidate) ? scriptMappings.get(yCandidate) : i;
			i++;
			scriptMappings.put(yCandidate, mapping);
		}
	}

	public String formatForJS(String toFormat) {
		return "\"" + toFormat + "\"";
	}

}
