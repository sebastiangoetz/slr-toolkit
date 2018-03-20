package de.tudresden.slr.model.mendeley.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;

public class MendeleyTreeLabelProvider implements ILabelProvider {
	  private List listeners;
	  
	  private Image file;

	  private Image dir;

	  private Image upload;

	  private Image download;
	  
	  private Image edit;

	  
	  public MendeleyTreeLabelProvider() {
	    listeners = new ArrayList();
	    file = AbstractUIPlugin.imageDescriptorFromPlugin("de.tudresden.slr.model.mendeley", "images/file.png").createImage();
		dir = AbstractUIPlugin.imageDescriptorFromPlugin("de.tudresden.slr.model.mendeley", "images/directory.png").createImage();
		upload = AbstractUIPlugin.imageDescriptorFromPlugin("de.tudresden.slr.model.mendeley", "images/upload.png").createImage();
		download = AbstractUIPlugin.imageDescriptorFromPlugin("de.tudresden.slr.model.mendeley", "images/download.png").createImage();
		edit = AbstractUIPlugin.imageDescriptorFromPlugin("de.tudresden.slr.model.mendeley", "images/edit.png").createImage();
	  }

	  public Image getImage(Object arg0) {
		  
		  if(arg0 instanceof MendeleyFolder){
			  if(((MendeleyFolder)arg0).getType().equals("AddToMendeley")){
				  return upload;
			  }
			  if(((MendeleyFolder)arg0).getType().equals("AddToWorkspace")){
				  return download;
			  }
			  if(((MendeleyFolder)arg0).getType().equals("Comparison")){
				  return edit;
			  }
			  return dir;
		  }
		  if(arg0 instanceof MendeleyDocument){
			  return file;
		  }
		  
		  return null;
	  }

	  public String getText(Object arg0) {
		  String text = "";
		  if(arg0 instanceof MendeleyFolder){
			  	text = ((MendeleyFolder)arg0).getName();
		  }
		  if(arg0 instanceof MendeleyDocument){
			  	text = ((MendeleyDocument)arg0).getTitle();
		  }
		  
		  return text;
	  }

	  public void addListener(ILabelProviderListener arg0) {
	    listeners.add(arg0);
	  }

	  public void dispose() {
	    // Dispose the images
	    if (dir != null)
	      dir.dispose();
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