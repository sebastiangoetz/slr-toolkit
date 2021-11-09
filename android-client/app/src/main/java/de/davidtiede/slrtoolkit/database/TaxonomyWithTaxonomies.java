package de.davidtiede.slrtoolkit.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TaxonomyWithTaxonomies {
    @Embedded Taxonomy parent;

    @Relation(
            parentColumn = "taxonomyId",
            entityColumn = "parentId",
            entity = Taxonomy.class
    )
    List<Taxonomy> children;
}
