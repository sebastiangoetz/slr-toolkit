package de.tudresden.slr.questionnaire.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public class EclipseUtils {

	public static List<IProject> getOpenProjects() {
		return Arrays.stream(ResourcesPlugin.getWorkspace()
				.getRoot()
				.getProjects())
				.filter(p -> p.isOpen())
				.collect(Collectors.toList());
	}

	public static List<IFile> listProjectFiles(IProject project) {
		List<IFile> files = new LinkedList<>();
		if (project == null)
			return files;

		try {
			for (IResource resource : project.members()) {
				if (resource instanceof IFile) {
					files.add((IFile) resource);
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return files;
	}

	public static void printToIFile(IFile ifile, String content) {
		PrintWriter pw = null;
		try {
			File file = ifile.getLocation()
					.toFile();
			pw = new PrintWriter(file);
			pw.write(content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			pw.close();
		}
	}
}
