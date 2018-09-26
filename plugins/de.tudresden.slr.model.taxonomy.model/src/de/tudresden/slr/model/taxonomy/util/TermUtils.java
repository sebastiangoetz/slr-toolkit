package de.tudresden.slr.model.taxonomy.util;

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
		if(obj1 == null || obj2 == null || !obj1.getName().equals(obj2.getName())){
			return false;
		}
		if(obj1.eContainer() instanceof Term ^ obj2.eContainer() instanceof Term){
			return false;
		}
		if(!(obj1.eContainer() instanceof Term) && !(obj2.eContainer() instanceof Term)){
			return true;
		}
		return equals((Term)obj1.eContainer(), (Term)obj2.eContainer());
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
	
}
