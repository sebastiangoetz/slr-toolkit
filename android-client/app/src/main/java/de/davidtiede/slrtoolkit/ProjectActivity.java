package de.davidtiede.slrtoolkit;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.davidtiede.slrtoolkit.repositories.RepoRepository;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.viewmodels.RepoViewModel;

import android.os.Build;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class ProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        Bundle extras = getIntent().getExtras();
        int id = 0;
        RepoViewModel repoViewModel = new ViewModelProvider(this).get(RepoViewModel.class);

        if(extras != null) {
            id = extras.getInt("repo");
            System.out.println(id);
            //LiveData<Repo> repo = repoViewModel.getRepoById(id);

            // Create the observer
            final Observer<Repo> nameObserver = new Observer<Repo>() {
                @Override
                public void onChanged(@Nullable final Repo repo) {
                    String path = repo.getLocal_path();
                    accessFiles(path);
                }
            };

            repoViewModel.getRepoById(id).observe(this, nameObserver);
        }
    }

    private void accessFiles(String path) {
        String pathToDirectory = getApplicationContext().getFilesDir() + "/" + path + "/";
        File directoryPath = new File(getApplicationContext().getFilesDir(), path);
        System.out.println(path);
        String contents[] = directoryPath.list();
        System.out.println("Listing Files");
        System.out.println(contents.length);
        for(int i = 0; i < contents.length; i++) {
            System.out.println(contents[i]);
        }
    }
}