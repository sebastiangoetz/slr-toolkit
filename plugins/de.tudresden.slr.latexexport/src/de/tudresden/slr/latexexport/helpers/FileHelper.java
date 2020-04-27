package de.tudresden.slr.latexexport.helpers;

import java.io.File;
import java.net.URL;

public class FileHelper {

	/**
	 * Extracts the folder from a given filename. E.g. C:\folder\app.exe returns
	 * C:\folder\
	 * 
	 * @param fileName
	 * @return Folder represented as String
	 */
	public static String extractFolderFromFilepath(String fileName) {
		if (fileName == null) {
			return null;
		}

		if (fileName.lastIndexOf(File.separator) == -1) {
			return null;
		}

		return fileName = fileName.substring(0, fileName.lastIndexOf(File.separator));
	}

	/**
	 * Extracts a filename from a given URL. E.g. http://www.test.de/test.html
	 * returns test.html
	 * 
	 * @param url URL, whose filename should be extracted
	 * @return filename, represented as string
	 */
	public static String extractFileNameFromURL(URL url) {
		if (url == null) {
			return null;
		}

		String urlString = url.toString();

		return urlString.substring(urlString.lastIndexOf("/") + 1, urlString.length());
	}
}
