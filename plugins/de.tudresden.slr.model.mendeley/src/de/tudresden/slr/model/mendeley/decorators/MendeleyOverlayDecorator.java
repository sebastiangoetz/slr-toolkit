package de.tudresden.slr.model.mendeley.decorators;

import java.awt.image.BufferedImage;

import org.eclipse.core.internal.resources.File;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.WWinActionBars;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceManager;

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
		// TODO Auto-generated method stub
		System.out.println("addlistener");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		System.out.println("dispose");
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		System.out.println("isLabelProperty");
		System.out.println(element);
		System.out.println(property);
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		System.out.println("remove listener");
	}

	@Override
	public Image decorateImage(Image image, Object element) {
		// TODO Auto-generated method stub
		System.out.println("decorate image");
		
		
		
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
		// TODO Auto-generated method stub
		System.out.println("decorate text");
		if(element instanceof File) {
			File file = (File) element;
			if(file.getFileExtension().equals("bib")) {
				WorkspaceBibTexEntry entry = wm.getWorkspaceBibTexEntryByUri(file.getLocationURI());
				if(entry != null) {
					if(entry.getMendeleyFolder() != null) {
						String folder_name = entry.getMendeleyFolder().getName();
						if(!text.contains("[" + folder_name + "]")) {
							String new_text = text + "  [" + folder_name + "]";
							return new_text;
						}
					}
				}
				updateMenuContribution();
			}
		}
		
		return null;
	}
	
	private void updateMenuContribution() {
		if(window instanceof WorkbenchWindow) {
		    //WWinActionBars menuManager = ((WorkbenchWindow)window).getActionBars();
			MenuManager menuManager = ((WorkbenchWindow)window).getMenuManager();
		   //Menu menu = menuManager.);
		    
		    //you'll need to find the id for the item
		    String itemId = "de.tudresden.slr.model.mendeley.menus.popupMenu";
		    IContributionItem item = menuManager.find(itemId);

		    System.out.println(item);
		    if (item != null) {
		    	System.out.println(item);
		        // clean old one
		        item.setVisible(false);

		        // refresh menu gui
		        menuManager.update();
		    }
		}
	}

}
