package logic;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.taxonomy.Term;

public class CitePerYearCreator {

	private Resource bibfileContainer;

	public CitePerYearCreator(Resource bibfileContainer) {
		super();
		this.bibfileContainer = bibfileContainer;
	}

	/***
	 * 
	 * @param nameOfClass
	 *            The class one wants the sub categories of
	 * @return a map with sub categories and the number of cites of papers in
	 *         them
	 */
	private HashMap<String, Integer> accumulateCites(Term inputTerm) {
		HashMap<String, Integer> countPerYearMap = new HashMap<>();

		ArrayList<String> subclasses = new ArrayList<>();
		for (Term t : inputTerm.getSubclasses())
			subclasses.add(t.getName());
		for (EObject e : bibfileContainer.getContents()) {
			if (e instanceof Document) {
				for (Term t : ((Document) e).getTaxonomy().getDimensions()) {
					if (subclasses.contains(t.getName())) {
						String name = t.getName();
						int count = countPerYearMap.containsKey(name) ? countPerYearMap
								.get(name) : 0;
						countPerYearMap.put(name, count + 1);
					}
				}
			}
		}

		return countPerYearMap;

	}

}
