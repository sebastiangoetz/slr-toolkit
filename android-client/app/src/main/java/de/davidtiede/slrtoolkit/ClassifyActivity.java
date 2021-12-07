package de.davidtiede.slrtoolkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.BibtexEntriesViewModel;
import de.davidtiede.slrtoolkit.views.EntriesListViewPagerAdapter;

public class ClassifyActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    BibtexEntriesViewModel bibtexEntriesViewModel;
    int repoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        bibtexEntriesViewModel = new ViewModelProvider(this).get(BibtexEntriesViewModel.class);

        Bundle extras = getIntent().getExtras();
        repoId = -1;
        if(extras != null) {
            repoId = extras.getInt("repo");
        }

        viewPager = findViewById(R.id.classify_entries_viewpager);

        bibtexEntriesViewModel.getEntriesForRepo(repoId).observe(this, new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                EntriesListViewPagerAdapter entriesListViewPagerAdapter = new EntriesListViewPagerAdapter((ArrayList<Entry>) entries);
                viewPager.setAdapter(entriesListViewPagerAdapter);
            }
        });
    }
}