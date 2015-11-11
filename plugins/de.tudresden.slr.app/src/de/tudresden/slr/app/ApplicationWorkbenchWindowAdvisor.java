package de.tudresden.slr.app;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Point;
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
        configurer.setTitle("SLR Toolkit"); //$NON-NLS-1$
    }
    
    public void postWindowOpen(){
    	IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    	configurer.getWindow().getShell().setMaximized(true); 
    }
    
    public void postWindowCreate(){
    	hideRunMenu();
    }
    
    //TODO: Find a way to hide the "Run" menu using activities or extensions points (hiddenMenuItem). Even better get rid of any dependencies which introduce the "Run" menu in the first place.
	private void hideRunMenu() {
		IMenuManager menuManager = this.actionBarConfigurer.getMenuManager();
        IContributionItem[] menuItems =  menuManager.getItems();
        for (IContributionItem item : menuItems)
        {
        	if(!item.getId().equalsIgnoreCase("org.eclipse.ui.run")){
        		continue;
        	}
            item.setVisible(false);
        }
	}
}
