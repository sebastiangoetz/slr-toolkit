package de.tudresden.slr.model.bibtex.util;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.ecore.resource.Resource;


public class BibtexFileWriter {

	public static void updateBibtexFile(Resource resource) {	
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
