package de.tudresden.slr.utils.quickfix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class QuickFixGenerator implements IMarkerResolutionGenerator {
	
	public IMarkerResolution[] getResolutions(IMarker marker) {
		String nodeName = "#child#";
		String subtermNote = "";
		try {
			System.out.println("path: "+marker.getAttribute("PATH"));
			String[] path = ((String) marker.getAttribute("PATH")).split("/");
			nodeName = path[path.length-1];
			if (path.length > 2) {
				subtermNote = "  (As subterm of "+path[path.length-2]+")";
			}
		} catch (CoreException e) {
			System.out.println("Error getting PATH attribute of marker.");
			e.printStackTrace();
		}
		IMarkerResolution[] iMarkerResolution = new IMarkerResolution[] {
				new QuickFix(
						"Add '"+nodeName+"' to taxonomy."+subtermNote,
						QuickFix.ADD_TO_TAXONOMY
				),
				new QuickFix(
						"Delete '"+nodeName+"' from document taxonomy."+subtermNote,
						QuickFix.DELETE_FROM_FILE
				)
		};
		//TODO if match then add third quickfix
		return iMarkerResolution;
	}
	
}