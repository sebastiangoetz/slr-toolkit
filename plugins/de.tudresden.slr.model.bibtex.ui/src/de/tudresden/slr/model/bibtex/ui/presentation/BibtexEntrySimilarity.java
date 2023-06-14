package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.Map;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.ui.presentation.BibtexMergeData.Criteria;
import de.tudresden.slr.util.stringSimilarity.Cosine;
import de.tudresden.slr.util.stringSimilarity.JaroWinkler;

public class BibtexEntrySimilarity {
	
	public static int MAX_YEAR_DIFFERENCE = 10;
	
	public BibtexEntrySimilarity() {
		// for testing reasons
	}
	
	public BibtexEntrySimilarity(DocumentImpl entry1, DocumentImpl entry2) {			
		// if doi empty -> try to get doi from url
		if (entry1.getDoi() == null || entry1.getDoi().isBlank())
			entry1.setDoi(getDoiFromUrl(entry1.getUrl()));
		if (entry2.getDoi() == null || entry2.getDoi().isBlank())
			entry2.setDoi(getDoiFromUrl(entry2.getUrl()));
		
		// compare doi
		if(entry1.getDoi() != null) entry1.setDoi(entry1.getDoi().trim());
		if(entry2.getDoi() != null) entry2.setDoi(entry2.getDoi().trim());
		DoiEquals = entry1.getDoi().equals(entry2.getDoi());
		
		// get author similarity
		JaroWinkler jw = new JaroWinkler();
		authorSimilarity = jw.similarity(escapeLatexSpecialChars(String.join(", ",entry1.getAuthors())), 
				escapeLatexSpecialChars(String.join(", ",entry2.getAuthors())));
		
		// get title similarity
		Cosine c = new Cosine(2);
		titleSimilarity = c.similarity(c.getProfile(escapeLatexSpecialChars(entry1.getTitle().trim())), 
				c.getProfile(escapeLatexSpecialChars(entry2.getTitle().trim())));
		
		// get year difference
		yearDifference = !entry1.getYear().isBlank() && !entry2.getYear().isBlank()
				? Math.abs(Long.parseLong(entry1.getYear()) - Long.parseLong(entry2.getYear()))
				: 100;
	}
	
	public static String escapeLatexSpecialChars(String input) {
		for (LatexSpecialCharResolution res : LatexSpecialCharResolution.values()) {
			input = input.replaceAll(res.getSpecialChar(), res.getResolution());
		}
		
		return input;
	}
	
	public static String getDoiFromUrl(String url) {
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
		return yearDifference < MAX_YEAR_DIFFERENCE ? yearDifference : MAX_YEAR_DIFFERENCE;
	}
	public void setYearDifference(long yearDifference) {
		this.yearDifference = yearDifference;
	}
	
	public boolean isSimilar(Map<Criteria, Integer> weights) {	
		if (validateCriteria(weights, Criteria.doi))
			if (!DoiEquals)
				return false;

		if (validateCriteria(weights, Criteria.year))
			if (getYearDifference() >= weights.get(Criteria.year) && weights.get(Criteria.year) < 10)
				return false;

		if (validateCriteria(weights, Criteria.authors))
			if (Double.isNaN(authorSimilarity) || authorSimilarity < ((double) weights.get(Criteria.authors)) / 100)
				return false;
		
		if (validateCriteria(weights, Criteria.title))
			if ((Double.isNaN(titleSimilarity) && weights.get(Criteria.title) > 0) 
					|| titleSimilarity < ((double) weights.get(Criteria.title)) / 100)
				return false;

		return  true;		
	}

	private boolean validateCriteria(Map<Criteria, Integer> weights, Criteria criteria) {
		switch (criteria) {
		case doi:
			return weights != null
				&& weights.containsKey(criteria) 
				&& weights.get(criteria) != null 
				&& weights.get(criteria) > 0;
		case year:
			return weights != null
				&& weights.containsKey(criteria) 
				&& weights.get(criteria) != null
				&& weights.get(criteria) >= 0;
		default:
			return weights != null
				&& weights.containsKey(criteria) 
				&& weights.get(criteria) != null 
				&& weights.get(criteria) >= 0 
				&& weights.get(criteria) <= 100;
		}
	}
}