package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.repositories.EntryRepository;
import de.davidtiede.slrtoolkit.repositories.TaxonomyRepository;
import de.davidtiede.slrtoolkit.repositories.TaxonomyWithEntriesRepository;

public class TaxonomiesViewModel extends AndroidViewModel {
    private int currentRepoId;
    private int currentEntryIdForCard;
    private int currentTaxonomyId;
    private final TaxonomyRepository taxonomyRepository;
    private final EntryRepository entryRepository;
    private List<Entry> currentEntriesInList;
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private int currentEntryInListCount;

    public TaxonomiesViewModel(Application application) {
        super(application);
        taxonomyRepository = new TaxonomyRepository(application);
        entryRepository = new EntryRepository(application);
        taxonomyWithEntriesRepository = new TaxonomyWithEntriesRepository(application);
    }

    public int getCurrentEntryInListCount() {
        return currentEntryInListCount;
    }

    public void setCurrentEntryInListCount(int currentEntryInListCount) {
        this.currentEntryInListCount = currentEntryInListCount;
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

    public void setCurrentEntriesInList(List<Entry> currentEntriesInList) {
        this.currentEntriesInList = currentEntriesInList;
    }

    public List<Entry> getCurrentEntriesInList() {
        return currentEntriesInList;
    }

    public LiveData<List<Taxonomy>> getChildrenForTaxonomy(int repoId, int parentId) {
        return taxonomyRepository.getChildTaxonomies(repoId, parentId);
    }

    public LiveData<Entry> getEntryById(int id) {
        return entryRepository.getEntryById(id);
    }

    public LiveData<TaxonomyWithEntries> getTaxonomyWithEntries(int repoId, int taxonomyId) {
        return taxonomyWithEntriesRepository.getTaxonomyWithEntries(repoId, taxonomyId);
    }
}
