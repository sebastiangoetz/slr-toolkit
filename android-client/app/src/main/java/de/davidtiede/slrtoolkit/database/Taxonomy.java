package de.davidtiede.slrtoolkit.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Taxonomy {
    @PrimaryKey(autoGenerate = true) private int taxonomyId;
    private String name;
    private int repoId;
    private int parentId;

    public int getParentId() {
        return parentId;
    }

    public int getTaxonomyId() {
        return taxonomyId;
    }

    public int getRepoId() {
        return repoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setTaxonomyId(int id) {
        this.taxonomyId = id;
    }
}
