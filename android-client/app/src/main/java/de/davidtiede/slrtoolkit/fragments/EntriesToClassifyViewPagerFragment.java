package de.davidtiede.slrtoolkit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import de.davidtiede.slrtoolkit.ClassificationActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntriesToClassifyViewPagerFragment extends Fragment {

    ViewPager2 viewPager;
    ProjectViewModel projectViewModel;
    private FragmentStateAdapter pagerAdapter;
    private TextView noEntriesToClassifyTextview;
    int repoId;
    List<Entry> entries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entries_to_classify_view_pager, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);
        repoId = projectViewModel.getCurrentRepoId();
        viewPager = view.findViewById(R.id.classify_entries_viewpager);
        noEntriesToClassifyTextview = view.findViewById(R.id.textview_no_entries_to_classify);
        pagerAdapter = new EntrySlidePagerAdapter(EntriesToClassifyViewPagerFragment.this.getActivity(), new ArrayList<>());
        viewPager.setAdapter(pagerAdapter);

        projectViewModel.getEntriesWithoutTaxonomies(repoId).observe(getViewLifecycleOwner(), list -> {
            entries = list;
            if (entries.size() == 0) {
                noEntriesToClassifyTextview.setVisibility(View.VISIBLE);
            } else {
                noEntriesToClassifyTextview.setVisibility(View.INVISIBLE);
                pagerAdapter = new EntrySlidePagerAdapter(EntriesToClassifyViewPagerFragment.this.getActivity(), entries);
                viewPager.setAdapter(pagerAdapter);
            }
        });
    }

    private void classifyEntry() {
        if (entries.size() == 0) return;
        Entry entry = entries.get(viewPager.getCurrentItem());

        if (entry != null) {
            Intent intent = new Intent(getActivity(), ClassificationActivity.class);
            intent.putExtra("repo", repoId);
            intent.putExtra("entry", entry.getEntryId());
            startActivity(intent);
        }
    }

    private void deleteEntry() {
        int i = viewPager.getCurrentItem();
        if (entries.size() <= 1) {
            noEntriesToClassifyTextview.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.INVISIBLE);
        } else {
            noEntriesToClassifyTextview.setVisibility(View.INVISIBLE);
            viewPager.setVisibility(View.VISIBLE);
        }
        Entry entry = entries.get(i);
        projectViewModel.deleteById(entry.getEntryId(), repoId);
        pagerAdapter.notifyDataSetChanged();
        viewPager.invalidate();
        viewPager.setCurrentItem(i);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_classification, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_classify: {
                classifyEntry();
                break;
            }
            case R.id.action_delete: {
                deleteEntry();
                break;
            }
            default:
                System.out.println("An error occurred");
                break;
        }
        return false;
    }

    public class EntrySlidePagerAdapter extends FragmentStateAdapter {
        List<Entry> entries;

        public EntrySlidePagerAdapter(FragmentActivity fa, List<Entry> entries) {
            super(fa);
            this.entries = entries;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Entry entry = entries.get(position);
            projectViewModel.setCurrentEntryIdForCard(entry.getEntryId());
            return new BibtexEntryDetailFragment();
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }
    }
}