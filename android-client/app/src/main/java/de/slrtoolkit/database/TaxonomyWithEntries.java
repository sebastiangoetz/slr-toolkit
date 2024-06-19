package de.slrtoolkit.database;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class TaxonomyWithEntries {
    @Embedded
    public Taxonomy taxonomy;
    @Relation(
            parentColumn = "taxonomyId",
            entityColumn = "entryId",
            associateBy = @Junction(BibEntryTaxonomyCrossRef.class)
    )
    public List<BibEntry> entries;
}
