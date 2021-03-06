/**
 */
package de.tudresden.slr.model.bibtex.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import de.tudresden.slr.model.bibtex.BibtexFactory;
import de.tudresden.slr.model.bibtex.BibtexFile;
import de.tudresden.slr.model.bibtex.BibtexPackage;
import de.tudresden.slr.model.bibtex.Document;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class BibtexFactoryImpl extends EFactoryImpl implements BibtexFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static BibtexFactory init() {
		try {
			BibtexFactory theBibtexFactory = (BibtexFactory) EPackage.Registry.INSTANCE
					.getEFactory(BibtexPackage.eNS_URI);
			if (theBibtexFactory != null) {
				return theBibtexFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new BibtexFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public BibtexFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case BibtexPackage.DOCUMENT:
			return createDocument();
		case BibtexPackage.BIBTEX_FILE:
			return createBibtexFile();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Document createDocument() {
		DocumentImpl document = new DocumentImpl();
		return document;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public BibtexFile createBibtexFile() {
		BibtexFileImpl bibtexFile = new BibtexFileImpl();
		return bibtexFile;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public BibtexPackage getBibtexPackage() {
		return (BibtexPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static BibtexPackage getPackage() {
		return BibtexPackage.eINSTANCE;
	}

} // BibtexFactoryImpl
