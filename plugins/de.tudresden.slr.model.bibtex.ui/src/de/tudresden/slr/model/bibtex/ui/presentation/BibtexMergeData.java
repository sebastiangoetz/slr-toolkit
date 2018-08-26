package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.tudresden.slr.model.bibtex.ui.util.Utils;
import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;

public class BibtexMergeData {
	
	private List<Object> resourceList;
	private Set<Object> toMerge;
	private boolean[] args;
	private String filename;
	private List<BibtexMergeConflict> conflicts;
	private List<String> stats;
	
	public BibtexMergeData(List<Object> resources) {
		this.resourceList = resources;
		this.args = new boolean[3];
		this.filename = "[missingFilename]";
		this.conflicts = new ArrayList<BibtexMergeConflict>();
		this.stats = new ArrayList<String>();
		this.toMerge = new HashSet<Object>();
		BibtexResourceImpl res;
		for(ListIterator<Object> i = resourceList.listIterator(); i.hasNext();) {
			res = (BibtexResourceImpl) i.next();
			toMerge.add(res.getURI());
		}
	}

	public IFile merge() {
		IFile result = null;
		if(!getArgs()[0]) {
			// merge with no duplicate handling
			BibtexResourceImpl res = null;
			for(ListIterator<Object> i = resourceList.listIterator(); i.hasNext();) {
				res = (BibtexResourceImpl) i.next();
				if(!toMerge.contains(res.getURI())) {
					i.remove();
				}
			}
			IPath filePath = Utils.getIFilefromEMFResource((BibtexResourceImpl) resourceList.get(0)).getLocation();
			filePath = filePath.removeLastSegments(1);
			filePath = new Path(filePath.toString() + "/" + getFilename());
			System.out.println(filePath);
			result = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(filePath)[0];
			if(result.exists()) {
				// if file already exists, signalise that
				return null;
			}
			try {
				InputStream source = Utils.getIFilefromEMFResource((BibtexResourceImpl) resourceList.get(0)).getContents();
			    result.create(source, IResource.NONE, null);
			    for(int i = 1; i < resourceList.size(); i++) {
			    	source = Utils.getIFilefromEMFResource((BibtexResourceImpl) resourceList.get(i)).getContents();
			    	result.appendContents(source, IResource.NONE, null);
			    }
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			if(getArgs()[2]) {
				// merge with manual duplicate handling
				
			}
			else {
				// merge with automatic duplicate handling
				
			}
		}
		return result;
	}
	
	public List<Object> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Object> resourceList) {
		this.resourceList = resourceList;
	}

	public boolean[] getArgs() {
		return args;
	}

	public void setArgs(boolean[] args) {
		this.args = args;
	}

	public void setSingleArg(int index, boolean arg) {
		this.args[index] = arg;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Set<Object> getToMerge() {
		return this.toMerge;
	}
	
	public void setToMerge(Set<Object> files) {
		this.toMerge = files;
	}

	public List<BibtexMergeConflict> getConflicts() {
		return conflicts;
	}

	public void setConflicts(List<BibtexMergeConflict> conflicts) {
		this.conflicts = conflicts;
	}

	public List<String> getStats() {
		return stats;
	}

	public void addStat(String stat) {
		this.stats.add(stat);
	}
	
}
