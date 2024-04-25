package de.slrtoolkit.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.slrtoolkit.database.AppDatabase;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.database.RepoDao;

/**
 * Abstracted Repository as promoted by the Architecture Guide. <a href="https://developer.android.com/topic/libraries/architecture/guide.html">...</a>
 */
public class RepoRepository {
    private final RepoDao repoDao;
    private final LiveData<List<Repo>> allRepos;

    public RepoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.repoDao = db.repoDao();
        this.allRepos = repoDao.getAllRepos();
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

    public void delete(Repo repo) {
        AppDatabase.databaseWriteExecutor.execute(() -> repoDao.delete(repo));
    }
}
