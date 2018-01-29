package de.tudresden.slr.ui.chart.radar;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage
 * @generated
 */
public interface RadarTypeFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RadarTypeFactory eINSTANCE = de.tudresden.slr.ui.chart.radar.impl.RadarTypeFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Radar Series</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Radar Series</em>'.
	 * @generated
	 */
	RadarSeries createRadarSeries();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	RadarTypePackage getRadarTypePackage();

} //RadarTypeFactory