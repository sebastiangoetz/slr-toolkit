/**
 */
package de.tudresden.slr.model.taxonomy;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.tudresden.slr.model.taxonomy.Model#getDimensions <em>Dimensions</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.tudresden.slr.model.taxonomy.TaxonomyPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject
{
  /**
   * Returns the value of the '<em><b>Dimensions</b></em>' containment reference list.
   * The list contents are of type {@link de.tudresden.slr.model.taxonomy.Term}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Dimensions</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Dimensions</em>' containment reference list.
   * @see de.tudresden.slr.model.taxonomy.TaxonomyPackage#getModel_Dimensions()
   * @model containment="true"
   * @generated
   */
  EList<Term> getDimensions();

} // Model
