package de.tudresden.slr.model.taxonomy.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;

import de.tudresden.slr.model.taxonomy.Term;

public class TermComparator extends EqualityHelper {
	private static final long serialVersionUID = 4933778349620657774L;

	/**
	 * Compares two terms using their names (including their ancestors names) 
	 * @param obj1 The first term
	 * @param obj2 The second term
	 * @return True if objects are equal, false otherwise
	 */
	public boolean equals(Term obj1, Term obj2){
		return buildQualifiedTermName(obj1).equals(buildQualifiedTermName(obj2));
	}
	
	private String buildQualifiedTermName(Term term){
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		sb.append(term.getName());
		EObject container = term.eContainer();
		while(container instanceof Term){
			sb.append("/");
			sb.append(((Term)container).getName());
			container = container.eContainer();
		}
		return sb.toString();
	}
}
