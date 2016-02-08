package de.tudresden.slr.model.taxonomy.util;

import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;

import de.tudresden.slr.model.taxonomy.Term;

public class TermComparer extends EqualityHelper {
	private static final long serialVersionUID = 4933778349620657774L;

	/**
	 * Compares two terms using their names (including their ancestors names) 
	 * @param obj1 The first term
	 * @param obj2 The second term
	 * @return True if objects are equal, false otherwise
	 */
	public boolean equals(Term obj1, Term obj2){
		if(obj1 == null || obj2 == null || !obj1.getName().equals(obj2.getName())){
			return false;
		}
		if(obj1.eContainer() instanceof Term ^ obj2.eContainer() instanceof Term){
			return false;
		}
		if(!(obj1.eContainer() instanceof Term) && !(obj2.eContainer() instanceof Term)){
			return true;
		}
		return equals((Term)obj1.eContainer(), (Term)obj1.eContainer());
	}
}
