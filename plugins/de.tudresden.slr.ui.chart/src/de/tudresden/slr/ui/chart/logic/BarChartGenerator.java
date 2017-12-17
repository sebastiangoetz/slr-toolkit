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

package de.tudresden.slr.ui.chart.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.PaletteImpl;
import org.eclipse.birt.chart.model.component.Axis;
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
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.internal.themes.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Text;

import de.tudresden.slr.ui.chart.settings.ChartConfiguration;
import de.tudresden.slr.ui.chart.settings.parts.AxisSettings;
import de.tudresden.slr.ui.chart.settings.parts.BlockSettings;
import de.tudresden.slr.ui.chart.settings.parts.GeneralSettings;
import de.tudresden.slr.ui.chart.settings.parts.LegendSettings;
import de.tudresden.slr.ui.chart.settings.parts.PlotSettings;
import de.tudresden.slr.ui.chart.settings.parts.SeriesSettings;

public class BarChartGenerator {

	/**
	 * Creates a bar chart from a given input
	 * @param input Input data
	 * @param title Chart title
	 * @return The bar chart
	 */
	public final Chart createBar(Map<String, Integer> input, String title) {
		// See: http://www.eclipsezone.com/eclipse/forums/t67188.html
		
		ChartConfiguration cc = ChartConfiguration.get(); //
		PlotSettings ps = cc.getPlotSettings();
		GeneralSettings gs = cc.getGeneralSettings();
		LegendSettings ls = cc.getLegendSettings();
		BlockSettings bs = cc.getBlockSettings();
		AxisSettings as = cc.getAxisSettings();
		SeriesSettings ss = cc.getSeriesSettings();
		
		RGB rgb = new RGB(0,255,0);
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setType(gs.getChartType());
		cwaBar.setSubType(gs.getChartSubType()); //$NON-NLS-1$
		cwaBar.setOrientation(Orientation.VERTICAL_LITERAL);

		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.create(bs.getBlockBackgroundRGB().red, bs.getBlockBackgroundRGB().green, bs.getBlockBackgroundRGB().blue));
		cwaBar.getBlock().getOutline().setVisible(bs.isBlockShowOutline());
		cwaBar.getBlock().getOutline().setStyle(bs.getBlockOutlineStyle());
		cwaBar.getBlock().getOutline().setThickness(bs.getBlockOutlineThickness());
		cwaBar.getBlock().getOutline().setColor(ColorDefinitionImpl.create(bs.getBlockOutlineRGB().red, bs.getBlockOutlineRGB().green, bs.getBlockOutlineRGB().blue));
		
		
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(ps.getPlotBackgroundRGB().red, ps.getPlotBackgroundRGB().green, ps.getPlotBackgroundRGB().blue));
		p.getClientArea().setShadowColor(ColorDefinitionImpl.create(ps.getPlotShadowRGB().red, ps.getPlotShadowRGB().green, ps.getPlotShadowRGB().blue));
		p.getClientArea().getInsets().set(ps.getPlotInsetTop(), ps.getPlotInsetLeft(), ps.getPlotInsetBottom(), ps.getPlotInsetRight());
		p.setHorizontalSpacing(ps.getPlotHorizontalSpacing());
		p.setVerticalSpacing(ps.getPlotVerticalSpacing());
		
		
		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue(gs.getChartTitle()); //$NON-NLS-1$
		cwaBar.getTitle().getLabel().getCaption().getFont().setSize(gs.getChartTitleSize());
		cwaBar.getTitle().getLabel().getCaption().setColor(ColorDefinitionImpl.create(gs.getChartTitleColor().red, gs.getChartTitleColor().green, gs.getChartTitleColor().blue));
		cwaBar.getTitle().getLabel().getCaption().getFont().setBold(gs.isChartTitleBold());
		cwaBar.getTitle().getLabel().getCaption().getFont().setItalic(gs.isChartTitleItalic());
		cwaBar.getTitle().getLabel().getCaption().getFont().setUnderline(gs.isChartTitleUnderline());
		
		
		// Legend
		Legend lg = cwaBar.getLegend();
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);
		
		lg.setBackground(ColorDefinitionImpl.create(ls.getLegendBackgroundRGB().red, ls.getLegendBackgroundRGB().green, ls.getLegendBackgroundRGB().blue));
		lg.getOutline().setVisible(ls.isLegendShowOutline());
		lg.getOutline().setStyle(ls.getLegendOutlineStyle());
		lg.getOutline().setThickness(ls.getLegendOutlineThickness());
		lg.getOutline().setColor(ColorDefinitionImpl.create(ls.getLegendOutlineRGB().red, ls.getLegendOutlineRGB().green, ls.getLegendOutlineRGB().blue));
		
		lg.getClientArea().setShadowColor(ColorDefinitionImpl.create(ls.getLegendShadowRGB().red, ls.getLegendShadowRGB().green, ls.getLegendShadowRGB().blue));
		lg.getClientArea().getInsets().set(ls.getLegendInsetTop(), ls.getLegendInsetLeft(), ls.getLegendInsetBottom(), ls.getLegendInsetRight());
		//lg.setDirection(ls.getLegendDirection());
		lg.setItemType(ls.getLegendItemType());
		lg.setMaxPercent(ls.getLegendMaxPercent());
		lg.setOrientation(ls.getLegendOrientation());
		lg.setPosition(ls.getLegendPosition());
		
		lg.getSeparator().setVisible(ls.isLegendShowSeparator());
		lg.getSeparator().setStyle(ls.getLegendSeparatorStyle());
		lg.getSeparator().setThickness(ls.getLegendSeparatorThickness());
		lg.getSeparator().setColor(ColorDefinitionImpl.create(ls.getLegendSeparatorRGB().red, ls.getLegendSeparatorRGB().green, ls.getLegendSeparatorRGB().blue));
		
		lg.getText().getFont().getAlignment().setHorizontalAlignment(ls.getLegendHorizontalAlignment());
		lg.getText().getFont().getAlignment().setVerticalAlignment(ls.getLegendVerticalAlignment());
		lg.getText().getFont().setBold(ls.isLegendTextBold());
		lg.getText().getFont().setItalic(ls.isLegendTextItalic());
		lg.getText().getFont().setRotation(ls.getLegendTextRotation());
		lg.getText().getFont().setSize(ls.getLegendTextSize());
		lg.getText().getFont().setStrikethrough(ls.isLegendTextStrikeThrough());
		lg.getText().getFont().setUnderline(ls.isLegendTextUnderline());
		lg.getText().getFont().setWordWrap(ls.isLegendWordWrap());
		lg.setTitlePercent(ls.getLegendTitlePercent());
		lg.setTitlePosition(ls.getLegendTitlePosition());
		lg.setWrappingSize(ls.getLegendWrappingSize());
		
		lg.getTitle().getCaption().setValue("Hallo");
		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(as.getxAxisTickStyle());
		xAxisPrimary.getOrigin().setType(as.getxAxisIntersectionType());
		
		xAxisPrimary.getLabel().getCaption().getFont().setSize(as.getAxisFontSize());
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(as.getxAxisRotation());
		xAxisPrimary.getTitle().getCaption().setValue(as.getxAxisTitle());
		xAxisPrimary.getTitle().getCaption().getFont().setSize(as.getAxisFontSize());
		xAxisPrimary.getTitle().setVisible(as.isxAxisTitleActive());
		xAxisPrimary.getScale().setTickBetweenCategories(as.isxAxisTickBetweenCategories());

		//xAxisPrimary.getSubTitle().getCaption().setValue("Jürgen");
		//TODO: Find a more intelligent way to set the rotation...
		//Rotate labels even further if we have many bars
		if(input.size() > 15){
			xAxisPrimary.getLabel().getCaption().getFont().setRotation(90);
		}
		xAxisPrimary.getLabel().getCaption().getFont().setName("Arial");

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.RIGHT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.setOrientation(Orientation.VERTICAL_LITERAL);
		
		yAxisPrimary.getLabel().getCaption().getFont().setSize(as.getAxisFontSize());
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(as.getyAxisRotation());
		yAxisPrimary.getTitle().setVisible(as.isyAxisTitleActive());
		yAxisPrimary.getTitle().getCaption().setValue(as.getyAxisTitle());
		yAxisPrimary.getTitle().getCaption().getFont().setSize(as.getyAxisTitleSize());
		yAxisPrimary.getScale().setStep(as.getyAxisScaleStep());
		
		
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
		
		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("");
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal1 = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal1.setDataSetRepresentation("");
		sdOrthogonal1.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal1);

		cwaBar.setSampleData(sd);

		// X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		
		ArrayList col1 = new ArrayList<Fill>();
		col1.add(ColorDefinitionImpl.create(0, 0, 255));
		col1.add(ColorDefinitionImpl.create(0, 255, 0));
		col1.add(ColorDefinitionImpl.create(255, 0, 0));
		ss.setSeriesColor(col1);
		
		
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		
		if(ss.isSeriesUseCustomColors()) {
			sdX.getSeriesPalette().eSet(sdX.getSeriesPalette().eContainingFeature(), ss.getSeriesColor());
		} else {
			sdX.getSeriesPalette().shift(1);
		}
		
		//sdX.getSeriesPalette().update(ColorDefinitionImpl.create(20, 20, 10));
		////SHIFT COLOR PALETTE
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);
		
		
		
		
		// Y-Series
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setDataSet(orthoValues1);
		bs1.getLabel().setVisible(ss.isSeriesShowLabels());
		bs1.setLabelPosition(Position.OUTSIDE_LITERAL);
		bs1.setTranslucent(ss.isSeriesTranslucent());
		bs1.setStacked(ss.isSeriesStacked());
		
		
		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);

		return cwaBar;
	}
	
}
