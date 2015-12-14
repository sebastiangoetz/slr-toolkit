package de.tudresden.slr.model.modelregistry;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ModelRegistryPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.tudresden.slr.model.modelregistry"; //$NON-NLS-1$

	// The shared instance
	private static ModelRegistryPlugin plugin;

	private static ModelRegistry modelRegistry;

	/**
	 * The constructor
	 */
	public ModelRegistryPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ModelRegistryPlugin getDefault() {
		return plugin;
	}

	public static ModelRegistry getModelRegistry() {

		if (plugin == null) {
			plugin = new ModelRegistryPlugin();
		}

		if (modelRegistry == null) {
			modelRegistry = new ModelRegistry();
		}
		return modelRegistry;
	}
}
