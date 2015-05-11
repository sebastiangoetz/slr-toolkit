/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package helloworldchart;

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

/**
 * 
 */

public class Bubble
{

	public final static Chart createBubble( )
	{
	
		ChartWithAxes cwaBubble = ChartWithAxesImpl.create( );
		
//		cwaBubble.setScript( "function beforeDrawAxisTitle(axis, title, scriptContext)" //$NON-NLS-1$
//				+ "{title.getCaption( ).setValue( \"Axis Title By JavaScript\");" //$NON-NLS-1$
//				+ "title.getCaption( ).getColor( ).set( 32, 168, 255 );}" //$NON-NLS-1$
//		);
		
		cwaBubble.setScript( "function beforeDrawAxisLabel(axis, label, scriptContext)" //$NON-NLS-1$
				+ "{if (label.getCaption( ).getValue( ) == 10)"
				+ "{label.getCaption().setValue( \"Apfel\" )}"
				+ "label.getCaption( ).getFont( ).setSize(7);" 
				+ "}\n"		
				);
				
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
		xAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
				
		// Y-Axis
		Axis yAxisPrimary = cwaBubble.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
//		yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 90 );
		yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setWordWrap(true);;

		// Data Set
//		NumberDataSet categoryValues = NumberDataSetImpl.create( new double[] {
//				20,45,70,100,120,130
//		});
		TextDataSet categoryValues = TextDataSetImpl.create( new String[]{
				"Birne", "Gurke"} );
		BubbleDataSet values1 = BubbleDataSetImpl.create( new BubbleEntry[]{
				new BubbleEntry( Integer.valueOf( 10 ), Integer.valueOf( 100 ) ),
				new BubbleEntry( Integer.valueOf( 20 ), Integer.valueOf( 200 ) )
				
				
		} );
		BubbleDataSet values2 = BubbleDataSetImpl.create( new BubbleEntry[]{
				new BubbleEntry( Integer.valueOf( 50 ), Integer.valueOf( 60 ) ),
				null,
				null,
				new BubbleEntry( Integer.valueOf( 43 ), Integer.valueOf( 80 ) ),
				new BubbleEntry( Integer.valueOf( 12 ), Integer.valueOf( 100 ) ),
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
		
		
		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).shift( -1 );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( bs1 );
//		sdY.getSeries( ).add( bs2 );
//		sdY.getSeries( ).add( bs3 );

		return cwaBubble;
	}
}
