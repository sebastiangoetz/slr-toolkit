package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
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

import de.davidtiede.slrtoolkit.database.EntryTaxonomyCrossRef;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.repositories.EntryRepository;
import de.davidtiede.slrtoolkit.repositories.RepoRepository;
import de.davidtiede.slrtoolkit.repositories.TaxonomyRepository;
import de.davidtiede.slrtoolkit.repositories.TaxonomyWithEntriesRepository;
import de.davidtiede.slrtoolkit.util.BibTexParser;
import de.davidtiede.slrtoolkit.util.FileUtil;
import de.davidtiede.slrtoolkit.util.TaxonomyParser;
import de.davidtiede.slrtoolkit.util.TaxonomyParserNode;

public class ProjectViewModel extends AndroidViewModel {
    private final RepoRepository repoRepository;
    private final EntryRepository entryRepository;
    private final TaxonomyRepository taxonomyRepository;
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private Application application;
    private int currentRepoId;
    private int currentEntryIdForCard;

    public ProjectViewModel(@NonNull Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        entryRepository = new EntryRepository(application);
        taxonomyRepository = new TaxonomyRepository(application);
        taxonomyWithEntriesRepository = new TaxonomyWithEntriesRepository(application);
        this.application = application;
    }

    public int getCurrentRepoId() {
        return currentRepoId;
    }

    public void setCurrentRepoId(int currentRepoId) {
        this.currentRepoId = currentRepoId;
    }

    public int getCurrentEntryIdForCard() {
        return currentEntryIdForCard;
    }

    public void setCurrentEntryIdForCard(int currentEntryIdForCard) {
        this.currentEntryIdForCard = currentEntryIdForCard;
    }

    public LiveData<Integer> getEntryAmount(int repoId) {
        return entryRepository.getEntryAmountForRepo(repoId);
    }

    public LiveData<Integer> getOpenEntryAmount(int repoId) {
        return entryRepository.getEntryAmountForStatus(repoId, Entry.Status.OPEN);
    }

    public LiveData<Repo> getRepoById(int id) {
        return repoRepository.getRepoById(id);
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initializeEntries(repoId, entriesWithTaxonomies);
        initializeTaxonomy(repoId, path);
        initializeTaxonomiesWithEntries(entriesWithTaxonomies, repoId);
    }

    public void initializeEntries(int repoId, Map<Entry, String> entriesWithTaxonomies) {
        List<Entry> entries = new ArrayList<>();
        for(Entry entry : entriesWithTaxonomies.keySet()) {
            entries.add(entry);
        }
        saveAllEntriesForRepo(entries, repoId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initializeTaxonomy(int repoId, String path) {
        taxonomyRepository.initializeTaxonomy(repoId, path, application);
    }

    public void initializeTaxonomiesWithEntries(Map<Entry, String> entriesWithTaxonomies, int repoId) {
        List<EntryTaxonomyCrossRef> entryTaxonomyCrossRefs = new ArrayList<>();
        TaxonomyParser taxonomyParser = new TaxonomyParser();
        for(Map.Entry<Entry, String> e : entriesWithTaxonomies.entrySet()) {
            String taxonomyString = e.getValue();
            if(taxonomyString.compareTo("") != 0) {
                List<TaxonomyParserNode> taxonomyParserNodes = taxonomyParser.parse(taxonomyString);
                for (TaxonomyParserNode node : taxonomyParserNodes) {
                    try {
                        //only if the taxonomy has no child taxonomies a relation is added
                        if(node.getChildren().size() == 0) {
                            Entry entry = entryRepository.getEntryByRepoAndKeyDirectly(repoId, e.getKey().getKey());
                            Taxonomy taxonomy = taxonomyRepository.getTaxonomyByRepoAndPathDirectly(repoId, node.getPath());
                            if (taxonomy != null && entry != null) {
                                //saved taxonomy also can't have children
                                if(!taxonomy.isHasChildren()) {
                                    EntryTaxonomyCrossRef entryTaxonomyCrossRef = new EntryTaxonomyCrossRef();
                                    entryTaxonomyCrossRef.setTaxonomyId(taxonomy.getTaxonomyId());
                                    entryTaxonomyCrossRef.setId(entry.getId());
                                    entryTaxonomyCrossRefs.add(entryTaxonomyCrossRef);
                                }
                            }
                        }
                    } catch (ExecutionException exception) {
                        exception.printStackTrace();
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
        taxonomyWithEntriesRepository.insertAll(entryTaxonomyCrossRefs);
    }

    public LiveData<List<Entry>> getEntriesForRepoWithSearchQuery(int repoId, String searchQueary) {
        return entryRepository.getEntriesForRepoWithSearchQuery(repoId, searchQueary);
    }

    public Repo getRepoByIdDirectly(int id) {
        Repo repo = null;
        try {
            repo = repoRepository.getRepoByIdDirectly(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return repo;
    }

    public void delete(Entry entry, int id) {
        Repo repo = getRepoByIdDirectly(id);
        if(repo != null) {
            entryRepository.delete(entry, repo);
        }
    }

    public void deleteById(int entryId, int id) {
        Entry entry = getEntryByIdDirectly(entryId);
        if(entry != null) {
            delete(entry, id);
        }
    }

    public LiveData<Entry> getEntryById(int id) {
        return entryRepository.getEntryById(id);
    }

    public Entry getEntryByIdDirectly(int id) {
        Entry entry;
        try {
            return entryRepository.getEntryByIdDirectly(id);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Entry>> getOpenEntriesForRepo(int repoId) {
        return entryRepository.getEntryForRepoByStatus(repoId, Entry.Status.OPEN);
    }

    public void updateEntry(Entry entry) {
        entryRepository.update(entry);
    }

    public LiveData<List<Entry>> getEntriesWithoutTaxonomies(int repoId) {
        return entryRepository.getEntriesWithoutTaxonomies(repoId);
    }

    public LiveData<Integer> getEntriesWithoutTaxonomiesCount(int repoId) {
        return entryRepository.getEntriesWithoutTaxonomiesCount(repoId);
    }
}
