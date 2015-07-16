package de.tudresden.slr.wizards.projects;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

import de.tudresden.slr.wizards.natures.ProjectNature;

public class SlrProjectSupport {

	// private static void createFolder(IFolder folder) throws CoreException {
	// IContainer parent = folder.getParent();
	// if (parent instanceof IFolder) {
	// createFolder((IFolder) parent);
	// }
	// if (!folder.exists()) {
	// folder.create(false, true, null);
	// }
	// }

	public static void addNature(IProject project) throws CoreException {
		if (!project.hasNature(ProjectNature.NATURE_ID)) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = ProjectNature.NATURE_ID;
			description.setNatureIds(newNatures);

			project.setDescription(description, null);
		}
	}

}
