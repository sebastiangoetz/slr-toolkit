package de.slrtoolkit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class RepoDao {
    @Query("SELECT * FROM repo")
    public abstract LiveData<List<Repo>> getAllRepos();

    @Query("SELECT * FROM repo WHERE id=:id ")
    public abstract LiveData<Repo> getRepoById(int id);

    @Query("SELECT * FROM repo WHERE id=:id ")
    public abstract Repo getRepoByIdDirectly(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(Repo repo);

    @Update
    public abstract void update(Repo repo);

    @Delete
    public abstract void delete(Repo repo);
}
