package de.davidtiede.slrtoolkit.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.davidtiede.slrtoolkit.database.AppDatabase;
import de.davidtiede.slrtoolkit.database.EntryTaxonomyCrossRef;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntriesDao;

public class TaxonomyWithEntriesRepository {
    TaxonomyWithEntriesDao taxonomyWithEntriesDao;
    Application application;

    public TaxonomyWithEntriesRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taxonomyWithEntriesDao = db.taxonomyWithEntriesDao();
        this.application = application;
    }

    public long insert(int taxonomyId, int entryId) {
        EntryTaxonomyCrossRef entryTaxonomyCrossRef = new EntryTaxonomyCrossRef();
        entryTaxonomyCrossRef.setTaxonomyId(taxonomyId);
        entryTaxonomyCrossRef.setEntryId(entryId);
        Callable<Long> insertCallable = () -> taxonomyWithEntriesDao.insert(entryTaxonomyCrossRef);
        long id = 0;

        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(insertCallable);
        try {
            id = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return id;

    }

    public void insertAll(List<EntryTaxonomyCrossRef> entryTaxonomyCrossRefs) {
        AppDatabase.databaseWriteExecutor.execute(() -> taxonomyWithEntriesDao.insertAll(entryTaxonomyCrossRefs));
    }

    public void delete(int taxonomyId, int entryId) {
        EntryTaxonomyCrossRef entryTaxonomyCrossRef = new EntryTaxonomyCrossRef();
        entryTaxonomyCrossRef.setTaxonomyId(taxonomyId);
        entryTaxonomyCrossRef.setEntryId(entryId);
        AppDatabase.databaseWriteExecutor.execute(() -> taxonomyWithEntriesDao.delete(entryTaxonomyCrossRef));
    }

    public List<TaxonomyWithEntries> getChildTaxonomiesForTaxonomyId(int repoId, int parentId) throws ExecutionException, InterruptedException {
        Callable<List<TaxonomyWithEntries>> getCallable = () -> taxonomyWithEntriesDao.getChildTaxonomiesForTaxonomyId(repoId, parentId);
        Future<List<TaxonomyWithEntries>> future = Executors.newSingleThreadExecutor().submit(getCallable);
        return future.get();
    }

    public List<TaxonomyWithEntries> getTaxonomyIdsWithLeafChildTaxonomies(int repoId) throws ExecutionException, InterruptedException {
        Callable<List<TaxonomyWithEntries>> getCallable = () -> taxonomyWithEntriesDao.getTaxonomyIdsWithLeafChildTaxonomies(repoId);
        Future<List<TaxonomyWithEntries>> future = Executors.newSingleThreadExecutor().submit(getCallable);
        return future.get();
    }

    public LiveData<List<TaxonomyWithEntries>> getChildTaxonomiesWithEntries(int repoId, int taxonomyId) {
        return taxonomyWithEntriesDao.getChildTaxonomiesWithEntries(repoId, taxonomyId);
    }

    public LiveData<TaxonomyWithEntries> getTaxonomyWithEntries(int repoId, int taxonomyId) {
        return taxonomyWithEntriesDao.getTaxonomyWithEntries(repoId, taxonomyId);
    }
}
