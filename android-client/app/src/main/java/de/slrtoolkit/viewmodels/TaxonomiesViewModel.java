package de.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.slrtoolkit.database.Entry;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.database.TaxonomyWithEntries;
import de.slrtoolkit.repositories.EntryRepository;
import de.slrtoolkit.repositories.TaxonomyRepository;
import de.slrtoolkit.repositories.TaxonomyWithEntriesRepository;

public class TaxonomiesViewModel extends AndroidViewModel {
    private final TaxonomyRepository taxonomyRepository;
    private final EntryRepository entryRepository;
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private int currentRepoId;
    private int currentEntryIdForCard;
    private int currentTaxonomyId;
    private List<Entry> currentEntriesInList;
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

    public int getCurrentTaxonomyId() {
        return currentTaxonomyId;
    }

    public void setCurrentTaxonomyId(int currentTaxonomyId) {
        this.currentTaxonomyId = currentTaxonomyId;
    }

    public List<Entry> getCurrentEntriesInList() {
        return currentEntriesInList;
    }

    public void setCurrentEntriesInList(List<Entry> currentEntriesInList) {
        this.currentEntriesInList = currentEntriesInList;
    }

    public LiveData<List<Taxonomy>> getChildrenForTaxonomy(int repoId, int parentId) {
        return taxonomyRepository.getChildTaxonomies(repoId, parentId);
    }

    public LiveData<List<Taxonomy>> getAllTaxonomiesForRepo(int repoId) {
        return taxonomyRepository.getAllTaxonomiesForRepo(repoId);
    }


    public LiveData<Entry> getEntryById(int id) {
        return entryRepository.getEntryById(id);
    }

    public LiveData<TaxonomyWithEntries> getTaxonomyWithEntries(int repoId, int taxonomyId) {
        return taxonomyWithEntriesRepository.getTaxonomyWithEntries(repoId, taxonomyId);
    }
}
