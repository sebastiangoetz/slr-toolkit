package de.tudresden.slr.ui.chart.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.chart.extension.datafeed.BubbleEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
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
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;

import de.tudresden.slr.model.taxonomy.Term;

/**
 * 
 */

public class BubbleChartGenerator
{

	public final static Chart createBubble(List<BubbleDataContainer> input, Term first, Term second )
	{
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
		
		
		// Y-Axis
		Axis yAxisPrimary = cwaBubble.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 90 );
		


		// Data Set
		NumberDataSet categoryValues = NumberDataSetImpl.create( new double[] {
				20,45,70,100,120,130
		});
		Set<String> xValues = new HashSet<String>();
		for (BubbleDataContainer data : input) {
			xValues.add(data.getxTerm().toString());
		}
		String[] xEntries = new String[xValues.size()];
		xEntries = xValues.toArray(xEntries);
		TextDataSet xAxisValues = TextDataSetImpl.create(xEntries);
		
		/*for(String inp : xValues) {
			System.out.println(inp.toString());
		}*/
		
		
		
		
		
		//System.out.println(xAxisValues.toString());
		/*System.out.println(input.toString());
		for(BubbleDataContainer data: input) {
			System.out.println(data.getxTerm().toString());
			System.out.println(data.getyTerm().toString());
			System.out.println(data.getBubbleSize());
		}*/
		List<BubbleDataSet> bubbleEntries = new ArrayList<BubbleDataSet>();
		for(BubbleDataContainer data : input) {
			bubbleEntries.add(BubbleDataSetImpl.create(new BubbleEntry(data.getxTerm(), data.getBubbleSize())));
		}
		
		/*for(BubbleDataSet series : bubbleEntries) {
			System.out.println(series.getValues().toString());
		}*/

		
		BubbleDataSet values1 = BubbleDataSetImpl.create( new BubbleEntry[]{
				null,
				new BubbleEntry( "architecture", 100 ),
				new BubbleEntry( "none", Integer.valueOf( 80 ) ),
				null,
				new BubbleEntry( "component", Integer.valueOf( 100 ) ),
				null
		} );
		
		SampleData sd = DataFactory.eINSTANCE.createSampleData( );
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData( );
		sdBase.setDataSetRepresentation( "" );//$NON-NLS-1$
		sd.getBaseSampleData( ).add( sdBase );

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		sdOrthogonal1.setDataSetRepresentation( "" );//$NON-NLS-1$
		sdOrthogonal1.setSeriesDefinitionIndex( 0 );
		sd.getOrthogonalSampleData( ).add( sdOrthogonal1 );

		cwaBubble.setSampleData( sd );

		// X-Series
		Series seCategory = SeriesImpl.create( );
		seCategory.setDataSet( xAxisValues );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		sdX.getSeriesPalette( ).shift( 0 );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seCategory );

		// Y-Series
		BubbleSeries bs1 = (BubbleSeries) BubbleSeriesImpl.create( );
		bs1.setDataSet( values1 );
		bs1.getLabel( ).setVisible( false );
	
		

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).shift( -1 );
		

		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( bs1 );
		sdY.getSeriesPalette().getEntries().clear();
		sdY.getSeriesPalette().getEntries().add(ColorDefinitionImpl.BLACK());
		
		
		return cwaBubble;
	}
}