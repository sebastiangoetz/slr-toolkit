package de.davidtiede.slrtoolkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.davidtiede.slrtoolkit.fragments.ClassificationFragment;
import de.davidtiede.slrtoolkit.viewmodels.ClassificationViewModel;

public class ClassificationActivity extends AppCompatActivity {
    private int repoId;
    private int entryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

        ClassificationViewModel classificationViewModel = new ViewModelProvider(this).get(ClassificationViewModel.class);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            repoId = extras.getInt("repo");
            entryId = extras.getInt("entry");
        }

        classificationViewModel.setCurrentEntryId(entryId);
        classificationViewModel.setCurrentRepoId(repoId);

        Fragment classificationFragment = ClassificationFragment.newInstance(0);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.classification_fragment_container_view, classificationFragment).commit();
    }
}