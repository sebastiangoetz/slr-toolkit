package de.tudresden.slr.questionnaire;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public abstract class ObservableCombo<E> {

	protected Combo combo;
	protected List<String> labels = new LinkedList<>();
	protected List<E> elements = new LinkedList<>();

	private List<Consumer<E>> observers = new LinkedList<>();

	public ObservableCombo(Composite parent, GridData gridData) {
		combo = new Combo(parent, SWT.READ_ONLY);
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				notifyObservers();
			}
		});
	}

	/** should update {@link #labels} and {@link #elements} */
	protected abstract void updateLabelsAndElements();

	public E getSelected() {
		if (elements.size() == 0)
			return null;
		int index = combo.getSelectionIndex();
		if (index < 0 || index >= elements.size())
			throw new IllegalStateException();
		return elements.get(index);
	}

	public void updateOptionsDisplay() {
		labels.clear();
		elements.clear();
		updateLabelsAndElements();
		combo.removeAll();
		if (labels.size() != elements.size())
			throw new IllegalStateException("unequal sizes of labels/elements");
		labels.forEach(combo::add);

		// TODO Instead of selecting first, try to select the previous element (if it
		// still exists)
		selectFirst();
	}

	public void selectFirst() {
		combo.select(0);
		notifyObservers();
	}

	private void notifyObservers() {
		E selection = getSelected();
		observers.forEach(o -> o.accept(selection));
	}

	public void addObserver(Consumer<E> observer) {
		observers.add(observer);
	}

}