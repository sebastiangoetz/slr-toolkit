package de.tudresden.slr.classification.validators;

/**
 * Transforms strings that would be invalid when used as term names into valid
 * ones
 * 
 * @param name  Term name to be transformed
 * @return Valid term name
 */
public interface IMalformedTermNameHandler {
	public String handleMalformedTermName(String name);
}