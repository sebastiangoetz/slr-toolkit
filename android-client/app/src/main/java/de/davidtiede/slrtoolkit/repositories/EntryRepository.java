package de.davidtiede.slrtoolkit.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXObject;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.davidtiede.slrtoolkit.database.AppDatabase;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.EntryDao;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.util.BibTexParser;
import de.davidtiede.slrtoolkit.util.FileUtil;

public class EntryRepository {
    private EntryDao entryDao;
    Application application;
    FileUtil fileUtil;

    public EntryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        entryDao = db.entryDao();
        this.application  = application;
        fileUtil = new FileUtil();
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
        File file = fileUtil.accessFiles(path, application, ".bib");
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

    public void saveAll(List<Entry> entries) {
        AppDatabase.databaseWriteExecutor.execute(() -> entryDao.insertAll(entries));
    }

    public void insertEntriesForRepo(int repoId, List<Entry> entries) {
        AppDatabase.databaseWriteExecutor.execute(() -> entryDao.insertEntriesForRepo(repoId, entries));
    }

    public void initializeEntries(int repoId, String path) {
        File file = fileUtil.accessFiles(path, application, ".bib");
        try {
            BibTexParser parser = BibTexParser.getBibTexParser();
            parser.setBibTeXDatabase(file);
            Map<Key, BibTeXEntry> entryMap = parser.getBibTeXEntries();
            List<Entry> entries = new ArrayList<>();

            for(Key key: entryMap.keySet()) {
                BibTeXEntry bibTeXEntry = entryMap.get(key);
                String title = bibTeXEntry.getField(BibTeXEntry.KEY_TITLE).toUserString();
                String author = bibTeXEntry.getField(BibTeXEntry.KEY_AUTHOR).toUserString();
                String year = bibTeXEntry.getField(BibTeXEntry.KEY_YEAR).toUserString();
                String month = bibTeXEntry.getField(BibTeXEntry.KEY_MONTH).toUserString();
                String journal = bibTeXEntry.getField(BibTeXEntry.KEY_JOURNAL).toUserString();
                String volume = bibTeXEntry.getField(BibTeXEntry.KEY_VOLUME).toUserString();
                String url = bibTeXEntry.getField(BibTeXEntry.KEY_URL).toUserString();
                Key KEY_CLASSES = new Key("classes");
                String classes = bibTeXEntry.getField(KEY_CLASSES).toUserString();
                Entry entry = new Entry(key.toString(), title);
                entry.setAuthor(author);
                entry.setYear(year);
                entry.setMonth(month);
                entries.add(entry);
            }
            insertEntriesForRepo(repoId, entries);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
