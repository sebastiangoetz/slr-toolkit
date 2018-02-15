package de.tudresden.slr.model.mendeley.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;
import de.tudresden.slr.model.mendeley.synchronisation.WorkspaceBibTexEntry;

public class WorkspaceTreeLabelProvider implements ILabelProvider {
	  private List listeners;
	  
	  private Image file;

	  private Image dir;

	  private Image upload;

	  private Image download;
	  
	  private Image edit;

	  
	  public WorkspaceTreeLabelProvider() {
	    listeners = new ArrayList();

	    try {
	    	file = new Image(null, new FileInputStream("/plugins/de.tudresden.slr.model.mendeley/images/file.png"));
	    	dir = new Image(null, new FileInputStream("/plugins/de.tudresden.slr.model.mendeley/images/directory.png"));
	    	upload = new Image(null, new FileInputStream("/plugins/de.tudresden.slr.model.mendeley/images/upload.png"));
	    	download = new Image(null, new FileInputStream("/plugins/de.tudresden.slr.model.mendeley/images/download.png"));
	    	edit = new Image(null, new FileInputStream("/plugins/de.tudresden.slr.model.mendeley/images/edit.png"));
	    } catch (FileNotFoundException e) {
	    }
	  }

	  public Image getImage(Object arg0) {
		  
		  if(arg0 instanceof WorkspaceBibTexEntry){
			  return dir;
		  }
		  if(arg0 instanceof String){
			  return file;
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