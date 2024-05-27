package de.slrtoolkit.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.database.TaxonomyWithEntries;
import de.slrtoolkit.repositories.TaxonomyRepository;
import de.slrtoolkit.repositories.TaxonomyWithEntriesRepository;

public class ClassificationViewModel extends AndroidViewModel {
    private final TaxonomyRepository taxonomyRepository;
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private int currentRepoId;
    private int currentEntryId;
    private Set<Integer> selectedTaxonomies;

    public ClassificationViewModel(@NonNull Application application) {
        super(application);
        taxonomyRepository = new TaxonomyRepository(application);
        taxonomyWithEntriesRepository = new TaxonomyWithEntriesRepository(application);
        selectedTaxonomies = new HashSet<>();

    }

    public int getCurrentEntryId() {
        return currentEntryId;
    }

    public void setCurrentEntryId(int currentEntryId) {
        this.currentEntryId = currentEntryId;
    }

    public int getCurrentRepoId() {
        return currentRepoId;
    }

    public void setCurrentRepoId(int currentRepoId) {
        this.currentRepoId = currentRepoId;
    }

    public Set<Integer> getSelectedTaxonomies() {
        if(selectedTaxonomies == null) selectedTaxonomies = new HashSet<>();
        try {
            for(TaxonomyWithEntries t : taxonomyWithEntriesRepository.getTaxonomiesForEntry(this.currentEntryId)) {
                selectedTaxonomies.add(t.taxonomy.getTaxonomyId());
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e(this.getClass().getName(), "couldn't fetch selected taxonomies for this entry", e);
        }
        return selectedTaxonomies;
    }

    public void insertEntryForTaxonomy(int taxonomyId, int entryId) {
        this.selectedTaxonomies.add(taxonomyId);
        taxonomyWithEntriesRepository.insert(taxonomyId, entryId);
    }

    public void delete(int taxonomyId, int entryId) {
        this.selectedTaxonomies.remove(taxonomyId);
        taxonomyWithEntriesRepository.delete(taxonomyId, entryId);
    }

    public LiveData<List<Taxonomy>> getAllTaxonomiesForRepo(int repoId) throws ExecutionException, InterruptedException {
        return taxonomyRepository.getAllTaxonomiesForRepo(repoId);
    }
}
