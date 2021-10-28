package de.davidtiede.slrtoolkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.FilterViewModel;
import de.davidtiede.slrtoolkit.views.FilterEntriesAdapter;

public class FilterActivity extends AppCompatActivity {
    private FilterEntriesAdapter arrayAdapter;
    private ArrayList<Entry> entries;
    SwipeFlingAdapterView flingAdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Bundle extras = getIntent().getExtras();

        FilterViewModel filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);

        flingAdapterView = findViewById(R.id.swipe_entries);
        entries = new ArrayList<>();

        arrayAdapter = new FilterEntriesAdapter(this, entries);
        flingAdapterView.setAdapter(arrayAdapter);

        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                arrayAdapter.removeFirstObject();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Entry entry = (Entry) o;
                entry.setStatus(Entry.Status.DISCARD);
                filterViewModel.updateEntry(entry);
            }

            @Override
            public void onRightCardExit(Object o) {
                Entry entry = (Entry) o;
                entry.setStatus(Entry.Status.KEEP);
                filterViewModel.updateEntry(entry);
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

        if(extras != null) {
            int id = extras.getInt("repo");

            final Observer openEntriesObserver = new Observer<List<Entry>>() {
                @Override
                public void onChanged(List<Entry> data) {
                    entries = (ArrayList<Entry>) data;
                    ArrayList<Entry> newItems = new ArrayList<>();
                    newItems.addAll(data);
                    arrayAdapter.setEntries(newItems);
                }
            };

            filterViewModel.getOpenEntriesForRepo(id).observe(this, openEntriesObserver);
        }

    }
}