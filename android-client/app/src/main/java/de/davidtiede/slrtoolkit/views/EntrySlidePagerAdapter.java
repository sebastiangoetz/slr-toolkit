package de.davidtiede.slrtoolkit.views;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.fragments.BibtexEntryDetailFragment;

public class EntrySlidePagerAdapter extends FragmentStateAdapter {
    List<Entry> entries;

    public EntrySlidePagerAdapter(FragmentActivity fa, List<Entry> entries) {
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
