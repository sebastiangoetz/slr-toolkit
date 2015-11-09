package de.tudresden.slr.app;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.WorkbenchWindow;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(400, 300));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setTitle("SLR Toolkit"); //$NON-NLS-1$
    }
    
    public void postWindowCreate(){
    	hideRunMenu();
    }
    
    //HACK: Only way to hide the "Run" menu that works ath the moment. WorkbenchWindow and getMenuBarManager() aren't API though. #28
    private void hideRunMenu ()
    {
        IWorkbenchWindow workbenchWindow =  PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if(workbenchWindow instanceof WorkbenchWindow){
            IContributionItem[] items = ((WorkbenchWindow) workbenchWindow).getMenuBarManager().getItems();
            for (IContributionItem item : items)
            {
            	if(!item.getId().equalsIgnoreCase("org.eclipse.ui.run")){
            		continue;
            	}
                item.setVisible(false);
            }
        }
    }
}
