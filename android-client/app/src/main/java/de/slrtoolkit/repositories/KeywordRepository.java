package de.slrtoolkit.repositories;

import android.app.Application;
import android.util.Log;

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

    final Application application;

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
            Log.e("KeywordRepository", "insert: ", e);
        }
        return id;
    }

    public void delete(Keyword keyword) {
        keywordDao.delete(keyword);
    }

    public void deleteAsync(Keyword keyword, OnDeleteCompleteListener listener) {
        Runnable deleteRunnable = () -> {
            keywordDao.delete(keyword);
            listener.onDeleteComplete();
        };
        AppDatabase.databaseWriteExecutor.execute(deleteRunnable);
    }

    public void deleteAllForRepo(int repoId) {
        Runnable deleteRunnable = () -> {
            keywordDao.deleteAllForRepo(repoId);
        };
        AppDatabase.databaseWriteExecutor.execute(deleteRunnable);
    }


    public LiveData<List<Keyword>> getKeywordsForRepo(int repoId) {
        return keywordDao.getKeywordsForRepo(repoId);
    }

    public LiveData<Keyword> getKeywordById(int id) {
        return keywordDao.getKeywordById(id);
    }


}
