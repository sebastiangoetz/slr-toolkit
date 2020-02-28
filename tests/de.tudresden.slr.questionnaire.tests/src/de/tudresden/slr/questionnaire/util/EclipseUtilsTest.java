package de.tudresden.slr.questionnaire.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

public class EclipseUtilsTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testListProjectFiles() throws CoreException {
		// null input returns empty list
		assertEquals(0, EclipseUtils.listProjectFiles(null).size());

		// project with two files returns exactly these two files
		IProject project = Mockito.mock(IProject.class);
		IFile file1 = Mockito.mock(IFile.class);
		IFile file2 = Mockito.mock(IFile.class);
		Mockito.when(project.members()).thenReturn(new IResource[] { file1, file2 });

		List<IFile> resultList = EclipseUtils.listProjectFiles(project);
		assertEquals(2, resultList.size());
		assertTrue(resultList.contains(file1));
		assertTrue(resultList.contains(file2));
	}

	@Test
	public void testPrintToIFile() throws IOException {
		IFile iFile = Mockito.mock(IFile.class);
		IPath iPath = Mockito.mock(IPath.class);
		Mockito.when(iFile.getLocation()).thenReturn(iPath);
		File realFile = folder.newFile("realFile.txt");
		Mockito.when(iPath.toFile()).thenReturn(realFile);
		
		String content = "content";
		EclipseUtils.printToIFile(iFile, content);
		
		String fileContent = new String(Files.readAllBytes(realFile.toPath()));
		
		assertEquals(content, fileContent);
	}

}
