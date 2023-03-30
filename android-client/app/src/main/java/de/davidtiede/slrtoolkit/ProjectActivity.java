package de.davidtiede.slrtoolkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

public class ProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        Bundle extras = getIntent().getExtras();
        ProjectViewModel projectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);


        if (extras != null) {
            int id = extras.getInt("repo");
            projectViewModel.setCurrentRepoId(id);
        }
    }
}