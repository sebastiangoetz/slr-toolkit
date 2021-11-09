package de.davidtiede.slrtoolkit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface TaxonomyDao {
    @Transaction
    @Query("SELECT * FROM taxonomy")
    public LiveData<List<TaxonomyWithEntries>> getTaxonomyWithEntries();
}
