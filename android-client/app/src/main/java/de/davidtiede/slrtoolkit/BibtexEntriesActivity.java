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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.BibtexEntriesViewModel;
import de.davidtiede.slrtoolkit.views.BibTexEntriesListAdapter;
import de.davidtiede.slrtoolkit.views.SwipeToDeleteCallbackBibTexEntries;

public class BibtexEntriesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BibTexEntriesListAdapter.RecyclerViewClickListener listener;
    private BibtexEntriesViewModel bibtexEntriesViewModel;
    BibTexEntriesListAdapter adapter;
    private int repoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibtex_entries);
        bibtexEntriesViewModel = new ViewModelProvider(this).get(BibtexEntriesViewModel.class);
        Bundle extras = getIntent().getExtras();
        repoId = -1;
        if(extras != null) {
            repoId = extras.getInt("repo");
        }
        //populateDB(entryMap, repoId);

        recyclerView = findViewById(R.id.bibTexEntriesRecyclerView);

        setOnClickListener();

        adapter = new BibTexEntriesListAdapter(new BibTexEntriesListAdapter.EntryDiff(), listener, repoId);
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
