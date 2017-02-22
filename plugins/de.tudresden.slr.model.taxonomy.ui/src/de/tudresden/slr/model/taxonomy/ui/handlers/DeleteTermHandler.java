package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.util.BibtexFileWriter;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.util.TermUtils;
import de.tudresden.slr.model.utils.SearchUtils;

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
				Iterator iter = currentSelection.iterator();
				while (iter.hasNext()) {
					Term currentTerm = (Term) iter.next();
					Map<Document, Term> termsInDocuments = SearchUtils.findDocumentsWithTerm(currentTerm);
					for (Map.Entry<Document, Term> entry : termsInDocuments.entrySet()) {
						EcoreUtil.remove(entry.getValue());
						Optional<Model> model = SearchUtils.getConainingModel(entry.getValue());
						if (model.isPresent()) {
							Model newTaxonomy = EcoreUtil.copy((Model) model.get());
							entry.getKey().setTaxonomy(newTaxonomy);					
						}				
						BibtexFileWriter.updateBibtexFile(entry.getKey().eResource());
					}
					Optional<Model> model = SearchUtils.getConainingModel(currentTerm);
					EcoreUtil.remove(currentTerm);					
					if (model.isPresent()) {
						Model newTaxonomy = EcoreUtil.copy((Model) model.get());
						ModelRegistryPlugin.getModelRegistry().setActiveTaxonomy(newTaxonomy);
						try {					
							// TODO save once per resource
							newTaxonomy.getResource().save(Collections.EMPTY_MAP);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	
					
				}
			}
		}
		
		return null;
	}

}
