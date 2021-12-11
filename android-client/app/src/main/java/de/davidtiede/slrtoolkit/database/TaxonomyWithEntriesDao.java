package de.davidtiede.slrtoolkit.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface TaxonomyWithEntriesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(EntityTaxonomyCrossRef entityTaxonomyCrossRef);

    @Delete
    void delete(EntityTaxonomyCrossRef entityTaxonomyCrossRef);
}
