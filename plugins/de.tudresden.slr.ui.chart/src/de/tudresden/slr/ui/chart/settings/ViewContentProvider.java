package de.tudresden.slr.ui.chart.settings;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;

class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
	public ViewContentProvider(Viewer v) {}
	
	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {}

	@Override
	public void dispose() {}

	@Override
	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}

	@Override
	public Object getParent(Object child) {
		if (child instanceof EObject) {
			return ((EObject) child).eContainer();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parent) {
		if (parent instanceof Model) {
			return ((Model) parent).getDimensions().toArray();
		}
		if (parent instanceof Term) {
			return ((Term) parent).getSubclasses().toArray();
		}
		throw new IllegalArgumentException("parent");
	}

	@Override
	public boolean hasChildren(Object parent) {
		if (parent instanceof EObject) {
			return getChildren(parent).length > 0;
		}
		return false;
	}
}
