/**
 */
package de.tudresden.slr.model.bibtex;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Document</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link de.tudresden.slr.model.bibtex.Document#getAuthors <em>Authors
 * </em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.Document#getAbstract <em>Abstract
 * </em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.Document#getYear <em>Year</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.Document#getMonth <em>Month</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.Document#getTitle <em>Title</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.Document#getKey <em>Key</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.Document#getDoi <em>Doi</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.Document#getUrl <em>Url</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.Document#getUnparsedAuthors <em>
 * Unparsed Authors</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getDocument()
 * @model
 * @generated
 */
public interface Document extends EObject {
	/**
	 * Returns the value of the '<em><b>Authors</b></em>' attribute list. The
	 * list contents are of type {@link java.lang.String}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Authors</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Authors</em>' attribute list.
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getDocument_Authors()
	 * @model required="true"
	 * @generated
	 */
	EList<String> getAuthors();

	/**
	 * Returns the value of the '<em><b>Abstract</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Abstract</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Abstract</em>' attribute.
	 * @see #setAbstract(String)
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getDocument_Abstract()
	 * @model
	 * @generated
	 */
	String getAbstract();

	/**
	 * Sets the value of the '
	 * {@link de.tudresden.slr.model.bibtex.Document#getAbstract
	 * <em>Abstract</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Abstract</em>' attribute.
	 * @see #getAbstract()
	 * @generated
	 */
	void setAbstract(String value);

	/**
	 * Returns the value of the '<em><b>Year</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Year</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Year</em>' attribute.
	 * @see #setYear(String)
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getDocument_Year()
	 * @model
	 * @generated
	 */
	String getYear();

	/**
	 * Sets the value of the '
	 * {@link de.tudresden.slr.model.bibtex.Document#getYear <em>Year</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Year</em>' attribute.
	 * @see #getYear()
	 * @generated
	 */
	void setYear(String value);

	/**
	 * Returns the value of the '<em><b>Month</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Month</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Month</em>' attribute.
	 * @see #setMonth(String)
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getDocument_Month()
	 * @model
	 * @generated
	 */
	String getMonth();

	/**
	 * Sets the value of the '
	 * {@link de.tudresden.slr.model.bibtex.Document#getMonth <em>Month</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Month</em>' attribute.
	 * @see #getMonth()
	 * @generated
	 */
	void setMonth(String value);

	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Title</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getDocument_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '
	 * {@link de.tudresden.slr.model.bibtex.Document#getTitle <em>Title</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see #setKey(String)
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getDocument_Key()
	 * @model id="true"
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '
	 * {@link de.tudresden.slr.model.bibtex.Document#getKey <em>Key</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

	/**
	 * Returns the value of the '<em><b>Doi</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Doi</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Doi</em>' attribute.
	 * @see #setDoi(String)
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getDocument_Doi()
	 * @model
	 * @generated
	 */
	String getDoi();

	/**
	 * Sets the value of the '
	 * {@link de.tudresden.slr.model.bibtex.Document#getDoi <em>Doi</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Doi</em>' attribute.
	 * @see #getDoi()
	 * @generated
	 */
	void setDoi(String value);

	/**
	 * Returns the value of the '<em><b>Url</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Url</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Url</em>' attribute.
	 * @see #setUrl(String)
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getDocument_Url()
	 * @model
	 * @generated
	 */
	String getUrl();

	/**
	 * Sets the value of the '
	 * {@link de.tudresden.slr.model.bibtex.Document#getUrl <em>Url</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Url</em>' attribute.
	 * @see #getUrl()
	 * @generated
	 */
	void setUrl(String value);

	/**
	 * Returns the value of the '<em><b>Unparsed Authors</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unparsed Authors</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Unparsed Authors</em>' attribute.
	 * @see #setUnparsedAuthors(String)
	 * @see de.tudresden.slr.model.bibtex.BibtexPackage#getDocument_UnparsedAuthors()
	 * @model
	 * @generated
	 */
	String getUnparsedAuthors();

	/**
	 * Sets the value of the '
	 * {@link de.tudresden.slr.model.bibtex.Document#getUnparsedAuthors
	 * <em>Unparsed Authors</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Unparsed Authors</em>' attribute.
	 * @see #getUnparsedAuthors()
	 * @generated
	 */
	void setUnparsedAuthors(String value);

} // Document
