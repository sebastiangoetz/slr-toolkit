package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.ui.util.Utils;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class BibtexMergeData {
	
	private List<BibtexResourceImpl> resourceList;
	private Set<Object> toMerge;
	private String filename;
	private Set<String> conflictTitles;
	private List<BibtexMergeConflict> conflicts;
	private Set<String> simpleDuplicateTitles;
	private String stats;
	
	public BibtexMergeData(List<BibtexResourceImpl> resources) {
		this.resourceList = resources;
		this.filename = "mergeResult.bib";
		this.conflictTitles = new HashSet<String>();
		this.simpleDuplicateTitles = new HashSet<String>();
		this.conflicts = findConflicts();
		this.stats = "";
		this.toMerge = new HashSet<Object>();
		for(ListIterator<BibtexResourceImpl> i = resourceList.listIterator(); i.hasNext();) {
			toMerge.add(i.next().getURI());
		}
	}
	
	public void removeUnselectedResources() {
		for(ListIterator<BibtexResourceImpl> i = resourceList.listIterator(); i.hasNext();) { 
			if(!toMergeContains(i.next().getURI())) {
				i.remove();
			}
		}
	}
	
	private List<BibtexMergeConflict> findConflicts(){
		List<BibtexMergeConflict> result = new ArrayList<BibtexMergeConflict>();
		Map<String, List<Integer>> titleMap = new HashMap<String, List<Integer>>();
		for(int i = 0; i < resourceList.size(); i++) {
			for(EObject e : resourceList.get(i).getContents()) {
				String eTitle = ((DocumentImpl) e).getTitle().toLowerCase();
				if(titleMap.get(eTitle) == null) {
					List<Integer> newList = new ArrayList<Integer>();
					newList.add(i);
					titleMap.put(eTitle, newList);
				}
				else {
					titleMap.get(eTitle).add(i);
				}
			}
		}
		for(String s : titleMap.keySet()) {
			List<Integer> indices = titleMap.get(s);
			if(indices.size() > 1) {
				String[] duplicateEntries = new String[indices.size()];
				String[] duplicateFilenames = new String[indices.size()];
				try {
					for(int j = 0; j < duplicateEntries.length; j++) {
						IFile f = Utils.getIFilefromEMFResource(resourceList.get(indices.get(j)));
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						InputStream inputStream = f.getContents();
						byte[] buffer = new byte[1024];
						int length;
						while ((length = inputStream.read(buffer)) != -1) {
							outputStream.write(buffer, 0, length);
						}
						for(String snippet : outputStream.toString().split("@")) {
							if(snippet.toLowerCase().contains("title = {" + s + "}")) {
								duplicateEntries[j] = "@" + snippet;
								duplicateFilenames[j] = f.getName();
								break;
							}
						}
						inputStream.close();
						outputStream.close();
					}
				}
				catch(CoreException | IOException e) {
					System.err.println(e.getMessage());
				}
				List<String> conflictEntries = new ArrayList<String>();
				List<String> conflictFilenames = new ArrayList<String>();
				conflictEntries.add(duplicateEntries[0]);
				conflictFilenames.add(duplicateFilenames[0]);
				String test = duplicateEntries[0].toLowerCase();
				for(int k = 1; k < duplicateEntries.length; k++) {
					String e = duplicateEntries[k].toLowerCase();
					if(!e.startsWith(test) && !test.startsWith(e)) {
						conflictEntries.add(duplicateEntries[k]);
						conflictFilenames.add(duplicateFilenames[k]);
					}
				}
				if(conflictEntries.size() > 1) {
					result.add(new BibtexMergeConflict(conflictEntries.toArray(new String[0]), conflictFilenames.toArray(new String[0])));
					conflictTitles.add(s);
				}
				else {
					simpleDuplicateTitles.add(s);
				}
			}
		}
		return result;
	}
	
	public List<BibtexResourceImpl> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<BibtexResourceImpl> resourceList) {
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

	public Set<String> getConflictTitles() {
		return conflictTitles;
	}
	
	public Set<String> getSimpleDuplicateTitles() {
		return simpleDuplicateTitles;
	}

	public String getStats() {
		return stats;
	}

	public void addStat(String stat) {
		stats += "\n" + stat;
	}
	
}
