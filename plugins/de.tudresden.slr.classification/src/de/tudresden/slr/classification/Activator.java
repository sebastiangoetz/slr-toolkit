package de.tudresden.slr.classification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
	public static final String PLUGIN_ID = "de.tudresden.slr.classification"; //$NON-NLS-1$
	
	private static String URL;

	private static String WEB_APP_WORKSPACE;
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String webappRoot = workspace.getRoot().getLocation().toString().concat("/webapp");
		new File(webappRoot).mkdirs();

		Activator.WEB_APP_WORKSPACE = webappRoot;

		// create the server on a free port
		Server server = new Server(0);

		// Configure the ResourceHandler. Setting the resource base indicates where the
		// files should be served out of.
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setResourceBase(webappRoot);

		// Add the ResourceHandler to the server.
		HandlerCollection handlers = new HandlerCollection(true);
		handlers.addHandler(resource_handler);

		server.setHandler(handlers);

		// Start server up.
		try {
			server.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// Get the URI of the server and set it global for the
		// diagram handlers to access
		if (null != server && null != server.getURI()) {
			String localhost = server.getURI().toString();
			Activator.URL = localhost;
		}

		writeVegaFiles();
	}
	
	/**
	 * Takes the preconfigured vega source and graph files from within the bundle and writes
	 * them to the workspace folder.
	 * 
	 * @return true, if the file was written successfully
	 * @throws IOException 
	 */
	private void writeVegaFiles() throws IOException {
		writeFileToWorkspace("vega.min.js");
		writeFileToWorkspace("v5.json");
		writeFileToWorkspace("knockout-3.5.1.js");
		writeFileToWorkspace("bar.vg.json");
		writeFileToWorkspace("bar.index.html");
		writeFileToWorkspace("bubble.vg.json");
		writeFileToWorkspace("bubble.index.html");
	}

	private void writeFileToWorkspace(String file) throws IOException {
		InputStream in = getClass().getResourceAsStream("/html/" + file);
    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String st;
	    File writeToFile = new File(Activator.WEB_APP_WORKSPACE + "/" + file);
	    PrintWriter writer = new PrintWriter(new FileWriter(writeToFile));
	    while ((st = br.readLine()) != null) {
    		writer.println(st);
	    }
	    writer.close(); 
	}

	public static String getUrl() {
		return URL;
	}

	public static String getWebAppWorkspace() {
		return WEB_APP_WORKSPACE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
