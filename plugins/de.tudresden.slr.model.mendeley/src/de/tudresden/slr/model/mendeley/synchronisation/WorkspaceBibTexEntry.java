package de.tudresden.slr.model.mendeley.synchronisation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;

/**
 * This class implements a connection between a specific Bib-File, its <samp>BibTeXEntry</samp> and a <samp>MendeleyFolder</samp>.
 * 
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class WorkspaceBibTexEntry {
	
	/**
	 * URI of a Bib-File
	 */
	private URI uri;
	
	/**
	 * IProject that contains Bib-File
	 */
	private IProject project;
	
	/**
	 * Map for BibTeXEntries
	 */
	private Map<Key, BibTeXEntry> bibEntries;
	
	/**
	 * BibTeXDatabase that is read from Bib-File
	 */
	private BibTeXDatabase bibTexDB;
	
	/**
	 * MendeleyFolder that is assigned to the URI. If no connection set MendeleyFolder will be null.
	 */
	private MendeleyFolder mendeleyFolder;
	
	/**
	 * Constructor creates an object from File <samp>URI</samp> and respective <samp>IProject</samp>. MendeleyFolder will be
	 * set to null
	 * @param uri set URI of Bib-File
	 * @param project set IProject of Bib-File
	 */
	public WorkspaceBibTexEntry(URI uri, IProject project) {
		this.uri = uri;
		this.project = project;
		this.mendeleyFolder = null;
		builtBibentriesFromFile(uri);
	}
	
	/**
	 * This method is used to read the content of a Bib-File via the JBibTex library and
	 * assigns the BibTeXDatabase to this WorkSpaceBibTexEntry
	 *  
	 * @param uri set URI of a Bib-File
	 */
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
	
	/**
	 * This method is used to update the current BibTeXDatabase. The assigned Bib-File will be re-read.
	 */
	public void updateBibTexEntry() {
		try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(uri.getPath())))) {
			BibTeXParser parser = new BibTeXParser();
			bibTexDB = parser.parse(reader);
			bibEntries = bibTexDB.getEntries();
		} catch (TokenMgrException | ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return map of BibTeXEntry from Bib-File
	 */
	public Map<Key, BibTeXEntry> getBibEntries() {
		return bibEntries;
	}
	
	/**
	 * 
	 * @return respective IProject from Bib-File
	 */
	public IProject getProject() {
		return project;
	}
	
	/**
	 * 
	 * @return URI of Bib-File
	 */
	public URI getUri() {
		return uri;
	}
	
	/**
	 * 
	 * @param bibEntries set Map of BibTeXEntries 
	 */
	public void setBibEntries(Map<Key, BibTeXEntry> bibEntries) {
		this.bibEntries = bibEntries;
	}
	
	/**
	 * 
	 * @return BibTeXDatabase of this Bib-File
	 */
	public BibTeXDatabase getBibTexDB() {
		return bibTexDB;
	}
	
	/**
	 * 
	 * @return MendeleyFolder that is assigned to Bib-File (returns null if there is no connection)
	 */
	public MendeleyFolder getMendeleyFolder() {
		return mendeleyFolder;
	}
	
	/**
	 * 
	 * @param mendeleyFolder assign a MendeleyFolder to this Bib-File
	 */
	public void setMendeleyFolder(MendeleyFolder mendeleyFolder) {
		this.mendeleyFolder = mendeleyFolder;
	}
	
	/**
	 * 
	 * @param bibTexDB set a BibTeXDatabase for this Bib-File
	 */
	public void setBibTexDB(BibTeXDatabase bibTexDB) {
		this.bibTexDB = bibTexDB;
		this.bibEntries = bibTexDB.getEntries();
	}
}
