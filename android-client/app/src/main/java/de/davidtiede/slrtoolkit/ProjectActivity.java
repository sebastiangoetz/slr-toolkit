package de.davidtiede.slrtoolkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProjectActivity extends AppCompatActivity {
    private Button allEntryButton;
    private Button filterButton;
    private Button taxonomyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        Bundle extras = getIntent().getExtras();
        ProjectViewModel projectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        allEntryButton = findViewById(R.id.button_all_entries);
        filterButton = findViewById(R.id.button_filter);
        taxonomyButton = findViewById(R.id.button_entries_by_taxonomy);

        if(extras != null) {
            int id = extras.getInt("repo");
            // Create the observer
            final Observer entryAmountObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer amount) {
                    allEntryButton.setText("All Entries (" + amount.toString() + ")");
                }
            };
            projectViewModel.getEntryAmount(id).observe(this, entryAmountObserver);

            final Observer openEntryAmountObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer amount) {
                    filterButton.setText("Filter (" + amount.toString() + ")");
                }
            };
            projectViewModel.getOpenEntryAmount(id).observe(this, openEntryAmountObserver);

            //to initialize the database
            /*final Observer projectPathObserver = new Observer<Repo>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onChanged(Repo repo) {
                    String path = repo.getLocal_path();
                    projectViewModel.initializeData(repo.getId(), repo.getLocal_path());
                    projectViewModel.initializeTaxonomy(repo.getId(), repo.getLocal_path());
                }
            };
            projectViewModel.getRepoById(id).observe(this, projectPathObserver);*/

            allEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), BibtexEntriesActivity.class);
                    intent.putExtra("repo", id);
                    startActivity(intent);
                }
            });

            filterButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                    intent.putExtra("repo", id);
                    startActivity(intent);
                }
            });

            taxonomyButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), EntriesByTaxonomyActivity.class);
                    intent.putExtra("repo", id);
                    startActivity(intent);
                }
            });
        }
    }
}