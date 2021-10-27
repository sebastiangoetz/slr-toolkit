package de.davidtiede.slrtoolkit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProjectActivity extends AppCompatActivity {
    private Button allEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        Bundle extras = getIntent().getExtras();
        ProjectViewModel projectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        allEntryButton = findViewById(R.id.button_all_entries);

        if(extras != null) {
            int id = extras.getInt("repo");
            final String[] path = new String[1];

            // Create the observer
            final Observer<Repo> nameObserver = new Observer<Repo>() {
                @Override
                public void onChanged(@Nullable final Repo repo) {
                    path[0] = repo.getLocal_path();
                }
            };
            final Observer entryAmountObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer amount) {
                    allEntryButton.setText("All Entries (" + amount.toString() + ")");
                }
            };
            projectViewModel.getRepoById(id).observe(this, nameObserver);
            projectViewModel.getEntryAmount(id).observe(this, entryAmountObserver);

            allEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), BibtexEntriesActivity.class);
                    intent.putExtra("path", path[0]);
                    intent.putExtra("repo", id);
                    startActivity(intent);
                }
            });
        }
    }
}