package de.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.database.TaxonomyWithEntries;
import de.slrtoolkit.repositories.EntryRepository;
import de.slrtoolkit.repositories.RepoRepository;
import de.slrtoolkit.repositories.TaxonomyRepository;
import de.slrtoolkit.repositories.TaxonomyWithEntriesRepository;

public class ClassificationViewModel extends AndroidViewModel {
    private final TaxonomyRepository taxonomyRepository;
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private int currentRepoId;
    private int currentEntryId;
    private List<Integer> selectedTaxonomies;

    public ClassificationViewModel(@NonNull Application application) {
        super(application);
        RepoRepository repoRepository = new RepoRepository(application);
        EntryRepository entryRepository = new EntryRepository(application);
        taxonomyRepository = new TaxonomyRepository(application);
        taxonomyWithEntriesRepository = new TaxonomyWithEntriesRepository(application);

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

    public List<Integer> getSelectedTaxonomies() {
        return selectedTaxonomies;
    }

    public void setSelectedTaxonomies(List<Integer> selectedTaxonomies) {
        this.selectedTaxonomies = selectedTaxonomies;
    }

    public LiveData<List<Taxonomy>> getChildrenForTaxonomy(int repoId, int parentId) {
        return taxonomyRepository.getChildTaxonomies(repoId, parentId);
    }

    public LiveData<List<TaxonomyWithEntries>> getChildTaxonomiesWithEntries(int repoId, int parentId) {
        return taxonomyWithEntriesRepository.getChildTaxonomiesWithEntries(repoId, parentId);
    }

    public void insertEntryForTaxonomy(int taxonomyId, int entryId) {
        this.selectedTaxonomies.add(taxonomyId);
        taxonomyWithEntriesRepository.insert(taxonomyId, entryId);
    }

    public void delete(int taxonomyId, int entryId) {
        int indexOfSelectedTaxonomy = this.selectedTaxonomies.indexOf(taxonomyId);
        if (indexOfSelectedTaxonomy != -1) {
            this.selectedTaxonomies.remove(indexOfSelectedTaxonomy);
        }
        taxonomyWithEntriesRepository.delete(taxonomyId, entryId);
    }

    public List<TaxonomyWithEntries> getTaxonomyWithEntriesDirectly(int repoId, int parentId) throws ExecutionException, InterruptedException {
        return taxonomyWithEntriesRepository.getChildTaxonomiesForTaxonomyId(repoId, parentId);
    }
}
