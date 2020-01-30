package de.tudresden.slr.app;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "de.tudresden.slr.app.perspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}
	
    public void initialize(IWorkbenchConfigurer configurer) 
	{
		IDE.registerAdapters();
	}
	
	public IAdaptable getDefaultPageInput(){
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	@Override
	public void postStartup() {
		PreferenceManager pm = PlatformUI.getWorkbench().getPreferenceManager();
		pm.remove("org.eclipse.ui.preferencePages.Workbench");
		pm.remove("org.eclipse.jdt.ui.preferences.JavaBasePreferencePage");
		pm.remove("org.eclipse.team.ui.TeamPreferences");
		pm.remove("org.eclipse.debug.ui.DebugPreferencePage");
		
		super.postStartup();
	}

}
