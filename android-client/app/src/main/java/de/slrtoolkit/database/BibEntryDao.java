package de.slrtoolkit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class BibEntryDao {
    @Query("SELECT * FROM BibEntry WHERE repoId=:id")
    public abstract LiveData<List<BibEntry>> getEntriesForRepo(int id);

    @Delete
    public abstract void delete(BibEntry bibEntry);

    @Insert
    public abstract void insertAll(List<BibEntry> entries);

    @Insert
    public abstract long insert(BibEntry bibEntry);

    @Update
    public abstract void update(BibEntry bibEntry);

    @Query("SELECT COUNT(entryId) FROM BibEntry WHERE repoId=:id")
    public abstract LiveData<Integer> getEntryAmount(int id);

    @Query("SELECT COUNT(entryId) FROM BibEntryTaxonomyCrossRef WHERE taxonomyId=:taxId")
    public abstract Integer getEntryAmountForTaxonomy(int taxId);

    @Query("SELECT COUNT(entryId) FROM BibEntry WHERE status=:status AND repoId=:id")
    public abstract LiveData<Integer> getEntryAmountForStatus(int id, int status);

    @Query("SELECT * FROM BibEntry WHERE status=:status AND repoId=:id")
    public abstract LiveData<List<BibEntry>> getEntryForRepoByStatus(int id, int status);

    @Query("SELECT * FROM BibEntry WHERE `key`=:key and repoId=:id")
    public abstract BibEntry getEntryByRepoAndKeyDirectly(int id, String key);

    @Query("SELECT * FROM BibEntry WHERE entryId=:id")
    public abstract LiveData<BibEntry> getEntryById(int id);

    public void insertEntriesForRepo(int repoId, List<BibEntry> entries) {

        for (BibEntry bibEntry : entries) {
            bibEntry.setRepoId(repoId);
        }

        insertAll(entries);
    }

    @Transaction
    @Query("SELECT * FROM BibEntry e WHERE repoId=:repoId AND NOT EXISTS (SELECT * FROM BibEntryTaxonomyCrossRef etcr WHERE e.entryId=etcr.entryId)")
    public abstract LiveData<List<BibEntry>> getEntriesWithoutTaxonomies(int repoId);

    @Query("SELECT COUNT(entryId) FROM BibEntry e WHERE repoId=:repoId AND NOT EXISTS (SELECT * FROM BibEntryTaxonomyCrossRef etcr WHERE e.entryId=etcr.entryId)")
    public abstract LiveData<Integer> getEntriesWithoutTaxonomiesCount(int repoId);

    @Query("SELECT * FROM BibEntry WHERE entryId=:id")
    public abstract BibEntry getEntryByIdDirectly(int id);

    @Query("DELETE FROM BibEntry WHERE repoId=:id")
    public abstract void deleteAllEntriesOfRepo(int id);
}
