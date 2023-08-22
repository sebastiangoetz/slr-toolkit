package de.slrtoolkit.database;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"taxonomyId", "entryId"}, indices = {
        @Index(value = "entryId")
})
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
