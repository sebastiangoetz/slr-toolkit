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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

public class ProjectActivity extends AppCompatActivity {
    private File file;
    private Button button;

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
            final String[] path = new String[1];
            // Create the observer
            final Observer<Repo> nameObserver = new Observer<Repo>() {
                @Override
                public void onChanged(@Nullable final Repo repo) {
                    path[0] = repo.getLocal_path();
                    file = accessFiles(path[0]);
                    /*try {
                        System.out.println("Trying");
                        Reader reader = new FileReader(file.getAbsolutePath());
                        BibTeXParser bibTeXParser = new BibTeXParser();
                        BibTeXDatabase database = bibTeXParser.parse(reader);
                        Map<Key, BibTeXEntry> entryMap= database.getEntries();
                        Collection<BibTeXEntry> entries = entryMap.values();
                        System.out.println("Before loop");
                        for(BibTeXEntry entry : entries){
                            org.jbibtex.Value value = entry.getField(org.jbibtex.BibTeXEntry.KEY_TITLE);
                            if(value == null){
                                continue;
                            }
                            System.out.println("The value is:");
                            System.out.println(value.toUserString());
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/
                }
            };
            repoViewModel.getRepoById(id).observe(this, nameObserver);

            button = findViewById(R.id.button_classify);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Clicked!");
                    Intent intent = new Intent(getApplicationContext(), BibtexEntriesActivity.class);
                    intent.putExtra("path", path[0]);
                    startActivity(intent);
                }
            });
        }
    }

    private File accessFiles(String path) {
        File directoryPath = new File(getApplicationContext().getFilesDir(), path);
        File[] files = directoryPath.listFiles();
        File bibFile = null;
        for(File file: files) {
            if(file.isDirectory()) {
                for(File f: file.listFiles()) {
                    if(f.getName().endsWith(".bib")) {
                        bibFile = f;
                    }
                }
            }
        }
        return bibFile;
    }
}