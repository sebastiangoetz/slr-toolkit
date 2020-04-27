package de.tudresden.slr.googlescholar;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import de.tudresden.slr.googlescholar.preferences.PreferenceConstants;

/**
 * Refactored access for testability purposes
 */
public class GSPreferenceStoreWrapper {
	private final String PREFERENCE_NAME =  "de.tudresden.slr.googlescholar";
	private final IPreferenceStore store = new ScopedPreferenceStore( InstanceScope.INSTANCE, PREFERENCE_NAME);
	private final int MIN_WAIT =  store.getInt(PreferenceConstants.P_MIN_WAIT);
	private final int MAX_WAIT =  store.getInt(PreferenceConstants.P_MAX_WAIT);
	
	public int getMinWait() {
		return MIN_WAIT;
	}
	
	public int getMaxWait() {
		return MAX_WAIT;
	}
}
