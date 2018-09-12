package de.tudresden.slr.utils.quickfix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class QuickFixGenerator implements IMarkerResolutionGenerator {
	
	public IMarkerResolution[] getResolutions(IMarker marker) {
		String nodeName = "#child#";
		String subtermNote = "";
		IMarkerResolution[] iMarkerResolution;
		try {
			String[] path = ((String) marker.getAttribute("PATH")).split("/");
			nodeName = path[path.length-1];
			if (path.length > 2) {
				subtermNote = "  (As subterm of "+path[path.length-2]+")";
			}
		} catch (CoreException e) {
			System.out.println("Error getting PATH attribute of marker.");
			e.printStackTrace();
		}

		iMarkerResolution = new IMarkerResolution[] {
				new QuickFix(
						"Add '"+nodeName+"' to taxonomy."+subtermNote,
						QuickFix.ADD_TO_TAXONOMY
				),
				new QuickFix(
						"Delete '"+nodeName+"' from document taxonomy."+subtermNote,
						QuickFix.DELETE_FROM_FILE
				)
		};
		
		// if move match found, add third quickfix
		try {
			String[] path = ((String) marker.getAttribute("PATH")).split("/");
			String[] path2 = ((String) marker.getAttribute("PATH2")).split("/");
			if (path2.length > 1 && !path[1].isEmpty()) {
				iMarkerResolution = new IMarkerResolution[] {
						new QuickFix(
								"Add '"+nodeName+"' to taxonomy."+subtermNote,
								QuickFix.ADD_TO_TAXONOMY
						),
						new QuickFix(
								"Delete '"+nodeName+"' from document taxonomy."+subtermNote,
								QuickFix.DELETE_FROM_FILE
						),
						new QuickFix(
								"Correct path from '"+((String) marker.getAttribute("PATH"))+"' to '"+((String) marker.getAttribute("PATH2"))+"'",
								QuickFix.MATCH_TAXONOMY
						)
				};
			}
		} catch (CoreException e) {
			System.out.println("PATH2 attribute of marker is not set.");
		}
		
		return iMarkerResolution;
	}
	
}