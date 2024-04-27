package de.slrtoolkit.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RepoWithEntriesKeywordsAndAuthors {
    @Embedded
    public Repo repo;
    @Relation(
            parentColumn = "id",
            entityColumn = "repoId",
            entity = BibEntry.class
    )
    public List<BibEntry> entries;

    @Relation(
            parentColumn = "id",
            entityColumn = "repoId",
            entity = Keyword.class
    )
    public List<Keyword> keywords;

    @Relation(
            parentColumn = "id",
            entityColumn = "repoId",
            entity = Author.class
    )
    public List<Author> authors;
}
