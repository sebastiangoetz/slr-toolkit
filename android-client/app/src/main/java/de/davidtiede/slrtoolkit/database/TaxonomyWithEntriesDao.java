package de.davidtiede.slrtoolkit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface TaxonomyWithEntriesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(EntryTaxonomyCrossRef entryTaxonomyCrossRef);

    @Insert
    void insertAll(List<EntryTaxonomyCrossRef> entryTaxonomyCrossRef);

    @Delete
    void delete(EntryTaxonomyCrossRef entryTaxonomyCrossRef);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId AND parentId=:parentId")
    List<TaxonomyWithEntries> getTaxonomyWithEntriesDirectly(int repoId, int parentId);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId")
    List<TaxonomyWithEntries> getTaxonomiesWithEntriesDirectly(int repoId);
}
