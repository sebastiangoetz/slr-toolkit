package de.slrtoolkit.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Repo.class, Entry.class, Taxonomy.class, EntryTaxonomyCrossRef.class, Keyword.class, Author.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract RepoDao repoDao();

    public abstract EntryDao entryDao();

    public abstract TaxonomyDao taxonomyDao();

    public abstract AuthorDao authorDao();

    public abstract KeywordDao keywordDao();

    public abstract TaxonomyWithEntriesDao taxonomyWithEntriesDao();
}
