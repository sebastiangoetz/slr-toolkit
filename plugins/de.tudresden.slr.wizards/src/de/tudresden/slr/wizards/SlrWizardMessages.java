package de.tudresden.slr.wizards;

import org.eclipse.osgi.util.NLS;

public class SlrWizardMessages extends NLS {
	private static final String BUNDLE_NAME = "de.tudresden.slr.wizards.messages"; //$NON-NLS-1$
	public static String NewSlrProjectWizard_Project_Name;
	public static String NewSlrProjectWizard_SLR_Project;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SlrWizardMessages.class);
	}

	private SlrWizardMessages() {
	}
}
