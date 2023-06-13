/*
 * generated by Xtext 2.31.0
 */
package de.tudresden.slr.model.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.tudresden.slr.model.TaxonomyRuntimeModule;
import de.tudresden.slr.model.TaxonomyStandaloneSetup;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class TaxonomyIdeSetup extends TaxonomyStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new TaxonomyRuntimeModule(), new TaxonomyIdeModule()));
	}
	
}
