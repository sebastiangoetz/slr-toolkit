package de.slrtoolkit.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jbibtex.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.database.Author;
import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.database.BibEntryTaxonomyCrossRef;
import de.slrtoolkit.database.Keyword;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.repositories.AuthorRepository;
import de.slrtoolkit.repositories.BibEntryRepository;
import de.slrtoolkit.repositories.KeywordRepository;
import de.slrtoolkit.repositories.RepoRepository;
import de.slrtoolkit.repositories.TaxonomyRepository;
import de.slrtoolkit.repositories.TaxonomyWithEntriesRepository;
import de.slrtoolkit.util.BibTexParser;
import de.slrtoolkit.util.FileUtil;
import de.slrtoolkit.util.TaxonomyParser;
import de.slrtoolkit.util.TaxonomyParserNode;

/**
 * View Model to keep a reference to the repo repository.
 */
public class RepoViewModel extends AndroidViewModel {

    private final RepoRepository repoRepository;
    private final BibEntryRepository bibEntryRepository;
    private final AuthorRepository authorRepository;
    private final KeywordRepository keywordRepository;
    private final TaxonomyRepository taxonomyRepository;
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private final LiveData<List<Repo>> allRepos;
    private final Application application;
    private Repo currentRepo;

    public RepoViewModel(Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        bibEntryRepository = new BibEntryRepository(application);
        authorRepository = new AuthorRepository(application);
        keywordRepository = new KeywordRepository(application);
        taxonomyRepository = new TaxonomyRepository(application);
        taxonomyWithEntriesRepository = new TaxonomyWithEntriesRepository(application);
        allRepos = repoRepository.getAllRepos();
        this.application = application;
    }

    public Repo getCurrentRepo() {
        return currentRepo;
    }

    public void setCurrentRepo(Repo currentRepo) {
        this.currentRepo = currentRepo;
    }

    public LiveData<List<Repo>> getAllRepos() {
        return allRepos;
    }

    public long insert(Repo repo) {
        return repoRepository.insert(repo);
    }

    public void update(Repo repo) {
        repoRepository.update(repo);
    }

    public void delete(Repo repo) {
        repoRepository.delete(repo);
    }

    public Repo getRepoDirectly(int id) throws ExecutionException, InterruptedException {
        return repoRepository.getRepoByIdDirectly(id);
    }

    public void saveAllEntriesForRepo(List<BibEntry> entries, int repoId) {
        bibEntryRepository.insertEntriesForRepo(repoId, entries);
    }

    public void initializeDataForRepo(int repoId, String path) {
        FileUtil fileUtil = new FileUtil();
        File metadata = fileUtil.accessFiles(path, application, ".slrproject");
        try (BufferedReader br = new BufferedReader(new FileReader(metadata))) {
            String line;
            StringBuilder contents = new StringBuilder();
            while ((line = br.readLine()) != null) {
                contents.append(line);
                contents.append(System.lineSeparator());
            }
            String title = getValueOfTag("title", contents.toString());
            currentRepo.setName(title);

            String abs = getValueOfTag("projectAbstract", contents.toString());
            currentRepo.setTextAbstract(abs);

            String taxAbs = getValueOfTag("taxonomyDescription", contents.toString());
            currentRepo.setTaxonomyDescription(taxAbs);

            String keywords = getValueOfTag("keywords", contents.toString());
            for (String keyword : keywords.split(",")) {
                keyword = keyword.trim();
                Keyword k = new Keyword(keyword);
                k.setRepoId(currentRepo.getId());
                keywordRepository.insert(k);
            }

            int lastAuthorIndex = 0;
            int nextAuthorIndex;
            while (true) {
                nextAuthorIndex = contents.indexOf("<authorsList>", lastAuthorIndex);
                if (nextAuthorIndex == -1) break;
                int nextAuthorIndexClose = contents.indexOf("</authorsList>", lastAuthorIndex);
                lastAuthorIndex = nextAuthorIndexClose + 14;
                String authors = contents.subSequence(nextAuthorIndex + 13, nextAuthorIndexClose).toString().trim();
                String name = getValueOfTag("name", authors);
                String email = getValueOfTag("email", authors);
                String affiliation = getValueOfTag("organisation", authors);
                Author author = new Author(name, affiliation, email);
                author.setRepoId(currentRepo.getId());
                authorRepository.insert(author);
            }


        } catch (IOException e) {
            Log.e(RepoViewModel.class.getName(), "initializeDataForRepo: couldn't read slrproject file", e);
        }

        File file = fileUtil.accessFiles(path, application, ".bib");
        Map<BibEntry, String> entriesWithTaxonomies = new HashMap<>();
        try {
            BibTexParser parser = BibTexParser.getBibTexParser();
            parser.setBibTeXDatabase(file);
            entriesWithTaxonomies = parser.parseBibTexFile(file);
        } catch (FileNotFoundException | ParseException e) {
            Log.e(RepoViewModel.class.getName(), "initializeDataForRepo: could read bibtex file", e);
        }
        initializeEntries(repoId, entriesWithTaxonomies);
        initializeTaxonomy(repoId, path);
        initializeTaxonomiesWithEntries(entriesWithTaxonomies, repoId);
    }

    private String getValueOfTag(String tag, String xml) {
        return xml.subSequence(xml.indexOf("<" + tag + ">") + tag.length() + 2, xml.indexOf("</" + tag + ">")).toString().trim();
    }
    public void initializeEntries(int repoId, Map<BibEntry, String> entriesWithTaxonomies) {
        List<BibEntry> entries = new ArrayList<>(entriesWithTaxonomies.keySet());
        saveAllEntriesForRepo(entries, repoId);
    }

    public void initializeTaxonomy(int repoId, String path) {
        taxonomyRepository.initializeTaxonomy(repoId, path, application);
    }

    public void initializeTaxonomiesWithEntries(Map<BibEntry, String> entriesWithTaxonomies, int repoId) {
        List<BibEntryTaxonomyCrossRef> bibEntryTaxonomyCrossRefs = new ArrayList<>();
        TaxonomyParser taxonomyParser = new TaxonomyParser();
        for (Map.Entry<BibEntry, String> e : entriesWithTaxonomies.entrySet()) {
            String taxonomyString = e.getValue();
            if (taxonomyString.compareTo("") != 0) {
                List<TaxonomyParserNode> taxonomyParserNodes = taxonomyParser.parse(taxonomyString);
                for (TaxonomyParserNode node : taxonomyParserNodes) {
                    try {
                        //only if the taxonomy has no child taxonomies a relation is added
                        if (node.getChildren().isEmpty()) {
                            BibEntry bibEntry = bibEntryRepository.getEntryByRepoAndKeyDirectly(repoId, e.getKey().getKey());
                            Taxonomy taxonomy = taxonomyRepository.getTaxonomyByRepoAndPathDirectly(repoId, node.getPath());
                            if (taxonomy != null && bibEntry != null) {
                                //saved taxonomy also can't have children
                                if (!taxonomy.isHasChildren()) {
                                    BibEntryTaxonomyCrossRef bibEntryTaxonomyCrossRef = new BibEntryTaxonomyCrossRef();
                                    bibEntryTaxonomyCrossRef.setTaxonomyId(taxonomy.getTaxonomyId());
                                    bibEntryTaxonomyCrossRef.setEntryId(bibEntry.getEntryId());
                                    bibEntryTaxonomyCrossRefs.add(bibEntryTaxonomyCrossRef);
                                }
                            }
                        }
                    } catch (ExecutionException | InterruptedException exception) {
                        Log.e(RepoViewModel.class.getName(), "initializeTaxonomiesWithEntries: ", exception);
                    }
                }
            }
        }
        taxonomyWithEntriesRepository.insertAll(bibEntryTaxonomyCrossRefs);
    }
}