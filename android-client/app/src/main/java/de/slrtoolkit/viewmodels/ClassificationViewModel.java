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

import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.database.TaxonomyWithEntries;
import de.slrtoolkit.repositories.BibEntryRepository;
import de.slrtoolkit.repositories.RepoRepository;
import de.slrtoolkit.repositories.TaxonomyRepository;
import de.slrtoolkit.repositories.TaxonomyWithEntriesRepository;
import de.slrtoolkit.util.BibUtil;

public class ClassificationViewModel extends AndroidViewModel {
    private final TaxonomyRepository taxonomyRepository;
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private final BibEntryRepository bibEntryRepository;
    private final RepoRepository repoRepository;
    private int currentRepoId;
    private Repo repo;
    private int currentEntryId;
    private Set<Integer> selectedTaxonomies;

    public ClassificationViewModel(@NonNull Application application) {
        super(application);
        taxonomyRepository = new TaxonomyRepository(application);
        taxonomyWithEntriesRepository = new TaxonomyWithEntriesRepository(application);
        repoRepository = new RepoRepository(application);
        bibEntryRepository = new BibEntryRepository(application);
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
        try {
            repo = repoRepository.getRepoByIdDirectly(currentRepoId);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(this.getClass().getName(), "could not load repo", e);
        }
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

    public void addTaxonomyToClassification(int taxonomyId, int entryId) {
        this.selectedTaxonomies.add(taxonomyId);
        taxonomyWithEntriesRepository.insert(taxonomyId, entryId);
        BibEntry bibEntry = null;
        try {
            bibEntry = bibEntryRepository.getEntryByIdDirectly(entryId);
            String classesString = bibEntry.getClasses();
            Taxonomy taxonomy = taxonomyRepository.getTaxonomyByIdDirectly(taxonomyId);
            String newClassification = BibUtil.addClassToClassification(classesString, taxonomy.getName());
            bibEntry.setClasses(newClassification);
            bibEntryRepository.update(bibEntry, repo);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeTaxonomyFromClassification(int taxonomyId, int entryId) {
        this.selectedTaxonomies.remove(taxonomyId);
        taxonomyWithEntriesRepository.delete(taxonomyId, entryId);
        try {
            BibEntry bibEntry = bibEntryRepository.getEntryByIdDirectly(entryId);
            String classesString = bibEntry.getClasses();
            Taxonomy taxonomy = taxonomyRepository.getTaxonomyByIdDirectly(taxonomyId);
            String newClassification = BibUtil.removeClassFromClassification(classesString, taxonomy.getName());
            bibEntry.setClasses(newClassification);
            bibEntryRepository.update(bibEntry, repo);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<List<Taxonomy>> getAllTaxonomiesForRepo(int repoId) throws ExecutionException, InterruptedException {
        return taxonomyRepository.getAllTaxonomiesForRepo(repoId);
    }
}
