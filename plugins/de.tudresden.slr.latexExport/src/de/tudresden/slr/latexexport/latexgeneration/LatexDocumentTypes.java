package de.tudresden.slr.latexexport.latexgeneration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LatexDocumentTypes {
	private static String tudscr = "tudscr";
	private static String[] tudscrFiles = {};
	
	private static String ieee = "IEEEconf";
	private static String[] ieeeFiles = {};
	
	private static String acm = "ACM SIGPLAN conf";
	private static String[] acmFiles = {};

	private static String lncs = "Springer LNCS";
	private static String[] lncsFiles = {};

	private static String plain = "Plain";
	private static String[] plainFiles = {};

	private static Map<String, String[]> templateFilesToCopy = new HashMap<String, String[]>();

	public static String[] documentTypes = { tudscr, ieee, acm, lncs, plain };

	public static void copyHelperFiles(String type, String path) {		
		
	}
	
	public Map<String, String[]> getTemplateFilesToCopy(){
		templateFilesToCopy.clear();
		initTemplateFilesToCopy();
		return templateFilesToCopy;
	}

	private static void initTemplateFilesToCopy() {
		templateFilesToCopy.put(tudscr, tudscrFiles);
		templateFilesToCopy.put(ieee, ieeeFiles);
		templateFilesToCopy.put(acm, acmFiles);
		templateFilesToCopy.put(lncs, lncsFiles);
		templateFilesToCopy.put(plain, plainFiles);
	}
	
	
}
