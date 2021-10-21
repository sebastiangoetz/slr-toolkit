package de.davidtiede.slrtoolkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXObject;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.davidtiede.slrtoolkit.util.BibTexParser;
import de.davidtiede.slrtoolkit.views.BibTexEntriesListAdapter;
import de.davidtiede.slrtoolkit.views.SwipeToDeleteCallbackBibTexEntries;

public class BibtexEntriesActivity extends AppCompatActivity {
    private File file;
    private RecyclerView recyclerView;
    private BibTexEntriesListAdapter.RecyclerViewClickListener listener;
    private ArrayList<BibTeXEntry> bibtexEntries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibtex_entries);
        Bundle extras = getIntent().getExtras();
        String path;
        Map<Key, BibTeXEntry> entryMap = new HashMap();
        if(extras != null) {
            path = extras.getString("path");
            file = accessFiles(path);
            try {
                BibTexParser parser = BibTexParser.getBibTexParser();
                parser.setBibTeXDatabase(file);
                entryMap.putAll(parser.getBibTeXEntries());
                Collection<BibTeXEntry> entries = entryMap.values();
                for(BibTeXEntry entry : entries){
                    bibtexEntries.add(entry);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        recyclerView = findViewById(R.id.bibTexEntriesRecyclerView);

        setOnClickListener();

        BibTexEntriesListAdapter adapter = new BibTexEntriesListAdapter(this, listener, entryMap);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new
                        ItemTouchHelper(new SwipeToDeleteCallbackBibTexEntries(adapter));
                itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setOnClickListener() {
        listener = new BibTexEntriesListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                System.out.println("Clicked in BibtexEntriesActivity");
                Intent intent = new Intent(getApplicationContext(), BibtexEntryActivity.class);
                //intent.putExtra("path", path[0]);
                Bundle extra = new Bundle();
                extra.putSerializable("bibtexEntry", bibtexEntries.get(position));
                intent.putExtra("extra", extra);
                startActivity(intent);
            }
        };
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
