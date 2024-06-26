package de.slrtoolkit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface TaxonomyDao {
    @Insert
    long insert(Taxonomy taxonomy);

    @Insert
    void insertAll(List<Taxonomy> taxonomies);

    @Transaction
    @Query("SELECT * FROM taxonomy WHERE taxonomyId=:taxId")
    LiveData<Taxonomy> getTaxonomyById(int taxId);

    @Query("SELECT * FROM taxonomy WHERE taxonomyId=:taxId")
    Taxonomy getTaxonomyByIdDirectly(int taxId);

    @Transaction
    @Query("SELECT * FROM taxonomy WHERE repoId=:repoId AND parentId=:parentId")
    LiveData<List<Taxonomy>> getChildTaxonomies(int repoId, int parentId);

    @Transaction
    @Query("SELECT * FROM taxonomy WHERE repoId=:repoId")
    LiveData<List<Taxonomy>> getAllTaxonomiesForRepo(int repoId);

    @Transaction
    @Query("SELECT * FROM TAXONOMY WHERE repoId=:repoId AND path=:path")
    Taxonomy getTaxonomyByRepoAndPathDirectly(int repoId, String path);

    @Transaction
    @Query("DELETE FROM TAXONOMY WHERE taxonomyId=:id")
    void removeTaxonomyEntryById(int id);

    @Transaction
    @Query("DELETE FROM TAXONOMY WHERE repoId=:repoId")
    void removeAllTaxonomiesOfRepo(int repoId);
}
