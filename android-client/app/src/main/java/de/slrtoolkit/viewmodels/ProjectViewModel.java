package de.slrtoolkit.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.database.Author;
import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.database.Keyword;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.repositories.AuthorRepository;
import de.slrtoolkit.repositories.BibEntryRepository;
import de.slrtoolkit.repositories.KeywordRepository;
import de.slrtoolkit.repositories.RepoRepository;
import de.slrtoolkit.util.BibUtil;
import de.slrtoolkit.util.FileUtil;

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

    public Integer getBibEntryAmountForTaxonomy(int taxId) throws ExecutionException, InterruptedException {
        return bibEntryRepository.getEntryAmountForTaxonomy(taxId);
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
            Log.e(this.getClass().getName(), "couldn't fetch bibentry.", exception);
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

    public void updateBibEntriesAfterPull() {
        int repoId = getCurrentRepoId();
        try {
            Repo repo = repoRepository.getRepoByIdDirectly(repoId);
            BibUtil bu = BibUtil.getInstance();
            FileUtil fu = new FileUtil();
            File bibFile = fu.accessFiles(repo.getLocal_path(), getApplication(), ".bib");
            bu.setBibTeXDatabase(bibFile);
            Map<Key,BibTeXEntry> entriesInFile = bu.getBibTeXEntries();
            bibEntryRepository.deleteAllEntriesOfRepo(repoId);
            for(Key k : entriesInFile.keySet()) {
                BibTeXEntry entry = entriesInFile.get(k);
                if(entry != null) {
                    BibEntry bEntry = bu.translate(entry);
                    bibEntryRepository.insertEntriesForRepo(repoId, List.of(bEntry));
                }
            }
        } catch (ExecutionException | InterruptedException | ParseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMetadataAfterPull() {
        try {
            Repo repo = repoRepository.getRepoByIdDirectly(getCurrentRepoId());
            File f = new FileUtil().accessFiles(repo.getLocal_path(), getApplication(), ".slrproject");
            StringBuilder contents = new StringBuilder();
            Files.readAllLines(f.toPath()).forEach(contents::append);
            String title = contents.substring(contents.indexOf("<title>")+7,contents.indexOf("</title>"));
            String pAbstract = "";
            if(contents.indexOf("<projectAbstract/>") == -1) {
                pAbstract = contents.substring(contents.indexOf("<projectAbstract>")+17, contents.indexOf("</projectAbstract>"));
            }
            repo.setName(title);
            repo.setTextAbstract(pAbstract);
            repoRepository.update(repo);
            List<String> keywords = new ArrayList<>();
            if(contents.indexOf("<keywords/>") == -1) {
                keywords = Arrays.stream(contents.substring(contents.indexOf("<keywords>")+10, contents.indexOf("</keywords>")).split(",")).toList();
            }
            keywordRepository.deleteAllForRepo(repo.getId());
            for(String keyword : keywords) {
                Keyword k = new Keyword(keyword.trim());
                k.setRepoId(repo.getId());
                keywordRepository.insert(k);
            }
            List<String> authors = new ArrayList<>();
            int curPos = 0;
            while(contents.indexOf("<authorsList>",curPos) != -1) {
                authors.add(contents.substring(contents.indexOf("<authorsList>", curPos)+13, contents.indexOf("</authorsList>", curPos)).replace("\n","").trim());
                curPos = contents.indexOf("</authorsList>", curPos)+13;
            }
            authorRepository.deleteAllForRepo(repo.getId());
            for(String author : authors) {
                if(author.contains("<name/>")) continue;
                String name = author.substring(author.indexOf("<name>")+6, author.indexOf("</name>"));
                String email = author.substring(author.indexOf("<email>")+7, author.indexOf("</email>"));
                String organisation = author.substring(author.indexOf("<organisation>")+14, author.indexOf("</organisation>"));
                Author a = new Author(name,organisation,email);
                a.setRepoId(repo.getId());
                authorRepository.insert(a);
            }
            //Toast.makeText(getApplication().getApplicationContext(), title, Toast.LENGTH_LONG).show();
        } catch (ExecutionException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
