package de.davidtiede.slrtoolkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import de.davidtiede.slrtoolkit.fragments.BibtexEntriesListFragment;

public class BibtexEntriesActivity extends AppCompatActivity {
    private int repoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibtex_entries);
        Bundle extras = getIntent().getExtras();
        repoId = -1;
        if(extras != null) {
            repoId = extras.getInt("repo");
        }

        Fragment bibtexEntriesListFragment = BibtexEntriesListFragment.newInstance(repoId);

        getSupportFragmentManager().beginTransaction().replace(R.id.bibtexEntriesFragment, bibtexEntriesListFragment).commit();


    }
}
