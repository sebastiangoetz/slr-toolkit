package de.tudresden.slr.ui.chart.logic;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;

import de.tudresden.slr.model.taxonomy.Term;

public class BubbleChartGenerator2 {
	/**
	 * Creates a scatter plot that emulates a bubble chart with non-overlapping bubbles.
	 * @param input
	 * @return Scatter plot
	 * return new BubbleChartGenerator().createBubble(input, first, second); -> in ChartGenerator.java
	 */
	public final Chart createBubble(List<BubbleDataContainer> input, Term first, Term second) {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();
		StringBuilder jsScript = new StringBuilder();
		cwaScatter.setType("Bubble Chart");
		cwaScatter.setSubType("Standard Bubble Chart");
		
		// Plot
		cwaScatter.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaScatter.getPlot().getClientArea().getOutline().setVisible( false );
		cwaScatter.getPlot().getClientArea().setBackground(ColorDefinitionImpl.WHITE());

		// Title
		cwaScatter.getTitle().getLabel().getCaption().setValue(first.getName().trim()+" / "+second.getName().trim());

		// Legend
		cwaScatter.getLegend().setVisible(false);
		
		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl)cwaScatter).getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(false);
		xAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMinorGrid().getLineAttributes().setThickness(2);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		Scale xScale = xAxisPrimary.getScale();
		long numberOfXTerms = input.stream().map(x -> x.getxTerm().getName()).distinct().count();
		xScale.setStep(1);
		xScale.setMin(NumberDataElementImpl.create(0));
		xScale.setMax(NumberDataElementImpl.create(numberOfXTerms));
		xScale.setMinorGridsPerUnit(2);
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(45);
		if(numberOfXTerms > 15){
			xAxisPrimary.getLabel().getCaption().getFont().setRotation(90);
		}
		//TODO: Find a more intelligent way to set the rotation...
		//Rotate labels even further if we have many bars
		xAxisPrimary.getLabel().getCaption().getFont().setName("Arial");

		// Y-Axis
		long numberOfYTerms = input.stream().map(x -> x.getyTerm().getName()).distinct().count();
		Axis yAxisPrimary = cwaScatter.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		Scale yScale = yAxisPrimary.getScale();
		yScale.setStep(1);
		yScale.setMin(NumberDataElementImpl.create(0));
		yScale.setMax(NumberDataElementImpl.create(numberOfYTerms + 1));
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
			double margin = maxStringLength.getAsInt() * 4.2 * factor;
			yAxisPrimary.setLabelSpan(margin >= 20 ? margin : 20);
		} else {
			yAxisPrimary.setLabelSpan(75);
		}
		
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setWordWrap(true);
		//yAxisPrimary.getScale().setMajorGridsStepNumber(2);
		
		
		List<String> xTerms = createXTerms(input, jsScript);
		TextDataSet dsNumericValues1 = TextDataSetImpl.create(xTerms);
		OptionalInt maxValue = input.stream().mapToInt(x -> x.getBubbleSize()).max();
		jsScript.append("var maxValue = " + maxValue.getAsInt() + ";\n");
		
		SampleData sd = DataFactory.eINSTANCE.createSampleData( );
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData( );
		sdBase.setDataSetRepresentation("");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		sdOrthogonal.setDataSetRepresentation("");//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);
		
		cwaScatter.setSampleData( sd );

		// X-Series
		Series seBase = SeriesImpl.create( );
		seBase.setDataSet(dsNumericValues1);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions( ).add(sdX);
		sdX.getSeries().add( seBase );
		
		
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		createYSeries(input, xTerms, sdY, jsScript);
		
		
		
		//Add JS
		appendJsScript(jsScript);
		cwaScatter.setScript(jsScript.toString());
		
		//System.out.println(xAxisPrimary.getSeriesDefinitions());
		//System.out.println(sdX.getSeries());
		//System.out.println(yAxisPrimary.getScale());
		//System.out.println(xAxisPrimary.getScale());
		System.out.println(jsScript.toString());
		//System.out.println(sdY.getSeries());
		//System.out.println(sdX.getSeries());
		return cwaScatter;
	}

	/**
	 * Gets a sorted list of distinct terms for the x-axis.
	 * Also writes JS variables to a StringBuilder
	 * @param input
	 * @param jsScript StrinBuilder for JS variables
	 * @return A sorted list of distinct terms.
	 */
	private List<String> createXTerms(List<BubbleDataContainer> input, StringBuilder jsScript) {
		List<String> xTerms = input.stream().map(x -> x.getxTerm().getName()).distinct().sorted().collect(Collectors.toList());
		jsScript.append("\n");
		jsScript.append("var seriesLength = " + xTerms.size() + ";\n");
		jsScript.append("var columns = " + xTerms.size() + ";\n");
		return xTerms;
	}

	/**
	 * Creates y-series. Each data point in each series uses the same value (1.0 for the first series, 2.0 for the second etc.).
	 * Also builds JS code in a StringBuilder.
	 * @param input
	 * @param xTerms Terms of the x-axis
	 * @param sd Series definition for y-axis.
	 * @param jsScript StringBuilder for JS variables.
	 */
	private void createYSeries(List<BubbleDataContainer> input, List<String> xTerms, SeriesDefinition sd, StringBuilder jsScript) {
		int xTermsLength = xTerms.size();
		List<String> yTerms = input.stream().map(x -> x.getyTerm().getName()).distinct().sorted().collect(Collectors.toList());
		StringBuilder jsLabels = new StringBuilder();
		
		jsLabels.append("var labels = {\"0\" : \"\", ");
		jsScript.append("var rows = " + (yTerms.size() + 1)  + ";\n");
		jsScript.append("var seriesPosition = {");
		int count = 1;
		for(String yTerm : yTerms){
			BubbleSeries ss = (BubbleSeries) BubbleSeriesImpl.create( );
			ss.getMarkers().stream().forEach(m -> m.setType(MarkerType.CIRCLE_LITERAL));
			ss.getMarkers().stream().forEach(m -> m.getOutline().setColor(ColorDefinitionImpl.RED()));
			ss.getMarkers().stream().forEach(m -> m.getOutline().setThickness(20));
			ss.getLabel().setVisible(true);
			
		
			ss.setLabelPosition(Position.INSIDE_LITERAL);
			//System.out.println(ss.getLabel());
			ss.setDataSet(NumberDataSetImpl.create(DoubleStream.iterate(count, i -> i).limit(xTermsLength).toArray()));
			String jsonKey = sanitizeJsonKey(yTerm);
			//System.out.println(jsonKey);
		
			ss.setSeriesIdentifier(jsonKey);
			sd.getSeries().add(ss);
			sd.getSeriesPalette().getEntries().clear();
			
			sd.getSeriesPalette().getEntries().add(ColorDefinitionImpl.WHITE());
			sd.getSeriesPalette().getEntries().add(ColorDefinitionImpl.WHITE());
			sd.getSeriesPalette().getEntries().add(ColorDefinitionImpl.WHITE());
			sd.getSeriesPalette().getEntries().add(ColorDefinitionImpl.WHITE());
			
		
			jsScript.append("\"" + jsonKey +"\": [");
			input.stream().filter(x -> x.getyTerm().getName().equals(yTerm))
							.sorted((a, b) -> a.getxTerm().getName().compareTo(b.getxTerm().getName()))
							.mapToInt(y -> y.getBubbleSize()).forEach(z -> {jsScript.append(z);	jsScript.append(",");});
			jsScript.append("],");
			
			jsLabels.append("\"" + count + "\": \"");
			jsLabels.append(yTerms.get(count - 1));
			jsLabels.append("\", ");
			count++;
		}
		jsLabels.append("\"" + (yTerms.size() + 1) + "\": \"\"};\n");
		jsScript.append("};\n");
		jsScript.append(jsLabels);
		
	}

	/**
	 * Sanitize term names (or any other string) for use as JSON key.
	 * @param Any string
	 * @return Sanitized string
	 */
	private String sanitizeJsonKey(String str) {
		return str.replaceAll("[^\\w]", "_");
	}

	/**
	 * Add JS code.
	 * @param jsValues
	 */
	private void appendJsScript(StringBuilder jsValues) {
		jsValues.append("var count = 0;\n");
		jsValues.append("var labelCount = 0;\n");
		jsValues.append("var resizeFactor;\n");
		jsValues.append("/**\n");
		jsValues.append(" * Called before drawing each marker.\n");
		jsValues.append(" * \n");
		jsValues.append(" * @param marker\n");
		jsValues.append(" *            Marker\n");
		jsValues.append(" * @param dph\n");
		jsValues.append(" *            DataPointHints\n");
		jsValues.append(" * @param icsc\n");
		jsValues.append(" *            IChartScriptContext\n");
		jsValues.append(" */\n");
		jsValues.append("function beforeDrawMarker( marker, dph, icsc ) {\n");
		jsValues.append("\tvar seriesValue = dph.getSeriesValue();\n");
		jsValues.append("\tvar size = 1;\n");
		jsValues.append("\tif(seriesPosition[seriesValue] != void 0 && seriesPosition[seriesValue][count%seriesLength] != void 0){\n");
		jsValues.append("\t\tsize = seriesPosition[seriesValue][count%seriesLength];\n");
		jsValues.append("\t}\n");
		jsValues.append("\tif(resizeFactor == void 0){\n");
		jsValues.append("\t\tvar chart = icsc.getChartInstance()\n");
		jsValues.append("\t\tvar bounds =  chart.getBlock().getBounds();\n");
		jsValues.append("\t\tvar width = bounds.getWidth();\n");
		jsValues.append("\t\tvar height = bounds.getHeight();\n");
		jsValues.append("\t\tvar maxSize = width/columns;\n");
		jsValues.append("\t\tif(height/rows < maxSize){\n");
		jsValues.append("\t\t\tmaxSize = height/columns;\n");
		jsValues.append("\t\t}\n");
		jsValues.append("\t\n");
		jsValues.append("\t\tresizeFactor = ((maxSize/2)/maxValue) * 0.9;\n");
		jsValues.append("\t}\n");
		jsValues.append("\tmarker.setSize(size * resizeFactor);\n");
		jsValues.append("\tcount++;\n");
		jsValues.append("\tmarker.getOutline().setThickness(20);\n");
		jsValues.append("}\n");
		jsValues.append("/**\n");
		jsValues.append(" * Called before rendering each label on a given Axis.\n");
		jsValues.append(" * \n");
		jsValues.append(" * @param axis\n");
		jsValues.append(" *            Axis\n");
		jsValues.append(" * @param label\n");
		jsValues.append(" *            Label\n");
		jsValues.append(" * @param icsc\n");
		jsValues.append(" *            IChartScriptContext\n");
		jsValues.append(" */\n");
		jsValues.append("function beforeDrawAxisLabel( axis, label, icsc )\n");
		jsValues.append("{\n");
		jsValues.append("\tif(labels[label.getCaption().getValue()] != void 0){\n");
		jsValues.append("\t\tlabel.getCaption().setValue(labels[label.getCaption().getValue()]);\n");
		jsValues.append("\t}\n");
		jsValues.append("}\n");
		jsValues.append("/**\n");
		jsValues.append(" * Called before rendering the label for each datapoint.\n");
		jsValues.append(" * \n");
		jsValues.append(" * @param dph\n");
		jsValues.append(" *            DataPointHints\n");
		jsValues.append(" * @param label\n");
		jsValues.append(" *            Label\n");
		jsValues.append(" * @param icsc\n");
		jsValues.append(" *            IChartScriptContext\n");
		jsValues.append(" */\n");
		jsValues.append("function beforeDrawDataPointLabel( dph, label, icsc )\n");
		jsValues.append("{\n");
		jsValues.append("\tvar seriesValue = dph.getSeriesValue();\n");
		jsValues.append("\tvar size = void 0;\n");
		jsValues.append("\tif(seriesPosition[seriesValue] != void 0 && seriesPosition[seriesValue][count%seriesLength] != void 0){\n");
		jsValues.append("\t\tsize = seriesPosition[seriesValue][labelCount%seriesLength];\n");
		jsValues.append("\t}\n");
		jsValues.append("\tlabel.getCaption().setValue(size);\n");
		jsValues.append("\t(size <= 10) && label.setVisible(false);\n");
		jsValues.append("\t(size > 10) && label.setVisible(true);\n");
		jsValues.append("\tlabelCount++;\n");
		jsValues.append("\ticsc.getChartInstance().getSeries().setLabelPosition(Position.INSIDE_LITERAL);\n");
		jsValues.append("}\n");
	}
}
