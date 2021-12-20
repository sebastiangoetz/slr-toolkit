package de.davidtiede.slrtoolkit.database;

import androidx.room.Entity;

@Entity(primaryKeys = {"taxonomyId", "id"})
public class EntryTaxonomyCrossRef {
    public int taxonomyId;
    public int id;

    public void setId(int id) {
        this.id = id;
    }

    public void setTaxonomyId(int taxonomyId) {
        this.taxonomyId = taxonomyId;
    }
}
