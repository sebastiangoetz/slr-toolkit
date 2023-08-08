package de.tudresden.slr.model.bibtex.ui.presentation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.tudresden.slr.model.bibtex.ui.presentation.BibtexMergeData.Criteria;

public class BibtexMergeDialog extends Dialog {

	private BibtexMergeData mergeData;
	private CheckboxTableViewer ctv;
	private Text filename;
	private Text filePath;
	private StyledText preview;
	private TableItem previewStats;
	private ListIterator<BibtexMergeConflict> currentConflictedResourceIter;
	private BibtexMergeConflict currentConflictedResource;
	private ListIterator<String> currentConflictedFieldIter;
	private String currentConflictedField;
	private int currentIndex;
	private boolean conflitsExist;
	private boolean previewWasEdited;
	private Button saveIntersection;
	private Button saveUnion;

	public BibtexMergeDialog(Shell parentShell, BibtexMergeData d) {
		super(parentShell);
		// make non modal
		setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
		setBlockOnOpen(false);
		this.mergeData = d;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		buildFilesPart(container);
		buildPreview(container);
		buildOptions(container);
		buildCriteria(container);
		buildNavigation(container);
		buildSavePart(container);

		return container;
	}

	private void buildNavigation(Composite container) {

		Group group = new Group(container, SWT.SHADOW_NONE);
		group.setText("Navigate through conflicts");
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData());

		// create navigate buttons
		new Button(group, SWT.ARROW | SWT.UP).addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				updateConflictIterator(false);
				preview.redraw();
			}
		});
		Button chooseFirst = new Button(group, SWT.NONE);
		chooseFirst.setText("Choose first");
		chooseFirst.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				currentConflictedResource.setField(currentConflictedField, true);
				initializeConflictIterator();
				updatePreview();
				previewWasEdited = true;
			}
		});
		new Button(group, SWT.ARROW | SWT.DOWN).addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				updateConflictIterator(true);
				preview.redraw();
			}
		});
		Button chooseSecond = new Button(group, SWT.NONE);
		chooseSecond.setText("Choose second");
		chooseSecond.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				currentConflictedResource.setField(currentConflictedField, false);
				initializeConflictIterator();
				updatePreview();
				previewWasEdited = true;
			}
		});
	}

	private void buildPreview(Composite container) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		gridData.verticalAlignment = SWT.TOP;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 5;

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(gridData);

		// create overview
		Table table = new Table(composite, SWT.BORDER);
		table.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true, 1, 1));

		TableColumn intersection = new TableColumn(table, SWT.CENTER);
		TableColumn mergeConflicts = new TableColumn(table, SWT.CENTER);
		TableColumn unionWithoutConflicts = new TableColumn(table, SWT.CENTER);
		intersection.setText("Intersection");
		mergeConflicts.setText("Merge conflicts");
		unionWithoutConflicts.setText("Union without conflicts");
		intersection.setWidth(80);
		mergeConflicts.setWidth(100);
		unionWithoutConflicts.setWidth(140);
		table.setHeaderVisible(true);

		previewStats = new TableItem(table, SWT.NONE);

		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true, 1, 1));
		label.setText("Preview: ");

		preview = new StyledText(composite, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		preview.setLayoutData(new GridData(GridData.FILL_BOTH));
		preview.setEditable(false);
		preview.setEnabled(false);
		preview.setBlockSelection(true);

		// highlight current conflict
		initializeConflictIterator();
		preview.addLineStyleListener(new LineStyleListener() {
			public void lineGetStyle(LineStyleEvent event) {
				if (currentConflictedField == null
						|| currentConflictedResource.getConflictForField(currentConflictedField) == null)
					return;

				String currentConflict = currentConflictedResource.getConflictForField(currentConflictedField);
				StyleRange styleRange = new StyleRange();
				styleRange.start = preview.getText().indexOf(currentConflict);
				styleRange.length = currentConflict.length();
				styleRange.background = getParentShell().getDisplay().getSystemColor(SWT.COLOR_YELLOW);
				event.styles = new StyleRange[] { styleRange };
			}
		});

		GridData textGrid = new GridData(500, 500);
		textGrid.horizontalSpan = 2;
		preview.setLayoutData(textGrid);

		updatePreview();
	}

	private void updatePreview() {
		preview.setText(mergeData.getConflicts().stream().map(conflict -> conflict.printConflict())
				.collect(Collectors.joining("\n")));
		previewWasEdited = false;

		int unionWithoutConflicts = mergeData.getConflicts().stream().filter(conflict -> !conflict.isConflicted())
				.collect(Collectors.toList()).size();

		int mergeConflicts = mergeData.getConflicts().size() - unionWithoutConflicts;
		int intersection = mergeData.getIntersection().size();
		previewStats.setText(new String[] { Integer.toString(intersection), Integer.toString(mergeConflicts),
				Integer.toString(unionWithoutConflicts) });
		conflitsExist = mergeConflicts > 0;

		initializeConflictIterator();
	}

	private void initializeConflictIterator() {
		if (mergeData.getConflicts().isEmpty())
			return;

		currentConflictedResourceIter = mergeData.getConflicts().listIterator();
		currentConflictedResource = currentConflictedResourceIter.next();

		// search through resources until we find conflicts
		while (currentConflictedResourceIter.hasNext() && currentConflictedResource.getConflictedFields().isEmpty())
			currentConflictedResource = currentConflictedResourceIter.next();
		if (currentConflictedResource.getConflictedFields().isEmpty())
			return;

		currentConflictedFieldIter = currentConflictedResource.getConflictedFields().listIterator();
		currentConflictedField = currentConflictedFieldIter.next();
		preview.redraw();
	}

	private void updateConflictIterator(boolean forward) {
		if (forward) {
			// move one conflict forward
			if (currentConflictedFieldIter.hasNext())
				currentConflictedField = currentConflictedFieldIter.next();
			else if (currentConflictedResourceIter.hasNext()) {
				// strange behavior: if we toggle between next and previous the index somehow
				// does not get updated
				if (currentConflictedResourceIter.nextIndex() == currentIndex)
					currentConflictedResourceIter.next();

				currentIndex = currentConflictedResourceIter.nextIndex();
				currentConflictedResource = currentConflictedResourceIter.next();
				if (currentConflictedResource.getConflictedFields().isEmpty()) {
					updateConflictIterator(forward);
					return;
				}

				// there is a next resource with conflicts
				currentConflictedFieldIter = currentConflictedResource.getConflictedFields().listIterator();
				currentConflictedField = currentConflictedFieldIter.next();
			} else {
				// move to beginning
				initializeConflictIterator();
			}
		} else {
			// move one conflict backward
			if (currentConflictedFieldIter.hasPrevious())
				currentConflictedField = currentConflictedFieldIter.previous();
			else if (currentConflictedResourceIter.hasPrevious()) {
				// strange behavior: if we toggle between next and previous the index somehow
				// does not get updated
				if (currentConflictedResourceIter.previousIndex() == currentIndex)
					currentConflictedResourceIter.previous();

				currentConflictedResource = currentConflictedResourceIter.previous();
				List<String> conflictedFields = currentConflictedResource.getConflictedFields();
				if (conflictedFields.isEmpty()) {
					updateConflictIterator(forward);
					return;
				}

				// there is a previous resource with conflicts
				currentConflictedFieldIter = getIteratorOnLastElement(conflictedFields);
				currentConflictedField = conflictedFields.get(conflictedFields.size() - 1);
			} else {
				// move to last conflict
				currentConflictedResourceIter = getIteratorOnLastElement(mergeData.getConflicts());
				currentConflictedResource = mergeData.getConflicts().get(mergeData.getConflicts().size() - 1);

				// search through resources until we find conflicts
				while (currentConflictedResourceIter.hasPrevious()
						&& currentConflictedResource.getConflictedFields().isEmpty())
					currentConflictedResource = currentConflictedResourceIter.previous();
				if (currentConflictedResource.getConflictedFields().isEmpty())
					return;

				currentConflictedFieldIter = getIteratorOnLastElement(currentConflictedResource.getConflictedFields());
				currentConflictedField = currentConflictedResource.getConflictedFields()
						.get(currentConflictedResource.getConflictedFields().size() - 1);

			}
		}
	}

	private <T> ListIterator<T> getIteratorOnLastElement(List<T> list) {
		ListIterator<T> iter = list.listIterator();
		while (iter.hasNext())
			iter.next();

		return iter;
	}

	private void buildCriteria(Composite container) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;

		Group criteriaGroup = new Group(container, SWT.SHADOW_NONE);
		criteriaGroup.setText("Similarity Criteria");
		criteriaGroup.setLayout(new GridLayout(3, false));
		criteriaGroup.setLayoutData(gridData);

		for (Criteria value : Criteria.values()) {
			// create check box
			Button b = new Button(criteriaGroup, SWT.CHECK);
			b.setSelection(mergeData.getWeights().get(Criteria.doi) > 0);
			b.setText(getButtonTextForCriteria(value));

			// create scale
			Scale scale = new Scale(criteriaGroup, SWT.HORIZONTAL);
			scale.setMinimum(0);
			scale.setMaximum(getMaxScaleValueForCriteria(value));
			scale.setSelection(getDefaultScaleSelectionValueForCriteria(value));
			scale.setIncrement(1);

			// create text box for value of scale
			Text text = new Text(criteriaGroup, SWT.CENTER);
			text.setText(Integer.toString(scale.getSelection()));
			scale.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					if (previewWasEdited && !createMessageBox("You have already edited some conflicts.")) {
						scale.setSelection(mergeData.getWeights().get(value));
						return;
					}

					mergeData.setWeight(value, scale.getSelection());
					text.setText(Integer.toString(scale.getSelection()));

					if (scale.getSelection() == 10 && value == Criteria.year
							|| value != Criteria.year && scale.getSelection() == 0) {
						b.setSelection(false);
					} else {
						b.setSelection(true);
					}

					updatePreview();
				}
			});

			// add listener for check box updates
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (previewWasEdited && !createMessageBox("You have already edited some conflicts.")) {
						b.setSelection(!b.getSelection());
						return;
					}

					scale.setSelection(b.getSelection() ? getDefaultScaleSelectionValueForCriteria(value)
							: getUnCheckedScaleValueForCriteria(value));
					mergeData.setWeight(value, scale.getSelection());
					text.setText(Integer.toString(scale.getSelection()));
					updatePreview();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					if (previewWasEdited && createMessageBox("You have already edited some conflicts."))
						return;
					scale.setSelection(b.getSelection() ? getDefaultScaleSelectionValueForCriteria(value)
							: getUnCheckedScaleValueForCriteria(value));
					mergeData.setWeight(value, scale.getSelection());
					text.setText(Integer.toString(scale.getSelection()));
					updatePreview();
				}
			});
		}
	}

	private String getButtonTextForCriteria(Criteria criteria) {
		switch (criteria) {
		case doi:
			return criteria.name() + " equals";
		case year:
			return criteria.name() + "s +-";
		default:
			return criteria.name() + " in %";
		}
	}

	private int getUnCheckedScaleValueForCriteria(Criteria criteria) {
		switch (criteria) {
		case year:
			return 10;
		default:
			return 0;
		}
	}

	private int getMaxScaleValueForCriteria(Criteria criteria) {
		switch (criteria) {
		case doi:
			return 1;
		case year:
			return 10;
		default:
			return 100;
		}
	}

	private int getDefaultScaleSelectionValueForCriteria(Criteria criteria) {
		switch (criteria) {
		case doi:
			return 1;
		case year:
			return 5;
		default:
			return 95;
		}
	}

	private void buildFilesPart(Composite container) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 1;
		gridData.verticalSpan = 1;

		Group group = new Group(container, SWT.SHADOW_NONE);
		group.setText("Files to merge");
		group.setLayout(new FillLayout());
		group.setLayoutData(gridData);

		Label label = new Label(group, SWT.NONE);
		label.setText(mergeData.getResourceList().stream()
				.map(resource -> resource.getURI().toString() + ":\t" + resource.getContents().size() + " entries")
				.collect(Collectors.joining("\n")));
	}

	private void buildSavePart(Composite container) {
		Composite savePart = new Composite(container, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = GridData.FILL;
		savePart.setLayout(new GridLayout(2, false));
		savePart.setLayoutData(gridData);
		
		// directory
		Label savePath = new Label(savePart, SWT.NONE);
		savePath.setText("Directory: ");
		savePath.setLayoutData(new GridData(SWT.END, SWT.END, false, false));
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = SWT.END;

		filePath = new Text(savePart, SWT.BORDER | SWT.SINGLE);
		filePath.setText(getDefaultFilePath());
		filePath.setEditable(false);
		filePath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validateDialog();
			}
		});
		filePath.setLayoutData(gd);

		// browse
		GridData gridDataFill = new GridData();
		gridDataFill.horizontalSpan = 2;
		gridDataFill.horizontalIndent = 60;
		
		Button browse;
		browse = new Button(savePart, SWT.PUSH);
		browse.setText("Browse ...");
		browse.setLayoutData(gridDataFill);
		Shell shell = this.getParentShell();
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.NULL);
				String path = dialog.open();
				if (path != null) {
					filePath.setText(path.toString());
				}
			}

		});

		// filename
		Label saveName = new Label(savePart, SWT.NONE);
		saveName.setText("Save as: ");
		saveName.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));

		filename = new Text(savePart, SWT.BORDER | SWT.SINGLE);
		filename.setText("mergeResult.bib");
		filename.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validateDialog();
			}
		});
		filename.setLayoutData(gd);
	}
	
	private String getDefaultFilePath() {
		String rootPath = ResourcesPlugin.getWorkspace().getRoot().getLocationURI().getPath();
		return rootPath.substring(0, rootPath.lastIndexOf("/")) + "/mergeResults";
	}

	private void buildOptions(Composite container) {
		Group options = new Group(container, SWT.SHADOW_NONE);
		options.setText("Options");
		options.setLayout(new RowLayout(SWT.VERTICAL));
		saveIntersection = new Button(options, SWT.CHECK);
		saveIntersection.setText("Save intersection");
		saveIntersection.setSelection(true);
		saveUnion = new Button(options, SWT.CHECK);
		saveUnion.setText("Save union");
		saveUnion.setSelection(true);

		saveIntersection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!saveIntersection.getSelection())
					saveUnion.setSelection(true);
			}
		});

		saveUnion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!saveUnion.getSelection())
					saveIntersection.setSelection(true);
			}
		});
	}

	private void validateDialog() {
		boolean valid = filename.getText().matches("[-_. A-Za-z0-9]+\\.bib") && ctv.getCheckedElements().length > 1;
		valid = valid && !(new File(getFilePath())).exists();
		getButton(IDialogConstants.OK_ID).setEnabled(valid);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Merge BibTeX files");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		createButton(parent, IDialogConstants.OK_ID, "Merge", true);
	}

	/**
	 * intervene if the user wants to manipulate the preview
	 * 
	 * @return true if he wants to proceed, false if he wants to cancel
	 */
	private boolean createMessageBox(String cause) {
		MessageBox messageBox = new MessageBox(this.getParentShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);

		messageBox.setText("Warning");
		messageBox.setMessage(cause + " Do you want to continue anyway?");
		int buttonID = messageBox.open();
		switch (buttonID) {
		case SWT.OK:
			return true;
		case SWT.CANCEL:
			return false;
		default:
			return false;
		}
	}

	@Override
	protected void okPressed() {
		if (conflitsExist && saveUnion.getSelection() && !createMessageBox(
				"There are still merge conflicts. If you continue, all conflicts will be merged automatically."))
			return;

		writeToFile();
		super.okPressed();
	}

	private String getFilePath() {
		return filePath.getText() + "/" + filename.getText();
	}

	private void writeToFile() {

		try {
			List<String> parts = new ArrayList<>();
			if (saveUnion.getSelection())
				parts.add(mergeData.writeUnion());
			if (saveIntersection.getSelection())
				parts.add(mergeData.writeIntersection());

			Files.write(Paths.get(getFilePath()), String.join("\n",parts).getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
