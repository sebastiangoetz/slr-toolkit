/**
 */
package de.tudresden.slr.model.taxonomy;

import java.util.UUID;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Term</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.tudresden.slr.model.taxonomy.Term#getName <em>Name</em>}</li>
 *   <li>{@link de.tudresden.slr.model.taxonomy.Term#getSubclasses <em>Subclasses</em>}</li>
 *   <li>{@link de.tudresden.slr.model.taxonomy.Term#getUuid <em>Uuid</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.tudresden.slr.model.taxonomy.TaxonomyPackage#getTerm()
 * @model
 * @generated
 */
public interface Term extends EObject
{
  /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see de.tudresden.slr.model.taxonomy.TaxonomyPackage#getTerm_Name()
	 * @model
	 * @generated
	 */
  String getName();

  /**
	 * Sets the value of the '{@link de.tudresden.slr.model.taxonomy.Term#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
  void setName(String value);

  /**
	 * Returns the value of the '<em><b>Subclasses</b></em>' containment reference list.
	 * The list contents are of type {@link de.tudresden.slr.model.taxonomy.Term}.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Subclasses</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Subclasses</em>' containment reference list.
	 * @see de.tudresden.slr.model.taxonomy.TaxonomyPackage#getTerm_Subclasses()
	 * @model containment="true"
	 * @generated
	 */
  EList<Term> getSubclasses();

		/**
	 * Returns the value of the '<em><b>Uuid</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uuid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uuid</em>' attribute.
	 * @see #setUuid(UUID)
	 * @see de.tudresden.slr.model.taxonomy.TaxonomyPackage#getTerm_Uuid()
	 * @model default="" id="true" dataType="de.tudresden.slr.model.taxonomy.EUUID" required="true"
	 * @generated
	 */
	UUID getUuid();

		/**
	 * Sets the value of the '{@link de.tudresden.slr.model.taxonomy.Term#getUuid <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uuid</em>' attribute.
	 * @see #getUuid()
	 * @generated
	 */
	void setUuid(UUID value);

} // Term
