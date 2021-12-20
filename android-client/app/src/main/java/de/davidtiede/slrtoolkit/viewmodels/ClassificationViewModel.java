package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.repositories.EntryRepository;
import de.davidtiede.slrtoolkit.repositories.RepoRepository;
import de.davidtiede.slrtoolkit.repositories.TaxonomyRepository;
import de.davidtiede.slrtoolkit.repositories.TaxonomyWithEntriesRepo;

public class ClassificationViewModel extends AndroidViewModel {
    private final RepoRepository repoRepository;
    private final EntryRepository entryRepository;
    private final TaxonomyRepository taxonomyRepository;
    private final TaxonomyWithEntriesRepo taxonomyWithEntriesRepo;
    private Application application;
    private int currentRepoId;
    private int currentEntryId;
    private List<Integer> selectedTaxonomies;

    public ClassificationViewModel(@NonNull Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        entryRepository = new EntryRepository(application);
        taxonomyRepository = new TaxonomyRepository(application);
        taxonomyWithEntriesRepo = new TaxonomyWithEntriesRepo(application);

        this.application = application;
    }

    public int getCurrentEntryId() {
        return currentEntryId;
    }

    public int getCurrentRepoId() {
        return currentRepoId;
    }

    public void setCurrentEntryId(int currentEntryId) {
        this.currentEntryId = currentEntryId;
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
        return taxonomyRepository.getChildTaxonomiesWithEntries(repoId, parentId);
    }

    public void insertEntryForTaxonomy(int taxonomyId, int entryId) {
        this.selectedTaxonomies.add(taxonomyId);
        taxonomyWithEntriesRepo.insert(taxonomyId, entryId);
    }

    public void delete(int taxonomyId, int entryId) {
        int indexOfSelectedTaxonomy = this.selectedTaxonomies.indexOf(taxonomyId);
        if(indexOfSelectedTaxonomy != -1) {
            this.selectedTaxonomies.remove(indexOfSelectedTaxonomy);
        }
        taxonomyWithEntriesRepo.delete(taxonomyId, entryId);
    }

    public List<TaxonomyWithEntries> getTaxonomyWithEntriesDirectly(int repoId, int parentId) throws ExecutionException, InterruptedException {
        return taxonomyWithEntriesRepo.getTaxonomyWithEntriesDirectly(repoId, parentId);
    }
}
