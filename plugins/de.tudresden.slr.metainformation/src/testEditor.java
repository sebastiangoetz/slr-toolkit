import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditor;
import org.eclipse.ui.part.MultiPageEditorPart;

public class testEditor extends MultiPageEditorPart {

	@Override
	protected void createPages() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
			if (!(editorInput instanceof IFileEditorInput))
				throw new PartInitException("Invalid Input: Must be IFileEditorInput");
			super.init(site, editorInput);
		}

	
}
