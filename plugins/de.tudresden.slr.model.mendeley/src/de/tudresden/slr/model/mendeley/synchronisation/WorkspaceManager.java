package de.tudresden.slr.model.mendeley.synchronisation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import com.google.gson.Gson;

import de.tudresden.slr.model.mendeley.api.client.MendeleyClient;
import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;

/**
 * This implements a class that is used to manage the connections between Bib-Files and MendeleyFolders.
 * In order to do that this class holds a <samp>WorkspaceBibTexEntry</samp> for every Bib-File that exists
 * in this Workspace. It is also responsible for updating these Bib-Files and MendeleyFolders if changes
 * occur. There is only a single instance of this class.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class WorkspaceManager {
	
	private MendeleyClient mc = MendeleyClient.getInstance();
	
	/**
	 * A list of WorkspaceBibTexEntries. There is a WorkspaceBibTexEntry for every Bib-File
	 */
	private ArrayList<WorkspaceBibTexEntry> workspaceEntries = new ArrayList<WorkspaceBibTexEntry>();
	
	  private static final class InstanceHolder {
	    static final WorkspaceManager INSTANCE = new WorkspaceManager();
	  }

	  private WorkspaceManager () {}

	  public static WorkspaceManager getInstance () {
	    return InstanceHolder.INSTANCE;
	  }
	
    /**
     * 
     * @param workspaceEntries set the ArrayList of WorkspaceBibTexEntries
     */
    public void setWorkspaceEntries(ArrayList<WorkspaceBibTexEntry> workspaceEntries) {
		this.workspaceEntries = workspaceEntries;
	}
	
    /**
     * 
     * @return an ArrayList of WorkspaceBibTexEntries. One entry for each Bib-File.
     */
	public ArrayList<WorkspaceBibTexEntry> getWorkspaceEntries() {
		return workspaceEntries;
	}
	
	/**
	 * This method adds a WorkspaceBibTeXEntry to the ArrayList.
	 * If an entry already exists it will be replaced by the new entry
	 * @param wEntry pass a WorkspaceBibTexEntry you want to add or update
	 */
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
	
	/**
	 * This methods returns the WorkspaceBibTexEntry of a Bib-File by a given URI
	 * The Entry will be taken from the ArrayList
	 * 
	 * @param uri URI of a Bib-File
	 * @return WorkspaceBibTexEntry if it is stored in the ArrayList
	 */
	public WorkspaceBibTexEntry getWorkspaceBibTexEntryByUri(URI uri){
		for(WorkspaceBibTexEntry entry : this.workspaceEntries){
			if(entry.getUri().equals(uri)){
				return entry;
			}
		}
		return null;
	}
	
	/**
	 * This methods returns an ArrayList of all WorkspaceBibTexEntries for a specific
	 * Project.
	 * 
	 * @param project 
	 * @return ArrayList of all WorkspaceBibTexEntries that are part of a specific Project
	 */
	public ArrayList<WorkspaceBibTexEntry> getBibEntriesByProject(IProject project){
		ArrayList<WorkspaceBibTexEntry> results = new ArrayList<WorkspaceBibTexEntry>();
		for(WorkspaceBibTexEntry entry: this.workspaceEntries){
			if(entry.getProject().equals(project)){
				results.add(entry);
			}
		}
		return results;
	}
	
	/**
	 * 
	 * @param wEntry
	 * @return the given WorkspaceBibTexEntry as ArrayList
	 */
	public ArrayList<WorkspaceBibTexEntry> getBibEntriesByWorkspaceEntry(WorkspaceBibTexEntry wEntry){
		ArrayList<WorkspaceBibTexEntry> results = new ArrayList<WorkspaceBibTexEntry>();
		results.add(wEntry);
		return results;
	}
	
	/**
	 * This method updates a specific WorkspaceBibTexEntry by re-reading and parsing the content of
	 * its Bib-File. 
	 * 
	 * @param wEntry WorkspaceBibTexEntry that needs to be updated
	 */
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
	
	
	/**
	 * This method searches every WorkspaceBibTexEntry that has a MendeleyFolder to
	 * synchronize it with the latest content of its Bib-File.
	 * 
	 * @param monitor is needed as part of a job to show user the progress 
	 * @return IStatus of a job
	 */
	public IStatus updateSyncFolders(IProgressMonitor monitor) {
		
		int tasks = 0;
		
		// searching for the number of tasks/documents inside of the bib-files
		for(WorkspaceBibTexEntry entry : workspaceEntries) {
			if(entry.getMendeleyFolder() != null) {
				// delete document from Mendeley if they were removed from Bib-File
				removeDeletedEntriesInMendeley(entry);
				
				entry.updateBibTexEntry();
				tasks += entry.getBibEntries().values().size();
			}
		}
		
		//split monitor with number of tasks
		SubMonitor subMonitor = SubMonitor.convert(monitor, tasks);
		int workRemaining = tasks;
		for(WorkspaceBibTexEntry entry : workspaceEntries) {
			if(entry.getMendeleyFolder() != null) {
				try {
					subMonitor.setTaskName("Download latest Content of " + entry.getMendeleyFolder().getName());
					
					// get newest Mendeley Folder
					MendeleyFolder updated_folder = mc.getMendeleyFolder(entry.getMendeleyFolder().getId());
					updated_folder.setName(entry.getMendeleyFolder().getName());
					entry.setMendeleyFolder(updated_folder);
					
					// read latest Bib-File content
					entry.updateBibTexEntry();
					
					Gson gson = new Gson();
					String documents_string = mc.getDocumentsByFolderJSON(entry.getMendeleyFolder().getId());
					MendeleyDocument[] documents = gson.fromJson(documents_string, MendeleyDocument[].class);
					
					subMonitor.setTaskName("Synchronize Content between " + entry.getUri().getPath().substring(entry.getUri().getPath().lastIndexOf("/"))
												+ " and Mendeley Folder \"" + entry.getMendeleyFolder().getName() + "\"");
					
					// iterate over BibTexEntries of Bib-File
					for(BibTeXEntry bib_entry : entry.getBibTexDB().getEntries().values()) {
						boolean exists = false;
						for(MendeleyDocument document : documents) {
							subMonitor.subTask("Update BibTex-Entry: " + bib_entry.getField(new Key("title")).toUserString());
							
							// if there is a similar document found at Mendeley it should be updated with bib-file content 
							if(bib_entry.getField(new Key("title")).toUserString().equals(document.getTitle())) {
								document.updateFields(bib_entry.getFields());
								mc.updateDocument(document);
								exists = true;
							}
						}
						// if there is a new entry in bib-file that doesn't exists in Mendeley Folder: add this to Mendeley Folder
						if(!exists) {
							MendeleyDocument document = mc.addDocument(bib_entry);
							mc.addDocumentToFolder(document, entry.getMendeleyFolder().getId());
						}
						subMonitor.worked(1);
						if (subMonitor.isCanceled()) return Status.CANCEL_STATUS;
					}
					//update bib-file wih synchronized content
					this.updateWorkspaceBibTexEntry(entry);
					
				} catch (TokenMgrException | IOException | ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return Status.OK_STATUS;
	}
	
	/**
	 * This method synchronizes a Bib-File with its connected Mendeley Folder.
	 * The latest content of a Bib-File will be transfered to its Mendeley Folder-
	 * 
	 * @param entry WorkspaceBibTexEntry that should be updated
	 * @param monitor IMonitor is needed as part of a job to show user the progress 
	 * @return IStatus of a job
	 */
	public IStatus updateSyncFolder(WorkspaceBibTexEntry entry, IProgressMonitor monitor) {
		// similar to updateSyncFolders - you can find detailed comments over there
		if(entry.getMendeleyFolder() != null) {
			
			try {
				removeDeletedEntriesInMendeley(entry);
				entry.updateBibTexEntry();
				int tasks = entry.getBibTexDB().getEntries().size() + 1;
				SubMonitor subMonitor = SubMonitor.convert(monitor, tasks);
				
				subMonitor.setTaskName("Download latest Content of " + entry.getMendeleyFolder().getName());
				MendeleyFolder updated_folder = mc.getMendeleyFolder(entry.getMendeleyFolder().getId());
				updated_folder.setName(entry.getMendeleyFolder().getName());
				entry.setMendeleyFolder(updated_folder);
				
				subMonitor.worked(1);
				
				Gson gson = new Gson();
				String documents_string = mc.getDocumentsByFolderJSON(entry.getMendeleyFolder().getId());
				MendeleyDocument[] documents = gson.fromJson(documents_string, MendeleyDocument[].class);
				
				
				subMonitor.setTaskName("Synchronize Content between " + entry.getUri().getPath().substring(entry.getUri().getPath().lastIndexOf("/"))
						+ " and Mendeley Folder \"" + entry.getMendeleyFolder().getName() + "\"");

				for(BibTeXEntry bib_entry : entry.getBibTexDB().getEntries().values()) {
					boolean exists = false;
					for(MendeleyDocument document : documents) {
						subMonitor.subTask("Update BibTex-Entry: " + bib_entry.getField(new Key("title")).toUserString());
						
						if(bib_entry.getField(new Key("title")).toUserString().equals(document.getTitle())) {
							document.updateFields(bib_entry.getFields());
							mc.updateDocument(document);
							exists = true;
						}
					}
					if(!exists) {
						MendeleyDocument document = mc.addDocument(bib_entry);
						mc.addDocumentToFolder(document, entry.getMendeleyFolder().getId());
					}
					subMonitor.worked(1);
					if (subMonitor.isCanceled()) return Status.CANCEL_STATUS;
				}
				
				this.updateWorkspaceBibTexEntry(entry);
				
			} catch (TokenMgrException | IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Status.OK_STATUS;
	}
	
	/**
	 * This Method updates a WorkspaceBibTexEntry or adds it when it is missing.
	 * Only BibTexEntries will be updated.
	 * 
	 * @param wEntry WorkspaceBibTexEntry with latest data
	 */
	public void updateFileBibTex(WorkspaceBibTexEntry wEntry) {
		boolean exists = false;
		
		for(WorkspaceBibTexEntry entry : this.workspaceEntries){
			if(entry.getUri().equals(wEntry.getUri())){
				exists = true;
				entry.setBibEntries(wEntry.getBibEntries());
				entry.setBibTexDB(wEntry.getBibTexDB());
			}
		}
		
		if(!exists){
			 addWorkspaceBibtexEntry(wEntry);
		}
	}
    
	/**
	 * This methods searches for BibTexEntries stored in a Bib-File that were recently deleted.
	 * If a missing BibTexEntry is found it will also be removed from the online Mendeley Folder
	 * 
	 * @param old_entries 
	 */
    private void removeDeletedEntriesInMendeley(WorkspaceBibTexEntry old_entries) {
    	
    	try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(old_entries.getUri().getPath())))) {
			BibTeXParser parser = new BibTeXParser();
			BibTeXDatabase new_entries = parser.parse(reader);
			
			for(BibTeXEntry old_entry : old_entries.getBibTexDB().getEntries().values()) {
	    		boolean exists = false;
	    		for(BibTeXEntry new_entry : new_entries.getEntries().values()) {
	    			if(new_entry.getField(new Key("title")).toUserString().equals(old_entry.getField(new Key("title")).toUserString())) {
	    				exists = true;
	    			}
	    		}    		
	    		if(!exists) {
	    			MendeleyDocument document = old_entries.getMendeleyFolder().getDocumentByTitle(old_entry.getField(new Key("title")).toUserString());
	    			mc.deleteDocument(document);
	    		}
	    	}
		} catch (TokenMgrException | ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
}
