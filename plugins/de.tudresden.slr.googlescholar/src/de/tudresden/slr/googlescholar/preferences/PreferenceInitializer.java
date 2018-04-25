package de.tudresden.slr.googlescholar.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		System.out.println("init preferences");
		IPreferenceStore store = new ScopedPreferenceStore( InstanceScope.INSTANCE, "de.tudresden.slr.googlescholar");
		store.setValue(PreferenceConstants.P_MAX_WAIT, 30000);
		store.setValue(PreferenceConstants.P_MIN_WAIT, 5000);
	}

}
