package de.davidtiede.slrtoolkit.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.davidtiede.slrtoolkit.database.AppDatabase;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.database.RepoDao;
import de.davidtiede.slrtoolkit.util.BibTexParser;
import de.davidtiede.slrtoolkit.util.FileUtil;

/**
 * Abstracted Repository as promoted by the Architecture Guide. https://developer.android.com/topic/libraries/architecture/guide.html
 */
public class RepoRepository {
    private RepoDao repoDao;
    private LiveData<List<Repo>> allRepos;
    private FileUtil fileUtil;
    private Application application;

    public RepoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.repoDao = db.repoDao();
        this.allRepos = repoDao.getAllRepos();
        this.fileUtil = new FileUtil();
        this.application = application;
    }

    public LiveData<List<Repo>> getAllRepos() {
        return allRepos;
    }

    public LiveData<Repo> getRepoById(int id) {
        return repoDao.getRepoById(id);
    }

    public Repo getRepoByIdDirectly(int id) throws ExecutionException, InterruptedException {
        Callable<Repo> getCallable = () -> repoDao.getRepoByIdDirectly(id);
        Future<Repo> future = Executors.newSingleThreadExecutor().submit(getCallable);
        return future.get();
    }

    public void update(Repo repo) {
        AppDatabase.databaseWriteExecutor.execute(() -> repoDao.update(repo));
    }

    public long insert(Repo repo) {
        Callable<Long> insertCallable = () -> repoDao.insert(repo);
        long id = 0;

        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(insertCallable);
        try {
            id = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void insertEntriesForRepo(int repoId, List<Entry> entries) {
        AppDatabase.databaseWriteExecutor.execute(() -> repoDao.insertEntriesForRepo(repoId, entries));
    }

    public void delete(Repo repo) {
        AppDatabase.databaseWriteExecutor.execute(() -> repoDao.delete(repo));
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
                Entry entry = new Entry(key.toString(), title);
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
