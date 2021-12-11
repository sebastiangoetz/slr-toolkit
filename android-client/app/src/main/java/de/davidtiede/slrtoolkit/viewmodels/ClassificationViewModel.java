package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.repositories.EntryRepository;
import de.davidtiede.slrtoolkit.repositories.RepoRepository;
import de.davidtiede.slrtoolkit.repositories.TaxonomyRepository;

public class ClassificationViewModel extends AndroidViewModel {
    private final RepoRepository repoRepository;
    private final EntryRepository entryRepository;
    private final TaxonomyRepository taxonomyRepository;
    private Application application;
    private int currentRepoId;
    private int currentEntryId;

    public ClassificationViewModel(@NonNull Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        entryRepository = new EntryRepository(application);
        taxonomyRepository = new TaxonomyRepository(application);
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

    public LiveData<List<Taxonomy>> getChildrenForTaxonomy(int repoId, int parentId) {
        return taxonomyRepository.getChildTaxonomies(repoId, parentId);
    }

    public LiveData<List<TaxonomyWithEntries>> getChildTaxonomiesWithEntries(int repoId, int parentId) {
        return taxonomyRepository.getChildTaxonomiesWithEntries(repoId, parentId);
    }
}
