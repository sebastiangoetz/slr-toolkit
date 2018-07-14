package de.tudresden.slr.latexexport.helpers;

import java.io.File;
import java.net.URL;

public class FileHelper {
	public static String extractFolderFromFilepath(String fileName) {
		return fileName = fileName.substring(0, fileName.lastIndexOf(File.separator));
	}
	
	public static String extractRelativePathFromFilepath(String filename) {
		return filename.substring(filename.lastIndexOf((File.separator)));
	}
	
	public static String extractFileNameFromURL(URL url) {
		//TODO unix paths
		return url.toString().substring( url.toString().lastIndexOf("/")+1, url.toString().length() );
	}
}
