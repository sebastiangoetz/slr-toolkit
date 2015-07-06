/**
 */
package de.tudresden.slr.model.bibtex;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see de.tudresden.slr.model.bibtex.BibtexFactory
 * @model kind="package"
 * @generated
 */
public interface BibtexPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "bibtex";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://tudresden.de/slr/2015";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	BibtexPackage eINSTANCE = de.tudresden.slr.model.bibtex.impl.BibtexPackageImpl
			.init();

	/**
	 * The meta object id for the '
	 * {@link de.tudresden.slr.model.bibtex.impl.DocumentImpl <em>Document</em>}
	 * ' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see de.tudresden.slr.model.bibtex.impl.DocumentImpl
	 * @see de.tudresden.slr.model.bibtex.impl.BibtexPackageImpl#getDocument()
	 * @generated
	 */
	int DOCUMENT = 0;

	/**
	 * The feature id for the '<em><b>Authors</b></em>' attribute list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__AUTHORS = 0;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__ABSTRACT = 1;

	/**
	 * The feature id for the '<em><b>Year</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__YEAR = 2;

	/**
	 * The feature id for the '<em><b>Month</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__MONTH = 3;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__TITLE = 4;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__KEY = 5;

	/**
	 * The feature id for the '<em><b>Doi</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__DOI = 6;

	/**
	 * The feature id for the '<em><b>Url</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__URL = 7;

	/**
	 * The feature id for the '<em><b>Unparsed Authors</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__UNPARSED_AUTHORS = 8;

	/**
	 * The feature id for the '<em><b>Taxonomy</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__TAXONOMY = 9;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__TYPE = 10;

	/**
	 * The feature id for the '<em><b>File</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__FILE = 11;

	/**
	 * The feature id for the '<em><b>Cites</b></em>' attribute list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT__CITES = 12;

	/**
	 * The number of structural features of the '<em>Document</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_FEATURE_COUNT = 13;

	/**
	 * The number of operations of the '<em>Document</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_OPERATION_COUNT = 0;

	/**
	 * Returns the meta object for class '
	 * {@link de.tudresden.slr.model.bibtex.Document <em>Document</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Document</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document
	 * @generated
	 */
	EClass getDocument();

	/**
	 * Returns the meta object for the attribute list '
	 * {@link de.tudresden.slr.model.bibtex.Document#getAuthors
	 * <em>Authors</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute list '<em>Authors</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getAuthors()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_Authors();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getAbstract
	 * <em>Abstract</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Abstract</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getAbstract()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_Abstract();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getYear <em>Year</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Year</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getYear()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_Year();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getMonth <em>Month</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Month</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getMonth()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_Month();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getTitle()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_Title();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getKey <em>Key</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getKey()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_Key();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getDoi <em>Doi</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Doi</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getDoi()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_Doi();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getUrl <em>Url</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Url</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getUrl()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_Url();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getUnparsedAuthors
	 * <em>Unparsed Authors</em>}'. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return the meta object for the attribute '<em>Unparsed Authors</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getUnparsedAuthors()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_UnparsedAuthors();

	/**
	 * Returns the meta object for the reference '
	 * {@link de.tudresden.slr.model.bibtex.Document#getTaxonomy
	 * <em>Taxonomy</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Taxonomy</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getTaxonomy()
	 * @see #getDocument()
	 * @generated
	 */
	EReference getDocument_Taxonomy();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getType()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_Type();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getFile <em>File</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>File</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getFile()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_File();

	/**
	 * Returns the meta object for the attribute '
	 * {@link de.tudresden.slr.model.bibtex.Document#getCites <em>Cites</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Cites</em>'.
	 * @see de.tudresden.slr.model.bibtex.Document#getCites()
	 * @see #getDocument()
	 * @generated
	 */
	EAttribute getDocument_Cites();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	BibtexFactory getBibtexFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that
	 * represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '
		 * {@link de.tudresden.slr.model.bibtex.impl.DocumentImpl
		 * <em>Document</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
		 * -->
		 * 
		 * @see de.tudresden.slr.model.bibtex.impl.DocumentImpl
		 * @see de.tudresden.slr.model.bibtex.impl.BibtexPackageImpl#getDocument()
		 * @generated
		 */
		EClass DOCUMENT = eINSTANCE.getDocument();

		/**
		 * The meta object literal for the '<em><b>Authors</b></em>' attribute
		 * list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__AUTHORS = eINSTANCE.getDocument_Authors();

		/**
		 * The meta object literal for the '<em><b>Abstract</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__ABSTRACT = eINSTANCE.getDocument_Abstract();

		/**
		 * The meta object literal for the '<em><b>Year</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__YEAR = eINSTANCE.getDocument_Year();

		/**
		 * The meta object literal for the '<em><b>Month</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__MONTH = eINSTANCE.getDocument_Month();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__TITLE = eINSTANCE.getDocument_Title();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__KEY = eINSTANCE.getDocument_Key();

		/**
		 * The meta object literal for the '<em><b>Doi</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__DOI = eINSTANCE.getDocument_Doi();

		/**
		 * The meta object literal for the '<em><b>Url</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__URL = eINSTANCE.getDocument_Url();

		/**
		 * The meta object literal for the '<em><b>Unparsed Authors</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__UNPARSED_AUTHORS = eINSTANCE
				.getDocument_UnparsedAuthors();

		/**
		 * The meta object literal for the '<em><b>Taxonomy</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DOCUMENT__TAXONOMY = eINSTANCE.getDocument_Taxonomy();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__TYPE = eINSTANCE.getDocument_Type();

		/**
		 * The meta object literal for the '<em><b>File</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__FILE = eINSTANCE.getDocument_File();

		/**
		 * The meta object literal for the '<em><b>Cites</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOCUMENT__CITES = eINSTANCE.getDocument_Cites();

	}

} // BibtexPackage
