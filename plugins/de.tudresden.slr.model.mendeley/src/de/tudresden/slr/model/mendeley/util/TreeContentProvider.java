package de.tudresden.slr.model.mendeley.util;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;

/**
 * This class implements the ITreeContentProvider that is used to
 * generate the input of the TreeViewer used on the
 * MSyncWizardFolderPage.
 * 
 * @author Johannes Pflugmacher
 * @version 1.0
 *
 */
public class TreeContentProvider implements ITreeContentProvider {
    @Override
    public boolean hasChildren(Object element) {
    	if(element instanceof MendeleyFolder){
    		return true;
    	}
    	else{
    		return false;
    	}
    }

    @Override
    public Object getParent(Object element) {
    	if( element == null) {
			return null;
		}
    	if(element instanceof MendeleyDocument){
    		return null;
    	}

		return ((MendeleyFolder)element);
    }

    @Override
    public Object[] getElements(Object inputElement) {
    	return (MendeleyFolder[])inputElement;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
    	List<MendeleyDocument> md_list = ((MendeleyFolder)parentElement).getDocuments();
    	MendeleyDocument[] docs_array = new MendeleyDocument[md_list.size()];
    	docs_array = md_list.toArray(docs_array);
    	
    	return docs_array;
    }

	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}
}