package de.tudresden.slr.model.bibtex.ui.presentation;

public class BibtexMergeConflict {
	
	private String[] entries;
	private String[] fileNames;
	
	public BibtexMergeConflict(String[] entries, String[] fileNames) throws IllegalArgumentException {
		if(entries.length == fileNames.length) {
			this.entries = entries;
			this.fileNames = fileNames;
		}
		else throw(new IllegalArgumentException("Argument lengths do not match!"));
	}

	public int amountOfEntries() {
		return entries.length;
	}

	public String getEntry(int index) {
		return entries[index];
	}
	
	public String getFileName(int index) {
		return fileNames[index];
	}
	
}
