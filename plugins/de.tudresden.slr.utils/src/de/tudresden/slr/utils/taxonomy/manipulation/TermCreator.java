package de.tudresden.slr.utils.taxonomy.manipulation;

import java.util.Optional;

import org.eclipse.emf.ecore.EObject;

import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.TaxonomyFactory;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.model.utils.TaxonomyUtils;
import de.tudresden.slr.utils.TermPosition;

public class TermCreator {
	
	public static Term create(String name, Term relative, TermPosition position) {
		Term createdTerm = TaxonomyFactory.eINSTANCE.createTerm();
		createdTerm.setName(name);
		if (position == TermPosition.SUBTERM) {
			relative.getSubclasses().add(createdTerm);
		} else if (position == TermPosition.NEIGHBOR) {
			EObject container = relative.eContainer();
			if (container instanceof Term) {
				((Term) container).getSubclasses().add(createdTerm);
			} else if (container instanceof Model) {
				((Model) container).getDimensions().add(createdTerm);
			}
		}
		Optional<Model> model = SearchUtils.getContainingModel(relative);
		if (model.isPresent()) {
			TaxonomyUtils.saveTaxonomy(model.get());
		}
		return createdTerm;
	}
	
	
	public static Term createChildIfNotExisting(EObject parent, String name) {
		return createChildIfNotExisting(parent,name,true);
	}
	
	public static Term createChildIfNotExisting(EObject parent, String name,boolean doSave) {
		Optional<Term> existingTerm = SearchUtils.findChildWithName(parent,name);
		if(existingTerm.isPresent()) {
			 return existingTerm.get();
		} else {
			 Term newTerm = TaxonomyFactory.eINSTANCE.createTerm();
			 newTerm.setName(name);
			 SearchUtils.getChildren(parent).add(newTerm);
			 if(doSave) {
				 if(parent instanceof Term) {
					 SearchUtils.getConainingModel((Term) parent).ifPresent((model -> TaxonomyUtils.saveTaxonomy(model)));
				 } else {
					 TaxonomyUtils.saveTaxonomy((Model) parent);
				 }
			 }
			 return newTerm;
			
		}
	 }
}
