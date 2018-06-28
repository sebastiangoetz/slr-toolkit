package de.tudresden.slr.metainformation;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;

/**
 * The activator class controls the plug-in life cycle
 */
public class MetainformationActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.tudresden.slr.metainformation"; //$NON-NLS-1$

	// The shared instance
	private static MetainformationActivator plugin;
	
	private static SlrProjectMetainformation metainformation;
	private static String currentFilepath = null;
	
	/**
	 * The constructor
	 */
	public MetainformationActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static MetainformationActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static SlrProjectMetainformation getMetainformation() {
		return metainformation;
	}

	public static void setMetainformation(SlrProjectMetainformation metainformation) {
		MetainformationActivator.metainformation = metainformation;
	}

	public static String getCurrentFilepath() {
		return currentFilepath;
	}

	public static void setCurrentFilepath(String currentFilepath) {
		MetainformationActivator.currentFilepath = currentFilepath;
	}
}
