package de.tudresden.slr.classification.validators;

public class TermNameValidatorImpl implements ITermNameValidator{

	public TermNameValidatorImpl() {
	}
	
	public boolean isTermNameValid(String name) {
		return Character.isLetter(name.charAt(0)) && name.matches("[A-Za-z0-9 ]+");
	}

}
