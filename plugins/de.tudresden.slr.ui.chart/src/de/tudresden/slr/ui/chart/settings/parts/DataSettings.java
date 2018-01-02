package de.tudresden.slr.ui.chart.settings.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.swt.graphics.RGB;

import de.tudresden.slr.model.taxonomy.Term;

public class DataSettings {
	
	private List<RGB> colorList = new ArrayList<>();
	private SortedMap<String, Boolean> visibleMap = new TreeMap<String, Boolean>();
	private Term selectedTerm = null;
	private boolean perSubTerm = true;

	private DataSettings() {}
	
	public static DataSettings get() {
		return DATASETTINGS;
	}
	
	private static DataSettings DATASETTINGS = new DataSettings();

	public List<RGB> getColorList() {return colorList;}

	public void setColorList(List<RGB> colorList) {this.colorList = colorList;}

	public SortedMap<String, Boolean> getVisibleMap() {return visibleMap;}

	public void setVisibleMap(SortedMap<String, Boolean> visibleMap) {this.visibleMap = visibleMap;}

	public Term getSelectedTerm() {return selectedTerm;}

	public void setSelectedTerm(Term selectedTerm) {this.selectedTerm = selectedTerm;}

	public boolean isPerSubTerm() {return perSubTerm;}

	public void setPerSubTerm(boolean perSubTerm) {this.perSubTerm = perSubTerm;}
	
	
	
}
