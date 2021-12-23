package de.davidtiede.slrtoolkit.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class Entry {
    @PrimaryKey(autoGenerate = true) private int id;
    private int repoId;
    private String key;
    private String title;
    private String author;
    private String entryAbstract;
    private String year;
    private String month;
    private String journal;
    private String volume;
    private String url;
    private String abstractText;
    private String doi;
    private String keywords;
    @TypeConverters(StatusConverter.class)
    private Status status;

    public Entry(String key, String title) {
        this.key = key;
        this.title = title;
        this.status = Status.OPEN;
    }

    public int getId() {
        return id;
    }

    public int getRepoId() {
        return repoId;
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

    public String getEntryAbstract() {
        return entryAbstract;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public Status getStatus() {
        return status;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public String getJournal() {
        return journal;
    }

    public String getUrl() {
        return url;
    }

    public String getVolume() {
        return volume;
    }

    public String getDoi() {
        return doi;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setEntryAbstract(String entryAbstract) {
        this.entryAbstract = entryAbstract;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public enum Status {
        OPEN(0),
        KEEP(1),
        DISCARD(2);

        private int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}

