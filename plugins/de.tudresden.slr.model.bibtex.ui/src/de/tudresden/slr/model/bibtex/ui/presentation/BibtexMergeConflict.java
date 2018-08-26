package de.tudresden.slr.model.bibtex.ui.presentation;

public class BibtexMergeConflict {
	
	private int noOfConflicts;
	private String[] snippets;
	private long[] lineIndices;
	private String description;
	
	public BibtexMergeConflict(String[] snippets, long[] lineIndices, String description) throws IllegalArgumentException {
		if(snippets.length == lineIndices.length) {
			this.noOfConflicts = snippets.length;
			this.snippets = snippets;
			this.lineIndices = lineIndices;
			this.description = description;
		}
		else throw(new IllegalArgumentException("Amount of snippets and line indices not equal!"));
	}

	public int getNoOfConflicts() {
		return noOfConflicts;
	}

	public String[] getSnippets() {
		return snippets;
	}

	public long[] getLineIndices() {
		return lineIndices;
	}

	public String getDescription() {
		return description;
	}	
	
}
