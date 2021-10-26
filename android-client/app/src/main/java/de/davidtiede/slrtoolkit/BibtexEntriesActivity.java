package de.davidtiede.slrtoolkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.util.BibTexParser;
import de.davidtiede.slrtoolkit.viewmodels.BibtexEntriesViewModel;
import de.davidtiede.slrtoolkit.views.BibTexEntriesListAdapter;
import de.davidtiede.slrtoolkit.views.SwipeToDeleteCallbackBibTexEntries;

public class BibtexEntriesActivity extends AppCompatActivity {
    private File file;
    private RecyclerView recyclerView;
    private BibTexEntriesListAdapter.RecyclerViewClickListener listener;
    private ArrayList<BibTeXEntry> bibtexEntries = new ArrayList<>();
    private BibtexEntriesViewModel bibtexEntriesViewModel;
    BibTexEntriesListAdapter adapter;
    private int repoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibtex_entries);
        bibtexEntriesViewModel = new ViewModelProvider(this).get(BibtexEntriesViewModel.class);
        Bundle extras = getIntent().getExtras();
        String path;
        repoId = -1;
        Map<Key, BibTeXEntry> entryMap = new HashMap();
        if(extras != null) {
            path = extras.getString("path");
            repoId = extras.getInt("repo");
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
        //populateDB(entryMap, repoId);

        recyclerView = findViewById(R.id.bibTexEntriesRecyclerView);

        setOnClickListener();

        adapter = new BibTexEntriesListAdapter(new BibTexEntriesListAdapter.EntryDiff(), listener);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new
                        ItemTouchHelper(new SwipeToDeleteCallbackBibTexEntries(adapter));
                itemTouchHelper.attachToRecyclerView(recyclerView);

        bibtexEntriesViewModel.getEntriesForRepo(repoId).observe(this, this::onLoaded);
    }

    private void setOnClickListener() {
        listener = new BibTexEntriesListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                System.out.println("Clicked in BibtexEntriesActivity");
                Intent intent = new Intent(getApplicationContext(), BibtexEntryActivity.class);
                Bundle extra = new Bundle();
                Entry clickedEntry = adapter.getItemAtPosition(position);
                extra.putString("bibtexEntry", clickedEntry.getKey());
                extra.putInt("repoId", repoId);
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

    private void populateDB(Map<Key, BibTeXEntry> entryMap, int repoId) {
        List<Entry> entries = new ArrayList<>();

        for(Key key: entryMap.keySet()) {
            BibTeXEntry bibTeXEntry = entryMap.get(key);
            String title = bibTeXEntry.getField(BibTeXEntry.KEY_TITLE).toUserString();
            Entry entry = new Entry(key.toString(), title);
            entries.add(entry);
        }

        bibtexEntriesViewModel.insertEntries(entries, repoId);
    }

    private void onLoaded(List<Entry> list){
        if (list.size() == 0) {
            //TODO: show that there are no entries yet
        }
        else {
            adapter.submitList(list);
        }
    }
}
