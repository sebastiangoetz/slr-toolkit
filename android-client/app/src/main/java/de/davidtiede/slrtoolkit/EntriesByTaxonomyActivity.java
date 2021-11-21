package de.davidtiede.slrtoolkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import de.davidtiede.slrtoolkit.fragments.TaxonomyListFragment;

public class EntriesByTaxonomyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries_by_taxonomy);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            int repoId = extras.getInt("repo");
            Fragment taxonomyFragment = TaxonomyListFragment.newInstance(repoId, 0);

            getSupportFragmentManager().beginTransaction().replace(R.id.taxonomyFragment, taxonomyFragment).commit();
        }
    }
}