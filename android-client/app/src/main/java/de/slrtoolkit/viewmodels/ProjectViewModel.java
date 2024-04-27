package de.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.database.Author;
import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.database.Keyword;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.repositories.AuthorRepository;
import de.slrtoolkit.repositories.BibEntryRepository;
import de.slrtoolkit.repositories.KeywordRepository;
import de.slrtoolkit.repositories.RepoRepository;

public class ProjectViewModel extends AndroidViewModel {
    private final RepoRepository repoRepository;
    private final BibEntryRepository bibEntryRepository;
    private final AuthorRepository authorRepository;
    private final KeywordRepository keywordRepository;
    private int currentRepoId;
    private int currentEntryIdForCard;
    private List<BibEntry> currentEntriesInList;
    private int currentEntryInListCount;

    public ProjectViewModel(@NonNull Application application) {
        super(application);
        repoRepository = new RepoRepository(application);
        bibEntryRepository = new BibEntryRepository(application);
        authorRepository = new AuthorRepository(application);
        keywordRepository = new KeywordRepository(application);
    }

    public LiveData<Repo> getRepoById(int id) {
        return repoRepository.getRepoById(id);
    }

    public LiveData<List<Keyword>> getKeywordsForCurrentProject() {
        return keywordRepository.getKeywordsForRepo(currentRepoId);
    }

    public void deleteKeyword(Keyword keyword){
        keywordRepository.delete(keyword);
    }

    public LiveData<List<Author>> getAuthorsForCurrentProject() {
        return authorRepository.getAuthorsForRepo(currentRepoId);
    }
    public int getCurrentEntryInListCount() {
        return currentEntryInListCount;
    }

    public void setCurrentEntryInListCount(int currentEntryInListCount) {
        this.currentEntryInListCount = currentEntryInListCount;
    }

    public List<BibEntry> getCurrentEntriesInList() {
        return currentEntriesInList;
    }

    public void setCurrentEntriesInList(List<BibEntry> currentEntriesInList) {
        this.currentEntriesInList = currentEntriesInList;
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
        return bibEntryRepository.getEntryAmountForRepo(repoId);
    }

    public LiveData<Integer> getOpenEntryAmount(int repoId) {
        return bibEntryRepository.getEntryAmountForStatus(repoId, BibEntry.Status.OPEN);
    }

    public LiveData<List<BibEntry>> getEntriesForRepo(int repoId) {
        return bibEntryRepository.getEntriesForRepo(repoId);
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

    public void delete(BibEntry bibEntry, int id) {
        Repo repo = getRepoByIdDirectly(id);
        if (repo != null) {
            bibEntryRepository.delete(bibEntry, repo);
        }
    }

    public void deleteById(int entryId, int id) {
        BibEntry bibEntry = getEntryByIdDirectly(entryId);
        if (bibEntry != null) {
            delete(bibEntry, id);
        }
    }

    public LiveData<BibEntry> getEntryById(int id) {
        return bibEntryRepository.getEntryById(id);
    }

    public BibEntry getEntryByIdDirectly(int id) {
        BibEntry bibEntry = null;
        try {
            bibEntry = bibEntryRepository.getEntryByIdDirectly(id);
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
        }
        return bibEntry;
    }

    public LiveData<List<BibEntry>> getOpenEntriesForRepo(int repoId) {
        return bibEntryRepository.getEntryForRepoByStatus(repoId, BibEntry.Status.OPEN);
    }

    public void updateEntry(BibEntry bibEntry) {
        bibEntryRepository.update(bibEntry);
    }

    public LiveData<List<BibEntry>> getEntriesWithoutTaxonomies(int repoId) {
        return bibEntryRepository.getEntriesWithoutTaxonomies(repoId);
    }

    public LiveData<Integer> getEntriesWithoutTaxonomiesCount(int repoId) {
        return bibEntryRepository.getEntriesWithoutTaxonomiesCount(repoId);
    }
}
