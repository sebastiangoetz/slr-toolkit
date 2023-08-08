package de.tudresden.slr.model;

import org.eclipse.emf.ecore.EObject;

import de.tudresden.slr.model.taxonomy.Term;

public class TermUtils {
	/**
	 * Compares two terms using their names (including their ancestors names) 
	 * @param obj1 The first term
	 * @param obj2 The second term
	 * @return True if objects are equal, false otherwise
	 */
	public static boolean equals(Term obj1, Term obj2){
		return termToString(obj1).equals(termToString(obj2));
	}

	public static boolean contains(Term container, Term containee) {
		EObject c = containee;
		while ((c = c.eContainer()) != null) {
			if (c instanceof Term && equals(container, (Term) c)) {
				return true;
			}
		}
		return false;
	}
	
	public static String termToString(Term t) {
		String ret = t.getName().trim();
		if(t.eContainer() instanceof Term parent) {
			ret = termToString(parent)+"{"+ret+"}";
		}
		return ret;
	}
}
