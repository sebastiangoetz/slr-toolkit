package de.tudresden.slr.questionnaire;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public abstract class ObservableCombo<E> {

	protected Combo combo;
	protected List<String> labels = new LinkedList<>();
	protected List<E> elements = new LinkedList<>();

	private List<Consumer<E>> observers = new LinkedList<>();

	public ObservableCombo(Composite parent, GridData gridData) {
	    Composite container = new Composite(parent, SWT.NONE);
	    container.setLayout(new GridLayout(2, false));
	    
		combo = new Combo(container, SWT.READ_ONLY);
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				notifyObservers();
			}
		});
		Button refresh = new Button(container, SWT.PUSH);
		refresh.setText("Refresh");
		refresh.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
		        updateOptionsDisplay();
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
	
	public String getSelectedLabel() {
        if (labels.size() == 0)
            return null;
        int index = combo.getSelectionIndex();
        if (index < 0 || index >= labels.size())
            throw new IllegalStateException();
        return labels.get(index);
    }

	public void updateOptionsDisplay() {
	    String previousLabel = getSelectedLabel();
	    
		labels.clear();
		elements.clear();
		updateLabelsAndElements();
		combo.removeAll();
		if (labels.size() != elements.size())
			throw new IllegalStateException("unequal sizes of labels/elements");
		labels.forEach(combo::add);

		int index = labels.indexOf(previousLabel);
		select(index);
	}

	private void select(int n) {
	    if (n < 0) n = 0;
	    combo.select(n);
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