package de.slrtoolkit.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.jbibtex.BibTeXObject;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.slrtoolkit.database.AppDatabase;
import de.slrtoolkit.database.Entry;
import de.slrtoolkit.database.EntryDao;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.util.BibTexParser;
import de.slrtoolkit.util.FileUtil;

public class EntryRepository {
    private final EntryDao entryDao;
    final Application application;
    final FileUtil fileUtil;

    public EntryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        entryDao = db.entryDao();
        this.application = application;
        fileUtil = new FileUtil();
    }

    public LiveData<List<Entry>> getEntriesForRepo(int repoId) {
        return entryDao.getEntriesForRepo(repoId);
    }

    public void update(Entry entry) {
        AppDatabase.databaseWriteExecutor.execute(() -> entryDao.update(entry));
    }

    public long insert(Entry entry) {
        Callable<Long> insertCallable = () -> entryDao.insert(entry);
        long id = 0;

        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(insertCallable);
        try {
            id = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void delete(Entry entry, Repo repo) {
        String path = repo.getLocal_path();
        File file = fileUtil.accessFiles(path, application, ".bib");
        try {
            //remove entry from file
            BibTexParser parser = BibTexParser.getBibTexParser();
            parser.setBibTeXDatabase(file);
            Key key = new Key(entry.getKey());
            BibTeXObject entryToDelete = parser.getBibTexObject(key);
            parser.removeObject(entryToDelete);
            //add entry to separate file where deleted entries are stored
            File fileForDeletedEntries = fileUtil.createFileIfNotExists(application, path, "deletedItems.bib");
            parser.setBibTeXDatabase(fileForDeletedEntries);
            parser.addObjectToFile(entryToDelete);
            //remove entry from database
            AppDatabase.databaseWriteExecutor.execute(() -> entryDao.delete(entry));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public LiveData<Integer> getEntryAmountForRepo(int repoId) {
        return entryDao.getEntryAmount(repoId);
    }

    public LiveData<Integer> getEntryAmountForStatus(int repoId, Entry.Status status) {
        int statusCode = status.getCode();
        return entryDao.getEntryAmountForStatus(repoId, statusCode);
    }

    public LiveData<List<Entry>> getEntryForRepoByStatus(int repoId, Entry.Status status) {
        int statusCode = status.getCode();
        return entryDao.getEntryForRepoByStatus(repoId, statusCode);
    }

    public LiveData<Entry> getEntryById(int id) {
        return entryDao.getEntryById(id);
    }

    public void insertEntriesForRepo(int repoId, List<Entry> entries) {
        AppDatabase.databaseWriteExecutor.execute(() -> entryDao.insertEntriesForRepo(repoId, entries));
    }

    public Entry getEntryByRepoAndKeyDirectly(int repoId, String key) throws ExecutionException, InterruptedException {
        Callable<Entry> getCallable = () -> entryDao.getEntryByRepoAndKeyDirectly(repoId, key);
        Future<Entry> future = Executors.newSingleThreadExecutor().submit(getCallable);
        return future.get();
    }

    public LiveData<List<Entry>> getEntriesWithoutTaxonomies(int repoId) {
        return entryDao.getEntriesWithoutTaxonomies(repoId);
    }

    public LiveData<Integer> getEntriesWithoutTaxonomiesCount(int repoId) {
        return entryDao.getEntriesWithoutTaxonomiesCount(repoId);
    }

    public Entry getEntryByIdDirectly(int id) throws ExecutionException, InterruptedException {
        Callable<Entry> getCallable = () -> entryDao.getEntryByIdDirectly(id);
        Future<Entry> future = Executors.newSingleThreadExecutor().submit(getCallable);
        return future.get();
    }
}
