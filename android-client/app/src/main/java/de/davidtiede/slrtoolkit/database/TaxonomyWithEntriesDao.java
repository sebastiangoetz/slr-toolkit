package de.davidtiede.slrtoolkit.database;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface TaxonomyWithEntriesDao {
    @Insert
    public abstract long insert(EntityTaxonomyCrossRef entityTaxonomyCrossRef);
}
