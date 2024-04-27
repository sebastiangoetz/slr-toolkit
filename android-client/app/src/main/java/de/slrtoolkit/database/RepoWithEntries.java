package de.slrtoolkit.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RepoWithEntries {
    @Embedded
    public Repo repo;
    @Relation(
            parentColumn = "id",
            entityColumn = "repoId",
            entity = BibEntry.class
    )
    public List<BibEntry> entries;

}
