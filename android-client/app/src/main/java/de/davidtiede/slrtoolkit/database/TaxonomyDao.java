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
    LiveData<List<TaxonomyWithEntries>> getTaxonomyWithEntries();

    @Insert
    long insert(Taxonomy taxonomy);

    @Insert
    void insertAll(List<Taxonomy> taxonomies);

    @Transaction
    @Query("SELECT * FROM taxonomy WHERE repoId=:repoId AND parentId=:parentId")
    LiveData<List<Taxonomy>> getChildTaxonomies(int repoId, int parentId);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId AND taxonomyId=:taxonomyId")
    LiveData<TaxonomyWithEntries> getTaxonomyWithEntries(int repoId, int taxonomyId);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId AND parentId=:parentId")
    LiveData<List<TaxonomyWithEntries>> getChildTaxonomiesWithEntries(int repoId, int parentId);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId AND path=:path")
    Taxonomy getTaxonomyByRepoAndPathDirectly(int repoId, String path);
}
