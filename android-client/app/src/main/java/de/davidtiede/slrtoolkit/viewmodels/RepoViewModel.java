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

    private final RepoRepository repoRepository;
    private final LiveData<List<Repo>> allRepos;
    private Repo currentRepo;

    public RepoViewModel(Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        allRepos = repoRepository.getAllRepos();
    }

    public Repo getCurrentRepo() {
        return currentRepo;
    }

    public void setCurrentRepo(Repo currentRepo) {
        this.currentRepo = currentRepo;
    }

    public LiveData<List<Repo>> getAllRepos() {
        return allRepos;
    }

    public void insert(Repo repo) {
        repoRepository.insert(repo);
    }

    public void update(Repo repo) {
        repoRepository.update(repo);
    }
}