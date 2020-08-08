package de.tudresden.slr.classification.tests;

import static org.junit.Assert.*;


import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.xtext.testing.XtextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import de.tudresden.slr.classification.classifiers.StandardTaxonomyClassifier;
import de.tudresden.slr.model.bibtex.BibtexFactory;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.TaxonomyFactory;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.utils.taxonomy.manipulation.TermCreator;

@RunWith(XtextRunner.class)
public class StandardTaxonomyClassifierTest {

	@Before
	public void setUp() throws Exception {

	}
	
	@Test
	public void createStandardtaxonomyTest() {
		Resource taxonomyResource = new NonPersistentResource();
		Model activeModel = createTestModel(null,null,null);
		taxonomyResource.getContents().add(activeModel);
		ModelRegistryPlugin.getModelRegistry().setActiveTaxonomy(activeModel);
		
		Document genericDoc = createDocument("Generic Doc","article","journal", "Generic Journal", null,null);
		
		Document scopusDoc = createDocument("Scopus Doc","article","journal", "Generic Journal", null, "Scopus Article");
		Document crossReferencedDoc = createDocument("CrossReferenced Doc","proceedings",null, "Crossref book",null,null);
		Document crossReferencingDoc = createDocument("CrossReferencing Doc","inproceedings","booktitle","Should not be used", null, null);
		
		Resource resource = new NonPersistentResource();
		
		resource.getContents().add(genericDoc);
		resource.getContents().add(scopusDoc);
		resource.getContents().add(crossReferencedDoc);
		resource.getContents().add(crossReferencingDoc);
		
		StandardTaxonomyClassifier classifier = new StandardTaxonomyClassifier();
		Model genericModelReference = createTestModel("article","journal","Generic Journal");
		classifyAndTestAgainstReference(classifier, genericDoc, null, genericModelReference);
		
		Model scopusModelReference = createTestModel("Scopus Article","journal","Generic Journal");
		classifyAndTestAgainstReference(classifier, scopusDoc, null, scopusModelReference);
		
		Model crossReferencedModelReference = createTestModel("proceedings",null,null);
		classifyAndTestAgainstReference(classifier, crossReferencedDoc, null, crossReferencedModelReference);
	
		Model crossReferencingModelReference = createTestModel("inproceedings","booktitle","CrossReferenced Doc");
		classifyAndTestAgainstReference(classifier, crossReferencingDoc, crossReferencedDoc.getTitle(), crossReferencingModelReference);
		
		Model activeModelReference = createTestModel("article","journal","Generic Journal");
		Term docTypeTerm = activeModelReference.getDimensions().get(0);
		Term venueTypeTerm = activeModelReference.getDimensions().get(1);
		TermCreator.createChildIfNotExisting(docTypeTerm, "Scopus Article",false);
		TermCreator.createChildIfNotExisting(docTypeTerm, "proceedings",false);
		TermCreator.createChildIfNotExisting(docTypeTerm, "inproceedings",false);
		Term booktitleTerm = TermCreator.createChildIfNotExisting(venueTypeTerm, "booktitle",false);
		TermCreator.createChildIfNotExisting(booktitleTerm, "CrossReferenced Doc",false);
		
		if(!modelEquals(activeModel, activeModelReference)) fail();
	}
	
	private void classifyAndTestAgainstReference(StandardTaxonomyClassifier classifier, Document doc, String crossReferenceString, Model reference) {
		classifier.createStandardTaxonomy(doc,crossReferenceString);
		if(!modelEquals(doc.getTaxonomy(), reference)) {
			fail();
		}
		
	}
	
	private Model createTestModel(String type, String venueType, String venue) {
		Model model = TaxonomyFactory.eINSTANCE.createModel();
		Resource resource = new NonPersistentResource();
		model.setResource(resource);
		
		if(type != null) {
			Term termType = TermCreator.createChildIfNotExisting(model, "Document Type",false);
			TermCreator.createChildIfNotExisting(termType, type,false);
		}
		
		if(venueType != null) {
			Term termCategoryVenue = TermCreator.createChildIfNotExisting(model, "Document Venue",false);
			Term termVenueType = TermCreator.createChildIfNotExisting(termCategoryVenue, venueType,false);
			if(venue != null) TermCreator.createChildIfNotExisting(termVenueType, venue,false);
		}
		
		return model;
	}
	
	private Document createDocument(String title, String type, String venueType, String venue, String crossref, String scopusType) {
		Document doc = BibtexFactory.eINSTANCE.createDocument();
		Map<String,String> additionalFieldsMap = new HashMap<String,String>();
		doc.setTitle(title);
		doc.setType(type);
		
		additionalFieldsMap.put(venueType, venue);
		Model model = TaxonomyFactory.eINSTANCE.createModel();
		Resource res = new NonPersistentResource();
		res.getContents().add(model);
		model.getDimensions();
		//Somehow no new terms can be added if the model is not initialised like this
		TermCreator.createChildIfNotExisting(model, "TEST",false);
		model.getDimensions().remove(0);
		
		doc.setTaxonomy(model);
		
		if(crossref != null ) {
			additionalFieldsMap.put("crossref",crossref);
		}
		
		if(scopusType != null) {
			additionalFieldsMap.put("document_type", scopusType);
		}
		doc.setAdditionalFields(additionalFieldsMap);
		return doc;
	}

	

	public boolean modelEquals(Model m1, Model m2) {
		return areChildrenEqual(m1.getDimensions(),m2.getDimensions());
	}
	
	
	public boolean areChildrenEqual(EList<Term> children1, EList<Term> children2) {
		if(children1.size()!=children2.size()) return false;
		
		for(int i = 0; i<children1.size();i++) {
			if(!children1.get(i).getName().equals(children2.get(i).getName())) return false;
			if(!areChildrenEqual(children1.get(i).getSubclasses(),children2.get(i).getSubclasses())) return false;
		}
		return true;
	}
	
	private class NonPersistentResource extends ResourceImpl {
		@Override
		public void save(Map<?,?> options) {
			return;
		}
	}

}
