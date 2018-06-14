package de.tudresden.slr.model.bibtex.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.jbibtex.*;

public class BibtexTooling {
	
	public BibTeXDatabase mergeToConsole(BibTeXDatabase db1, BibTeXDatabase db2) {
		BibTeXDatabase dbRes = new BibTeXDatabase();
		Map<Key, BibTeXEntry> entryMap = db1.getEntries();
		for(BibTeXEntry entry : entryMap.values()) {
			dbRes.addObject(entry);
		}
		entryMap = db2.getEntries();
		for(BibTeXEntry entry : entryMap.values()) {
			dbRes.addObject(entry);
		}
		Writer writer = new StringWriter();
		BibTeXFormatter formatter = new BibTeXFormatter();
		try {
			formatter.format(dbRes, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String toPrint = writer.toString();
		System.out.println(toPrint);
		return dbRes;
	}

}
