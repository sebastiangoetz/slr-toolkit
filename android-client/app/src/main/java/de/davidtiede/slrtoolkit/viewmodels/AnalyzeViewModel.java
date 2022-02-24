package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.repositories.TaxonomyRepository;
import de.davidtiede.slrtoolkit.repositories.TaxonomyWithEntriesRepository;

public class AnalyzeViewModel extends AndroidViewModel {
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private int currentRepoId;
    private int parentTaxonomyToDisplayChildrenFor1;
    private int parentTaxonomyToDisplayChildrenFor2;

    public AnalyzeViewModel(@NonNull Application application) {
        super(application);
        taxonomyWithEntriesRepository = new TaxonomyWithEntriesRepository(application);
    }

    public int getParentTaxonomyToDisplayChildrenFor1() {
        return parentTaxonomyToDisplayChildrenFor1;
    }

    public void setParentTaxonomyToDisplayChildrenFor1(int parentTaxonomyToDisplayChildrenFor1) {
        this.parentTaxonomyToDisplayChildrenFor1 = parentTaxonomyToDisplayChildrenFor1;
    }

    public int getParentTaxonomyToDisplayChildrenFor2() {
        return parentTaxonomyToDisplayChildrenFor2;
    }

    public void setParentTaxonomyToDisplayChildrenFor2(int parentTaxonomyToDisplayChildrenFor2) {
        this.parentTaxonomyToDisplayChildrenFor2 = parentTaxonomyToDisplayChildrenFor2;
    }

    public int getCurrentRepoId() {
        return currentRepoId;
    }

    public void setCurrentRepoId(int currentRepoId) {
        this.currentRepoId = currentRepoId;
    }


    public Map<Taxonomy, Integer> getNumberOfEntriesForChildrenOfTaxonomy(int repoId, int parentId) throws ExecutionException, InterruptedException {
        return taxonomyWithEntriesRepository.getNumberOfEntriesForChildrenOfTaxonomy(repoId, parentId);
    }

    public List<TaxonomyWithEntries> getTaxonomiesWithLeafChildTaxonomies(int repoId) throws ExecutionException, InterruptedException {
        return taxonomyWithEntriesRepository.getTaxonomyIdsWithLeafChildTaxonomies(repoId);
    }

    public List<TaxonomyWithEntries> getChildTaxonomiesForTaxonomyId(int repoId, int parentId) throws ExecutionException, InterruptedException {
        return taxonomyWithEntriesRepository.getChildTaxonomiesForTaxonomyId(repoId, parentId);
    }
}
