package de.davidtiede.slrtoolkit.database;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class EntryWithTaxonomies {
    @Embedded
    public Entry entry;
    @Relation(
            parentColumn = "entryId",
            entityColumn = "taxonomyId",
            associateBy = @Junction(EntryTaxonomyCrossRef.class)
    )
    public List<Taxonomy> taxonomies;
}
