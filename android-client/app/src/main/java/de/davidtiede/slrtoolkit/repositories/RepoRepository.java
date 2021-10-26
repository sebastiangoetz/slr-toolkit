package de.davidtiede.slrtoolkit.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.davidtiede.slrtoolkit.database.AppDatabase;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.database.RepoDao;

/**
 * Abstracted Repository as promoted by the Architecture Guide. https://developer.android.com/topic/libraries/architecture/guide.html
 */
public class RepoRepository {
    private RepoDao repoDao;
    private LiveData<List<Repo>> allRepos;

    public RepoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        repoDao = db.repoDao();
        allRepos = repoDao.getAllRepos();
    }

    public LiveData<List<Repo>> getAllRepos() {
        return allRepos;
    }

    public LiveData<Repo> getRepoById(int id) {
        return repoDao.getRepoById(id);
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
}
