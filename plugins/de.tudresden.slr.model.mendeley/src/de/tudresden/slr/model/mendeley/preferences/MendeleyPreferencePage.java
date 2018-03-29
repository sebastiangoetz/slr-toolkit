package de.tudresden.slr.model.mendeley.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.tudresden.slr.model.mendeley.Activator;
import de.tudresden.slr.model.mendeley.api.client.MendeleyClient;

/**
 * This class represents the Mendeley preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class MendeleyPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	MendeleyClient mendeley_client;
	private IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	
	public MendeleyPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Mendeley Preference Page");
		this.mendeley_client = MendeleyClient.getInstance();
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		// only a radiobutto is needed to remember the state of your mendeley login
		addField(new RadioGroupFieldEditor(PreferenceConstants.P_MENDELEY, 
				"Activate Mendeley Integration", 
				1, 
				new String[][]{{"&No", "mendeley_off"}, {"&Yes", "mendeley_on"}
				}, getFieldEditorParent()));
		{
			Composite composite = getFieldEditorParent();
			
		}
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		if(event.getOldValue().toString().equals("mendeley_off") && event.getNewValue().toString().equals("mendeley_on")){
			// Mendeley Support activated
			mendeley_client.refreshTokenIfNecessary();
		}
		
		if(event.getOldValue().toString().equals("mendeley_on") && event.getNewValue().toString().equals("mendeley_off")){
			// Mendeley Support dectivated
			mendeley_client.logout();
		}
		
		super.propertyChange(event);
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		
	}
	
}