package de.tudresden.slr.model.mendeley.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import de.tudresden.slr.model.mendeley.Activator;

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
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_BOOLEAN, true);
		store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
		store.setDefault(PreferenceConstants.P_STRING, "Default value");
		store.setDefault(PreferenceConstants.P_MENDELEY, "mendeley_off");
		store.setDefault(PreferenceConstants.P_TOKEN, "");
		store.setDefault(PreferenceConstants.P_REFRESH_TOKEN, "");
	}

}
