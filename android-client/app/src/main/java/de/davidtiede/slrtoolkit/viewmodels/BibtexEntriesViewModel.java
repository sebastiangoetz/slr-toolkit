package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.repositories.EntryRepository;
import de.davidtiede.slrtoolkit.repositories.RepoRepository;

public class BibtexEntriesViewModel extends AndroidViewModel {
    private final RepoRepository repoRepository;
    private final EntryRepository entryRepository;

    public BibtexEntriesViewModel(Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        entryRepository = new EntryRepository(application);
    }

    public void insertEntries(List<Entry> entries, int repoId) {
        repoRepository.insertEntriesForRepo(repoId, entries);
    }

    public LiveData<List<Entry>> getEntriesForRepo(int repoId) {
        return entryRepository.getEntryForRepo(repoId);
    }

    public LiveData<Repo> getRepoById(int id) {
        return repoRepository.getRepoById(id);
    }

    public void delete(Entry entry) {
        entryRepository.delete(entry);
    }
}
