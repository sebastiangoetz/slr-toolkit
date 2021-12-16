package de.davidtiede.slrtoolkit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface TaxonomyDao {
    @Transaction
    @Query("SELECT * FROM taxonomy")
    public LiveData<List<TaxonomyWithEntries>> getTaxonomyWithEntries();

    @Insert
    public abstract long insert(Taxonomy taxonomy);

    @Insert
    public abstract void insertAll(List<Taxonomy> taxonomies);

    @Transaction
    @Query("SELECT * FROM taxonomy WHERE repoId=:repoId AND parentId=:parentId")
    public LiveData<List<Taxonomy>> getChildTaxonomies(int repoId, int parentId);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId AND taxonomyId=:taxonomyId")
    public LiveData<TaxonomyWithEntries> getTaxonomyWithEntries(int repoId, int taxonomyId);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId AND parentId=:parentId")
    public LiveData<List<TaxonomyWithEntries>> getChildTaxonomiesWithEntries(int repoId, int parentId);
}
