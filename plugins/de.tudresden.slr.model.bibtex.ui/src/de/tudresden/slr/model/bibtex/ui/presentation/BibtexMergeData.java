package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.JaroWinkler;

public class BibtexMergeData {
	
	private List<BibtexResourceImpl> resourceList;
	private Set<Object> toMerge;
	private String filename;
	private Map<DocumentImpl, Map<DocumentImpl, BibtexEntrySimilarity>> similarityMatrix;
	private Map<Criteria, Integer> weights;
	
	public BibtexMergeData(List<BibtexResourceImpl> resources) {
		this.resourceList = resources;
		this.filename = "mergeResult.bib";
		this.similarityMatrix = createSimilarityMatrix();
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
		resourceList.forEach(item -> {
			item.getContents().forEach(content -> {
				entries.add((DocumentImpl) content);
			});
		});
		
		// build similarityMatrix
		entries.forEach(entry1 -> {					
			similarityMatrix.put(entry1, new HashMap<>());
			
			// search for dois
			if (entry1.getDoi() != null)
				System.out.println("Doi: " + entry1.getDoi());
			entries.forEach(entry2 -> {
				if (entry1 == entry2)
					return;
				
				BibtexEntrySimilarity matrixEntry = new BibtexEntrySimilarity(entry1, entry2);
				similarityMatrix.get(entry1).put(entry2, matrixEntry);
				if ((matrixEntry.getAuthorSimilarity() + matrixEntry.getTitleSimilarity()) / 2 > 0.9) {
					System.out.println("title similarity: " + matrixEntry.getTitleSimilarity());
					System.out.println("author similarity: " + matrixEntry.getAuthorSimilarity());
					System.out.println(entry1.toString());
					System.out.println(entry2.toString());
				}
			});
		});
		
		return similarityMatrix;
	}
	
	public List<BibtexMergeConflict> getConflicts(double threshold) {
		
		List<BibtexMergeConflict> conflicts = new ArrayList<>();
		for (DocumentImpl entry1 : similarityMatrix.keySet()) {
			for (DocumentImpl entry2 : similarityMatrix.get(entry1).keySet()) {
				System.out.println("similarity: " + similarityMatrix.get(entry1).get(entry2).getTotalScore(weights));
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

	public class BibtexEntrySimilarity {
		
		public BibtexEntrySimilarity() {
			// for testing reasons
		}
		
		public BibtexEntrySimilarity(DocumentImpl entry1, DocumentImpl entry2) {
			JaroWinkler jw = new JaroWinkler();
			authorSimilarity = jw.similarity(StringUtils.join(entry1.getAuthors(), ", "), 
					StringUtils.join(entry2.getAuthors(), ", "));
			
			Cosine c = new Cosine(2);
			titleSimilarity = c.similarity(c.getProfile(entry1.getTitle()), c.getProfile(entry2.getTitle()));
			
			DoiEquals = StringUtils.isNotBlank(entry1.getDoi()) 
					&& StringUtils.isNotBlank(entry2.getDoi()) 
					&& StringUtils.equals(entry1.getDoi(), entry2.getDoi());
			
			yearDifference = StringUtils.isNotBlank(entry1.getYear()) && StringUtils.isNotBlank(entry2.getYear()) ?
					Math.abs(Long.parseLong(entry1.getYear()) - Long.parseLong(entry2.getYear()))
					: 100;
		}
		
		public boolean equals(Map<Criteria, Integer> weights, double threshold) {
			return DoiEquals || getTotalScore(weights) > threshold;
		}
		
		private double authorSimilarity;
		private double titleSimilarity;
		private boolean DoiEquals;
		private long yearDifference;
		
		public double getAuthorSimilarity() {
			return authorSimilarity;
		}
		public void setAuthorSimilarity(double authorSimilarity) {
			this.authorSimilarity = authorSimilarity;
		}
		public double getTitleSimilarity() {
			return titleSimilarity;
		}
		public void setTitleSimilarity(double titleSimilarity) {
			this.titleSimilarity = titleSimilarity;
		}
		public boolean isDoiEquals() {
			return DoiEquals;
		}
		public void setDoiEquals(boolean doiEquals) {
			DoiEquals = doiEquals;
		}
		public long getYearDifference() {
			return yearDifference;
		}
		public void setYearDifference(long yearDifference) {
			this.yearDifference = yearDifference;
		}
		
		public double getYearSimilarity() {
			// if entries are more than 10 years apart, there is no similarity
			return yearDifference < 10 ? 1 - ((double) yearDifference / 10) : 0;
		}
		
		public double getTotalScore(Map<Criteria, Integer> weights) {
			double totalScore = 0;
			int totalWeight = 0;
			
			if (validateCriteria(weights, Criteria.doi)) {
				if (DoiEquals)
					totalScore += ((double) 1 / 100) * weights.get(Criteria.doi);
				totalWeight += weights.get(Criteria.doi);
			} 
			if (validateCriteria(weights, Criteria.year)) {
				totalScore += ((double) getYearSimilarity() / 100) * weights.get(Criteria.year);
				totalWeight += weights.get(Criteria.year);
			} 
			if (validateCriteria(weights, Criteria.authors)) {
				totalScore += ((double) authorSimilarity / 100) * weights.get(Criteria.authors);
				totalWeight += weights.get(Criteria.authors);
			}
			if (validateCriteria(weights, Criteria.title)) {
				totalScore += ((double) titleSimilarity / 100) * weights.get(Criteria.title);
				totalWeight += weights.get(Criteria.title);
			}
			
			return  totalWeight > 0 ? ((double) totalScore / totalWeight) * 100 : 0;
		}

		private boolean validateCriteria(Map<Criteria, Integer> weights, Criteria criteria) {
			return weights.containsKey(criteria) 
					&& weights.get(criteria) != null 
					&& weights.get(criteria) >= 0 
					&& weights.get(criteria) <= 100;
		}
	}
	
	public class BibtexMergeConflict {
		private DocumentImpl resource1;
		private DocumentImpl resource2;
		private DocumentImpl finalResource;
		
		public BibtexMergeConflict(DocumentImpl resource1, DocumentImpl resource2) {
			this.resource1 = resource1;
			this.resource2 = resource2;
		}
		
		public void useResource1() {
			finalResource = resource1;
		}

		public void useResource2() {
			finalResource = resource2;
		}
		
		public DocumentImpl getResource1() {
			return resource1;
		}
		
		public void setResource1(DocumentImpl resource1) {
			this.resource1 = resource1;
		}
		
		public DocumentImpl getResource2() {
			return resource2;
		}
		
		public void setResource2(DocumentImpl resource2) {
			this.resource2 = resource2;
		}
		
		public DocumentImpl getFinalResource() {
			return finalResource;
		}
		
		public void setFinalResource(DocumentImpl finalResource) {
			this.finalResource = finalResource;
		}
	}
	
	public enum Criteria {
		authors, doi, title, year;
	}
}
