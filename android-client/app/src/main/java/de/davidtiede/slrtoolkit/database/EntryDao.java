package de.davidtiede.slrtoolkit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class EntryDao {
    @Query("SELECT * FROM entry WHERE repoId=:id")
    public abstract LiveData<List<Entry>> getEntriesForRepo(int id);

    @Delete
    public abstract void delete(Entry entry);

    @Insert
    public abstract void insertAll(List<Entry> entries);

    @Insert
    public abstract long insert(Entry entry);

    @Update
    public abstract void update(Entry entry);

    @Query("SELECT COUNT(entryId) FROM entry WHERE repoId=:id")
    public abstract LiveData<Integer> getEntryAmount(int id);

    @Query("SELECT COUNT(entryId) FROM entry WHERE status=:status AND repoId=:id")
    public abstract LiveData<Integer> getEntryAmountForStatus(int id, int status);

    @Query("SELECT * FROM entry WHERE status=:status AND repoId=:id")
    public abstract LiveData<List<Entry>> getEntryForRepoByStatus(int id, int status);

    @Query("SELECT * FROM entry WHERE `key`=:key and repoId=:id")
    public abstract Entry getEntryByRepoAndKeyDirectly(int id, String key);

    @Query("SELECT * FROM entry WHERE entryId=:id")
    public abstract LiveData<Entry> getEntryById(int id);

    public void insertEntriesForRepo(int repoId, List<Entry> entries){

        for(Entry entry : entries){
            entry.setRepoId(repoId);
        }

        insertAll(entries);
    }

    @Transaction
    @Query("SELECT * FROM ENTRY e WHERE repoId=:repoId AND NOT EXISTS (SELECT * FROM EntryTaxonomyCrossRef etcr WHERE e.entryId=etcr.entryId)")
    public abstract LiveData<List<Entry>> getEntriesWithoutTaxonomies(int repoId);

    @Query("SELECT COUNT(entryId) FROM ENTRY e WHERE repoId=:repoId AND NOT EXISTS (SELECT * FROM EntryTaxonomyCrossRef etcr WHERE e.entryId=etcr.entryId)")
    public abstract LiveData<Integer> getEntriesWithoutTaxonomiesCount(int repoId);

    @Query("SELECT * FROM entry WHERE entryId=:id")
    public abstract Entry getEntryByIdDirectly(int id);
}
