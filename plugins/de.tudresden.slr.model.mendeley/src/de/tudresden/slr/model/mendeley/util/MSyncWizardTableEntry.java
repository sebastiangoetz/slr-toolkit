package de.tudresden.slr.model.mendeley.util;
import org.jbibtex.Key;
import org.jbibtex.Value;

/**
 * This class is used to fill the Table of the MSyncWizardConflictPage.
 * For each BibTeX field Value there are 2 possible sources:
 * <ul>
 * 	<li> Value from a Bib-File field </li>
 * 	<li> Value from a Mendeley Document field </li>
 * </ul>
 * 
 * Depending on the users selection between those two there will be
 * a third value that stores the desired source.
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class MSyncWizardTableEntry {
	
	/**
	 * Key of the field
	 */
	private Key key;
	
	/**
	 * Value of Mendeley field
	 */
	private Value value1;
	
	/**
	 * Value of Bib-File field
	 */
	private Value value2;
	
	/**
	 * Value of field selected by user (Default is Mendeley)
	 */
	private Value selected;
	
	private SyncItem syncItem;
	
	public MSyncWizardTableEntry(Key key, Value v1, Value v2, Value selected){
		this.key = key;
		this.value1 = v1;
		this.value2 = v2;
		this.selected = selected;
		
	}
	
	public Key getKey() {
		return key;
	}
	
	public Value getValue1() {
		return value1;
	}
	
	public Value getValue2() {
		return value2;
	}
	
	public Value getSelected() {
		return selected;
	}
	
	public void setSelected(Value selected) {
		this.selected = selected;
	}
	
	public SyncItem getSyncItem() {
		return syncItem;
	}
	
	public void setSyncItem(SyncItem syncItem) {
		this.syncItem = syncItem;
	}
	
	
}
