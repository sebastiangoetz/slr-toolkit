package de.slrtoolkit.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.repositories.BibEntryRepository;
import de.slrtoolkit.repositories.RepoRepository;
import de.slrtoolkit.repositories.TaxonomyRepository;
import de.slrtoolkit.repositories.TaxonomyWithEntriesRepository;

public class TaxonomiesViewModel extends AndroidViewModel {
    private final TaxonomyRepository taxonomyRepository;
    private final BibEntryRepository bibEntryRepository;
    private final RepoRepository repoRepository;
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private int currentRepoId;
    private int currentEntryIdForCard;
    private int currentTaxonomyId;
    private List<BibEntry> currentEntriesInList;
    private int currentEntryInListCount;

    public TaxonomiesViewModel(Application application) {
        super(application);
        taxonomyRepository = new TaxonomyRepository(application);
        bibEntryRepository = new BibEntryRepository(application);
        repoRepository = new RepoRepository(application);
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

    public List<BibEntry> getCurrentEntriesInList() {
        return currentEntriesInList;
    }

    public void setCurrentEntriesInList(List<BibEntry> currentEntriesInList) {
        this.currentEntriesInList = currentEntriesInList;
    }

    public LiveData<List<Taxonomy>> getAllTaxonomiesForRepo(int repoId) {
        return taxonomyRepository.getAllTaxonomiesForRepo(repoId);
    }


    public LiveData<BibEntry> getEntryById(int id) {
        return bibEntryRepository.getEntryById(id);
    }

    public LiveData<List<BibEntry>> getTaxonomyWithEntries(List<Integer> taxonomyIds) {
        return taxonomyWithEntriesRepository.getTaxonomyWithEntries(taxonomyIds);
    }

    public Taxonomy getTaxonomyById(int taxId) throws ExecutionException, InterruptedException {
        return taxonomyRepository.getTaxonomyByIdDirectly(taxId);
    }

    public void updateTaxonomyAfterPull() {
        try {
            taxonomyRepository.removeAllTaxonomiesOfRepo(currentRepoId);
            String path = repoRepository.getRepoByIdDirectly(currentRepoId).getLocal_path();
            taxonomyRepository.initializeTaxonomy(currentRepoId, path, getApplication());
        } catch(ExecutionException | InterruptedException e) {
            Log.e(this.getClass().getName(), "update taxonomy after pull failed", e);
        }
    }
}
