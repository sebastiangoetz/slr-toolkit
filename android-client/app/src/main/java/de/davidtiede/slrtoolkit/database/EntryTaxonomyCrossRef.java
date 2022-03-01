package de.davidtiede.slrtoolkit.database;

import androidx.room.Entity;

@Entity(primaryKeys = {"taxonomyId", "entryId"})
public class EntryTaxonomyCrossRef {
    public int taxonomyId;
    public int entryId;

    public void setEntryId(int id) {
        this.entryId = id;
    }

    public void setTaxonomyId(int taxonomyId) {
        this.taxonomyId = taxonomyId;
    }
}
