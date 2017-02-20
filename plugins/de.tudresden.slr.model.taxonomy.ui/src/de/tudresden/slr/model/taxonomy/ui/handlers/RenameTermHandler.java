package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.ui.util.Utils;
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
			Map<Document, Term> termsInDocuments = SearchUtils.findDocumentsWithTerm(term);
			term.setName("TestName");
			for (Map.Entry<Document, Term> entry : termsInDocuments.entrySet()) {
				entry.getValue().setName("TestName");
				Optional<Model> model = SearchUtils.getConainingModel(entry.getValue());
				if (model.isPresent()) {
					Model newTaxonomy = EcoreUtil.copy((Model) model.get());
					entry.getKey().setTaxonomy(newTaxonomy);
				}
				Utils.updateBibtexFileForDocument(document);
				
			}
			Optional<Model> model = SearchUtils.getConainingModel(term);
			if (model.isPresent()) {
				Model newTaxonomy = EcoreUtil.copy((Model) model.get());
				ModelRegistryPlugin.getModelRegistry().setActiveTaxonomy(newTaxonomy);
			}			
		}		
		return null;
	}

}
