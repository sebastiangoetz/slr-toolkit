package de.tudresden.slr.metainformation.editors;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import de.tudresden.slr.metainformation.MetainformationActivator;
import de.tudresden.slr.metainformation.data.SlrProjectMetainformation;
import de.tudresden.slr.metainformation.util.MetainformationUtil;

public class MetainformationEditor extends EditorPart implements IEditorPart {

	private SlrProjectMetainformation metainformation;
	private boolean dirty = false;
	private IPath activeFilePath;
	private Text textboxFile, textboxTitle, textboxAuthors, textboxKeywords, textboxAbstract, textboxDescriptionTaxonomy;

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {

			File file = new File(activeFilePath.toOSString());
			JAXBContext jaxbContext = JAXBContext.newInstance(SlrProjectMetainformation.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			SlrProjectMetainformation toSave = new SlrProjectMetainformation();
			toSave.setAuthors(textboxAuthors.getText());
			toSave.setKeywords(textboxKeywords.getText());
			toSave.setProjectAbstract(textboxAbstract.getText());
			toSave.setTaxonomyDescription(textboxDescriptionTaxonomy.getText());
			toSave.setTitle(textboxTitle.getText());

			jaxbMarshaller.marshal(toSave, file);
			
			setDirty(false);
		} catch (JAXBException e) {
			e.printStackTrace();
			//TODO error message
		}
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		activeFilePath = ((IFileEditorInput) input).getFile().getLocation();
	}

	public void initTextFields() {
		try {
			File file = new File(activeFilePath.toOSString());
			metainformation = MetainformationUtil.getMetainformationFromFile(file);
			
			MetainformationActivator.setMetainformation(metainformation);
			MetainformationActivator.setCurrentFilepath(activeFilePath.toOSString());

			textboxTitle.setText(metainformation.getTitle());
			textboxAuthors.setText(metainformation.getAuthors());
			textboxKeywords.setText(metainformation.getKeywords());
			textboxDescriptionTaxonomy.setText(metainformation.getTaxonomyDescription());
			textboxAbstract.setText(metainformation.getProjectAbstract());
			textboxFile.setText(activeFilePath.toOSString());
		} catch (JAXBException e) {
			e.printStackTrace();
			// TODO error message
		}

	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void setDirty(boolean dirty) {
		if (this.dirty != dirty) {
			this.dirty = dirty;
			this.firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(SWT.V_SCROLL, false);
		layout.numColumns = 2;
		parent.setLayout(layout);
		
		new Label(parent, SWT.NONE).setText("File");
		textboxFile = new Text(parent, SWT.BORDER);
		textboxFile.setEditable(false);
		textboxFile.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		new Label(parent, SWT.NONE).setText("Title");
		textboxTitle = new Text(parent, SWT.BORDER);
		textboxTitle.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		new Label(parent, SWT.NONE).setText("Authors");
		textboxAuthors = new Text(parent, SWT.BORDER);
		textboxAuthors.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		new Label(parent, SWT.NONE).setText("Keywords");
		textboxKeywords = new Text(parent, SWT.BORDER);
		textboxKeywords.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		GridData gridData = new GridData();
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;

		new Label(parent, SWT.NONE).setText("Abstract");
		textboxAbstract = new Text(parent, SWT.BORDER | SWT.V_SCROLL);
		textboxAbstract.setLayoutData(gridData);

		new Label(parent, SWT.NONE).setText("Description of the taxonomy");
		textboxDescriptionTaxonomy = new Text(parent, SWT.BORDER | SWT.V_SCROLL);
		textboxDescriptionTaxonomy.setLayoutData(gridData);

		initTextFields();

		textboxTitle.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				setDirty(true);
			}
		});
		textboxAuthors.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				setDirty(true);
			}
		});
		textboxKeywords.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				setDirty(true);
			}
		});
		textboxAbstract.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				setDirty(true);
			}
		});
		textboxDescriptionTaxonomy.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				setDirty(true);
			}
		});

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void testSave(String path) {
		SlrProjectMetainformation m = new SlrProjectMetainformation();
		m.setTitle("title");
		m.setAuthors("authors");
		m.setKeywords("keywords");
		m.setProjectAbstract("abstract");
		m.setTaxonomyDescription("descr");

		try {

			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(SlrProjectMetainformation.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(m, file);
			// jaxbMarshaller.marshal(m, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

}