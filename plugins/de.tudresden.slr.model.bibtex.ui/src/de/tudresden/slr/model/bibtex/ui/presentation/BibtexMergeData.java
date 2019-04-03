package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class BibtexMergeData {
	
	private List<BibtexResourceImpl> resourceList;
	private Set<Object> toMerge;
	private String filename;
	private Map<DocumentImpl, Map<DocumentImpl, BibtexEntrySimilarity>> similarityMatrix;
	private Map<Criteria, Integer> weights;
	
	public BibtexMergeData(List<BibtexResourceImpl> resources) {
		this.resourceList = resources;
		this.filename = "mergeResult.bib";
		createSimilarityMatrix();
		this.weights = new HashMap<>();
		for (Criteria value : Criteria.values()) {
			weights.put(value, 100);
		}
	}
	
	public void removeUnselectedResources() {
		for(ListIterator<BibtexResourceImpl> i = resourceList.listIterator(); i.hasNext();) { 
			if(!toMergeContains(i.next().getURI())) {
				i.remove();
			}
		}
		createSimilarityMatrix();
	}
	
	private Map<DocumentImpl, Map<DocumentImpl, BibtexEntrySimilarity>> createSimilarityMatrix() {		
		similarityMatrix = new HashMap<>();
		
		// get all entries from resourceList
		List<DocumentImpl> entries = new ArrayList<>();
		for (BibtexResourceImpl item : resourceList) {
			for (EObject content : item.getContents()) {
				entries.add((DocumentImpl) content);
			}
		}
		
		// build similarityMatrix
		entries.forEach(entry1 -> {					
			similarityMatrix.put(entry1, new HashMap<>());
			
			entries.forEach(entry2 -> {
				if (entry1 == entry2)
					return;
				
				BibtexEntrySimilarity matrixEntry = new BibtexEntrySimilarity(entry1, entry2);
				similarityMatrix.get(entry1).put(entry2, matrixEntry);
			});
		});
		
		return similarityMatrix;
	}
	
	public List<BibtexMergeConflict> getConflictsForWeights() {
		
		List<BibtexMergeConflict> conflicts = new ArrayList<>();
		for (DocumentImpl entry1 : similarityMatrix.keySet()) {
			for (DocumentImpl entry2 : similarityMatrix.get(entry1).keySet()) {
				//System.out.println("similarity: " + similarityMatrix.get(entry1).get(entry2).getTotalScore(weights));
				if (similarityMatrix.get(entry1).get(entry2).isSimilar(weights)) {
					conflicts.add(new BibtexMergeConflict(entry1, entry2));
				}
			}
		}
		
		return conflicts;
	}
	
	public List<BibtexMergeConflict> getConflicts(double threshold) {
		
		List<BibtexMergeConflict> conflicts = new ArrayList<>();
		for (DocumentImpl entry1 : similarityMatrix.keySet()) {
			for (DocumentImpl entry2 : similarityMatrix.get(entry1).keySet()) {
				//System.out.println("similarity: " + similarityMatrix.get(entry1).get(entry2).getTotalScore(weights));
				if (similarityMatrix.get(entry1).get(entry2).getTotalScore(weights) > threshold) {
					conflicts.add(new BibtexMergeConflict(entry1, entry2));
				}
			}
		}
		
		return conflicts;
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

	public Map<DocumentImpl, Map<DocumentImpl, BibtexEntrySimilarity>> getSimilarityMatrix() {
		return similarityMatrix;
	}
	
	public Map<Criteria, Integer> getWeights() {
		return weights;
	}

	public void setWeights(Map<Criteria, Integer> weights) {
		this.weights = weights;
	}
	
	public enum Criteria {
		authors, doi, title, year;
	}
}
