package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.repositories.RepoRepository;

/**
 * View Model to keep a reference to the repo repository.
 */

public class RepoViewModel extends AndroidViewModel {

    private RepoRepository repoRepository;
    private final LiveData<List<Repo>> allRepos;

    public RepoViewModel(Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        allRepos = repoRepository.getAllRepos();
    }

    public LiveData<List<Repo>> getAllRepos() {
        return allRepos;
    }

    public void insert(Repo repo) {
        repoRepository.insert(repo);
    }
}