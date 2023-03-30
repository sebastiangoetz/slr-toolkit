package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.repositories.EntryRepository;
import de.davidtiede.slrtoolkit.repositories.RepoRepository;

public class ProjectViewModel extends AndroidViewModel {
    private final RepoRepository repoRepository;
    private final EntryRepository entryRepository;
    private int currentRepoId;
    private int currentEntryIdForCard;
    private List<Entry> currentEntriesInList;
    private int currentEntryInListCount;

    public ProjectViewModel(@NonNull Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        entryRepository = new EntryRepository(application);
    }

    public LiveData<Repo> getRepoById(int id) {
        return repoRepository.getRepoById(id);
    }

    public int getCurrentEntryInListCount() {
        return currentEntryInListCount;
    }

    public void setCurrentEntryInListCount(int currentEntryInListCount) {
        this.currentEntryInListCount = currentEntryInListCount;
    }

    public void setCurrentEntriesInList(List<Entry> currentEntriesInList) {
        this.currentEntriesInList = currentEntriesInList;
    }

    public List<Entry> getCurrentEntriesInList() {
        return currentEntriesInList;
    }

    public int getCurrentRepoId() {
        return currentRepoId;
    }

    public void setCurrentRepoId(int currentRepoId) {
        this.currentRepoId = currentRepoId;
    }

    public int getCurrentEntryIdForCard() {
        return currentEntryIdForCard;
    }

    public void setCurrentEntryIdForCard(int currentEntryIdForCard) {
        this.currentEntryIdForCard = currentEntryIdForCard;
    }

    public LiveData<Integer> getEntryAmount(int repoId) {
        return entryRepository.getEntryAmountForRepo(repoId);
    }

    public LiveData<Integer> getOpenEntryAmount(int repoId) {
        return entryRepository.getEntryAmountForStatus(repoId, Entry.Status.OPEN);
    }

    public LiveData<List<Entry>> getEntriesForRepo(int repoId) {
        return entryRepository.getEntriesForRepo(repoId);
    }

    public Repo getRepoByIdDirectly(int id) {
        Repo repo = null;
        try {
            repo = repoRepository.getRepoByIdDirectly(id);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return repo;
    }

    public void delete(Entry entry, int id) {
        Repo repo = getRepoByIdDirectly(id);
        if (repo != null) {
            entryRepository.delete(entry, repo);
        }
    }

    public void deleteById(int entryId, int id) {
        Entry entry = getEntryByIdDirectly(entryId);
        if (entry != null) {
            delete(entry, id);
        }
    }

    public LiveData<Entry> getEntryById(int id) {
        return entryRepository.getEntryById(id);
    }

    public Entry getEntryByIdDirectly(int id) {
        Entry entry = null;
        try {
            entry = entryRepository.getEntryByIdDirectly(id);
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
        }
        return entry;
    }

    public LiveData<List<Entry>> getOpenEntriesForRepo(int repoId) {
        return entryRepository.getEntryForRepoByStatus(repoId, Entry.Status.OPEN);
    }

    public void updateEntry(Entry entry) {
        entryRepository.update(entry);
    }

    public LiveData<List<Entry>> getEntriesWithoutTaxonomies(int repoId) {
        return entryRepository.getEntriesWithoutTaxonomies(repoId);
    }

    public LiveData<Integer> getEntriesWithoutTaxonomiesCount(int repoId) {
        return entryRepository.getEntriesWithoutTaxonomiesCount(repoId);
    }
}
