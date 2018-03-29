package de.tudresden.slr.model.mendeley.util;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

/**
 * This class implements the EdditingSupport of the conflict table
 * so that it is possible to choose between two different field Values
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class MendeleyTableEditingSupport extends EditingSupport {
	
	TableViewer tableViewer;
	ComboBoxCellEditor cbce;
	
	public MendeleyTableEditingSupport(TableViewer viewer) {
		super(viewer);
		tableViewer = viewer;
	}

	@Override
	protected void setValue(Object element, Object value) {
		MSyncWizardTableEntry entry = (MSyncWizardTableEntry) element;
		
		switch ((int) value) {
			case -1: 
					break;
			case 0: 
					entry.setSelected(entry.getValue1());
					entry.getSyncItem().addSelectedField(entry.getKey(), entry.getValue1());
					break;
			case 1: 
					entry.setSelected(entry.getValue2());
					entry.getSyncItem().addSelectedField(entry.getKey(), entry.getValue2());
					break;
			default: 
					break;
		}
		
		tableViewer.update(element, null);
	}

	@Override
	protected Object getValue(Object element) {
		MSyncWizardTableEntry entry = (MSyncWizardTableEntry) element;
		
		if(entry.getSelected()== null){
			if(entry.getValue1() == null) {
				return 0;
			}
			else {
				return 1;
			}	
		}
		
		if(entry.getSelected().equals(entry.getValue1())) 
			return 0;
		else 
			return 1;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		MSyncWizardTableEntry entry = (MSyncWizardTableEntry) element;
		
		if(entry.getValue1() != null && entry.getValue2() != null){
			cbce = new ComboBoxCellEditor(
					tableViewer.getTable(), 
					new String[] { 	entry.getValue1().toUserString() + " \t     @Mendeley", 
									entry.getValue2().toUserString() + " \t     @Workspace" }, 
					SWT.BORDER | SWT.READ_ONLY);
		}
		
		if(entry.getValue1() != null && entry.getValue2() == null){
			cbce = new ComboBoxCellEditor(
					tableViewer.getTable(), 
					new String[] {	entry.getValue1().toUserString() + " \t     @Mendeley", 
									"--[empty]-- \t     @Workspace" }, 
					SWT.BORDER | SWT.READ_ONLY);
		}
		
		if(entry.getValue1() == null && entry.getValue2() != null){
			cbce = new ComboBoxCellEditor(
					tableViewer.getTable(), 
					new String[] { 	"--[empty]-- \t     @Mendeley", 
									entry.getValue2().toUserString() + " \t     @Workspace" }, 
					SWT.BORDER | SWT.READ_ONLY);
		}
	
		return cbce;
	}

	@Override
	protected boolean canEdit(Object element) {
		MSyncWizardTableEntry entry = (MSyncWizardTableEntry) element;
		if(entry.getValue1() != null && entry.getValue2() != null){
			if(entry.getValue1().toUserString().equals(entry.getValue2().toUserString()) ){
				return false;
			}
		}
		return true;
	}
	
	
}
