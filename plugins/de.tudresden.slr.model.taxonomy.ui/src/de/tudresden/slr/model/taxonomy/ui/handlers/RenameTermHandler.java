package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexFileWriter;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;

public class RenameTermHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {			
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() == 1) {
			Term term = (Term) currentSelection.getFirstElement();	
			InputDialog dialog = new InputDialog(null, 
					"Rename Term", 
					"Rename Term: " + term.getName() + " to:",
					null,
					null);
			dialog.setBlockOnOpen(true);
			if (dialog.open() == InputDialog.OK) {
				renameTerm(term, dialog.getValue());
			}
		}		
		return null;
	}

	private void renameTerm(Term term, String newValue) {
		Map<Document, Term> termsInDocuments = SearchUtils.findDocumentsWithTerm(term);
		term.setName(newValue);
		Set<Resource> resourcesToUpdate = new TreeSet<>(
				(Resource r1, Resource r2) -> r1 == r2 ? 0 : 1);
		for (Map.Entry<Document, Term> entry : termsInDocuments.entrySet()) {
			entry.getValue().setName(newValue);
			Optional<Model> model = SearchUtils.getConainingModel(entry.getValue());
			if (model.isPresent()) {
				Model newTaxonomy = EcoreUtil.copy((Model) model.get());
				entry.getKey().setTaxonomy(newTaxonomy);					
			}				
			resourcesToUpdate.add(entry.getKey().eResource());			
		}			
		resourcesToUpdate.forEach(r -> BibtexFileWriter.updateBibtexFile(r));
		Optional<Model> model = SearchUtils.getConainingModel(term);
		if (model.isPresent()) {
			Model newTaxonomy = EcoreUtil.copy((Model) model.get());
			ModelRegistryPlugin.getModelRegistry().setActiveTaxonomy(newTaxonomy);
			try {					
				newTaxonomy.getResource().save(Collections.EMPTY_MAP);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
