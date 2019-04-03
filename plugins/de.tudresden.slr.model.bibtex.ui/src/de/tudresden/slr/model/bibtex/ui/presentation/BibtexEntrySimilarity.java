package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.ui.presentation.BibtexMergeData.Criteria;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.JaroWinkler;

public class BibtexEntrySimilarity {
	
	public BibtexEntrySimilarity() {
		// for testing reasons
	}
	
	public BibtexEntrySimilarity(DocumentImpl entry1, DocumentImpl entry2) {			
		// if doi empty -> try to get doi from url
		if (StringUtils.isBlank(entry1.getDoi()))
			entry1.setDoi(getDoiFromUrl(entry1.getUrl()));
		if (StringUtils.isBlank(entry2.getDoi()))
			entry2.setDoi(getDoiFromUrl(entry2.getUrl()));
		
		// compare doi
		DoiEquals = StringUtils.isNotBlank(entry1.getDoi()) 
				&& StringUtils.isNotBlank(entry2.getDoi()) 
				&& StringUtils.equals(entry1.getDoi(), entry2.getDoi());
		
		// get author similarity
		JaroWinkler jw = new JaroWinkler();
		authorSimilarity = jw.similarity(StringUtils.join(entry1.getAuthors(), ", "), 
				StringUtils.join(entry2.getAuthors(), ", "));
		
		// get title similarity
		Cosine c = new Cosine(2);
		titleSimilarity = c.similarity(c.getProfile(entry1.getTitle()), c.getProfile(entry2.getTitle()));
		
		// get year difference
		yearDifference = StringUtils.isNotBlank(entry1.getYear()) && StringUtils.isNotBlank(entry2.getYear()) ?
				Math.abs(Long.parseLong(entry1.getYear()) - Long.parseLong(entry2.getYear()))
				: 100;
	}
	
	static String getDoiFromUrl(String url) {
		if (url == null)
			return null;
		
		// search for pattern dd.dddd/.. in url (d means digit)
		String[] parts = url.split("/");
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if (part.matches("10\\.\\d\\d\\d\\d*") && i + 1 < parts.length) {
				//System.out.println("doi: " + parts[i] + "/" + parts[i + 1]);
				return parts[i] + "/" + parts[i + 1];
			}
		}
		return null;
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
	
	public boolean isSimilar(Map<Criteria, Integer> weights) {	
		System.out.print("\ndoi: " + DoiEquals + "\t");
		if (validateCriteria(weights, Criteria.doi))
			if (!DoiEquals)
				return false;

		System.out.print("year: " + getYearSimilarity() + "\t");
		if (validateCriteria(weights, Criteria.year))
			if (getYearSimilarity() < weights.get(Criteria.year) / 100)
				return false;

		System.out.print("aurthors: " + authorSimilarity + "\t");
		if (validateCriteria(weights, Criteria.authors))
			if (authorSimilarity < weights.get(Criteria.authors) / 100)
				return false;

		System.out.print("title: " + titleSimilarity + "\t");
		if (validateCriteria(weights, Criteria.title))
			if (titleSimilarity < weights.get(Criteria.title) / 100)
				return false;

		System.out.print("is similar!");
		return  true;		
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
				&& weights.get(criteria) > 0 
				&& weights.get(criteria) <= 100;
	}
}