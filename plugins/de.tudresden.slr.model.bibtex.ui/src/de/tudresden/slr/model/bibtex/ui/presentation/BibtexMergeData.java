package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class BibtexMergeData {

	private List<BibtexResourceImpl> resourceList;
	private Set<Object> toMerge;
	private Map<DocumentImpl, Map<DocumentImpl, BibtexEntrySimilarity>> similarityMatrix;
	private List<BibtexMergeConflict> conflicts;
	private Set<DocumentImpl> intersection;
	private Map<Criteria, Integer> weights;

	public BibtexMergeData(List<BibtexResourceImpl> resources) {
		this.resourceList = resources;
		createSimilarityMatrix();
		this.weights = new HashMap<>();
		for (Criteria value : Criteria.values()) {
			weights.put(value, 100);
		}
		this.conflicts = new ArrayList<>();
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
		
		intersection = new HashSet<>(similarityMatrix.keySet());

		return similarityMatrix;
	}

	public List<BibtexMergeConflict> getConflicts() {
		return conflicts;
	}
	
	public Set<DocumentImpl> getIntersection() {
		return intersection;
	}

	public void extractConflicts() {
		Map<DocumentImpl, List<DocumentImpl>> conflictedEntries = new HashMap<>();
		conflicts = new ArrayList<>();
		for (DocumentImpl entry1 : similarityMatrix.keySet()) {
			for (DocumentImpl entry2 : similarityMatrix.get(entry1).keySet()) {
				if (similarityMatrix.get(entry1).get(entry2).isSimilar(weights) 
						&& !(conflictedEntries.containsKey(entry1) && conflictedEntries.get(entry1).contains(entry2))) {
					conflicts.add(new BibtexMergeConflict(entry1, entry2));
					
					// store conflictedEntries
					List<DocumentImpl> similarities = conflictedEntries.containsKey(entry1) ? conflictedEntries.get(entry1) : new ArrayList<>();
					similarities.add(entry2);
					conflictedEntries.put(entry1, similarities);
					similarities = conflictedEntries.containsKey(entry2) ? conflictedEntries.get(entry2) : new ArrayList<>();
					similarities.add(entry1);
					conflictedEntries.put(entry2, similarities);
				}
			}
		}
		
		// remove conflicted entries from intersection
		intersection = new HashSet<>(similarityMatrix.keySet());
		for (DocumentImpl conflictedEntry : conflictedEntries.keySet()) intersection.remove(conflictedEntry);
	}

	public List<BibtexResourceImpl> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<BibtexResourceImpl> resourceList) {
		this.resourceList = resourceList;
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

	public void setWeight(Criteria criteria, Integer weight) {
		weights.put(criteria, weight);
		extractConflicts();
	}

	public void setWeights(Map<Criteria, Integer> weights) {
		this.weights = weights;
		extractConflicts();
	}

	public int getNumberOfPossibleConflicts() {
		return similarityMatrix.size() * (similarityMatrix.size() - 1) / 2 - 1;
	}

	public int getNumerOfEntries() {
		return similarityMatrix.size();
	}

	public enum Criteria {
		authors, doi, title, year;
	}

	public String writeIntersection() {
		return intersection.stream().map(entry -> (new BibtexMergeConflict(entry, entry)).printResult())
				.collect(Collectors.joining("\n"));
	}
	
	public String writeUnion() {
		return this.getConflicts().stream().map(conflict -> conflict.printResult())
				.collect(Collectors.joining("\n"));
	}
}
