package logic;

import java.util.ArrayList;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Term;

public class ChartDataProvider {

	private Optional<AdapterFactoryEditingDomain> domainOptional;
	private AdapterFactoryEditingDomain adapterFactoryEditingDomain;
	private ArrayList<Resource> resources;

	public ChartDataProvider() {
		domainOptional = ModelRegistryPlugin.getModelRegistry()
				.getEditingDomain();
		if (domainOptional.isPresent()) {
			adapterFactoryEditingDomain = domainOptional.get();
			resources = new ArrayList<>(adapterFactoryEditingDomain
					.getResourceSet().getResources());
		}
	}

	/***
	 * 
	 * @param term
	 *            The term one wants the sub categories of
	 * @return a map with sub categories and the number of cites of papers in
	 *         them
	 */
	public SortedMap<String, Integer> accumulateCites(Term inputTerm) {
		SortedMap<String, Integer> countPerYearMap = new TreeMap<>();

		ArrayList<String> subclasses = new ArrayList<>();
		for (Term t : inputTerm.getSubclasses())
			subclasses.add(t.getName());

		// for (Term t : d.getTaxonomy().getDimensions()) {
		// if (subclasses.contains(t.getName())) {
		// String name = t.getName();
		// int count = countPerYearMap.containsKey(name) ? countPerYearMap
		// .get(name) : 0;
		// countPerYearMap.put(name, count + 1);
		// }
		// }

		return countPerYearMap;

	}

	public SortedMap<String, Integer> calculateNumberOfPapersPerClass(
			Term inputTerm) {
		SortedMap<String, Integer> countOfPapersPerSubTerm = new TreeMap<>();
		ArrayList<Term> subclasses = new ArrayList<>(inputTerm.getSubclasses());
		for (EObject e : resources.get(0).getContents()) {
			if (e instanceof Document) {
				Document workingCopy = (Document) e;
				for (Term t : workingCopy.getTaxonomy().getDimensions()) {
					if (t != null && subclasses.contains(t)) {
						String name = t.getName();
						int count = countOfPapersPerSubTerm.containsKey(name) ? countOfPapersPerSubTerm
								.get(name) : 0;
						countOfPapersPerSubTerm.put(name, count + 1);
					}
				}

			}
		}
		if (countOfPapersPerSubTerm.size() == 0)
			throw new RuntimeException("There is no data to display.");
		return countOfPapersPerSubTerm;
	}
}
