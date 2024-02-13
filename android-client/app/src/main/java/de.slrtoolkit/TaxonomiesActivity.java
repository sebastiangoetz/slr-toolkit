package de.slrtoolkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import de.slrtoolkit.R;
import de.slrtoolkit.fragments.TaxonomyListFragment;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;

public class
TaxonomiesActivity extends AppCompatActivity {
    private int repoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxonomies);

        Bundle extras = getIntent().getExtras();
        TaxonomiesViewModel taxonomiesViewModel = new ViewModelProvider(this).get(TaxonomiesViewModel.class);

        if (extras != null) {
            repoId = extras.getInt("repo");
            taxonomiesViewModel.setCurrentRepoId(repoId);
        }

        Fragment taxonomyFragment = TaxonomyListFragment.newInstance(0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.taxonomies_fragment_container_view, taxonomyFragment);
        ft.commit();
    }
}