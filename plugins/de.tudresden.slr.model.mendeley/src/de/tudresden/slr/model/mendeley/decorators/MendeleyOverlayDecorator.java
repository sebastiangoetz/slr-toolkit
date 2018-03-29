package de.tudresden.slr.model.mendeley.decorators;

import org.eclipse.core.internal.resources.File;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;

/**
 * This class implements additional image and string overlays that are used to decorate
 * the Project Explorer Navigation Tree.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class MendeleyOverlayDecorator implements ILabelDecorator {
	WorkspaceManager wm = WorkspaceManager.getInstance();
	private static final ImageDescriptor DECORATOR;
	private static final ImageDescriptor ON;
	private static final ImageDescriptor OFF;
	private IWorkbenchWindow window = Workbench.getInstance().getActiveWorkbenchWindow();
			
	static {
		DECORATOR = AbstractUIPlugin.imageDescriptorFromPlugin(
				"de.tudresden.slr.model.mendeley", "icons/sample_decorator.gif");
		ON = AbstractUIPlugin.imageDescriptorFromPlugin(
				"de.tudresden.slr.model.mendeley", "icons/on.png");
		OFF = AbstractUIPlugin.imageDescriptorFromPlugin(
				"de.tudresden.slr.model.mendeley", "icons/off.png");
	}
	
	@Override
	public void addListener(ILabelProviderListener listener) {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image decorateImage(Image image, Object element) {
		/*
		 * - decorates only .bib files 
		 * - 	if there is a WorkSpaceBibTexEntry for a .bib file that holds an instance of a MendeleyFolder,
		 * 		the Decorator Image ON will be placed at the bottom right of the file symbol
		 */
		if(element instanceof File) {
			File file = (File) element;
			if(file.getFileExtension().equals("bib")) {
				WorkspaceBibTexEntry entry = wm.getWorkspaceBibTexEntryByUri(file.getLocationURI());
				if(entry != null) {
					if(entry.getMendeleyFolder() != null) {
						DecorationOverlayIcon overlayedimage = new DecorationOverlayIcon(image, ON, IDecoration.BOTTOM_RIGHT);
						return overlayedimage.createImage();
					}
				}
				DecorationOverlayIcon overlayedimage = new DecorationOverlayIcon(image, OFF, IDecoration.BOTTOM_RIGHT);
				return overlayedimage.createImage();
			}
		}
		return null;
	}

	@Override
	public String decorateText(String text, Object element) {
		/*
		 * - decorates only .bib files 
		 * - 	if there is a WorkSpaceBibTexEntry for a .bib file that holds an instance of a MendeleyFolder,
		 * 		the Folder Name will be placed next to the .bib file name
		 */
		if(element instanceof File) {
			File file = (File) element;
			if(file.getFileExtension().equals("bib")) {
				WorkspaceBibTexEntry entry = wm.getWorkspaceBibTexEntryByUri(file.getLocationURI());
				if(entry != null) {
					if(entry.getMendeleyFolder() != null) {
						String folder_name = entry.getMendeleyFolder().getName();
						
						//checks if .bib file is already decorated
						if(!text.contains("[" + folder_name + "]")) {
							String new_text = text + "  [" + folder_name + "]";
							return new_text;
						}
					}
				}
				// the main menu entry 'Mendeley -> Update Folders' must be disabled when no file is decorated
				updateMenuContribution();
			}
		}
		return null;
	}
	
	/**
	 * THis method sets the main menu contribution 'Mendeley' to visible
	 */
	private void updateMenuContribution() {
		if(window instanceof WorkbenchWindow) {
			MenuManager menuManager = ((WorkbenchWindow)window).getMenuManager();
		    
		    //you'll need to find the id for the item
		    String mainMenuId = "de.tudresden.slr.model.mendeley.menus.mainMenu";
		    IContributionItem item = menuManager.find(mainMenuId);
		    item.update();
		    if (item != null) {
		        // clean old one
		        item.setVisible(true);

		        // refresh menu gui
		        menuManager.update();
		    }
		}
	}
}
