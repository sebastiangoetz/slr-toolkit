package de.tudresden.slr.ui.chart.settings;

import java.util.ArrayList;
import java.util.List;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.BubbleDataTerm;
import de.tudresden.slr.ui.chart.settings.parts.AxisSettings;
import de.tudresden.slr.ui.chart.settings.parts.BlockSettings;
import de.tudresden.slr.ui.chart.settings.parts.GeneralSettings;
import de.tudresden.slr.ui.chart.settings.parts.LegendSettings;
import de.tudresden.slr.ui.chart.settings.parts.SeriesSettings;

public class BubbleChartConfiguration {

	private BubbleChartConfiguration() {
		// TODO Auto-generated constructor stub
	}
	public static BubbleChartConfiguration get() {
		return BUBBLECHARTCONFIGURATION;
	}
	private static BubbleChartConfiguration BUBBLECHARTCONFIGURATION = new BubbleChartConfiguration();

	
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
	

	
	private List<BubbleDataTerm> bubbleTermListX = new ArrayList<>();
	private List<BubbleDataTerm> bubbleTermListY = new ArrayList<>();
	private Term selectedTermX = null;
	private Term selectedTermY = null;
	
	public List<BubbleDataTerm> getBubbleTermListX() {
		return bubbleTermListX;
	}
	public void setBubbleTermListX(List<BubbleDataTerm> bubbleTermListX) {
		this.bubbleTermListX = bubbleTermListX;
	}
	public List<BubbleDataTerm> getBubbleTermListY() {
		return bubbleTermListY;
	}
	public void setBubbleTermListY(List<BubbleDataTerm> bubbleTermListY) {
		this.bubbleTermListY = bubbleTermListY;
	}
	public Term getSelectedTermX() {
		return selectedTermX;
	}
	public void setSelectedTermX(Term selectedTermX) {
		this.selectedTermX = selectedTermX;
	}
	public Term getSelectedTermY() {
		return selectedTermY;
	}
	public void setSelectedTermY(Term selectedTermY) {
		this.selectedTermY = selectedTermY;
	}
}
