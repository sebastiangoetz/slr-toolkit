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
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.settings.ChartConfiguration;
import de.tudresden.slr.ui.chart.settings.parts.AxisSettings;
import de.tudresden.slr.ui.chart.settings.parts.BlockSettings;
import de.tudresden.slr.ui.chart.settings.parts.GeneralSettings;
import de.tudresden.slr.ui.chart.settings.parts.LegendSettings;
import de.tudresden.slr.ui.chart.settings.parts.PlotSettings;
import de.tudresden.slr.ui.chart.settings.parts.SeriesSettings;

public class BubbleChartGenerator {
	/**
	 * Creates a scatter plot that emulates a bubble chart with non-overlapping bubbles.
	 * @param input
	 * @return Scatter plot
	 */
	public final Chart createBubble(List<BubbleDataContainer> input, Term first, Term second) {
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create();
		StringBuilder jsScript = new StringBuilder();
		cwaScatter.setType("Scatter Chart");
		cwaScatter.setSubType("Standard Scatter Chart");
		
		ChartConfiguration cc = ChartConfiguration.BUBBLECHARTCONFIG;//
		PlotSettings ps = cc.getPlotSettings();
		GeneralSettings gs = cc.getGeneralSettings();
		LegendSettings ls = cc.getLegendSettings();
		BlockSettings bs = cc.getBlockSettings();
		AxisSettings as = cc.getAxisSettings();
		SeriesSettings ss = cc.getSeriesSettings();
		
		List<BubbleDataTerm> yTermData = cc.getBubbleTermListY();
		int a = gs.getChartShowLabel();
		
		// Plot
		cwaScatter.getPlot().getClientArea().getOutline().setVisible( false );
		cwaScatter.getPlot().getClientArea().setBackground(ColorDefinitionImpl.WHITE());
		cwaScatter.getBlock().setBackground(ColorDefinitionImpl.create(bs.getBlockBackgroundRGB().red, bs.getBlockBackgroundRGB().green, bs.getBlockBackgroundRGB().blue));
		cwaScatter.getBlock().getOutline().setVisible(bs.isBlockShowOutline());
		cwaScatter.getBlock().getOutline().setStyle(bs.getBlockOutlineStyle());
		cwaScatter.getBlock().getOutline().setThickness(bs.getBlockOutlineThickness());
		cwaScatter.getBlock().getOutline().setColor(ColorDefinitionImpl.create(bs.getBlockOutlineRGB().red, bs.getBlockOutlineRGB().green, bs.getBlockOutlineRGB().blue));

		// Title
		if(gs.getChartTitle().equals("")) {
		gs.setChartTitle(first.getName().trim()+" / "+second.getName().trim());
		}
		//cwaScatter.getTitle().getLabel().getCaption().setValue(first.getName().trim()+" / "+second.getName().trim());
		cwaScatter.getTitle().getLabel().getCaption().setValue(gs.getChartTitle());
		cwaScatter.getTitle().getLabel().getCaption().getFont().setSize(gs.getChartTitleSize());
		cwaScatter.getTitle().getLabel().getCaption().setColor(ColorDefinitionImpl.create(gs.getChartTitleColor().red, gs.getChartTitleColor().green, gs.getChartTitleColor().blue));
		cwaScatter.getTitle().getLabel().getCaption().getFont().setBold(gs.isChartTitleBold());
		cwaScatter.getTitle().getLabel().getCaption().getFont().setItalic(gs.isChartTitleItalic());
		cwaScatter.getTitle().getLabel().getCaption().getFont().setUnderline(gs.isChartTitleUnderline());
		
		// Legend
		cwaScatter.getLegend().setVisible(false);
		
		// X-Axis
		Axis xAxisPrimary = ((ChartWithAxesImpl)cwaScatter).getPrimaryBaseAxes()[0];
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.ABOVE_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(false);
		xAxisPrimary.getMinorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMinorGrid().getLineAttributes().setThickness(2);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		
		if(as.getxAxisTitle().equals("")) {
		as.setxAxisTitle(first.getName().trim());
		}
		xAxisPrimary.getTitle().getCaption().setValue(as.getxAxisTitle());
		//xAxisPrimary.getTitle().getCaption().setValue(first.getName().trim());
		xAxisPrimary.getTitle().getCaption().getFont().setSize(as.getxAxisTitleSize());
		xAxisPrimary.getTitle().setVisible(true);
		
		Scale xScale = xAxisPrimary.getScale();
		long numberOfXTerms = input.stream().map(x -> x.getxTerm().getName()).distinct().count();
		xScale.setStep(1);
		xScale.setMin(NumberDataElementImpl.create(0));
		xScale.setMax(NumberDataElementImpl.create(numberOfXTerms));
		xScale.setMinorGridsPerUnit(2);
		
		if(as.isxAxisAutoRotation()) {
			xAxisPrimary.getLabel().getCaption().getFont().setRotation(45);
			if(numberOfXTerms > 15){
				xAxisPrimary.getLabel().getCaption().getFont().setRotation(90);
			}
		}
		else {
			xAxisPrimary.getLabel().getCaption().getFont().setRotation(as.getxAxisRotation());
			
		}
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
			
		} else if(numberOfYTerms > 10) {
			yAxisPrimary.getLabel().getCaption().getFont().setRotation(0);
		}
		
			yAxisPrimary.getLabel().getCaption().getFont().setName("Arial");
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
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setWordWrap(true);
		
		if(as.getyAxisTitle().equals("")) {
		as.setyAxisTitle(second.getName().trim());
		}
		yAxisPrimary.getTitle().getCaption().setValue(as.getyAxisTitle());
		//yAxisPrimary.getTitle().getCaption().setValue(second.getName().trim());
		yAxisPrimary.getTitle().getCaption().getFont().setSize(as.getyAxisTitleSize());
		yAxisPrimary.getTitle().setVisible(true);
		
		
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
		createYSeries(input, xTerms, sdY, jsScript, yTermData);
		
		//Add JS
		appendJsScript(jsScript, a);
		cwaScatter.setScript(jsScript.toString());

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
	private void createYSeries(List<BubbleDataContainer> input, List<String> xTerms, SeriesDefinition sd, StringBuilder jsScript,
			List<BubbleDataTerm> yTermData) {

		int xTermsLength = xTerms.size();
		List<String> yTerms = input.stream().map(x -> x.getyTerm().getName()).distinct().sorted().collect(Collectors.toList());
		StringBuilder jsLabels = new StringBuilder();
		
		jsLabels.append("var labels = {\"0\" : \"\", ");
		jsScript.append("var rows = " + (yTerms.size() + 1)  + ";\n");
		jsScript.append("var seriesPosition = {");
		int count = 1;
		for(String yTerm : yTerms){
			ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create( );
			ss.getMarkers().stream().forEach(m -> m.setType(MarkerType.CIRCLE_LITERAL));
			ss.getLabel().setVisible(true);
			ss.setDataSet(NumberDataSetImpl.create(DoubleStream.iterate(count, i -> i).limit(xTermsLength).toArray()));
			String jsonKey = sanitizeJsonKey(yTerm);
			ss.setSeriesIdentifier(jsonKey);
			sd.getSeries().add(ss);
			
			//change Colors
			sd.getSeriesPalette().getEntries().clear();
			for(BubbleDataTerm item : yTermData) {
				if(item.isDisplayed()) {
					sd.getSeriesPalette().getEntries().add(ColorDefinitionImpl.create(
							item.getRGB().red, item.getRGB().green, item.getRGB().blue));
				}
			}
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
	private void appendJsScript(StringBuilder jsValues, int a) {
		jsValues.append("var count = 0;\n");
		jsValues.append("var labelCount = 0;\n");
		jsValues.append("var resizeFactor = 0.5;\n");
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
		jsValues.append("\t(size <= "+ a + ") && label.setVisible(false);\n");
		jsValues.append("\t(size > " + a + ") && label.setVisible(true);\n");
		jsValues.append("\tlabelCount++;\n");
		jsValues.append("}\n");
	}
}