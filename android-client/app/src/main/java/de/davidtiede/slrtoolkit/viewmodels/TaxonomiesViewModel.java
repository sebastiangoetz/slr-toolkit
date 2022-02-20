package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.repositories.EntryRepository;
import de.davidtiede.slrtoolkit.repositories.RepoRepository;
import de.davidtiede.slrtoolkit.repositories.TaxonomyRepository;

public class TaxonomiesViewModel extends AndroidViewModel {
    private int currentRepoId;
    private int currentEntryIdForCard;
    private int currentTaxonomyId;
    private final TaxonomyRepository taxonomyRepository;
    private final EntryRepository entryRepository;
    private final RepoRepository repoRepository;

    public TaxonomiesViewModel(Application application) {
        super(application);
        taxonomyRepository = new TaxonomyRepository(application);
        entryRepository = new EntryRepository(application);
        repoRepository = new RepoRepository(application);
    }

    public void setCurrentRepoId(int currentRepoId) {
        this.currentRepoId = currentRepoId;
    }

    public int getCurrentRepoId() {
        return currentRepoId;
    }

    public void setCurrentEntryIdForCard(int currentEntryIdForCard) {
        this.currentEntryIdForCard = currentEntryIdForCard;
    }

    public void setCurrentTaxonomyId(int currentTaxonomyId) {
        this.currentTaxonomyId = currentTaxonomyId;
    }

    public int getCurrentEntryIdForCard() {
        return currentEntryIdForCard;
    }

    public int getCurrentTaxonomyId() {
        return currentTaxonomyId;
    }

    public LiveData<List<Taxonomy>> getChildrenForTaxonomy(int repoId, int parentId) {
        return taxonomyRepository.getChildTaxonomies(repoId, parentId);
    }

    public LiveData<Entry> getEntryById(int id) {
        return entryRepository.getEntryById(id);
    }

    public LiveData<TaxonomyWithEntries> getTaxonomyWithEntries(int repoId, int taxonomyId) {
        return taxonomyRepository.getTaxonomyWithEntries(repoId, taxonomyId);
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

    public void deleteEntry(Entry entry, int id) {
        Repo repo = getRepoByIdDirectly(id);
        if(repo != null) {
            entryRepository.delete(entry, repo);
        }
    }

    public void deleteEntryById(int entryId, int id) {
        Entry entry = getEntryByIdDirectly(entryId);
        if(entry != null) {
            deleteEntry(entry, id);
        }
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
}
