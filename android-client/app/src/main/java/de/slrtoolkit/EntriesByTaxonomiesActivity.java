package de.slrtoolkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import de.slrtoolkit.fragments.EntriesByTaxonomiesFragment;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;

public class
EntriesByTaxonomiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries_by_taxonomies);

        Bundle extras = getIntent().getExtras();
        TaxonomiesViewModel taxonomiesViewModel = new ViewModelProvider(this).get(TaxonomiesViewModel.class);

        if (extras != null) {
            int repoId = extras.getInt("repo");
            taxonomiesViewModel.setCurrentRepoId(repoId);
        }

        Fragment entriesByTaxonomyFragment = EntriesByTaxonomiesFragment.newInstance(0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.entries_by_taxonomies_fragment_container_view, entriesByTaxonomyFragment);
        ft.commit();
    }
}