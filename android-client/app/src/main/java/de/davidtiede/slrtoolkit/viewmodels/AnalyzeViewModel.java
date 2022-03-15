package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.repositories.TaxonomyRepository;
import de.davidtiede.slrtoolkit.repositories.TaxonomyWithEntriesRepository;

public class AnalyzeViewModel extends AndroidViewModel {
    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;
    private int currentRepoId;
    private int parentTaxonomyToDisplayChildrenFor1;
    private int parentTaxonomyToDisplayChildrenFor2;
    private List<TaxonomyWithEntries> childTaxonomiesToDisplay1;
    private List<TaxonomyWithEntries> childTaxonomiesToDisplay2;
    private int currentTaxonomySpinner;

    public AnalyzeViewModel(@NonNull Application application) {
        super(application);
        taxonomyWithEntriesRepository = new TaxonomyWithEntriesRepository(application);
    }

    public List<TaxonomyWithEntries> getChildTaxonomiesToDisplay1() {
        return childTaxonomiesToDisplay1;
    }

    public void setChildTaxonomiesToDisplay1(List<TaxonomyWithEntries> childTaxonomiesToDisplay1) {
        this.childTaxonomiesToDisplay1 = childTaxonomiesToDisplay1;
    }

    public int getCurrentTaxonomySpinner() {
        return currentTaxonomySpinner;
    }

    public void setCurrentTaxonomySpinner(int currentTaxonomySpinner) {
        this.currentTaxonomySpinner = currentTaxonomySpinner;
    }

    public List<TaxonomyWithEntries> getChildTaxonomiesToDisplay2() {
        return childTaxonomiesToDisplay2;
    }

    public void setChildTaxonomiesToDisplay2(List<TaxonomyWithEntries> childTaxonomiesToDisplay2) {
        this.childTaxonomiesToDisplay2 = childTaxonomiesToDisplay2;
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

    public List<TaxonomyWithEntries> getTaxonomiesWithLeafChildTaxonomies(int repoId) throws ExecutionException, InterruptedException {
        return taxonomyWithEntriesRepository.getTaxonomyIdsWithLeafChildTaxonomies(repoId);
    }

    public List<TaxonomyWithEntries> getChildTaxonomiesForTaxonomyId(int repoId, int parentId) throws ExecutionException, InterruptedException {
        return taxonomyWithEntriesRepository.getChildTaxonomiesForTaxonomyId(repoId, parentId);
    }

    public List<TaxonomyWithEntries> getChildTaxonomiesWithAggregatedChildrenForTaxonomyId(int repoId, int parentId) throws ExecutionException, InterruptedException {
        List<TaxonomyWithEntries> childTaxonomies = taxonomyWithEntriesRepository.getChildTaxonomiesForTaxonomyId(repoId, parentId);
        for(TaxonomyWithEntries taxonomy : childTaxonomies) {
            if(taxonomy.taxonomy.isHasChildren()) {
                List<TaxonomyWithEntries> aggregatedChildren = aggregateChildrenOfTaxonomy(repoId, taxonomy);
                List<Entry> entries = getAllEntriesForTaxonomies(aggregatedChildren);
                taxonomy.entries.addAll(entries);
            }
        }

        return childTaxonomies;
    }

    public List<Entry> getAllEntriesForTaxonomies(List<TaxonomyWithEntries> taxonomyWithEntries) {
        List<Entry> entries = new ArrayList<>();

        for(TaxonomyWithEntries taxonomy : taxonomyWithEntries) {
            if(taxonomy.entries.size() > 0) {
                entries.addAll(taxonomy.entries);
            }
        }

        return entries;
    }

    public List<TaxonomyWithEntries> aggregateChildrenOfTaxonomy(int repoId, TaxonomyWithEntries taxonomyWithEntries) throws ExecutionException, InterruptedException {
        List<TaxonomyWithEntries> childTaxonomies = new ArrayList<>();
        if(taxonomyWithEntries.taxonomy.isHasChildren()) {
            List<TaxonomyWithEntries> children = getChildTaxonomiesForTaxonomyId(repoId, taxonomyWithEntries.taxonomy.getTaxonomyId());
            for(TaxonomyWithEntries child: children) {
                childTaxonomies.addAll(aggregateChildrenOfTaxonomy(repoId, child));
            }
        } else {
            childTaxonomies.add(taxonomyWithEntries);
        }
        return childTaxonomies;
    }

    public int getNumberOfEntriesForTaxonomy(List<TaxonomyWithEntries> taxonomyWithEntries) {
        int numberOfEntries = 0;
        for(TaxonomyWithEntries t : taxonomyWithEntries) {
            numberOfEntries += t.entries.size();
        }
        return numberOfEntries;
    }

    public Map<Taxonomy, Integer> getNumberOfEntriesForChildrenOfTaxonomy(int repoId, int parentId) throws ExecutionException, InterruptedException {
        List<TaxonomyWithEntries> taxonomyWithEntries = getChildTaxonomiesForTaxonomyId(repoId, parentId);
        Map<Taxonomy, Integer> numberOfEntriesForTaxonomy = new HashMap<>();
        for(TaxonomyWithEntries taxWithEntries : taxonomyWithEntries) {
            Taxonomy currentTaxonomy = taxWithEntries.taxonomy;
            if(!currentTaxonomy.isHasChildren()) {
                numberOfEntriesForTaxonomy.put(currentTaxonomy, taxWithEntries.entries.size());
            } else {
                List<TaxonomyWithEntries> children = aggregateChildrenOfTaxonomy(repoId, taxWithEntries);
                int numberOfEntries = getNumberOfEntriesForTaxonomy(children);
                numberOfEntriesForTaxonomy.put(currentTaxonomy, numberOfEntries);
            }
        }
        return numberOfEntriesForTaxonomy;
    }
}
