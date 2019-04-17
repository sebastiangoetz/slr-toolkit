package de.tudresden.slr.model.bibtex.ui.presentation;

public enum LatexSpecialCharResolution {
	i("\\{\\\\'\\\\i}", "i"), a("\\{\\\\'ae}", "a"), o("\\{\\\\'o}", "oe"),
	I("\\{\\\\'\\\\I}", "I"), A("\\{\\\\'Ae}", "A"), O("\\{\\\\'O}", "Oe"),
	AE1("\\{\\\\\"A}", "Ae"), OE1("\\{\\\\\"Oe}", "Oe"), UE1("\\{\\\"U}", "Ue"),
	ae1("\\{\\\\\\\"a}", "ae"), oe1("\\{\\\\\\\"o}", "oe"), ue1("\\{\\\\\\\"u}", "ue"),
	AE("\\\\\\\"A", "Ae"), OE("\\\\\\\"O", "Oe"), UE("\\\\\\\"U", "Ue"), 
	ae("\\\"a", "ae"), oe("\\\"o", "oe"), ue("\\\"u", "ue"),
	SS1("\\{\\\\ss}", "ss"), SS("\\\\ss", "ss");
	
	private String specialChar;
	private String resolution;
	
	private LatexSpecialCharResolution(String specialChar, String resolution) {
		this.specialChar = specialChar;
		this.resolution = resolution;
	}

	public String getSpecialChar() {
		return specialChar;
	}

	public String getResolution() {
		return resolution;
	}
}
