package de.tudresden.slr.app;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private IActionBarConfigurer actionBarConfigurer;
	
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		this.actionBarConfigurer = configurer;
		return new ApplicationActionBarAdvisor(configurer);
	}
	
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setTitle("SLR Toolkit");
	}
	
	public void postWindowOpen() {
		hideSearchMenu();
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.getWindow().getShell().setMaximized(true); 
	}
	
	private void hideSearchMenu() {
		IMenuManager menuManager = this.actionBarConfigurer.getMenuManager();
		IContributionItem[] menuItems =  menuManager.getItems();
		for (IContributionItem item : menuItems) {
			if(item.getId().equalsIgnoreCase("org.eclipse.search.menu")) {
        		item.setVisible(false);
        		}
		}
	}
}
