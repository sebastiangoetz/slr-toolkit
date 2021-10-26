package de.davidtiede.slrtoolkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.BibtexEntryViewModel;

public class BibtexEntryActivity extends AppCompatActivity {
    private TextView titleTextView;
    private BibtexEntryViewModel bibtexEntryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibtex_entry);
        Bundle extra = getIntent().getBundleExtra("extra");
        String key = extra.getString("bibtexEntry");
        int repoId = extra.getInt("repoId");
        titleTextView = findViewById(R.id.bibtex_entry_title);
        bibtexEntryViewModel = new ViewModelProvider(this).get(BibtexEntryViewModel.class);
        final Observer<Entry> entryObserver = new Observer<Entry>() {
            @Override
            public void onChanged(Entry entry) {
                titleTextView.setText(entry.getTitle());
            }
        };

        bibtexEntryViewModel.getEntryByRepoAndKey(repoId, key).observe(this, entryObserver);
    }
}