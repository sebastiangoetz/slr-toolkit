package de.tudresden.slr.ui.chart.logic;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.util.TermComparer;
import de.tudresden.slr.model.utils.SearchUtils;

public class ChartDataProvider {

	private Optional<AdapterFactoryEditingDomain> domainOptional;
	private AdapterFactoryEditingDomain adapterFactoryEditingDomain;
	private ArrayList<Resource> resources;
	private TermComparer termComparator = new TermComparer();

	public ChartDataProvider() {
		domainOptional = ModelRegistryPlugin.getModelRegistry().getEditingDomain();
		if (domainOptional.isPresent()) {
			adapterFactoryEditingDomain = domainOptional.get();
			resources = new ArrayList<>(adapterFactoryEditingDomain.getResourceSet().getResources());
		}
	}

	/***
	 * 
	 * @param term
	 *            The term one wants the sub categories of
	 * @return a map with sub categories and the number of cites of papers in
	 *         them
	 */
	// TODO: not yet implemented
	public SortedMap<String, Integer> accumulateCites(Term inputTerm) {
		SortedMap<String, Integer> countPerYearMap = new TreeMap<>();

		ArrayList<String> subclasses = new ArrayList<>();
		for (Term t : inputTerm.getSubclasses())
			subclasses.add(t.getName());

		return countPerYearMap;

	}

	/**
	 * 
	 * @param startNode
	 *            Where to look for
	 * @param searchTerm
	 *            The term that is looked for
	 * @return Whether the searchTerm is included anywhere in the startNode
	 *         taxonomy
	 */
	private boolean isTermIncludedInTaxonomy(Term startNode, Term searchTerm) {
		
		//if (startNode.hashCode() == searchTerm.hashCode())
		if(termComparator.equals(startNode, searchTerm)){
			return true;
		}
		ArrayList<Term> subclasses = new ArrayList<>(startNode.getSubclasses());
		if (subclasses.size() > 0) {
			for (Term t : subclasses) {
				if (isTermIncludedInTaxonomy(t, searchTerm)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param inputTerm
	 *            The term one wants to find all the papers for
	 * @return A mapping from sub-term names to number of papers they are
	 *         assigned to
	 */

	public SortedMap<String, Integer> calculateNumberOfPapersPerClass(Term inputTerm) {
		SortedMap<String, Integer> countOfPapersPerSubTerm = new TreeMap<>();
		ArrayList<Term> subclasses = new ArrayList<>(inputTerm.getSubclasses());
		for (Term searchTerm : subclasses) {
			// TODO: just working for a single bibtex file at the moment
			for (Document d : getDocumentList(resources.get(0))) {
				for (Term t : getDimensionsForDocument(d)) {
					boolean isTermFoundInPaperTaxonomy = isTermIncludedInTaxonomy(
							t, searchTerm);
					if (isTermFoundInPaperTaxonomy) {
						String name = searchTerm.getName();
						int count = countOfPapersPerSubTerm.containsKey(name) ? countOfPapersPerSubTerm
								.get(name) : 0;
						countOfPapersPerSubTerm.put(name, count + 1);
					}
				}
			}
		}
		return countOfPapersPerSubTerm;
	}

	public SortedMap<String, Integer> calculateNumberOfCitesPerYearForClass(
			Term inputTerm) {
		SortedMap<String, Integer> citesPerYear = new TreeMap<>();
		boolean isTermFoundInPaperTaxonomy = false;
		for (Document d : getDocumentList(resources.get(0))) {
			isTermFoundInPaperTaxonomy = SearchUtils.findTermInDocument(d,
					inputTerm) != null;
			if (isTermFoundInPaperTaxonomy) {
				String year = d.getYear();
				int count = citesPerYear.containsKey(year) ? citesPerYear
						.get(year) : 0;
				citesPerYear.put(year, count + d.getCites());

			}
		}
		return citesPerYear;
	}

	/**
	 * 
	 * @param inputList
	 *            The list of BubbleChartData
	 * @return The modified list which contains how many papers are assigned the
	 *         two terms from the BubbleChartDataContainer
	 */
	public List<BubbleDataContainer> calculateBubbleChartData(Term firstTerm,
			Term secondTerm) {
		List<BubbleDataContainer> inputList = generatePairsForBubbleChart(
				firstTerm, secondTerm);
		for (BubbleDataContainer b : inputList) {
			for (Document d : getDocumentList(resources.get(0))) {
				if ((SearchUtils.findTermInDocument(d, b.getxTerm()) != null)
						&& (SearchUtils.findTermInDocument(d, b.getyTerm()) != null)) {
					b.setBubbleSize(b.getBubbleSize() + 1);

				}
			}
		}

		return inputList;
	}

	/**
	 * Helper function to get a list of documents from a resource
	 * 
	 * @param r
	 *            The resource
	 * @return
	 */
	private List<Document> getDocumentList(Resource r) {
		ArrayList<Document> results = new ArrayList<>();
		for (EObject e : r.getContents()) {
			if (e instanceof Document) {
				results.add((Document) e);
			}
		}
		return results;
	}

	private List<Term> getDimensionsForDocument(Document doc) {
		return doc.getTaxonomy().getDimensions();
	}

	/**
	 * Helper function to generate term pairs for the bubble chart
	 * 
	 * @param firstTerm
	 * @param secondTerm
	 * @return
	 */
	private List<BubbleDataContainer> generatePairsForBubbleChart(
			Term firstTerm, Term secondTerm) {
		ArrayList<BubbleDataContainer> bubbleChartData = new ArrayList<>();
		for (Term subclassFromFirst : firstTerm.getSubclasses()) {
			for (Term subclassFromSecond : secondTerm.getSubclasses()) {
				bubbleChartData.add(new BubbleDataContainer(subclassFromFirst,
						subclassFromSecond, 0));
			}
		}
		return bubbleChartData;
	}
}
