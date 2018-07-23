/**
 */
package de.tudresden.slr.model.taxonomy.impl;

import de.tudresden.slr.model.taxonomy.TaxonomyPackage;
import de.tudresden.slr.model.taxonomy.Term;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Term</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.tudresden.slr.model.taxonomy.impl.TermImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.tudresden.slr.model.taxonomy.impl.TermImpl#getSubclasses <em>Subclasses</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TermImpl extends MinimalEObjectImpl.Container implements Term {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSubclasses() <em>Subclasses</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubclasses()
	 * @generated
	 * @ordered
	 */
	protected EList<Term> subclasses;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TermImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TaxonomyPackage.Literals.TERM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName != null ? newName.trim() : newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TaxonomyPackage.TERM__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Term> getSubclasses() {
		if (subclasses == null) {
			subclasses = new EObjectContainmentEList<Term>(Term.class, this, TaxonomyPackage.TERM__SUBCLASSES);
		}
		return subclasses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int hashCode() {
		final int prime = 31;
		int hash = 1;
		if (eContainer instanceof Term) {
			int parentHash = eContainer.hashCode();
			hash = prime * hash + parentHash ^ parentHash;
		}
		int nameHash = name.hashCode();
		hash = prime * hash + nameHash ^ nameHash;
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		String fullyQualifiedName = "";
		if(eContainer instanceof Term) {
			fullyQualifiedName += ((Term)eContainer).getName()+"/";
		}
		fullyQualifiedName += name; 
		if(obj instanceof Term)
			return fullyQualifiedName.equals(((Term)obj).getName());
		else return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TaxonomyPackage.TERM__SUBCLASSES:
				return ((InternalEList<?>)getSubclasses()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TaxonomyPackage.TERM__NAME:
				return getName();
			case TaxonomyPackage.TERM__SUBCLASSES:
				return getSubclasses();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TaxonomyPackage.TERM__NAME:
				setName((String)newValue);
				return;
			case TaxonomyPackage.TERM__SUBCLASSES:
				getSubclasses().clear();
				getSubclasses().addAll((Collection<? extends Term>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TaxonomyPackage.TERM__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TaxonomyPackage.TERM__SUBCLASSES:
				getSubclasses().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TaxonomyPackage.TERM__NAME:
				return name != null;
				//return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TaxonomyPackage.TERM__SUBCLASSES:
				return subclasses != null && !subclasses.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case TaxonomyPackage.TERM___HASH_CODE:
				return hashCode();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //TermImpl
