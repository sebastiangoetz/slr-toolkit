package de.tudresden.slr.ui.chart.settings;

import java.util.ArrayList;
import java.util.List;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.BarDataTerm;
import de.tudresden.slr.ui.chart.logic.TermSort;
import de.tudresden.slr.ui.chart.settings.parts.AxisSettings;
import de.tudresden.slr.ui.chart.settings.parts.BlockSettings;
import de.tudresden.slr.ui.chart.settings.parts.GeneralSettings;
import de.tudresden.slr.ui.chart.settings.parts.LegendSettings;
import de.tudresden.slr.ui.chart.settings.parts.SeriesSettings;

public class BarChartConfiguration {

	private BarChartConfiguration() {
		
	}
	public static BarChartConfiguration get() {
		return BARCHARTCONFIGURATION;
	}
	private static BarChartConfiguration BARCHARTCONFIGURATION = new BarChartConfiguration();
	
	public GeneralSettings getGeneralSettings() {
		return GENERALSETTINGS;
	}
	public LegendSettings getLegendSettings() {
		return LegendSettings.get();
	}
	public BlockSettings getBlockSettings() {
		return BLOCKSETTINGS;
	}
	public AxisSettings getAxisSettings() {
		return  AXISSETTINGS;
	}
	public SeriesSettings getSeriesSettings() {
		return SERIESSETTINGS;
	}
	private static AxisSettings AXISSETTINGS = new AxisSettings();
	private static BlockSettings BLOCKSETTINGS = new BlockSettings();
	private static SeriesSettings SERIESSETTINGS = new SeriesSettings();
	private static GeneralSettings GENERALSETTINGS = new GeneralSettings();
	private static LegendSettings LEGENDSETTINGS = new LegendSettings();
	
	
	
	private List<BarDataTerm> barTermList = new ArrayList<>();

	private Term selectedTerm = null;
	private TermSort termSort = TermSort.SUBCLASS;
	
	public List<BarDataTerm> getBarTermList() {
		return barTermList;
	}

	public void setBarTermList(List<BarDataTerm> barTermList) {
		this.barTermList = barTermList;
		SeriesSettings.get().setSeriesColor(barTermList);
	}

	public Term getSelectedTerm() {
		return selectedTerm;
	}

	public void setSelectedTerm(Term selectedTerm) {
		this.selectedTerm = selectedTerm;
	}

	public TermSort getTermSort() {
		return termSort;
	}

	public void setTermSort(TermSort termSort) {
		this.termSort = termSort;
	}
}
