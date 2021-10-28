package de.davidtiede.slrtoolkit.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.jbibtex.BibTeXObject;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.davidtiede.slrtoolkit.database.AppDatabase;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.EntryDao;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.util.BibTexParser;

public class EntryRepository {
    private EntryDao entryDao;
    Application application;

    public EntryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        entryDao = db.entryDao();
        this.application  = application;
    }


    public LiveData<List<Entry>> getEntryForRepo(int id) {
        return entryDao.getEntriesForRepo(id);
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
        File file = accessFiles(path);
        try {
            BibTexParser parser = BibTexParser.getBibTexParser();
            parser.setBibTeXDatabase(file);
            Key key = new Key(entry.getKey());
            BibTeXObject entryToDelete =  parser.getBibTexObject(key);
            parser.removeObject(entryToDelete);
            AppDatabase.databaseWriteExecutor.execute(() -> entryDao.delete(entry));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public LiveData<Entry> getEntryByKey(int repoId, String key) {
        return entryDao.getEntryByKey(repoId, key);
    }

    public LiveData<Integer> getEntryAmountForRepo(int repoId) {
        return entryDao.getEntryAmount(repoId);
    }

    public LiveData<List<Entry>> getEntryForRepoByStatus(int repoId, Entry.Status status) {
        int statusCode = status.getCode();
        return entryDao.getEntryForRepoByStatus(repoId, statusCode);
    }

    private File accessFiles(String path) {
        File directoryPath = new File(application.getApplicationContext().getFilesDir(), path);
        File[] files = directoryPath.listFiles();
        File bibFile = null;
        for(File file: files) {
            if(file.isDirectory()) {
                for(File f: file.listFiles()) {
                    if(f.getName().endsWith(".bib")) {
                        bibFile = f;
                    }
                }
            }
        }
        return bibFile;
    }
}
