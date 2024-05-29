package de.slrtoolkit.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXObject;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.slrtoolkit.database.AppDatabase;
import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.database.BibEntryDao;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.util.BibUtil;
import de.slrtoolkit.util.FileUtil;

public class BibEntryRepository {
    private final BibEntryDao bibEntryDao;
    final Application application;
    final FileUtil fileUtil;

    public BibEntryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        bibEntryDao = db.entryDao();
        this.application = application;
        fileUtil = new FileUtil();
    }

    public LiveData<List<BibEntry>> getEntriesForRepo(int repoId) {
        return bibEntryDao.getEntriesForRepo(repoId);
    }

    public void update(BibEntry bibEntry, Repo repo) {
        AppDatabase.databaseWriteExecutor.execute(() -> bibEntryDao.update(bibEntry));
        BibUtil bibUtil = BibUtil.getInstance();
        String path = repo.getLocal_path();
        File file = fileUtil.accessFiles(path, application, ".bib");
        try {
            bibUtil.setBibTeXDatabase(file);
            bibUtil.update(bibEntry);
        } catch (FileNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public void insert(String bibtexString, Repo repo) {
        String path = repo.getLocal_path();
        File file = fileUtil.accessFiles(path, application, ".bib");
        Map<String,BibTeXEntry> parsedObjects = new HashMap<>();
        BibUtil bibUtil = BibUtil.getInstance();
        try {
            //add entry to file
            bibUtil.setBibTeXDatabase(file);
            //transform bibEntry to bibtex
            BibTeXDatabase db = bibUtil.parse(bibtexString);
            for(Key k : db.getEntries().keySet()) {
                parsedObjects.put(k.getValue(),db.resolveEntry(k));
            }
        } catch (ParseException | IOException e) {
            Log.e(BibEntryRepository.class.getName(), "delete: failed", e);
        }

        for(String key : parsedObjects.keySet()) {
            BibTeXEntry entry = parsedObjects.get(key);
            BibEntry bibEntry = bibUtil.translate(entry);
            bibEntry.setRepoId(repo.getId());
            bibUtil.addObjectToFile(entry);
            Callable<Long> insertCallable = () -> bibEntryDao.insert(bibEntry);
            AppDatabase.databaseWriteExecutor.submit(insertCallable);
        }
    }

    public void delete(BibEntry bibEntry, Repo repo) {
        String path = repo.getLocal_path();
        File file = fileUtil.accessFiles(path, application, ".bib");
        try {
            //remove entry from file
            BibUtil parser = BibUtil.getInstance();
            parser.setBibTeXDatabase(file);
            Key key = new Key(bibEntry.getKey());
            BibTeXObject entryToDelete = parser.getBibTexObject(key);
            parser.removeObject(entryToDelete);
            //add entry to separate file where deleted entries are stored
            File fileForDeletedEntries = fileUtil.createFileIfNotExists(application, path, "deletedItems.bib");
            parser.setBibTeXDatabase(fileForDeletedEntries);
            parser.addObjectToFile(entryToDelete);
            //remove entry from database
            AppDatabase.databaseWriteExecutor.execute(() -> bibEntryDao.delete(bibEntry));
        } catch (ParseException | IOException e) {
            Log.e(BibEntryRepository.class.getName(), "delete: failed", e);
        }
    }

    public LiveData<Integer> getEntryAmountForRepo(int repoId) {
        return bibEntryDao.getEntryAmount(repoId);
    }

    public LiveData<Integer> getEntryAmountForStatus(int repoId, BibEntry.Status status) {
        int statusCode = status.getCode();
        return bibEntryDao.getEntryAmountForStatus(repoId, statusCode);
    }

    public LiveData<List<BibEntry>> getEntryForRepoByStatus(int repoId, BibEntry.Status status) {
        int statusCode = status.getCode();
        return bibEntryDao.getEntryForRepoByStatus(repoId, statusCode);
    }

    public LiveData<BibEntry> getEntryById(int id) {
        return bibEntryDao.getEntryById(id);
    }

    public void insertEntriesForRepo(int repoId, List<BibEntry> entries) {
        AppDatabase.databaseWriteExecutor.execute(() -> bibEntryDao.insertEntriesForRepo(repoId, entries));
    }

    public BibEntry getEntryByRepoAndKeyDirectly(int repoId, String key) throws ExecutionException, InterruptedException {
        Callable<BibEntry> getCallable = () -> bibEntryDao.getEntryByRepoAndKeyDirectly(repoId, key);
        Future<BibEntry> future = Executors.newSingleThreadExecutor().submit(getCallable);
        return future.get();
    }

    public LiveData<List<BibEntry>> getEntriesWithoutTaxonomies(int repoId) {
        return bibEntryDao.getEntriesWithoutTaxonomies(repoId);
    }

    public LiveData<Integer> getEntriesWithoutTaxonomiesCount(int repoId) {
        return bibEntryDao.getEntriesWithoutTaxonomiesCount(repoId);
    }

    public BibEntry getEntryByIdDirectly(int id) throws ExecutionException, InterruptedException {
        Callable<BibEntry> getCallable = () -> bibEntryDao.getEntryByIdDirectly(id);
        Future<BibEntry> future = Executors.newSingleThreadExecutor().submit(getCallable);
        return future.get();
    }
}
