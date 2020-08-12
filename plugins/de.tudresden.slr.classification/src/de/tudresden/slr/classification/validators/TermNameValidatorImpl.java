package de.tudresden.slr.classification.validators;

import org.eclipse.jface.dialogs.IInputValidator;

public class TermNameValidatorImpl implements ITermNameValidator, IInputValidator{

	public TermNameValidatorImpl() {
	}
	
	@Override
	public boolean isTermNameValid(String name) {
		return Character.isLetter(name.charAt(0)) && name.matches("[A-Za-z0-9 ]+");
	}
	
	@Override
	public String isValid(String string) {
		return (isTermNameValid(string)) ? null : "";
	}

}
