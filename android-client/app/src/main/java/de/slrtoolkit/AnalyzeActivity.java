package de.slrtoolkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.slrtoolkit.R;
import de.slrtoolkit.fragments.ChartSelectionFragment;
import de.slrtoolkit.viewmodels.AnalyzeViewModel;

public class AnalyzeActivity extends AppCompatActivity {
    int repoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        AnalyzeViewModel analyzeViewModel = new ViewModelProvider(this).get(AnalyzeViewModel.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            repoId = extras.getInt("repo");
        }
        analyzeViewModel.setCurrentRepoId(repoId);

        Fragment chartSelectionFragment = new ChartSelectionFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.analyze_fragment_container_view, chartSelectionFragment).commit();
    }
}