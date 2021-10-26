package de.davidtiede.slrtoolkit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.viewmodels.RepoViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProjectActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        Bundle extras = getIntent().getExtras();
        RepoViewModel repoViewModel = new ViewModelProvider(this).get(RepoViewModel.class);

        if(extras != null) {
            int id = extras.getInt("repo");
            System.out.println(id);
            final String[] path = new String[1];
            // Create the observer
            final Observer<Repo> nameObserver = new Observer<Repo>() {
                @Override
                public void onChanged(@Nullable final Repo repo) {
                    path[0] = repo.getLocal_path();
                }
            };
            repoViewModel.getRepoById(id).observe(this, nameObserver);

            button = findViewById(R.id.button_classify);
            button.setOnClickListener(new View.OnClickListener() {
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