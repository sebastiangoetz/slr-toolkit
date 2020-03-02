package de.tudresden.slr.questionnaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
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

public class QuestionnaireStorageTest {
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testGetInstance() throws CoreException {
		assertNotNull(QuestionnaireStorage.getInstance());
	}

	@Test
	public void testListExistingQuestionnaires() throws CoreException {
		IProject project = Mockito.mock(IProject.class);
		File questionnaire1File = new File("resources"+File.separator+"questionnaire1.questionnaire");
		File questionnaire2File = new File("resources"+File.separator+"questionnaire2.questionnaire");
		
		IFile iFile1 = Mockito.mock(IFile.class);
		IPath iPath1 = Mockito.mock(IPath.class);
		Mockito.when(iFile1.getName()).thenReturn("questionnaire1.questionnaire");
		Mockito.when(iPath1.toFile()).thenReturn(questionnaire1File);
		Mockito.when(iFile1.getLocation()).thenReturn(iPath1);
		
		IFile iFile2 = Mockito.mock(IFile.class);
		IPath iPath2 = Mockito.mock(IPath.class);
		Mockito.when(iFile2.getName()).thenReturn("questionnaire2.questionnaire");
		Mockito.when(iPath2.toFile()).thenReturn(questionnaire2File);
		Mockito.when(iFile2.getLocation()).thenReturn(iPath2);
		
		Mockito.when(project.members()).thenReturn(new IResource[] {iFile1, iFile2});
		
		List<Questionnaire> result = QuestionnaireStorage.getInstance().listExistingQuestionnaires(project);
	
		assertEquals(2, result.size());
		
		assertEquals(result.get(0).getName(), "questionnaire1.questionnaire");
		assertEquals(result.get(1).getName(), "questionnaire2.questionnaire");
	}

	@Test
	public void testPersistQuestionnaire() throws IOException {
		QuestionnaireStorage questionnaireStorageSpy = Mockito.spy(QuestionnaireStorage.getInstance());
		
		// do not persist clean questionnaire
		Questionnaire questionnaire = new Questionnaire("name");
		questionnaire.setDirty(false);
		
		questionnaireStorageSpy.fileOriginMap.put(questionnaire, Mockito.mock(IFile.class));
		Mockito.doNothing().when(questionnaireStorageSpy).persist(Mockito.any(), Mockito.any());
		
		// questionnaire wasn't dirty, thus persisting method shouldn't have been called
		questionnaireStorageSpy.persist(questionnaire);
		Mockito.verify(questionnaireStorageSpy, Mockito.never()).persist(Mockito.any(), Mockito.any());
		
		// set to dirty, see if method to make it persistent was called and questionnaire is set to clean again
		questionnaire.setDirty(true);
		questionnaireStorageSpy.persist(questionnaire);
		Mockito.verify(questionnaireStorageSpy).persist(Mockito.any(), Mockito.any());
	}

}
