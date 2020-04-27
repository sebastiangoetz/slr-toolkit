package de.tudresden.slr.model.bibtex.ui.util;

import static org.junit.Assert.*;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetIFilefromEMFResource() {
		// should return null when called with null arg
		assertNull(Utils.getIFilefromEMFResource(null));

		// empty resource set should return null
		Resource resourceEmptyResourceSet = Mockito.mock(Resource.class);
		Mockito.when(resourceEmptyResourceSet.getResourceSet()).thenReturn(null);
		assertNull(Utils.getIFilefromEMFResource(resourceEmptyResourceSet));
	}

	@Test
	public void testIsPersisted() {
		Resource resource = Mockito.mock(Resource.class);
		URI uri = Mockito.mock(URI.class);
		Mockito.when(resource.getURI()).thenReturn(uri);

		// if URI of the resource does not refer to a file, method should return false
		Mockito.when(uri.isFile()).thenReturn(false);
		assertFalse(Utils.isPersisted(resource));

		// if URI of the resource refers to a file and it does not exists, method should
		// return false
		String filepathNotExisting = "resources" + File.separator + "persistedFileWhichDoesNotExist.p";
		Mockito.when(uri.isFile()).thenReturn(true);
		Mockito.when(uri.toFileString()).thenReturn(filepathNotExisting);
		assertFalse(Utils.isPersisted(resource));

		// if URI refers to a file and said file exists, method should return true
		String filepathExisting = "resources" + File.separator + "persistedFile.p";
		Mockito.when(uri.isFile()).thenReturn(true);
		Mockito.when(uri.toFileString()).thenReturn(filepathExisting);
		assertTrue(Utils.isPersisted(resource));

	}
}
