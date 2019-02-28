package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.ui.util.Utils;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.JaroWinkler;

public class BibtexMergeData {
	
	private List<BibtexResourceImpl> resourceList;
	private Set<Object> toMerge;
	private String filename;
	private Set<String> conflictTitles;
	private List<BibtexMergeConflict> conflicts;
	private Map<DocumentImpl, Map<DocumentImpl, BibtexEntrySimilarity>> similarityMatrix;
	private Set<String> simpleDuplicateTitles;
	private String stats;
	private Map<Criteria, Integer> criteria;
	
	public BibtexMergeData(List<BibtexResourceImpl> resources) {
		this.resourceList = resources;
		this.filename = "mergeResult.bib";
		this.conflictTitles = new HashSet<String>();
		this.simpleDuplicateTitles = new HashSet<String>();
		this.conflicts = findConflicts();
		this.similarityMatrix = createSimilarityMatrix();
		this.criteria = new HashMap<>();
		for (Criteria value : Criteria.values()) {
			criteria.put(value, 95);
		}
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
		findConflicts();
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
//		entries.forEach(entry1 -> {					
//			System.out.println(entry1.toString());
//			similarityMatrix.put(entry1, new HashMap<>());
//			entries.forEach(entry2 -> {
//				System.out.println(entry2.toString());
//				if (entry1 == entry2)
//					return;
//				
//				similarityMatrix.get(entry1).put(entry2, new BibtexEntrySimilarity(entry1, entry2));
//			});
//		});
		
		return similarityMatrix;
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
				String test = "";//duplicateEntries[0].toLowerCase();
				for(int k = 1; k < duplicateEntries.length; k++) {
					String e = "";//duplicateEntries[k].toLowerCase();
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

	public Map<DocumentImpl, Map<DocumentImpl, BibtexEntrySimilarity>> getSimilarityMatrix() {
		return similarityMatrix;
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
	
	public Map<Criteria, Integer> getCriteria() {
		return criteria;
	}

	public void setCriteria(Map<Criteria, Integer> criteria) {
		this.criteria = criteria;
	}

	public class BibtexEntrySimilarity {
		
		public BibtexEntrySimilarity(DocumentImpl entry1, DocumentImpl entry2) {
			JaroWinkler jw = new JaroWinkler();
			authorSimilarity = jw.similarity(StringUtils.join(entry1.getAuthors(), ", "), 
					StringUtils.join(entry2.getAuthors(), ", "));
			
			Cosine c = new Cosine(2);
			titleSimilarity = c.similarity(c.getProfile(entry1.getTitle()), c.getProfile(entry2.getTitle()));
			
			DoiEquals = StringUtils.equals(entry1.getDoi(), entry2.getDoi());
			
			//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			//LocalDate date1 = LocalDate.parse("1/" + (entry1.getMonth().isEmpty() ? "01" : entry1.getMonth()) + "/" + entry1.getYear(), formatter);
			//LocalDate date2 = LocalDate.parse("1/" + (entry2.getMonth().isEmpty() ? "01" : entry2.getMonth()) + "/" + entry2.getYear(), formatter);
			//yearMonthDifference = Math.abs(Long.parseLong(entry1.getYear()) - Long.parseLong(entry2.getYear()));
		}
		
		public boolean equals(double threshold) {
			return DoiEquals || getTotalScore() > threshold;
		}
		
		private double authorSimilarity;
		private double titleSimilarity;
		private boolean DoiEquals;
		private double urlSimilarity;
		private long yearMonthDifference;
		
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
		public double getUrlSimilarity() {
			return urlSimilarity;
		}
		public void setUrlSimilarity(double urlSimilarity) {
			this.urlSimilarity = urlSimilarity;
		}
		public long getYearMonthDifference() {
			return yearMonthDifference;
		}
		public void setYearMonthDifference(long yearMonthDifference) {
			this.yearMonthDifference = yearMonthDifference;
		}
		
		public double getTotalScore() {
			return (authorSimilarity + titleSimilarity + urlSimilarity) / 3;
		}
	}
	
	public enum Criteria {
		authors, doi, title, url;
	}
}
