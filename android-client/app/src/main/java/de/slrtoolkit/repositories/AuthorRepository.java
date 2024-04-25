package de.slrtoolkit.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.slrtoolkit.database.AppDatabase;
import de.slrtoolkit.database.Author;
import de.slrtoolkit.database.AuthorDao;

public class AuthorRepository {
    private final AuthorDao authorDao;
    final Application application;

    public AuthorRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        authorDao = db.authorDao();
        this.application = application;
    }

    public LiveData<List<Author>> getAuthorsForRepo(int repoId) {
        return authorDao.getAuthorsForRepo(repoId);
    }

    public void update(Author author) {
        AppDatabase.databaseWriteExecutor.execute(() -> authorDao.update(author));
    }

    public long insert(Author author) {
        Callable<Long> insertCallable = () -> authorDao.insert(author);
        long id = 0;

        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(insertCallable);
        try {
            id = future.get();
        } catch(InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void deleteAsync(Author author, OnDeleteCompleteListener listener) {
        Runnable deleteRunnable = () -> {
            authorDao.delete(author);
            listener.onDeleteComplete();
        };
        AppDatabase.databaseWriteExecutor.execute(deleteRunnable);
    }

    public LiveData<Author> getAuthorById(int authorId) {
        return authorDao.getAuthorById(authorId);
    }
}
