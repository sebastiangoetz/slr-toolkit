package de.tudresden.slr.latexexport.handlers;

import de.tudresden.slr.latexexport.wizard.LatexExportWizard;
import de.tudresden.slr.metainformation.util.DataProvider;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class exportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DataProvider dataprovider = new DataProvider();
  		
  		
//  		for(Term t : dataprovider.getAllDimensionsOrdered()) {
//  			System.out.println(t.getName() + dataprovider.getNumberOfElementsInDimension(t));
//  		}
		
  		
		Shell activeShell = HandlerUtil.getActiveShell(event);
		IWizard wizard = new LatexExportWizard();
		WizardDialog wizardDialog = new WizardDialog(activeShell, wizard);
		wizardDialog.open();

		
		//FileDialog dialog = new FileDialog(HandlerUtil.getActiveShell(event), SWT.SAVE);
		//dialog.setFilterExtensions(new String[] { "*.tex" });
//		try {
//			filename = dialog.open();
//			if (filename != null ) {
//				PrintWriter writer = new PrintWriter(filename, "UTF-8");
//				writer.println("LatexDocument");
//				writer.close();
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		
		//ZUGRIFF AUF Dokumente, Taxonomy
//  		List<Document> documents = dataprovider.getDocuments();
//  		for(Document d : documents) {
//  			System.out.println(d.getTitle());
//  		}
//  		
//  		Optional<Model> m = dataprovider.getTaxonomyRootNode();
//  		System.out.println(m.get().getDimensions());
//  		
//  		for (Term t : m.get().getDimensions()) {
//  			System.out.println(t.getName());
//  		}
//  		
  		

		return null;
	}
	
	
}
