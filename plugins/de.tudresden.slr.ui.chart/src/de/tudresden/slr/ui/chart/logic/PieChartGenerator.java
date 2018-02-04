package de.tudresden.slr.ui.chart.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;

import de.tudresden.slr.ui.chart.settings.ChartConfiguration;
import de.tudresden.slr.ui.chart.settings.parts.AxisSettings;
import de.tudresden.slr.ui.chart.settings.parts.BlockSettings;
import de.tudresden.slr.ui.chart.settings.parts.GeneralSettings;
import de.tudresden.slr.ui.chart.settings.parts.LegendSettings;
import de.tudresden.slr.ui.chart.settings.parts.PlotSettings;
import de.tudresden.slr.ui.chart.settings.parts.SeriesSettings;

public class PieChartGenerator
{

	public final Chart createPie(Map<String, Integer> input, String title)
	{
		ChartConfiguration cc = ChartConfiguration.PIECHARTCONFIG;//
		PlotSettings ps = cc.getPlotSettings();
		GeneralSettings gs = cc.getGeneralSettings();
		LegendSettings ls = cc.getLegendSettings();
		BlockSettings bs = cc.getBlockSettings();
		AxisSettings as = cc.getAxisSettings();
		SeriesSettings ss = cc.getSeriesSettings();
		
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create( );
		cwoaPie.setDimension( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL );
		cwoaPie.setType( "Pie Chart" ); //$NON-NLS-1$	
		cwoaPie.setSubType( "Standard Pie Chart" ); //$NON-NLS-1$
		
		// Plot
		cwoaPie.setSeriesThickness( 10 );
		cwoaPie.getBlock().setBackground(ColorDefinitionImpl.create(bs.getBlockBackgroundRGB().red, bs.getBlockBackgroundRGB().green, bs.getBlockBackgroundRGB().blue));
		cwoaPie.getBlock().getOutline().setVisible(bs.isBlockShowOutline());
		cwoaPie.getBlock().getOutline().setStyle(bs.getBlockOutlineStyle());
		cwoaPie.getBlock().getOutline().setThickness(bs.getBlockOutlineThickness());
		cwoaPie.getBlock().getOutline().setColor(ColorDefinitionImpl.create(bs.getBlockOutlineRGB().red, bs.getBlockOutlineRGB().green, bs.getBlockOutlineRGB().blue));
		
		// Legend
		Legend lg = cwoaPie.getLegend( );
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);
		lg.getTitle().getCaption().setValue(ls.getLegendTitle());
		
		lg.setBackground(ColorDefinitionImpl.create(ls.getLegendBackgroundRGB().red, ls.getLegendBackgroundRGB().green, ls.getLegendBackgroundRGB().blue));
		lg.getOutline().setVisible(ls.isLegendShowOutline());
		lg.getOutline().setStyle(ls.getLegendOutlineStyle());
		lg.getOutline().setThickness(ls.getLegendOutlineThickness());
		lg.getOutline().setColor(ColorDefinitionImpl.create(ls.getLegendOutlineRGB().red, ls.getLegendOutlineRGB().green, ls.getLegendOutlineRGB().blue));
		
		lg.getClientArea().setShadowColor(ColorDefinitionImpl.create(ls.getLegendShadowRGB().red, ls.getLegendShadowRGB().green, ls.getLegendShadowRGB().blue));
		lg.getClientArea().getInsets().set(ls.getLegendInsetTop(), ls.getLegendInsetLeft(), ls.getLegendInsetBottom(), ls.getLegendInsetRight());

		// Title
		if(gs.getChartTitle().equals("")) {
			gs.setChartTitle(title);
			}
		cwoaPie.getTitle().getLabel().getCaption().setValue(gs.getChartTitle()); //$NON-NLS-1$
		cwoaPie.getTitle().getLabel().getCaption().getFont().setSize(gs.getChartTitleSize());
		cwoaPie.getTitle().getLabel().getCaption().setColor(ColorDefinitionImpl.create(gs.getChartTitleColor().red, gs.getChartTitleColor().green, gs.getChartTitleColor().blue));
		cwoaPie.getTitle().getLabel().getCaption().getFont().setBold(gs.isChartTitleBold());
		cwoaPie.getTitle().getLabel().getCaption().getFont().setItalic(gs.isChartTitleItalic());
		cwoaPie.getTitle().getLabel().getCaption().getFont().setUnderline(gs.isChartTitleUnderline());

		// Data Set
		
		/*TextDataSet categoryValues = TextDataSetImpl.create( new String[]{
				"New York", "Boston", "Chicago", "San Francisco", "Dallas"} );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		NumberDataSet seriesOneValues = NumberDataSetImpl.create( new double[]{
				54.65, 21, 75.95, 91.28, 37.43
		} );*/
		List<String> names = new ArrayList<>();
		List<Double> values = new ArrayList<>();
		if(!title.contains("per year")) {
			input.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).forEach(x -> {
				names.add(x.getKey());
				values.add((double)x.getValue());
			});
		} else {
			input.entrySet().stream().forEach(x -> {
				names.add(x.getKey());
				values.add((double)x.getValue());
			});
		}
		
		TextDataSet categoryValues = TextDataSetImpl.create(names);
		NumberDataSet orthoValues1 = NumberDataSetImpl.create(values);
		//PercentileDataSet orthoValues2 = PercentileDataSetImpl.create();
		
		SampleData sdata = DataFactory.eINSTANCE.createSampleData( );
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData( );
		sdBase.setDataSetRepresentation( "" );//$NON-NLS-1$
		sdata.getBaseSampleData( ).add( sdBase );

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		sdOrthogonal.setDataSetRepresentation( "" );//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex( 0 );
		sdata.getOrthogonalSampleData( ).add( sdOrthogonal );

		cwoaPie.setSampleData( sdata );

		// Base Series
		Series seCategory = SeriesImpl.create( );
		seCategory.setDataSet( categoryValues );

		SeriesDefinition sd = SeriesDefinitionImpl.create( );
		cwoaPie.getSeriesDefinitions( ).add( sd );
		sd.getSeriesPalette().getEntries().clear();
		for(PieDataTerm item : cc.getPieTermList()) {
			if(item.isDisplayed()) {
				sd.getSeriesPalette().getEntries().add(ColorDefinitionImpl.create(
						item.getRgb().red, item.getRgb().green, item.getRgb().blue));
			}
		}
		
		sd.getSeries( ).add( seCategory );

		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create( );
		sePie.setDataSet( orthoValues1 );
		sePie.setSeriesIdentifier( "Cites" );//$NON-NLS-1$ 
		sePie.setExplosion( ss.getSeriesExplosion() );
		sePie.getLabel().setVisible(gs.isChartShowLabels());
		//sePie.setRatio(0.2);
		//sePie.setRotation(0.9);
		sePie.setLabelPosition(ss.getSeriesLabelPosition());
		
		
		
		
		
		SeriesDefinition sdCity = SeriesDefinitionImpl.create( );
		sd.getSeriesDefinitions( ).add( sdCity );
		sdCity.getSeries( ).add( sePie );
		
		
		return cwoaPie;
	}

}