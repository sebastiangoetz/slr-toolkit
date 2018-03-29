package de.tudresden.slr.model.mendeley.util;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jbibtex.Key;
import org.jbibtex.Value;
/**
 * This class implements the IStructuredContentProvider that is used to
 * generate the input of the conflict table.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class MendeleyTableContentProvider implements IStructuredContentProvider{

	@Override
	public Object[] getElements(Object inputElement) {
		SyncItem si = (SyncItem) inputElement;
		List<MSyncWizardTableEntry> fields = new ArrayList<>();
		
		for(Key key : si.getFields().keySet()){
			Value v1 = si.getFields().get(key).get(0);
			Value v2 = si.getFields().get(key).get(1);
			Value vSelected = si.getSelectedFields().get(key);
			MSyncWizardTableEntry entry = new MSyncWizardTableEntry(key, v1, v2, vSelected);
			entry.setSyncItem(si);
			fields.add(entry);
		}

		MSyncWizardTableEntry[] si_array = new MSyncWizardTableEntry[fields.size()];
    	si_array = fields.toArray(si_array);
		return si_array;
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
		
	}

}
