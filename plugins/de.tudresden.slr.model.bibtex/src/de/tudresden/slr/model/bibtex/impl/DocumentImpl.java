/**
 */
package de.tudresden.slr.model.bibtex.impl;

import de.tudresden.slr.model.bibtex.BibtexPackage;
import de.tudresden.slr.model.bibtex.Document;

import de.tudresden.slr.model.taxonomy.Model;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Document</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getAuthors
 * <em>Authors</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getAbstract
 * <em>Abstract</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getYear
 * <em>Year</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getMonth
 * <em>Month</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getTitle
 * <em>Title</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getKey
 * <em>Key</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getDoi
 * <em>Doi</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getUrl
 * <em>Url</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getUnparsedAuthors
 * <em>Unparsed Authors</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getTaxonomy
 * <em>Taxonomy</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getType
 * <em>Type</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getFile
 * <em>File</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getCites
 * <em>Cites</em>}</li>
 * <li>{@link de.tudresden.slr.model.bibtex.impl.DocumentImpl#getLine
 * <em>Line</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DocumentImpl extends MinimalEObjectImpl.Container implements Document {
	/**
	 * The cached value of the '{@link #getAuthors() <em>Authors</em>}' attribute
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAuthors()
	 * @generated
	 * @ordered
	 */
	protected EList<String> authors;

	/**
	 * The default value of the '{@link #getAbstract() <em>Abstract</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final String ABSTRACT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAbstract()
	 * @generated
	 * @ordered
	 */
	protected String abstract_ = ABSTRACT_EDEFAULT;

	/**
	 * The default value of the '{@link #getYear() <em>Year</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getYear()
	 * @generated
	 * @ordered
	 */
	protected static final String YEAR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getYear() <em>Year</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getYear()
	 * @generated
	 * @ordered
	 */
	protected String year = YEAR_EDEFAULT;

	/**
	 * The default value of the '{@link #getMonth() <em>Month</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMonth()
	 * @generated
	 * @ordered
	 */
	protected static final String MONTH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMonth() <em>Month</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMonth()
	 * @generated
	 * @ordered
	 */
	protected String month = MONTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected static final String TITLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected String title = TITLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getKey() <em>Key</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKey() <em>Key</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

	/**
	 * The default value of the '{@link #getDoi() <em>Doi</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDoi()
	 * @generated
	 * @ordered
	 */
	protected static final String DOI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDoi() <em>Doi</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDoi()
	 * @generated
	 * @ordered
	 */
	protected String doi = DOI_EDEFAULT;

	/**
	 * The default value of the '{@link #getUrl() <em>Url</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUrl()
	 * @generated
	 * @ordered
	 */
	protected static final String URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUrl() <em>Url</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUrl()
	 * @generated
	 * @ordered
	 */
	protected String url = URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getUnparsedAuthors() <em>Unparsed
	 * Authors</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnparsedAuthors()
	 * @generated
	 * @ordered
	 */
	protected static final String UNPARSED_AUTHORS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUnparsedAuthors() <em>Unparsed
	 * Authors</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnparsedAuthors()
	 * @generated
	 * @ordered
	 */
	protected String unparsedAuthors = UNPARSED_AUTHORS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTaxonomy() <em>Taxonomy</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTaxonomy()
	 * @generated
	 * @ordered
	 */
	protected Model taxonomy;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getFile() <em>File</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFile()
	 * @generated
	 * @ordered
	 */
	protected static final String FILE_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getFile() <em>File</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFile()
	 * @generated
	 * @ordered
	 */
	protected String file = FILE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCites() <em>Cites</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCites()
	 * @generated
	 * @ordered
	 */
	protected static final int CITES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCites() <em>Cites</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCites()
	 * @generated
	 * @ordered
	 */
	protected int cites = CITES_EDEFAULT;

	/**
	 * The default value of the '{@link #getLine() <em>Line</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLine()
	 * @generated
	 * @ordered
	 */
	protected static final int LINE_EDEFAULT = 0;

	protected int line;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DocumentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BibtexPackage.Literals.DOCUMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<String> getAuthors() {
		if (authors == null) {
			authors = new EDataTypeUniqueEList<String>(String.class, this, BibtexPackage.DOCUMENT__AUTHORS);
		}
		return authors;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getAbstract() {
		return abstract_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setAbstract(String newAbstract) {
		String oldAbstract = abstract_;
		abstract_ = newAbstract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__ABSTRACT, oldAbstract,
					abstract_));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getYear() {
		return year;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setYear(String newYear) {
		String oldYear = year;
		year = newYear;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__YEAR, oldYear, year));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getMonth() {
		return month;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setMonth(String newMonth) {
		String oldMonth = month;
		month = newMonth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__MONTH, oldMonth, month));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setTitle(String newTitle) {
		String oldTitle = title;
		title = newTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__TITLE, oldTitle, title));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getKey() {
		return key;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setKey(String newKey) {
		String oldKey = key;
		key = newKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__KEY, oldKey, key));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getDoi() {
		return doi;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDoi(String newDoi) {
		String oldDoi = doi;
		doi = newDoi;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__DOI, oldDoi, doi));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setUrl(String newUrl) {
		String oldUrl = url;
		url = newUrl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__URL, oldUrl, url));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getUnparsedAuthors() {
		return unparsedAuthors;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setUnparsedAuthors(String newUnparsedAuthors) {
		String oldUnparsedAuthors = unparsedAuthors;
		unparsedAuthors = newUnparsedAuthors;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__UNPARSED_AUTHORS,
					oldUnparsedAuthors, unparsedAuthors));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Model getTaxonomy() {
		if (taxonomy != null && taxonomy.eIsProxy()) {
			InternalEObject oldTaxonomy = (InternalEObject) taxonomy;
			taxonomy = (Model) eResolveProxy(oldTaxonomy);
			if (taxonomy != oldTaxonomy) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BibtexPackage.DOCUMENT__TAXONOMY,
							oldTaxonomy, taxonomy));
			}
		}
		return taxonomy;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Model basicGetTaxonomy() {
		return taxonomy;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setTaxonomy(Model newTaxonomy) {
		Model oldTaxonomy = taxonomy;
		taxonomy = newTaxonomy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__TAXONOMY, oldTaxonomy,
					taxonomy));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getFile() {
		return file;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setFile(String newFile) {
		String oldFile = file;
		file = newFile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__FILE, oldFile, file));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getCites() {
		return cites;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setCites(int newCites) {
		int oldCites = cites;
		cites = newCites;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__CITES, oldCites, cites));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getLine() {
		return line;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setLine(int newLine) {
		int oldLine = line;
		line = newLine;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BibtexPackage.DOCUMENT__LINE, oldLine, line));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case BibtexPackage.DOCUMENT__AUTHORS:
			return getAuthors();
		case BibtexPackage.DOCUMENT__ABSTRACT:
			return getAbstract();
		case BibtexPackage.DOCUMENT__YEAR:
			return getYear();
		case BibtexPackage.DOCUMENT__MONTH:
			return getMonth();
		case BibtexPackage.DOCUMENT__TITLE:
			return getTitle();
		case BibtexPackage.DOCUMENT__KEY:
			return getKey();
		case BibtexPackage.DOCUMENT__DOI:
			return getDoi();
		case BibtexPackage.DOCUMENT__URL:
			return getUrl();
		case BibtexPackage.DOCUMENT__UNPARSED_AUTHORS:
			return getUnparsedAuthors();
		case BibtexPackage.DOCUMENT__TAXONOMY:
			if (resolve)
				return getTaxonomy();
			return basicGetTaxonomy();
		case BibtexPackage.DOCUMENT__TYPE:
			return getType();
		case BibtexPackage.DOCUMENT__FILE:
			return getFile();
		case BibtexPackage.DOCUMENT__CITES:
			return getCites();
		case BibtexPackage.DOCUMENT__LINE:
			return getLine();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case BibtexPackage.DOCUMENT__AUTHORS:
			getAuthors().clear();
			getAuthors().addAll((Collection<? extends String>) newValue);
			return;
		case BibtexPackage.DOCUMENT__ABSTRACT:
			setAbstract((String) newValue);
			return;
		case BibtexPackage.DOCUMENT__YEAR:
			setYear((String) newValue);
			return;
		case BibtexPackage.DOCUMENT__MONTH:
			setMonth((String) newValue);
			return;
		case BibtexPackage.DOCUMENT__TITLE:
			setTitle((String) newValue);
			return;
		case BibtexPackage.DOCUMENT__KEY:
			setKey((String) newValue);
			return;
		case BibtexPackage.DOCUMENT__DOI:
			setDoi((String) newValue);
			return;
		case BibtexPackage.DOCUMENT__URL:
			setUrl((String) newValue);
			return;
		case BibtexPackage.DOCUMENT__UNPARSED_AUTHORS:
			setUnparsedAuthors((String) newValue);
			return;
		case BibtexPackage.DOCUMENT__TAXONOMY:
			setTaxonomy((Model) newValue);
			return;
		case BibtexPackage.DOCUMENT__TYPE:
			setType((String) newValue);
			return;
		case BibtexPackage.DOCUMENT__FILE:
			setFile((String) newValue);
			return;
		case BibtexPackage.DOCUMENT__CITES:
			setCites((Integer) newValue);
			return;
		case BibtexPackage.DOCUMENT__LINE:
			setLine((Integer) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case BibtexPackage.DOCUMENT__AUTHORS:
			getAuthors().clear();
			return;
		case BibtexPackage.DOCUMENT__ABSTRACT:
			setAbstract(ABSTRACT_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__YEAR:
			setYear(YEAR_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__MONTH:
			setMonth(MONTH_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__TITLE:
			setTitle(TITLE_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__KEY:
			setKey(KEY_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__DOI:
			setDoi(DOI_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__URL:
			setUrl(URL_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__UNPARSED_AUTHORS:
			setUnparsedAuthors(UNPARSED_AUTHORS_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__TAXONOMY:
			setTaxonomy((Model) null);
			return;
		case BibtexPackage.DOCUMENT__TYPE:
			setType(TYPE_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__FILE:
			setFile(FILE_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__CITES:
			setCites(CITES_EDEFAULT);
			return;
		case BibtexPackage.DOCUMENT__LINE:
			setLine(LINE_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case BibtexPackage.DOCUMENT__AUTHORS:
			return authors != null && !authors.isEmpty();
		case BibtexPackage.DOCUMENT__ABSTRACT:
			//return ABSTRACT_EDEFAULT == null ? abstract_ != null : !ABSTRACT_EDEFAULT.equals(abstract_);
			return abstract_ != null;
		case BibtexPackage.DOCUMENT__YEAR:
			//return YEAR_EDEFAULT == null ? year != null : !YEAR_EDEFAULT.equals(year);
			return year != null;
		case BibtexPackage.DOCUMENT__MONTH:
			//return MONTH_EDEFAULT == null ? month != null : !MONTH_EDEFAULT.equals(month);
			return month != null;
		case BibtexPackage.DOCUMENT__TITLE:
			//return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
			return title != null;
		case BibtexPackage.DOCUMENT__KEY:
			//return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
			return key != null;
		case BibtexPackage.DOCUMENT__DOI:
			//return DOI_EDEFAULT == null ? doi != null : !DOI_EDEFAULT.equals(doi);
			return doi != null;
		case BibtexPackage.DOCUMENT__URL:
			//return URL_EDEFAULT == null ? url != null : !URL_EDEFAULT.equals(url);
			return url != null;
		case BibtexPackage.DOCUMENT__UNPARSED_AUTHORS:
			//return UNPARSED_AUTHORS_EDEFAULT == null ? unparsedAuthors != null
			//		: !UNPARSED_AUTHORS_EDEFAULT.equals(unparsedAuthors);
			return unparsedAuthors != null;
		case BibtexPackage.DOCUMENT__TAXONOMY:
			return taxonomy != null;
		case BibtexPackage.DOCUMENT__TYPE:
			//return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			return type != null;
		case BibtexPackage.DOCUMENT__FILE:
			//return FILE_EDEFAULT == null ? file != null : !FILE_EDEFAULT.equals(file);
			return file != null;
		case BibtexPackage.DOCUMENT__CITES:
			return cites != CITES_EDEFAULT;
		case BibtexPackage.DOCUMENT__LINE:
			return line != LINE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (authors: ");
		result.append(authors);
		result.append(", abstract: ");
		result.append(abstract_);
		result.append(", year: ");
		result.append(year);
		result.append(", month: ");
		result.append(month);
		result.append(", title: ");
		result.append(title);
		result.append(", key: ");
		result.append(key);
		result.append(", doi: ");
		result.append(doi);
		result.append(", url: ");
		result.append(url);
		result.append(", unparsedAuthors: ");
		result.append(unparsedAuthors);
		result.append(", type: ");
		result.append(type);
		result.append(", file: ");
		result.append(file);
		result.append(", cites: ");
		result.append(cites);
		result.append(", line: ");
		result.append(line);
		result.append(')');
		return result.toString();
	}

} // DocumentImpl
