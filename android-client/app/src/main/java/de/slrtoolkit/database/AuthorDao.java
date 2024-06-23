package de.slrtoolkit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class AuthorDao {
    @Query("SELECT * FROM author WHERE repoId=:id")
    public abstract LiveData<List<Author>> getAuthorsForRepo(int id);

    @Insert
    public abstract long insert(Author author);

    @Insert
    public abstract void insertAll(List<Author> authors);

    @Update
    public abstract void update(Author author);

    @Delete
    public abstract void delete(Author author);

    @Query("SELECT * FROM author WHERE authorId=:id")
    public abstract LiveData<Author> getAuthorById(int id);

    @Query("DELETE FROM author WHERE repoId=:repoId")
    public abstract void deleteAllForRepo(int repoId);
}
