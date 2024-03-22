package de.slrtoolkit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class KeywordDao {
    @Query("SELECT * FROM keyword WHERE repoId=:id")
    public abstract LiveData<List<Keyword>> getKeywordsForRepo(int id);

    @Insert
    public abstract long insert(Keyword keyword);

    @Insert
    public abstract void insertAll(List<Keyword> keywords);

    @Update
    public abstract void update(Keyword keyword);

    @Delete
    public abstract void delete(Keyword keyword);

    @Query("SELECT * FROM keyword WHERE keywordId=:id")
    public abstract LiveData<Keyword> getKeywordById(int id);
}
