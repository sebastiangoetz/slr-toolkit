package de.davidtiede.slrtoolkit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.repositories.EntryRepository;

public class BibtexEntryViewModel extends AndroidViewModel {
    private final EntryRepository entryRepository;

    public BibtexEntryViewModel(@NonNull Application application) {
        super(application);
        entryRepository = new EntryRepository(application);
    }

    public LiveData<Entry> getEntryById(int id) {
        return entryRepository.getEntryById(id);
    }


}
