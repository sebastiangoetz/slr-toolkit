package de.tudresden.slr.model.mendeley.util;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.tudresden.slr.model.mendeley.api.model.MendeleyDocument;
import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;


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
    	//Object[] objects = ArrayContentProvider.getInstance().getElements(inputElement);
    	//Object[] objects = ((MendeleyFolder)inputElement).getDocuments();
    	return (MendeleyFolder[])inputElement;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
    	List<MendeleyDocument> md_list = ((MendeleyFolder)parentElement).getDocuments();
    	//return getElements(parentElement);
    	MendeleyDocument[] docs_array = new MendeleyDocument[md_list.size()];
    	docs_array = md_list.toArray(docs_array);
    	
    	//return ((MendeleyFolder)parentElement).getDocuments();
    	return docs_array;
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}
}