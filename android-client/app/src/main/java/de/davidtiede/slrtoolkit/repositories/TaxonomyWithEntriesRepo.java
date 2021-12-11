package de.davidtiede.slrtoolkit.repositories;

import android.app.Application;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.davidtiede.slrtoolkit.database.AppDatabase;
import de.davidtiede.slrtoolkit.database.EntityTaxonomyCrossRef;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntriesDao;
import de.davidtiede.slrtoolkit.util.FileUtil;

public class TaxonomyWithEntriesRepo {
    TaxonomyWithEntriesDao taxonomyWithEntriesDao;
    Application application;

    public TaxonomyWithEntriesRepo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taxonomyWithEntriesDao = db.taxonomyWithEntriesDao();
        this.application  = application;
    }

    public long insert(int taxonomyId, int entryId) {
        EntityTaxonomyCrossRef entityTaxonomyCrossRef = new EntityTaxonomyCrossRef();
        entityTaxonomyCrossRef.setTaxonomyId(taxonomyId);
        entityTaxonomyCrossRef.setId(entryId);
        Callable<Long> insertCallable = () -> taxonomyWithEntriesDao.insert(entityTaxonomyCrossRef);
        long id = 0;

        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(insertCallable);
        try {
            id = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return id;

    }

    public void delete(int taxonomyId, int entryId) {
        EntityTaxonomyCrossRef entityTaxonomyCrossRef = new EntityTaxonomyCrossRef();
        entityTaxonomyCrossRef.setTaxonomyId(taxonomyId);
        entityTaxonomyCrossRef.setId(entryId);
        AppDatabase.databaseWriteExecutor.execute(() -> taxonomyWithEntriesDao.delete(entityTaxonomyCrossRef));
    }
}
