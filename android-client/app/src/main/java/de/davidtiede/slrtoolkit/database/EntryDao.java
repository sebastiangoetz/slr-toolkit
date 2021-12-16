package de.davidtiede.slrtoolkit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EntryDao {
    @Query("SELECT * FROM entry WHERE repoId=:id ")
    LiveData<List<Entry>> getEntriesForRepo(int id);

    @Delete
    void delete(Entry entry);

    @Insert
    public void insertAll(List<Entry> entries);

    @Insert
    public abstract long insert(Entry entry);

    @Update
    void update(Entry entry);

    @Query("SELECT COUNT(title) FROM entry WHERE repoId=:id")
    LiveData<Integer> getEntryAmount(int id);

    @Query("SELECT COUNT(title) FROM entry WHERE status=:status AND repoId=:id")
    LiveData<Integer> getEntryAmountForStatus(int id, int status);

    @Query("SELECT * FROM entry WHERE status=:status AND repoId=:id")
    LiveData<List<Entry>> getEntryForRepoByStatus(int id, int status);

    @Query("SELECT * FROM entry WHERE id=:id")
    LiveData<Entry> getEntryById(int id);
}
