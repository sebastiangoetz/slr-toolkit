package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class BibtexMergeData {
	
	private List<Object> resourceList;
	private Set<Object> toMerge;
	private String filename;
	private List<BibtexMergeConflict> conflicts;
	private List<String> stats;
	
	public BibtexMergeData(List<Object> resources) {
		this.resourceList = resources;
		this.filename = "[missingFilename]";
		this.conflicts = new ArrayList<BibtexMergeConflict>();
		this.stats = new ArrayList<String>();
		this.toMerge = new HashSet<Object>();
		BibtexResourceImpl res;
		for(ListIterator<Object> i = resourceList.listIterator(); i.hasNext();) {
			res = (BibtexResourceImpl) i.next();
			toMerge.add(res.getURI());
		}
	}
	public List<Object> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Object> resourceList) {
		this.resourceList = resourceList;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Set<Object> getToMerge() {
		return toMerge;
	}
	
	public void setToMerge(Set<Object> files) {
		toMerge = files;
	}
	
	public boolean toMergeContains(Object o) {
		return toMerge.contains(o);
	}

	public List<BibtexMergeConflict> getConflicts() {
		return conflicts;
	}

	public void addConflict(BibtexMergeConflict conflict) {
		conflicts.add(conflict);
	}

	public List<String> getStats() {
		return stats;
	}

	public void addStat(String stat) {
		stats.add(stat);
	}
	
}
