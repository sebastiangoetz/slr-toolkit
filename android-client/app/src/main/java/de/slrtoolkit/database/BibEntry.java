package de.slrtoolkit.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class BibEntry {
    private final String key;
    private final String title;
    @PrimaryKey(autoGenerate = true)
    private int entryId;
    private int repoId;
    private String author;
    private String entryAbstract;
    private String year;
    private String month;
    private String journal;
    private String booktitle;
    private String volume;
    private String url;
    private String abstractText;
    private String doi;
    private String keywords;
    private String type;
    private String classes;
    @TypeConverters(StatusConverter.class)
    private Status status;

    public BibEntry(String key, String title) {
        this.key = key;
        this.title = title;
        this.status = Status.OPEN;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getBooktitle() { return booktitle; }

    public void setBooktitle(String booktitle) { this.booktitle = booktitle; }

    public int getRepoId() {
        return repoId;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEntryAbstract() {
        return entryAbstract;
    }

    public void setEntryAbstract(String entryAbstract) {
        this.entryAbstract = entryAbstract;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClasses() { return classes; }

    public void setClasses(String classes) { this.classes = classes; }

    public enum Status {
        OPEN(0), KEEP(1), DISCARD(2);

        private final int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}

