package de.tudresden.slr.utils.quickfix;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.IMarkerResolution;

import de.tudresden.slr.model.bibtex.BibtexFactory;
import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.model.bibtex.impl.BibtexFactoryImpl;
import de.tudresden.slr.model.bibtex.util.BibtexFileWriter;
import de.tudresden.slr.model.modelregistry.ModelRegistryPlugin;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.utils.SearchUtils;
import de.tudresden.slr.utils.TermPosition;
import de.tudresden.slr.utils.taxonomy.manipulation.TermCreator;

public class QuickFix implements IMarkerResolution {

	public static final int ADD_TO_TAXONOMY = 1;
	public static final int DELETE_FROM_FILE = 2;
	public static final int MATCH_TAXONOMY = 3;
	
	private String label;
	private int fixType;
	
	public QuickFix(String label, int fixType) {
	   this.label = label;
	   this.fixType = fixType;
	}
	
	public String getLabel() {
	   return label;
	}

	public int getFixType() {
	   return fixType;
	}
	
	public void run(IMarker marker) {
		try {
			String[] path = ((String) marker.getAttribute("PATH")).split("/");
			System.out.println("quickfix run "+fixType+"|"+marker.getAttribute("PATH"));
			
			switch (fixType) {
				case ADD_TO_TAXONOMY:
					addToTaxonomy(path);
					break;
				case DELETE_FROM_FILE:
					deleteFromFile(path, marker.getAttribute(IMarker.LOCATION).toString());
					break;
				case MATCH_TAXONOMY:
					System.out.println("## remove "+path+"\n## add "+((String) marker.getAttribute("PATH2")));
					moveTermInFile(path, ((String) marker.getAttribute("PATH2")).split("/"), marker.getAttribute(IMarker.LOCATION).toString());
					
					break;
			}
			
			marker.delete();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void addToTaxonomy(String[] path) {
		EList<Term> terms = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy().get().getDimensions();
		Term term = terms.get(0);
		
		//add to taxonomy root
		if (path.length == 2) {
			TermCreator.create(path[1], term, TermPosition.NEIGHBOR);
			return;
		}
		
		for (int pathPosition=1; pathPosition<(path.length-1); pathPosition++) {
			for (Term t : terms) {
				if (t.getName().equals(path[pathPosition])) {
					term = t;
					terms = t.getSubclasses();
					break;
				}
			}
		}
		
		//check if taxonomy contains term already
		boolean alreadyInTaxonomy = false;
		for (Term t : term.getSubclasses()) {
			if(t.getName().equals(path[path.length-1])) {
				alreadyInTaxonomy = true;
			}
		}
		if (alreadyInTaxonomy) {
			System.out.println(path[path.length-1]+" is already in taxonomy.");
		}
		else {
			TermCreator.create(path[path.length-1], term, TermPosition.SUBTERM);
		}
		
	}

	private void deleteFromFile(String[] path, String markerLocation) {
		EList<Term> terms = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy().get().getDimensions();
		List<Document> documents = SearchUtils.getDocumentList();
		for (Document d : documents) {
			if (d.getKey().equals(markerLocation)) {
				terms = d.getTaxonomy().getDimensions();
				
				int[] pathIndex = new int[path.length];

				for (int pathPosition=1; pathPosition<path.length; pathPosition++) {
					int index = 0;
					for (Term t : terms) {
						if (t.getName().equals(path[pathPosition])) {
							pathIndex[pathPosition-1] = index;
							if (pathPosition == (path.length-1)) { //check if pointer is at last path-element
								terms.remove(index);
								pathPosition = path.length; //break parent loop
							}
							terms = t.getSubclasses();
								
							break;
						}
						index++;
					}
					
				}
				BibtexFileWriter.updateBibtexFile(d.eResource());	
				
				BibtexFactory bib = BibtexFactoryImpl.init();
				bib.createBibtexFile();
			
				break;
			}
		}
		
	}
	

	private void moveTermInFile(String[] path, String[] path2, String markerLocation) {
		EList<Term> terms = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy().get().getDimensions();
		List<Document> documents = SearchUtils.getDocumentList();
		for (Document d : documents) {
			if (d.getKey().equals(markerLocation)) {
				terms = d.getTaxonomy().getDimensions();
				
				int[] pathIndex = new int[path.length];

				for (int pathPosition=1; pathPosition<path.length; pathPosition++) {

					int index = 0;
					for (Term t : terms) {
						if (t.getName().equals(path[pathPosition])) {
							pathIndex[pathPosition-1] = index;
							if (pathPosition == (path.length-1)) { //check if pointer is at last path-element
								Term moveTerm = terms.get(index);
								addTerm(d, path2, moveTerm);
								
								terms.remove(index);
								pathPosition = path.length; //break parent loop
							}
							terms = t.getSubclasses();
							break;
						}
						index++;
					}
				}		

				BibtexFileWriter.updateBibtexFile(d.eResource());	
				//printTaxonomy(d.getTaxonomy().getDimensions(), "|");
				
				BibtexFactory bib = BibtexFactoryImpl.init();
				bib.createBibtexFile();
			
				break;
			}
		}
		
	}


	private void addTerm(Document d, String[] path2, Term moveTerm) {
		EList<Term> terms = d.getTaxonomy().getDimensions();
		int pathPosition = 1;
		boolean termFound = false;

		for (String pathPart : path2) {
			termFound = false;
			if (pathPart.equals("")) {
				continue;
			}
			for (Term term : terms) {
				if (term.getName().equals(path2[pathPosition])) {
					termFound = true;
					if (!term.getSubclasses().isEmpty()) {
						terms = term.getSubclasses();
					}
					break;
				}
			}
			if (!termFound) {
				Term newTerm = TermCreator.create(
						pathPart,
						ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy().get().getDimensions().get(3).getSubclasses().get(0),
						TermPosition.SUBTERM
				);
				terms.add(newTerm);
				terms = newTerm.getSubclasses();
				
				BibtexFileWriter.updateBibtexFile(d.eResource());
			}
			pathPosition++;
		}
		
		BibtexFileWriter.updateBibtexFile(d.eResource());
		
	}

	/*
	 *
	 * helper function to print taxonomy in tree-view
	 * 
	 */
	private void printTaxonomy(EList<Term> terms, String spacer) {
		for (Term term : terms) {
			System.out.println(spacer+term.getName());
			if (!term.getSubclasses().isEmpty()) {
				printTaxonomy(term.getSubclasses(), spacer+"-");
			}
		}
		
	}
	
}
