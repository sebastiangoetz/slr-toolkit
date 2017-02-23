package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexFileWriter;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.model.utils.TaxonomyUtils;

public class DeleteTermHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() > 0) {			
			MessageDialog dialog = new MessageDialog(null,
					"Delete Term",
					null,
					"Are you sure you want to delete terms: " + 
							currentSelection.toList()
								.stream()
								.filter(s -> s instanceof Term)
								.map(s -> ((Term) s).getName())
								.collect(Collectors.joining(", ")),
					MessageDialog.QUESTION,
					new String[]{"Yes", "Cancel"},
					0);
			dialog.setBlockOnOpen(true);
			if (dialog.open() == MessageDialog.OK && dialog.getReturnCode() == MessageDialog.OK) {
				List<Term> termsToDelete = new ArrayList<>(currentSelection.size());
				currentSelection.toList().stream().filter(s -> s instanceof Term).forEach(s -> termsToDelete.add((Term) s));;
				deleteTerm(termsToDelete);
			}
		}
		
		return null;
	}
	
	private void deleteTerm(List<Term> termsToDelete) {
		Map<Document, List<Term>> termsInDocuments = new HashMap<>(); 
		for (Term term : termsToDelete) {
			Map<Document, Term> termInDocuments = SearchUtils.findDocumentsWithTerm(term);
			termInDocuments.forEach((k, v) -> {
					if (termsInDocuments.containsKey(k)) termsInDocuments.get(k).add(v);
					else termsInDocuments.put(k, new LinkedList<Term>(Arrays.asList(v)));
			});  
			Optional<Model> model = SearchUtils.getConainingModel(term);
			EcoreUtil.remove(term);			
			if (model.isPresent()) {
				TaxonomyUtils.saveTaxonomy(model.get());
			}
		}
		Set<Resource> resourcesToUpdate = new TreeSet<>(
				(Resource r1, Resource r2) -> r1 == r2 ? 0 : 1);
		for (Map.Entry<Document, List<Term>> entry : termsInDocuments.entrySet()) {	
			entry.getValue().forEach(t -> EcoreUtil.remove(t));
			Model newTaxonomy = EcoreUtil.copy(entry.getKey().getTaxonomy());
			resourcesToUpdate.add(entry.getKey().eResource());
		}
		resourcesToUpdate.forEach(r -> BibtexFileWriter.updateBibtexFile(r));
	}

}
