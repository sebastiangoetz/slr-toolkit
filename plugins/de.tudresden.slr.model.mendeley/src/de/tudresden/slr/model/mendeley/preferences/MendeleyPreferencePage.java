package de.tudresden.slr.model.mendeley.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import de.tudresden.slr.model.mendeley.Activator;
import de.tudresden.slr.model.mendeley.authentication.MendeleyClient;
import de.tudresden.slr.model.mendeley.ui.MendeleyOAuthDialog;
import org.eclipse.swt.widgets.Composite;

/**
 * This class represents a preference page that
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
	private StringFieldEditor documentfield;
	
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
	/*	addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH, 
				"&Directory preference:", getFieldEditorParent()));
		addField(
			new BooleanFieldEditor(
				PreferenceConstants.P_BOOLEAN,
				"&An example of a boolean preference",
				getFieldEditorParent()));

		addField(new RadioGroupFieldEditor(
				PreferenceConstants.P_CHOICE,
			"An example of a multiple-choice preference",
			1,
			new String[][] { { "&Choice 1", "choice1" }, {
				"C&hoice 2", "choice2" }
				}, getFieldEditorParent()));
		addField(
			new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));
		
		addField(new RadioGroupFieldEditor(
				PreferenceConstants.P_CHOICE,
			"An example of a multiple-choice preference",
			1,
			new String[][] { { "&Choice 1", "choice1" }, {
				"C&hoice 2", "choice2" }
		}, getFieldEditorParent()));
		*/
		addField(new RadioGroupFieldEditor(PreferenceConstants.P_MENDELEY, 
				"Activate Mendeley Integration", 
				1, 
				new String[][]{{"&No", "mendeley_off"}, {"&Yes", "mendeley_on"}
				}, getFieldEditorParent()));
		{
			Composite composite = getFieldEditorParent();
			
			documentfield = new StringFieldEditor("DocumenField", "Username", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, composite);
			documentfield.getLabelControl(composite).setText("Access Token (Debug)");
			documentfield.getTextControl(composite).setEditable(false);
			addField(documentfield);
			
		}
		
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				IPreferenceStore store = Activator.getDefault().getPreferenceStore();
				if (event.getProperty().equals(PreferenceConstants.P_TOKEN)) {
					documentfield.setStringValue(store.getString(PreferenceConstants.P_TOKEN));
                }
			}
		});
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		if(event.getOldValue().toString().equals("mendeley_off") && event.getNewValue().toString().equals("mendeley_on")){
			System.out.println("Mendeley Support Activated");
			mendeley_client.displayAuthorizationUserInterface(getFieldEditorParent().getShell());
		}
		
		super.propertyChange(event);
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		
	}
	
}