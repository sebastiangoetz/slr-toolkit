/**
 */
package de.tudresden.slr.model.bibtex;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Term</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link de.tudresden.slr.model.bibtex.Term#getSubTerms <em>Sub Terms</em>}
 * </li>
 * </ul>
 * </p>
 *
 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getTerm()
 * @model
 * @generated
 */
public interface Term extends EObject {
	/**
	 * Returns the value of the '<em><b>Sub Terms</b></em>' reference list. The
	 * list contents are of type {@link de.tudresden.slr.model.bibtex.Term}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Terms</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Sub Terms</em>' reference list.
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getTerm_SubTerms()
	 * @model
	 * @generated
	 */
	EList<Term> getSubTerms();

} // Term
