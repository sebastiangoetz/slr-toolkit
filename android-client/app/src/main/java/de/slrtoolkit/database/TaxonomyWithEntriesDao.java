package de.slrtoolkit.database;

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
    long insert(BibEntryTaxonomyCrossRef bibEntryTaxonomyCrossRef);

    @Insert
    void insertAll(List<BibEntryTaxonomyCrossRef> bibEntryTaxonomyCrossRef);

    @Delete
    void delete(BibEntryTaxonomyCrossRef bibEntryTaxonomyCrossRef);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId AND parentId=:parentId")
    List<TaxonomyWithEntries> getChildTaxonomiesForTaxonomyId(int repoId, int parentId);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE taxonomyId IN (SELECT parentId FROM TAXONOMY WHERE repoId=:repoId AND NOT hasChildren)")
    List<TaxonomyWithEntries> getTaxonomyIdsWithLeafChildTaxonomies(int repoId);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId AND parentId=:parentId")
    LiveData<List<TaxonomyWithEntries>> getChildTaxonomiesWithEntries(int repoId, int parentId);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId AND taxonomyId=:taxonomyId")
    LiveData<TaxonomyWithEntries> getTaxonomyWithEntries(int repoId, int taxonomyId);
}
