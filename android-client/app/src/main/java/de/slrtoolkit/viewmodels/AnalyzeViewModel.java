package de.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.database.TaxonomyWithEntries;
import de.slrtoolkit.repositories.TaxonomyWithEntriesRepository;

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

    public List<TaxonomyWithEntries> aggregateChildrenOfTaxonomy(int repoId, TaxonomyWithEntries taxonomyWithEntries) throws ExecutionException, InterruptedException {
        List<TaxonomyWithEntries> childTaxonomies = new ArrayList<>();
        if (taxonomyWithEntries.taxonomy.isHasChildren()) {
            List<TaxonomyWithEntries> children = getChildTaxonomiesForTaxonomyId(repoId, taxonomyWithEntries.taxonomy.getTaxonomyId());
            for (TaxonomyWithEntries child : children) {
                childTaxonomies.addAll(aggregateChildrenOfTaxonomy(repoId, child));
            }
        } else {
            childTaxonomies.add(taxonomyWithEntries);
        }
        return childTaxonomies;
    }

    public int getNumberOfEntriesForTaxonomy(List<TaxonomyWithEntries> taxonomyWithEntries) {
        Set<Integer> entries = new HashSet<>();
        for (TaxonomyWithEntries t : taxonomyWithEntries) {
            for (BibEntry bibEntry : t.entries) {
                entries.add(bibEntry.getEntryId());
            }
        }
        return entries.size();
    }

    public List<TaxonomyWithEntries> getTaxonomiesWithAggregatedChildren(int repoId, List<TaxonomyWithEntries> taxonomyWithEntries) throws ExecutionException, InterruptedException {
        List<TaxonomyWithEntries> taxonomiesWithAggregatedChildren = new ArrayList<>();
        for (TaxonomyWithEntries currentTaxonomy : taxonomyWithEntries) {
            if (!currentTaxonomy.taxonomy.isHasChildren()) {
                taxonomiesWithAggregatedChildren.add(currentTaxonomy);
            } else {
                List<TaxonomyWithEntries> children = aggregateChildrenOfTaxonomy(repoId, currentTaxonomy);
                List<BibEntry> entries = new ArrayList<>();
                ArrayList<Integer> entryIds = new ArrayList<>();

                for (TaxonomyWithEntries child : children) {
                    for (BibEntry bibEntry : child.entries) {
                        if (!entryIds.contains(bibEntry.getEntryId())) {
                            entryIds.add(bibEntry.getEntryId());
                            entries.add(bibEntry);
                        }
                    }
                }
                currentTaxonomy.entries = entries;
                taxonomiesWithAggregatedChildren.add(currentTaxonomy);
            }
        }

        return taxonomiesWithAggregatedChildren;
    }

    public Map<Taxonomy, Integer> getNumberOfEntriesForChildrenOfTaxonomy(int repoId, List<TaxonomyWithEntries> taxonomyWithEntries) throws ExecutionException, InterruptedException {
        Map<Taxonomy, Integer> numberOfEntriesForTaxonomy = new HashMap<>();
        for (TaxonomyWithEntries taxWithEntries : taxonomyWithEntries) {
            Taxonomy currentTaxonomy = taxWithEntries.taxonomy;
            if (!currentTaxonomy.isHasChildren()) {
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
