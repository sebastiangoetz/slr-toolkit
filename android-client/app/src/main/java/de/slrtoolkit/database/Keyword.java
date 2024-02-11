package de.slrtoolkit.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Keyword {
    @PrimaryKey(autoGenerate = true)
    private int keywordId;

    private String name;
    private int repoId;

    public Keyword(String name) {
        this.name = name;
    }

    public int getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(int keywordId) {
        this.keywordId = keywordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRepoId() {
        return repoId;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }
}
