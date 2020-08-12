package de.tudresden.slr.classification.validators;


public interface ITermNameValidator {
	
	/**
	 * Checks whether the supplied string is 
	 * 
	 * @param name  A term name to be validated
	 * @return A boolean indicating whether the supplied string would be a valid term name
	 */
	public boolean isTermNameValid(String name);
}
