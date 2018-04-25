package de.tudresden.slr.model.mendeley.util;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;

/**
 * This class implements the ILabelProvider that is used to
 * generate the input of the TreeViewer used on the
 * MSyncWizardFolderPage.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class WorkspaceTreeLabelProvider implements ILabelProvider {
	  private List listeners;
	  
	  private Image file;

	  private Image document;


	  
	  public WorkspaceTreeLabelProvider() {
	    listeners = new ArrayList();

	    file = AbstractUIPlugin.imageDescriptorFromPlugin("de.tudresden.slr.model.mendeley", "images/bibtex.png").createImage();
		document = AbstractUIPlugin.imageDescriptorFromPlugin("de.tudresden.slr.model.mendeley", "images/file.png").createImage();
	  }

	  public Image getImage(Object arg0) {
		  
		  if(arg0 instanceof WorkspaceBibTexEntry){
			  return file;
		  }
		  if(arg0 instanceof String){
			  return document;
		  }
		  
		  return null;
	  }

	  public String getText(Object arg0) {
		  String text = "";
		  if(arg0 instanceof WorkspaceBibTexEntry){
			  String[] path = ((WorkspaceBibTexEntry)arg0).getUri().toString().split("/");
			  return path[path.length-1];
		  }
		  if(arg0 instanceof String){
			  return ((String)arg0);
		  }
		  
		  return text;
	  }

	  public void addListener(ILabelProviderListener arg0) {
	    listeners.add(arg0);
	  }

	  public void dispose() {
	    // Dispose the images
	    if (document != null)
	    	document.dispose();
	    if (file != null)
	    	file.dispose();
	  }

	  public boolean isLabelProperty(Object arg0, String arg1) {
	    return false;
	  }

	  public void removeListener(ILabelProviderListener arg0) {
	    listeners.remove(arg0);
	  }
	}