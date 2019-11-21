package de.tudresden.slr.questionnaire;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPartSite;

import de.tudresden.slr.model.bibtex.Document;
import de.tudresden.slr.questionnaire.model.Question;
import de.tudresden.slr.questionnaire.util.BibtexExplorer;

public class UnansweredDocumentsView {
	private Label incompleteDocumentsLabel;
	private ListViewer incompleteDocumentsList;

	private Supplier<Questionnaire> questionnaireSupplier;
	private Supplier<IProject> projectSupplier;

	public UnansweredDocumentsView(Composite parent, IWorkbenchPartSite site,
			Supplier<Questionnaire> questionnaireSupplier, Supplier<IProject> projectSupplier) {
		this.questionnaireSupplier = questionnaireSupplier;
		this.projectSupplier = projectSupplier;

		incompleteDocumentsLabel = new Label(parent, 0);
		incompleteDocumentsLabel.setText("Answers missing for following documents:");
		incompleteDocumentsLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false, 2, 1));

		incompleteDocumentsList = new ListViewer(parent, SWT.V_SCROLL);
		GridData gd = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false, 2, 1);
		gd.heightHint = (int) (((org.eclipse.swt.widgets.List) incompleteDocumentsList.getControl()).getItemHeight()
				* 4);
		gd.widthHint = 300;
		incompleteDocumentsList.getControl().setLayoutData(gd);
		incompleteDocumentsList.setSorter(new ViewerSorter());
		incompleteDocumentsList.setContentProvider(ArrayContentProvider.getInstance());
		incompleteDocumentsList.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Document) element).getKey();
			}
		});
		site.setSelectionProvider(incompleteDocumentsList);
	}

	public List<Document> findDocuments() {
		List<Document> badDocuments = new LinkedList<>();
		IProject project = projectSupplier.get();
		Questionnaire questionnaire = questionnaireSupplier.get();
		if (questionnaire != null && project != null) {
			BibtexExplorer be = new BibtexExplorer(project);
			List<Question<?>> questions = questionnaire.getQuestions();
			List<Document> documents = be.getDocuments();
			for (Document doc : documents) {
				for (Question<?> question : questions) {
					if (!question.hasAnswerFor(doc.getKey())) {
						badDocuments.add(doc);
						break;
					}
				}
			}
		}
		incompleteDocumentsList.setInput(badDocuments);
		return badDocuments;
	}

}
