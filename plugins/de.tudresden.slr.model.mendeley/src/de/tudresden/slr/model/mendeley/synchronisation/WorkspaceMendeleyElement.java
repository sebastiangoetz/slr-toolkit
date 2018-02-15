package de.tudresden.slr.model.mendeley.synchronisation;

import de.tudresden.slr.model.mendeley.api.model.MendeleyFolder;

public class WorkspaceMendeleyElement {
	private WorkspaceBibTexEntry workspace;
	private MendeleyFolder mendeley;
	
	public WorkspaceMendeleyElement(WorkspaceBibTexEntry workspace, MendeleyFolder mendeley) {
		this.mendeley = mendeley;
		this.workspace = workspace;
	}
	
	public MendeleyFolder getMendeley() {
		return mendeley;
	}
	
	public WorkspaceBibTexEntry getWorkspace() {
		return workspace;
	}
	
	public void setMendeley(MendeleyFolder mendeley) {
		this.mendeley = mendeley;
	}
	
	public void setWorkspace(WorkspaceBibTexEntry workspace) {
		this.workspace = workspace;
	}
	
}
