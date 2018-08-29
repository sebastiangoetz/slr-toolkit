package de.tudresden.slr.model.bibtex.ui.presentation;

public class BibtexMergeConflict {
	
	private String[] entries;
	private String[] fileNames;
	// TODO: these selections will have a different background colour in the StyledTexts of the wizard to show conflicting data
//	private <textselection>[] conflicts
	
	public BibtexMergeConflict(String[] entries, String[] fileNames/*, <textselection>[] conflicts*/) throws IllegalArgumentException {
		if(entries.length == fileNames.length) {
			this.entries = entries;
			this.fileNames = fileNames;
//			this.conflicts = conflicts;
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
	
//	public <textselection> getConflict(int index){
//		return conflicts[index];
//	}
	
}
