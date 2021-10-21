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
}
