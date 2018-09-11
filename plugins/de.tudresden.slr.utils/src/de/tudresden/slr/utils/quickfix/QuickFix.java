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
			EList<Term> terms;
			System.out.println("quickfix run "+fixType+"|"+marker.getAttribute("PATH"));
			
			switch (fixType) {
				case ADD_TO_TAXONOMY:
					terms = ModelRegistryPlugin.getModelRegistry().getActiveTaxonomy().get().getDimensions();
					Term term = terms.get(0);
					

					System.out.println("path.length: "+path.length);
					//add to taxonomy root
					if (path.length == 2) {
						TermCreator.create(path[1], term, TermPosition.NEIGHBOR);
						break;
					}
					
					for (int pathPosition=1; pathPosition<(path.length-1); pathPosition++) {
						System.out.println("search for "+path[pathPosition]);

						for (Term t : terms) {
							System.out.println("compare "+t.getName()+" ?= "+path[pathPosition]);
							if (t.getName().equals(path[pathPosition])) {
								System.out.println("found "+t.getName()+" "+pathPosition);
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
					break;
				case DELETE_FROM_FILE:
					List<Document> documents = SearchUtils.getDocumentList();
					for (Document d : documents) {
						if (d.getKey().equals(marker.getAttribute(IMarker.LOCATION))) {
							System.out.println("list "+d.getLine());
							terms = d.getTaxonomy().getDimensions();
							term = terms.get(0);
							
							int[] pathIndex = new int[path.length];

							for (int pathPosition=1; pathPosition<path.length; pathPosition++) {
								System.out.println("search for "+path[pathPosition]);

								int index = 0;
								for (Term t : terms) {
									System.out.println("compare "+t.getName()+" ?= "+path[pathPosition]);
									if (t.getName().equals(path[pathPosition])) {
										pathIndex[pathPosition-1] = index;
										System.out.println("found "+t.getName()+" "+pathPosition);
										term = t;
										System.out.println("test #42");
										if (pathPosition == (path.length-1)) { //check if pointer is at last path-element
											System.out.println("Remove "+terms.get(index).getName());
											terms.remove(index);
											pathPosition = path.length; //break parent loop
										}
										terms = t.getSubclasses();
											
										break;
									}
									index++;
								}
								
							}
							System.out.println("t:"+term.getName());
							

							for (int i : pathIndex)
							System.out.println(i+" ");
							

							//System.out.println("add: "+d.getTaxonomy().getDimensions().get(0).getSubclasses().get(0).getName());
							//d.getTaxonomy().getDimensions().remove(d.getTaxonomy().getDimensions().get(0).getSubclasses().get(0));
					//		d.getTaxonomy().getDimensions().remove(1);
							//remove(d.getTaxonomy().getDimensions().get(0).getSubclasses().get(0));

							

							BibtexFileWriter.updateBibtexFile(d.eResource());	

							printTaxonomy(d.getTaxonomy().getDimensions(), "|");
							System.out.println("\n");
							
							BibtexFactory bib = BibtexFactoryImpl.init();
							bib.createBibtexFile();
						
							//TODO save bibtex doc
							break;
						}
					}
					break;
				case MATCH_TAXONOMY:
					//TODO remove old from file taxonomy and add new to file taxonomy
					break;
			}
			
			
			marker.delete();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void printTaxonomy(EList<Term> terms, String spacer) {
		for (Term term : terms) {
			System.out.println(spacer+term.getName());
			if (!term.getSubclasses().isEmpty()) {
				printTaxonomy(term.getSubclasses(), spacer+"-");
			}
		}
		
	}
	
}
