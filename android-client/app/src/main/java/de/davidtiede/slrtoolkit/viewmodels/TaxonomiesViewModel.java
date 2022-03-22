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

public class TaxonomiesViewModel extends AndroidViewModel {
    private int currentRepoId;
    private int currentEntryIdForCard;
    private int currentTaxonomyId;
    private final TaxonomyRepository taxonomyRepository;
    private final EntryRepository entryRepository;

    public TaxonomiesViewModel(Application application) {
        super(application);
        taxonomyRepository = new TaxonomyRepository(application);
        entryRepository = new EntryRepository(application);
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
}
