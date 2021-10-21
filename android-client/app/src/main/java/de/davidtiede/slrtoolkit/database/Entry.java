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
    private int year;
    private int month;
    @TypeConverters(StatusConverter.class)
    private Status status;

    public Entry(String key, String title, String author, String entryAbstract, int year, int month) {
        this.key = key;
        this.title = title;
        this.author = author;
        this.entryAbstract = entryAbstract;
        this.year = year;
        this.month = month;
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

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public Status getStatus() {
        return status;
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

