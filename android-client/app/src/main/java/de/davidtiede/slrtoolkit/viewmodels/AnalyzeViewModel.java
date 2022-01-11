package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.repositories.TaxonomyWithEntriesRepository;

public class AnalyzeViewModel extends AndroidViewModel {

    private final TaxonomyWithEntriesRepository taxonomyWithEntriesRepository;

    public AnalyzeViewModel(@NonNull Application application) {
        super(application);
        taxonomyWithEntriesRepository = new TaxonomyWithEntriesRepository(application);
    }

    public Map<Taxonomy, Integer> getNumberOfEntriesForTaxonomy(int repoId) throws ExecutionException, InterruptedException {
        return taxonomyWithEntriesRepository.getNumberOfEntriesForTaxonomy(repoId);
    }
}
