/**
 */
package de.tudresden.slr.model.taxonomy;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see de.tudresden.slr.model.taxonomy.TaxonomyFactory
 * @model kind="package"
 * @generated
 */
public interface TaxonomyPackage extends EPackage
{
  /**
	 * The package name.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  String eNAME = "taxonomy";

  /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  String eNS_URI = "http://www.tudresden.de/slr/model/Taxonomy";

  /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  String eNS_PREFIX = "taxonomy";

  /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  TaxonomyPackage eINSTANCE = de.tudresden.slr.model.taxonomy.impl.TaxonomyPackageImpl.init();

  /**
	 * The meta object id for the '{@link de.tudresden.slr.model.taxonomy.impl.ModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see de.tudresden.slr.model.taxonomy.impl.ModelImpl
	 * @see de.tudresden.slr.model.taxonomy.impl.TaxonomyPackageImpl#getModel()
	 * @generated
	 */
  int MODEL = 0;

  /**
	 * The feature id for the '<em><b>Dimensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int MODEL__DIMENSIONS = 0;

  /**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int MODEL_FEATURE_COUNT = 1;

  /**
	 * The meta object id for the '{@link de.tudresden.slr.model.taxonomy.impl.TermImpl <em>Term</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see de.tudresden.slr.model.taxonomy.impl.TermImpl
	 * @see de.tudresden.slr.model.taxonomy.impl.TaxonomyPackageImpl#getTerm()
	 * @generated
	 */
  int TERM = 1;

  /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int TERM__NAME = 0;

  /**
	 * The feature id for the '<em><b>Subclasses</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int TERM__SUBCLASSES = 1;

  /**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERM__UUID = 2;

		/**
	 * The number of structural features of the '<em>Term</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int TERM_FEATURE_COUNT = 3;


  /**
	 * The meta object id for the '<em>EUUID</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.UUID
	 * @see de.tudresden.slr.model.taxonomy.impl.TaxonomyPackageImpl#getEUUID()
	 * @generated
	 */
	int EUUID = 2;


		/**
	 * Returns the meta object for class '{@link de.tudresden.slr.model.taxonomy.Model <em>Model</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see de.tudresden.slr.model.taxonomy.Model
	 * @generated
	 */
  EClass getModel();

  /**
	 * Returns the meta object for the containment reference list '{@link de.tudresden.slr.model.taxonomy.Model#getDimensions <em>Dimensions</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dimensions</em>'.
	 * @see de.tudresden.slr.model.taxonomy.Model#getDimensions()
	 * @see #getModel()
	 * @generated
	 */
  EReference getModel_Dimensions();

  /**
	 * Returns the meta object for class '{@link de.tudresden.slr.model.taxonomy.Term <em>Term</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Term</em>'.
	 * @see de.tudresden.slr.model.taxonomy.Term
	 * @generated
	 */
  EClass getTerm();

  /**
	 * Returns the meta object for the attribute '{@link de.tudresden.slr.model.taxonomy.Term#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see de.tudresden.slr.model.taxonomy.Term#getName()
	 * @see #getTerm()
	 * @generated
	 */
  EAttribute getTerm_Name();

  /**
	 * Returns the meta object for the containment reference list '{@link de.tudresden.slr.model.taxonomy.Term#getSubclasses <em>Subclasses</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Subclasses</em>'.
	 * @see de.tudresden.slr.model.taxonomy.Term#getSubclasses()
	 * @see #getTerm()
	 * @generated
	 */
  EReference getTerm_Subclasses();

  /**
	 * Returns the meta object for the attribute '{@link de.tudresden.slr.model.taxonomy.Term#getUuid <em>Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uuid</em>'.
	 * @see de.tudresden.slr.model.taxonomy.Term#getUuid()
	 * @see #getTerm()
	 * @generated
	 */
	EAttribute getTerm_Uuid();

		/**
	 * Returns the meta object for data type '{@link java.util.UUID <em>EUUID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EUUID</em>'.
	 * @see java.util.UUID
	 * @model instanceClass="java.util.UUID"
	 * @generated
	 */
	EDataType getEUUID();

		/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
  TaxonomyFactory getTaxonomyFactory();

  /**
	 * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
	 * @generated
	 */
  interface Literals
  {
    /**
		 * The meta object literal for the '{@link de.tudresden.slr.model.taxonomy.impl.ModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see de.tudresden.slr.model.taxonomy.impl.ModelImpl
		 * @see de.tudresden.slr.model.taxonomy.impl.TaxonomyPackageImpl#getModel()
		 * @generated
		 */
    EClass MODEL = eINSTANCE.getModel();

    /**
		 * The meta object literal for the '<em><b>Dimensions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference MODEL__DIMENSIONS = eINSTANCE.getModel_Dimensions();

    /**
		 * The meta object literal for the '{@link de.tudresden.slr.model.taxonomy.impl.TermImpl <em>Term</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see de.tudresden.slr.model.taxonomy.impl.TermImpl
		 * @see de.tudresden.slr.model.taxonomy.impl.TaxonomyPackageImpl#getTerm()
		 * @generated
		 */
    EClass TERM = eINSTANCE.getTerm();

    /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute TERM__NAME = eINSTANCE.getTerm_Name();

    /**
		 * The meta object literal for the '<em><b>Subclasses</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference TERM__SUBCLASSES = eINSTANCE.getTerm_Subclasses();

				/**
		 * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TERM__UUID = eINSTANCE.getTerm_Uuid();

				/**
		 * The meta object literal for the '<em>EUUID</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.UUID
		 * @see de.tudresden.slr.model.taxonomy.impl.TaxonomyPackageImpl#getEUUID()
		 * @generated
		 */
		EDataType EUUID = eINSTANCE.getEUUID();

  }

} //TaxonomyPackage
