/**
 */
package de.tudresden.slr.model.bibtex;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>File</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link de.tudresden.slr.model.bibtex.BibtexFile#getEntries
 * <em>Entries</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.BibtexFile#getPath
 * <em>Path</em>}</li>
 * </ul>
 *
 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getBibtexFile()
 * @model
 * @generated
 */
public interface BibtexFile extends EObject {
	/**
	 * Returns the value of the '<em><b>Entries</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link de.tudresden.slr.model.bibtex.Document}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entries</em>' containment reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Entries</em>' containment reference list.
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getBibtexFile_Entries()
	 * @model containment="true"
	 * @generated
	 */
	EList<Document> getEntries();

	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getBibtexFile_Path()
	 * @model
	 * @generated
	 */
	String getPath();

	/**
	 * Sets the value of the
	 * '{@link de.tudresden.slr.model.bibtex.BibtexFile#getPath <em>Path</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
	void setPath(String value);

} // BibtexFile
