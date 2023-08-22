package de.slrtoolkit.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jbibtex.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.database.Entry;
import de.slrtoolkit.database.EntryTaxonomyCrossRef;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.repositories.EntryRepository;
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
    private final EntryRepository entryRepository;
    private final TaxonomyRepository taxonomyRepository;
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private final LiveData<List<Repo>> allRepos;
    private final Application application;
    private Repo currentRepo;

    public RepoViewModel(Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        entryRepository = new EntryRepository(application);
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

    public void saveAllEntriesForRepo(List<Entry> entries, int repoId) {
        entryRepository.insertEntriesForRepo(repoId, entries);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initializeDataForRepo(int repoId, String path) {
        FileUtil fileUtil = new FileUtil();
        File file = fileUtil.accessFiles(path, application, ".bib");
        Map<Entry, String> entriesWithTaxonomies = new HashMap<>();
        try {
            BibTexParser parser = BibTexParser.getBibTexParser();
            parser.setBibTeXDatabase(file);
            entriesWithTaxonomies = parser.parseBibTexFile(file);
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
        initializeEntries(repoId, entriesWithTaxonomies);
        initializeTaxonomy(repoId, path);
        initializeTaxonomiesWithEntries(entriesWithTaxonomies, repoId);
    }

    public void initializeEntries(int repoId, Map<Entry, String> entriesWithTaxonomies) {
        List<Entry> entries = new ArrayList<>(entriesWithTaxonomies.keySet());
        saveAllEntriesForRepo(entries, repoId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initializeTaxonomy(int repoId, String path) {
        taxonomyRepository.initializeTaxonomy(repoId, path, application);
    }

    public void initializeTaxonomiesWithEntries(Map<Entry, String> entriesWithTaxonomies, int repoId) {
        List<EntryTaxonomyCrossRef> entryTaxonomyCrossRefs = new ArrayList<>();
        TaxonomyParser taxonomyParser = new TaxonomyParser();
        for (Map.Entry<Entry, String> e : entriesWithTaxonomies.entrySet()) {
            String taxonomyString = e.getValue();
            if (taxonomyString.compareTo("") != 0) {
                List<TaxonomyParserNode> taxonomyParserNodes = taxonomyParser.parse(taxonomyString);
                for (TaxonomyParserNode node : taxonomyParserNodes) {
                    try {
                        //only if the taxonomy has no child taxonomies a relation is added
                        if (node.getChildren().size() == 0) {
                            Entry entry = entryRepository.getEntryByRepoAndKeyDirectly(repoId, e.getKey().getKey());
                            Taxonomy taxonomy = taxonomyRepository.getTaxonomyByRepoAndPathDirectly(repoId, node.getPath());
                            if (taxonomy != null && entry != null) {
                                //saved taxonomy also can't have children
                                if (!taxonomy.isHasChildren()) {
                                    EntryTaxonomyCrossRef entryTaxonomyCrossRef = new EntryTaxonomyCrossRef();
                                    entryTaxonomyCrossRef.setTaxonomyId(taxonomy.getTaxonomyId());
                                    entryTaxonomyCrossRef.setEntryId(entry.getEntryId());
                                    entryTaxonomyCrossRefs.add(entryTaxonomyCrossRef);
                                }
                            }
                        }
                    } catch (ExecutionException | InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
        taxonomyWithEntriesRepository.insertAll(entryTaxonomyCrossRefs);
    }
}