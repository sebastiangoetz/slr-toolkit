package de.tudresden.slr.ui.chart.logic;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.birt.chart.extension.datafeed.BubbleEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.BubbleDataSet;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.BubbleDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * 
 */

public class BubbleChartGenerator
{

	public final static Chart createBubble( )
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
		xAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
		xAxisPrimary.getScale( ).setMin( NumberDataElementImpl.create( 0 ) );
		xAxisPrimary.getScale( ).setMax( NumberDataElementImpl.create( 140 ) );
		
		// Y-Axis
		Axis yAxisPrimary = cwaBubble.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 90 );
		


		// Data Set
		NumberDataSet categoryValues = NumberDataSetImpl.create( new double[] {
				20,45,70,100,120,130
		});
		BubbleDataSet values1 = BubbleDataSetImpl.create( new BubbleEntry[]{
				null,
				new BubbleEntry( 15, 100 ),
				new BubbleEntry( Integer.valueOf( 18 ), Integer.valueOf( 80 ) ),
				null,
				new BubbleEntry( Integer.valueOf( 23 ), Integer.valueOf( 100 ) ),
				null
		} );
		BubbleDataSet values2 = BubbleDataSetImpl.create( new BubbleEntry[]{
				new BubbleEntry( Integer.valueOf( 50 ), Integer.valueOf( 60 ) ),
				null,
				null,
				new BubbleEntry( Integer.valueOf( 43 ), Integer.valueOf( 80 ) ),
				new BubbleEntry( Integer.valueOf( 12 ), Integer.valueOf( 100 ) ),
				null
		} );
		BubbleDataSet values3 = BubbleDataSetImpl.create( new BubbleEntry[]{
				null,
				null,
				new BubbleEntry( Integer.valueOf( 43 ), Integer.valueOf( 75 ) ),
				new BubbleEntry( Integer.valueOf( 31 ), Integer.valueOf( 93 ) ),
				null,
				new BubbleEntry( Integer.valueOf( 25 ), Integer.valueOf( 50 ) )
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
		seCategory.setDataSet( categoryValues );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		sdX.getSeriesPalette( ).shift( 0 );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seCategory );

		// Y-Series
		BubbleSeries bs1 = (BubbleSeries) BubbleSeriesImpl.create( );
		bs1.setDataSet( values1 );
		bs1.getLabel( ).setVisible( false );
		
		BubbleSeries bs2 = (BubbleSeries) BubbleSeriesImpl.create( );
		bs2.setDataSet( values2 );
		bs2.getLabel( ).setVisible( false );
		
		
		BubbleSeries bs3 = (BubbleSeries) BubbleSeriesImpl.create( );
		bs3.setDataSet( values3 );
		
		bs3.getLabel( ).setVisible( true );
		System.out.println(bs3.getDisplayName());

		

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).shift( -1 );
		

		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( bs1 );
		sdY.getSeries( ).add( bs2 );
		sdY.getSeries( ).add( bs3 );
		sdY.getSeriesPalette().getEntries().clear();
		sdY.getSeriesPalette().getEntries().add(ColorDefinitionImpl.BLACK());
		
		
		return cwaBubble;
	}
}