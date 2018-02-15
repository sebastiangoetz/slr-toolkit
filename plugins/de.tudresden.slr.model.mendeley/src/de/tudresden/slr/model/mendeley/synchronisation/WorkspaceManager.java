package de.tudresden.slr.model.mendeley.synchronisation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import de.tudresden.slr.model.mendeley.api.authentication.MendeleyClient;


public class WorkspaceManager {
	
	private MendeleyClient mc = MendeleyClient.getInstance();
	private ArrayList<WorkspaceBibTexEntry> workspaceEntries = new ArrayList<WorkspaceBibTexEntry>();
	
	// Innere private Klasse, die erst beim Zugriff durch die umgebende Klasse initialisiert wird
	  private static final class InstanceHolder {
	    // Die Initialisierung von Klassenvariablen geschieht nur einmal 
	    // und wird vom ClassLoader implizit synchronisiert
	    static final WorkspaceManager INSTANCE = new WorkspaceManager();
	  }

	  // Verhindere die Erzeugung des Objektes über andere Methoden
	  private WorkspaceManager () {}
	  // Eine nicht synchronisierte Zugriffsmethode auf Klassenebene.
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
	
	public ArrayList<WorkspaceBibTexEntry> getBibEntriesByProject(IProject project){
		ArrayList<WorkspaceBibTexEntry> results = new ArrayList<WorkspaceBibTexEntry>();
		for(WorkspaceBibTexEntry entry: this.workspaceEntries){
			if(entry.getProject().equals(project)){
				results.add(entry);
			}
		}
		return results;
	}
	
	public void updateWorkspaceBibTexEntry(WorkspaceBibTexEntry wEntry){
		for(WorkspaceBibTexEntry entry : this.workspaceEntries){
			if(entry.getUri().equals(wEntry.getUri())){
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(wEntry.getUri().getPath()));
					String bibString = mc.getDocumentsByFolderBibTex(wEntry.getMendeleyFolder().getId());
					wEntry.setBibTexDB(mc.parseStringToJBibTex(bibString));
					writer.write(bibString);
					writer.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TokenMgrException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
    
    
}
