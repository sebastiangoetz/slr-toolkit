package de.tudresden.slr.model.mendeley.synchronisation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;

public class WorkspaceBibTexEntry {
	private URI uri;
	private IProject project;
	private Map<Key, BibTeXEntry> bibEntries;
	private BibTeXDatabase bibTexDB;
	private MendeleyFolder mendeleyFolder;
	
	public WorkspaceBibTexEntry(URI uri, IProject project) {
		this.uri = uri;
		this.project = project;
		this.mendeleyFolder = null;
		builtBibentriesFromFile(uri);
	}
	
	private void builtBibentriesFromFile(URI uri){
		try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(uri.getPath())))) {
			BibTeXParser parser = new BibTeXParser();
			bibTexDB = parser.parse(reader);
			bibEntries = bibTexDB.getEntries();
		} catch (TokenMgrException | ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public void updateBibTexEntry() {
		try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(uri.getPath())))) {
			BibTeXParser parser = new BibTeXParser();
			bibTexDB = parser.parse(reader);
			bibEntries = bibTexDB.getEntries();
		} catch (TokenMgrException | ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public Map<Key, BibTeXEntry> getBibEntries() {
		return bibEntries;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public URI getUri() {
		return uri;
	}
	
	public void setBibEntries(Map<Key, BibTeXEntry> bibEntries) {
		this.bibEntries = bibEntries;
	}
	
	public BibTeXDatabase getBibTexDB() {
		return bibTexDB;
	}
	
	public MendeleyFolder getMendeleyFolder() {
		return mendeleyFolder;
	}
	
	public void setMendeleyFolder(MendeleyFolder mendeleyFolder) {
		this.mendeleyFolder = mendeleyFolder;
	}
	
	public void setBibTexDB(BibTeXDatabase bibTexDB) {
		this.bibTexDB = bibTexDB;
		this.bibEntries = bibTexDB.getEntries();
	}
	
}
