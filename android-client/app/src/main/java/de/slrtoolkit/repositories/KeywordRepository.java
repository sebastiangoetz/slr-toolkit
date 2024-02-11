package de.slrtoolkit.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.slrtoolkit.database.AppDatabase;
import de.slrtoolkit.database.Keyword;
import de.slrtoolkit.database.KeywordDao;

public class KeywordRepository {
    private final KeywordDao keywordDao;

    Application application;

    public KeywordRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        keywordDao = db.keywordDao();
        this.application = application;
    }

    public long insert(Keyword keyword) {
        Callable<Long> insertCallable = () -> keywordDao.insert(keyword);
        long id = 0;

        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(insertCallable);
        try {
            id = future.get();
        } catch(InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public LiveData<List<Keyword>> getKeywordsForRepo(int repoId) {
        return keywordDao.getKeywordsForRepo(repoId);
    }

    public LiveData<Keyword> getKeywordById(int id) {
        return keywordDao.getKeywordById(id);
    }


}
