package de.davidtiede.slrtoolkit;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.fragments.BibtexEntryDetailFragment;
import de.davidtiede.slrtoolkit.viewmodels.BibtexEntriesViewModel;

public class ClassifyActivity extends FragmentActivity {
    ViewPager2 viewPager;
    BibtexEntriesViewModel bibtexEntriesViewModel;
    int repoId;
    private FragmentStateAdapter pagerAdapter;


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
        pagerAdapter = new ScreenSlidePagerAdapter(ClassifyActivity.this, new ArrayList<Entry>());
        viewPager.setAdapter(pagerAdapter);

        bibtexEntriesViewModel.getEntriesForRepo(repoId).observe(this, new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                pagerAdapter = new ScreenSlidePagerAdapter(ClassifyActivity.this, entries);
                viewPager.setAdapter(pagerAdapter);
            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        List<Entry> entries;

        public ScreenSlidePagerAdapter(FragmentActivity fa, List<Entry> entries) {
            super(fa);
            this.entries = entries;
        }

        @Override
        public Fragment createFragment(int position) {
            Entry entry = entries.get(position);
            Fragment fragment = BibtexEntryDetailFragment.newInstance(entry.getId());
            return fragment;
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }
    }

}