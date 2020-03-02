package de.tudresden.slr.questionnaire;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import com.google.gson.Gson;

import de.tudresden.slr.questionnaire.util.EclipseUtils;
import de.tudresden.slr.questionnaire.util.GsonFactory;

public class QuestionnaireStorage {

	private static QuestionnaireStorage instance;
	protected Map<Questionnaire, IFile> fileOriginMap = new HashMap<Questionnaire, IFile>();

	private QuestionnaireStorage() {
	}

	public static QuestionnaireStorage getInstance() {
		if (instance == null)
			instance = new QuestionnaireStorage();
		return instance;
	}

	public List<Questionnaire> listExistingQuestionnaires(IProject project) {
		List<IFile> files = EclipseUtils.listProjectFiles(project).stream()
				.filter(f -> f.getName().endsWith(".questionnaire")).collect(Collectors.toList());
		List<Questionnaire> qs = new LinkedList<Questionnaire>();
		Gson gson = GsonFactory.makeGson();
		for (IFile f : files) {
			try {
				FileReader reader = new FileReader(f.getLocation().toFile());
				Questionnaire q = gson.fromJson(reader, Questionnaire.class);
				qs.add(q);
				fileOriginMap.put(q, f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return qs;
	}

	public void persist(IFile target, Questionnaire questionnaire) {
		Gson gson = GsonFactory.makeGson();
		EclipseUtils.printToIFile(target, gson.toJson(questionnaire));
	}

	/**
	 * overwrites the original file a questionnaire came from, iff the questionnaire
	 * has been modified
	 */
	public void persist(Questionnaire questionnaire) {
		if (!questionnaire.isDirty())
			return;
		IFile originalFile = fileOriginMap.get(questionnaire);
		if (originalFile == null)
			throw new RuntimeException(new FileNotFoundException("could not resolve the file questionnaire came from"));
		persist(originalFile, questionnaire);
		questionnaire.setDirty(false);
	}

}
