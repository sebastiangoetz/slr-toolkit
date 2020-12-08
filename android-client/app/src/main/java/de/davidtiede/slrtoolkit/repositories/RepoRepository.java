package de.davidtiede.slrtoolkit.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.davidtiede.slrtoolkit.database.AppDatabase;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.database.RepoDao;

/**
 * Abstracted Repository as promoted by the Architecture Guide. https://developer.android.com/topic/libraries/architecture/guide.html
 */

public class RepoRepository {
    private RepoDao repoDao;
    private LiveData<List<Repo>> allRepos;

    RepoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        repoDao = db.repoDao();
        allRepos = repoDao.getAllRepos();
    }

    public LiveData<List<Repo>> getAllRepos() {
        return allRepos;
    }

    public void insert(Repo repo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            repoDao.insert(repo);
        });
    }
}
