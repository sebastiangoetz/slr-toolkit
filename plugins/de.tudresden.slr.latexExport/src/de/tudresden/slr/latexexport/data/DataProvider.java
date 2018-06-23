package de.tudresden.slr.latexexport.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import com.ibm.icu.util.ULocale;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.ui.chart.logic.BarChartGenerator;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;

public class DataProvider {
//	private Optional<AdapterFactoryEditingDomain> domainOptional;
//	private AdapterFactoryEditingDomain adapterFactoryEditingDomain;
	private List<Document> documents = SearchUtils.getDocumentList();
	private Optional<Model> taxonomyOptional = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy();
	
	public DataProvider() {
//		domainOptional = ModelRegistryPlugin.getModelRegistry().getEditingDomain();
//		if (domainOptional.isPresent()) {
//			adapterFactoryEditingDomain = domainOptional.get();
//			resources = new ArrayList<>(adapterFactoryEditingDomain.getResourceSet().getResources());
//		}
	}

	public List<Document> getDocuments() {
		return documents;
	}
	
	public EList<Term> getMainDimensions() {
		if(taxonomyOptional.isPresent()) {
			return taxonomyOptional.get().getDimensions();
		}
		else {
			return new BasicEList<Term>();
		}
	}
	
	/**
	 * 
	 * @return number of documents which are in the current SLR project
	 */
	public int getNumberOfDocuments() {
		return documents.size();
	}
	
	public EList<Term> getAllSubdimenions(Term t){
		EList<Term> toReturn = new BasicEList<Term>();
		for(Term subTerm : t.getSubclasses()) {
			toReturn.add(subTerm);
			toReturn.addAll(getAllSubdimenions(subTerm));
		}
		
		return toReturn;
		
	}
	
	public EList<Term> getAllDimensionsOrdered(){
		EList<Term> allTerms = new BasicEList<Term>();
		EList<Term> allClasses = getMainDimensions();

		for (Term t : allClasses) {
			allTerms.add(t);
			allTerms.addAll(getAllSubdimenions(t));
		}
		
		return allTerms;
	}
	
	public int getNumberOfElementsInDimension(Term t) {
		Map<Document, Term> termsInDocuments = SearchUtils.findDocumentsWithTerm(t);
		return termsInDocuments.size();
	}
	
	public String[] getDimensionInformation(){
		EList<Term> dimensions = getAllDimensionsOrdered();
		String[] toReturn = new String[dimensions.size()];
		
		int index = 0;
		
		if(dimensions.size() > 0) {
			for(Term t : dimensions) {
				toReturn[index] = extractMetainformationFromDimension(t);
			}	
		}
		
		return toReturn;
	}

	public String extractMetainformationFromDimension(Term t) {    
		//TODO: über regexp?
		//TODO: assertions
		int indexBegin, indexEnd;
		indexBegin = t.getName().indexOf('[');
		indexEnd = t.getName().indexOf(']');
		
		if(indexBegin > 0 && indexEnd > 0) {
			return t.getName().substring(t.getName().indexOf("[") + 1, t.getName().indexOf("]"));
		}
		else {
			return "";
		}
	}
	
	public Map<Term, String> getAllDimensionsAndMetainformation(boolean allDimensions){
		Map<Term, String> toReturn = new HashMap<Term, String>();
		EList<Term> data;
		
		if(allDimensions) {
			data = getAllDimensionsOrdered();
		}else {
			data = getMainDimensions();
		}
		
		for(Term t : data) {
			toReturn.put(t, extractMetainformationFromDimension(t));
		}
		
		return toReturn;
	}
	

	

	
	
	

}
