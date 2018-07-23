package de.tudresden.slr.model.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;

public class TaxonomyIterator implements Iterator<Term>, Iterable<Term> {

	private Queue<Term> elements;

	public TaxonomyIterator(Model model) {
		elements = new LinkedList<>(model.getDimensions());
	}

	@Override
	public boolean hasNext() {
		return !elements.isEmpty();
	}

	@Override
	public Term next() {
		Term next = elements.poll();
		if(next == null) throw new NoSuchElementException();
		elements.addAll(next.getSubclasses());
		return next;
	}

	@Override
	public Iterator<Term> iterator() {
		if (elements == null) {
			throw new IllegalStateException("");
		}
		return this;
	}
}
