package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.repositories.EntryRepository;
import de.davidtiede.slrtoolkit.repositories.RepoRepository;

public class ProjectViewModel extends AndroidViewModel {
    private final RepoRepository repoRepository;
    private final EntryRepository entryRepository;

    public ProjectViewModel(@NonNull Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        entryRepository = new EntryRepository(application);
    }

    public LiveData<Repo> getRepoById(int id) {
        return repoRepository.getRepoById(id);
    }

    public LiveData<Integer> getEntryAmount(int repoId) {
        return entryRepository.getEntryAmountForRepo(repoId);
    }
}
