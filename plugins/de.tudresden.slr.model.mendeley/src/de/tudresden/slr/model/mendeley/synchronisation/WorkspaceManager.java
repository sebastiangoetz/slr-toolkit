package de.tudresden.slr.model.mendeley.synchronisation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import de.tudresden.slr.model.bibtex.util.BibtexResourceImpl;
import de.tudresden.slr.model.mendeley.api.authentication.MendeleyClient;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;


public class WorkspaceManager {
	
	private MendeleyClient mc = MendeleyClient.getInstance();
	private ArrayList<WorkspaceBibTexEntry> workspaceEntries = new ArrayList<WorkspaceBibTexEntry>();
	protected AdapterFactoryEditingDomain editingDomain;
	
	  private static final class InstanceHolder {
	    static final WorkspaceManager INSTANCE = new WorkspaceManager();
	  }

	  private WorkspaceManager () {}

	  public static WorkspaceManager getInstance () {
	    return InstanceHolder.INSTANCE;
	  }
	
    
    public void setWorkspaceEntries(ArrayList<WorkspaceBibTexEntry> workspaceEntries) {
		this.workspaceEntries = workspaceEntries;
	}
	
	public ArrayList<WorkspaceBibTexEntry> getWorkspaceEntries() {
		return workspaceEntries;
	}
	
	public void addWorkspaceBibtexEntry(WorkspaceBibTexEntry wEntry){
		boolean exists = false;
		
		for(WorkspaceBibTexEntry entry : this.workspaceEntries){
			if(entry.getUri().equals(wEntry.getUri())){
				exists = true;
				entry.setBibEntries(wEntry.getBibEntries());
				entry.setMendeleyFolder(wEntry.getMendeleyFolder());
				entry.setBibTexDB(wEntry.getBibTexDB());
			}
		}
		
		if(!exists){
			this.workspaceEntries.add(wEntry);
		}
	}
	
	public WorkspaceBibTexEntry getWorkspaceBibTexEntryByUri(URI uri){
		for(WorkspaceBibTexEntry entry : this.workspaceEntries){
			if(entry.getUri().equals(uri)){
				return entry;
			}
		}
		return null;
	}
	
	public ArrayList<WorkspaceBibTexEntry> getBibEntriesByProject(IProject project){
		ArrayList<WorkspaceBibTexEntry> results = new ArrayList<WorkspaceBibTexEntry>();
		for(WorkspaceBibTexEntry entry: this.workspaceEntries){
			if(entry.getProject().equals(project)){
				results.add(entry);
			}
		}
		return results;
	}
	
	public ArrayList<WorkspaceBibTexEntry> getBibEntriesByWorkspaceEntry(WorkspaceBibTexEntry wEntry){
		ArrayList<WorkspaceBibTexEntry> results = new ArrayList<WorkspaceBibTexEntry>();
		results.add(wEntry);
		return results;
	}
	
	public void updateWorkspaceBibTexEntry(WorkspaceBibTexEntry wEntry){
		for(WorkspaceBibTexEntry entry : this.workspaceEntries){
			if(entry.getUri().equals(wEntry.getUri())){
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(wEntry.getUri().getPath()));
					BibTeXDatabase bib = mc.getDocumentsByFolderBibTex(wEntry.getMendeleyFolder());
					
					BibTeXFormatter bibtexFormatter = new BibTeXFormatter();
					bibtexFormatter.format(bib, writer);
					wEntry.setBibTexDB(bib);
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TokenMgrException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void updateEditingDomain(WorkspaceBibTexEntry wEntry){
		
		ModelRegistryPlugin.getModelRegistry().getEditingDomain().ifPresent((domain) -> editingDomain = domain);
		if(editingDomain != null){
			for (Resource resource : editingDomain.getResourceSet().getResources()){
				if(wEntry.getUri().equals(resource.getURI())){
					if(resource instanceof BibtexResourceImpl){
						//((BibtexResourceImpl)resource).load(new InputStream(), options);;
					}
				}
			}
		}
	}
    
    
}
