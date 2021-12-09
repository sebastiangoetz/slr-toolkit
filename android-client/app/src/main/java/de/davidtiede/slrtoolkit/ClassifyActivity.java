package de.davidtiede.slrtoolkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import de.davidtiede.slrtoolkit.fragments.EntriesToClassifyViewPagerFragment;
import de.davidtiede.slrtoolkit.viewmodels.BibtexEntriesViewModel;

public class ClassifyActivity extends AppCompatActivity {
    BibtexEntriesViewModel bibtexEntriesViewModel;
    int repoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        bibtexEntriesViewModel = new ViewModelProvider(this).get(BibtexEntriesViewModel.class);

        Bundle extras = getIntent().getExtras();
        repoId = -1;
        if(extras != null) {
            repoId = extras.getInt("repo");
        }

        Fragment entriesToClassifyViewPagerFragment = EntriesToClassifyViewPagerFragment.newInstance(repoId);

        getSupportFragmentManager().beginTransaction().replace(R.id.bibtexEntriesClassificationFragment, entriesToClassifyViewPagerFragment).commit();
    }

}