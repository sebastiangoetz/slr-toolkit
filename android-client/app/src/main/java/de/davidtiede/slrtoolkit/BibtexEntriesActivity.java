package de.davidtiede.slrtoolkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import de.davidtiede.slrtoolkit.viewmodels.RepoViewModel;
import de.davidtiede.slrtoolkit.views.BibTexEntriesListAdapter;

public class BibtexEntriesActivity extends AppCompatActivity {
    File file;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibtex_entries);
        Bundle extras = getIntent().getExtras();
        String path;
        RepoViewModel repoViewModel = new ViewModelProvider(this).get(RepoViewModel.class);
        ArrayList<String> titles = new ArrayList<>();
        if(extras != null) {
            path = extras.getString("path");
            System.out.println("In BibtexEntries");
            System.out.println(path);
            file = accessFiles(path);

            try {
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
                    titles.add(value.toUserString());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        recyclerView = findViewById(R.id.bibTexEntriesRecyclerView);
        BibTexEntriesListAdapter adapter = new BibTexEntriesListAdapter(this, titles);
        recyclerView.setAdapter(adapter);
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
