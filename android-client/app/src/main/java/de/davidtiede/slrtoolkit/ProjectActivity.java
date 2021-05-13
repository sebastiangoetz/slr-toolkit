package de.davidtiede.slrtoolkit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        Bundle extras = getIntent().getExtras();
        int id = 0;

        if(extras != null) {
            id = extras.getInt("repo");
            System.out.println(id);
        }
    }
}