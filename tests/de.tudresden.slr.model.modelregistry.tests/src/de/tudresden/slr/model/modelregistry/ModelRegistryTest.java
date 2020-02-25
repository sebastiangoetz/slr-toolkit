package de.tudresden.slr.model.modelregistry;

import static org.junit.Assert.*;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

// IMPORTANT: tests have to be run as junit plugin tests
public class ModelRegistryTest {

	private ModelRegistry registry;
	
	@Before
	public void setUp() throws Exception {
		registry = new ModelRegistry();
	}

	@Test
	public void testCreateEditingDomain() {
		assertTrue(registry.getEditingDomain().isPresent());
	}
	
	@Test
	public void testSetEditingDomain() {
		AdapterFactoryEditingDomain mockDomain = Mockito.mock(AdapterFactoryEditingDomain.class);
		registry.setEditingDomain(mockDomain);
		assertEquals(registry.getEditingDomain().orElse(null), mockDomain);
	}

}
