package de.slrtoolkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.slrtoolkit.fragments.TaxonomyTreeViewFragment;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;

public class
ManageTaxonomyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_taxonomy);

        Bundle extras = getIntent().getExtras();
        TaxonomiesViewModel taxonomiesViewModel = new ViewModelProvider(this).get(TaxonomiesViewModel.class);

        if (extras != null) {
            int repoId = extras.getInt("repo");
            taxonomiesViewModel.setCurrentRepoId(repoId);
        }

        Fragment taxonomyTreeViewFragment = new TaxonomyTreeViewFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.manage_taxonomy_fragment_container_view, taxonomyTreeViewFragment).commit();
    }
}