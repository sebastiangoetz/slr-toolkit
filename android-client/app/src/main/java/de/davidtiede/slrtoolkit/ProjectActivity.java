package de.davidtiede.slrtoolkit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

import android.os.Build;
import android.os.Bundle;

public class ProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        Bundle extras = getIntent().getExtras();
        ProjectViewModel projectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);


        if(extras != null) {
            int id = extras.getInt("repo");
            projectViewModel.setCurrentRepoId(id);
            // Create the observer
            //to initialize the database
            final Observer projectPathObserver = new Observer<Repo>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onChanged(Repo repo) {
                    String path = repo.getLocal_path();
                    projectViewModel.initializeDataForRepo(repo.getId(), repo.getLocal_path());
                }
            };
            projectViewModel.getRepoById(id).observe(this, projectPathObserver);
        }
    }
}