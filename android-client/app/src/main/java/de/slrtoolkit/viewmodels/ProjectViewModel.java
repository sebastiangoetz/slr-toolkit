package de.slrtoolkit.viewmodels;

import android.app.Application;
import android.util.Log;

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
    private int currentBibEntryIdForCard;
    private List<BibEntry> currentBibEntriesInList;
    private int currentBibEntryInListCount;

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
    public int getCurrentBibEntryInListCount() {
        return currentBibEntryInListCount;
    }

    public void setCurrentBibEntryInListCount(int currentEntryInListCount) {
        this.currentBibEntryInListCount = currentEntryInListCount;
    }

    public List<BibEntry> getCurrentBibEntriesInList() {
        return currentBibEntriesInList;
    }

    public void setCurrentBibEntriesInList(List<BibEntry> currentEntriesInList) {
        this.currentBibEntriesInList = currentEntriesInList;
    }

    public int getCurrentRepoId() {
        return currentRepoId;
    }

    public void setCurrentRepoId(int currentRepoId) {
        this.currentRepoId = currentRepoId;
    }

    public int getCurrentBibEntryIdForCard() {
        return currentBibEntryIdForCard;
    }

    public void setCurrentBibEntryIdForCard(int currentEntryIdForCard) {
        this.currentBibEntryIdForCard = currentEntryIdForCard;
    }

    public LiveData<Integer> getBibEntryAmount(int repoId) {
        return bibEntryRepository.getEntryAmountForRepo(repoId);
    }

    public LiveData<Integer> getOpenBibEntryAmount(int repoId) {
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
            Log.e(ProjectViewModel.class.getName(), "getRepoByIdDirectly: ", e);
        }
        return repo;
    }

    public void deleteBibEntry(BibEntry bibEntry, int repoId) {
        Repo repo = getRepoByIdDirectly(repoId);
        if (repo != null) {
            bibEntryRepository.delete(bibEntry, repo);
        }
    }

    public void addBibEntry(String bibtex, int repoId) {
        Repo repo = getRepoByIdDirectly(repoId);
        bibEntryRepository.insert(bibtex, repo);
    }

    public void deleteBibEntryById(int entryId, int repoId) {
        BibEntry bibEntry = getBibEntryByIdDirectly(entryId);
        if (bibEntry != null) {
            deleteBibEntry(bibEntry, repoId);
        }
    }

    public LiveData<BibEntry> getBibEntryById(int id) {
        return bibEntryRepository.getEntryById(id);
    }

    public BibEntry getBibEntryByIdDirectly(int id) {
        BibEntry bibEntry = null;
        try {
            bibEntry = bibEntryRepository.getEntryByIdDirectly(id);
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
        }
        return bibEntry;
    }

    public LiveData<List<BibEntry>> getOpenBibEntriesForRepo(int repoId) {
        return bibEntryRepository.getEntryForRepoByStatus(repoId, BibEntry.Status.OPEN);
    }

    public void updateBibEntry(BibEntry bibEntry, Repo repo) {
        bibEntryRepository.update(bibEntry, repo);
    }

    public LiveData<List<BibEntry>> getBibEntriesWithoutTaxonomies(int repoId) {
        return bibEntryRepository.getEntriesWithoutTaxonomies(repoId);
    }

    public LiveData<Integer> getBibEntriesWithoutTaxonomiesCount(int repoId) {
        return bibEntryRepository.getEntriesWithoutTaxonomiesCount(repoId);
    }
}
