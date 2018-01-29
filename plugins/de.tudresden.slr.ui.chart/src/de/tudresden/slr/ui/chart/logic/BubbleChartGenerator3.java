package de.tudresden.slr.ui.chart.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.eclipse.birt.chart.extension.datafeed.BubbleEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.MarkerType;
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
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.BubbleDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;

import de.tudresden.slr.model.taxonomy.Term;

/**
 * 
 */

public class BubbleChartGenerator3
{

	public final Chart createBubble(List<BubbleDataContainer> input, Term first, Term second )
	{
		StringBuilder jsScript = new StringBuilder();
		ChartWithAxes cwaBubble = ChartWithAxesImpl.create( );
		cwaBubble.setType( "Bubble Chart" ); //$NON-NLS-1$
		cwaBubble.setSubType( "Standard Bubble Chart" ); //$NON-NLS-1$
		// Plot
		cwaBubble.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		cwaBubble.getBlock( ).getOutline( ).setVisible( true );
		Plot p = cwaBubble.getPlot( );
		p.getClientArea( ).setBackground( ColorDefinitionImpl.create( 255,
				255,
				225 ) );

		// Title
		cwaBubble.getTitle( )
				.getLabel( )
				.getCaption( )
				.setValue( "Bubble Chart" ); //$NON-NLS-1$

		// Legend
		Legend lg = cwaBubble.getLegend( );
		lg.setItemType( LegendItemType.SERIES_LITERAL );

		// X-Axis
		Axis xAxisPrimary = cwaBubble.getPrimaryBaseAxes( )[0];
		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
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
		Axis yAxisPrimary = cwaBubble.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL);
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setWordWrap(true);
		yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 90 );
		long numberOfYTerms = input.stream().map(x -> x.getyTerm().getName()).distinct().count();
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


		// Data Set
		/*NumberDataSet categoryValues = NumberDataSetImpl.create( new double[] {
				20,45,70,100,120,130
		});
		Set<String> xValues = new HashSet<String>();
		for (BubbleDataContainer data : input) {
			xValues.add(data.getxTerm().getName());
		}
		String[] xEntries = new String[xValues.size()];
		xEntries = xValues.toArray(xEntries);
		TextDataSet xAxisValues = TextDataSetImpl.create(xEntries);
		
		for(String inp : xValues) {
			System.out.println(inp.toString());
		}
		Set<String> yValues = new HashSet<String>();
		for (BubbleDataContainer data : input) {
			yValues.add(data.getyTerm().getName());
		}
		String[] yEntries = new String[yValues.size()];
		yEntries = yValues.toArray(yEntries);
		String[] test= {"alle","test","ja","nein","jo"};
		NumberDataSet yAxisValues = NumberDataSetImpl.create(test); */
		
		
		//System.out.println(xAxisValues.toString());
		/*System.out.println(input.toString());
		for(BubbleDataContainer data: input) {
			System.out.println(data.getxTerm().toString());
			System.out.println(data.getyTerm().toString());
			System.out.println(data.getBubbleSize());
		}*/
		
		/*List<BubbleDataSet> bubbleEntries = new ArrayList<BubbleDataSet>();
		for(BubbleDataContainer data : input) {
			bubbleEntries.add(BubbleDataSetImpl.create(new BubbleEntry(data.getxTerm(), data.getBubbleSize())));
		}*/
		
		/*for(BubbleDataSet series : bubbleEntries) {
			System.out.println(series.getValues().toString());
		}*/

		
		/*BubbleDataSet values1 = BubbleDataSetImpl.create( new BubbleEntry[]{
				
				new BubbleEntry( "nein", 5,1 ),
				new BubbleEntry( "jo", Integer.valueOf( 3 ),2 )
				
		} ); */
		
		List<String> xTerms = createXTerms(input, jsScript);
		TextDataSet dsNumericValues1 = TextDataSetImpl.create(xTerms);
		OptionalInt maxValue = input.stream().mapToInt(x -> x.getBubbleSize()).max();
		jsScript.append("var maxValue = " + maxValue.getAsInt() + ";\n");
		
		SampleData sd = DataFactory.eINSTANCE.createSampleData( );
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData( );
		sdBase.setDataSetRepresentation( "" );//$NON-NLS-1$lman
		sd.getBaseSampleData( ).add( sdBase );

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		sdOrthogonal1.setDataSetRepresentation( "" );//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex( 0 );
		sd.getOrthogonalSampleData( ).add( sdOrthogonal1 );

		//cwaBubble.setSampleData( sd );

		// X-Series
		Series seCategory = SeriesImpl.create( );
		seCategory.setDataSet( dsNumericValues1);

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		sdX.getSeriesPalette( ).shift( 0 );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seCategory );

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		createYSeries(input, xTerms, sdY, jsScript);
		
		// Y-Series
		/*Series yCategory = SeriesImpl.create();
		yCategory.setDataSet(yAxisValues);*/
		
		/* BubbleSeries bs1 = (BubbleSeries) BubbleSeriesImpl.create( );
		bs1.setDataSet( values1 );
		bs1.getLabel( ).setVisible( true );
	
		

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).shift( -1 );
		

		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( bs1 );
		sdY.getSeriesPalette().getEntries().clear();
		sdY.getSeriesPalette().getEntries().add(ColorDefinitionImpl.BLACK());
		*/
	
		//Add JS
		appendJsScript(jsScript);
		cwaBubble.setScript(jsScript.toString());
		System.out.println(xTerms);
		
		System.out.println(jsScript.toString());
		return cwaBubble;
		
		}
		
		private  List<String> createXTerms(List<BubbleDataContainer> input, StringBuilder jsScript) {
			List<String> xTerms = input.stream().map(x -> x.getxTerm().getName()).distinct().sorted().collect(Collectors.toList());
			jsScript.append("\n");
			jsScript.append("var seriesLength = " + xTerms.size() + ";\n");
			jsScript.append("var columns = " + xTerms.size() + ";\n");
			return xTerms;
		}
		
		
		private  void createYSeries(List<BubbleDataContainer> input, List<String> xTerms, SeriesDefinition sd, StringBuilder jsScript) {
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
				
			
				//ss.setLabelPosition(Position.INSIDE_LITERAL);
				//System.out.println(ss.getLabel());
				ss.setDataSet(NumberDataSetImpl.create(DoubleStream.iterate(count, i -> i).limit(xTermsLength).toArray()));
				String jsonKey = sanitizeJsonKey(yTerm);
				//System.out.println(jsonKey);
				//System.out.println(ss.getDataSet().toString());
				ss.setSeriesIdentifier(jsonKey);
				sd.getSeries().add(ss);
				//System.out.println(sd.getSeries().toString());
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
		private  String sanitizeJsonKey(String str) {
			return str.replaceAll("[^\\w]", "_");
		}
		
		
		/**
		 * Add JS code.
		 * @param jsValues
		 */
		private  void appendJsScript(StringBuilder jsValues) {
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