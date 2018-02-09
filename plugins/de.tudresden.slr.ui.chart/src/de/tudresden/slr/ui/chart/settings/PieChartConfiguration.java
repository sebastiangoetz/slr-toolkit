package de.tudresden.slr.ui.chart.settings;

import java.util.ArrayList;
import java.util.List;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.PieDataTerm;
import de.tudresden.slr.ui.chart.logic.TermSort;
import de.tudresden.slr.ui.chart.settings.parts.AxisSettings;
import de.tudresden.slr.ui.chart.settings.parts.BlockSettings;
import de.tudresden.slr.ui.chart.settings.parts.GeneralSettings;
import de.tudresden.slr.ui.chart.settings.parts.LegendSettings;
import de.tudresden.slr.ui.chart.settings.parts.SeriesSettings;

public class PieChartConfiguration {

	private PieChartConfiguration() {
		// TODO Auto-generated constructor stub
	}
	public static PieChartConfiguration get() {
		return PIECHARTCONFIGURATION;
	}
	private static PieChartConfiguration PIECHARTCONFIGURATION = new PieChartConfiguration();

	
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
	

	private List<PieDataTerm> pieTermList = new ArrayList<>();
	
	private Term pieSelectedTerm = null;
	private TermSort pieTermSort = TermSort.SUBCLASS;
	
	
	public List<PieDataTerm> getPieTermList() {
		return pieTermList;
	}
	public void setPieTermList(List<PieDataTerm> pieTermList) {
		this.pieTermList = pieTermList;
		SeriesSettings.get().setPieSeriesColor(pieTermList);
	}
	public Term getPieSelectedTerm() {
		return pieSelectedTerm;
	}
	public void setPieSelectedTerm(Term pieSelectedTerm) {
		this.pieSelectedTerm = pieSelectedTerm;
	}
	public TermSort getPieTermSort() {
		return pieTermSort;
	}
	public void setPieTermSort(TermSort pieTermSort) {
		this.pieTermSort = pieTermSort;
	}

	
	
	
}
