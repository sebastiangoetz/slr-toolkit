package de.davidtiede.slrtoolkit.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RepoWithTaxonomy {
    @Embedded
    Repo repo;

    @Relation(
            parentColumn = "id",
            entityColumn = "repoId",
            entity = Taxonomy.class
    )
    List<Taxonomy> taxonomies;
}
