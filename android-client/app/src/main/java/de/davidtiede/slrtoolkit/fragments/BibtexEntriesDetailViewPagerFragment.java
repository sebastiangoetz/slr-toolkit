package de.davidtiede.slrtoolkit.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.davidtiede.slrtoolkit.ClassificationActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BibtexEntriesDetailViewPagerFragment extends Fragment {
    ViewPager2 viewPager;
    ProjectViewModel projectViewModel;
    private TextView noEntriesDetails;
    private FragmentStateAdapter pagerAdapter;
    int repoId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bibtex_entries_detail_view_pager, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);
        repoId = projectViewModel.getCurrentRepoId();
        viewPager = view.findViewById(R.id.entries_detail_viewpager);
        noEntriesDetails = view.findViewById(R.id.textview_no_entries_details);
        pagerAdapter = new BibtexEntriesDetailViewPagerFragment.EntrySlidePagerAdapter(BibtexEntriesDetailViewPagerFragment.this.getActivity(), new ArrayList<>());
        viewPager.setAdapter(pagerAdapter);

        setViewPager();
    }

    private void setViewPager() {
        List<Entry> entries = projectViewModel.getCurrentEntriesInList();
        if(entries.size() == 0) {
            noEntriesDetails.setVisibility(View.VISIBLE);
        } else {
            noEntriesDetails.setVisibility(View.INVISIBLE);
            pagerAdapter = new BibtexEntriesDetailViewPagerFragment.EntrySlidePagerAdapter(BibtexEntriesDetailViewPagerFragment.this.getActivity(), entries);
            viewPager.setAdapter(pagerAdapter);
        }
        int currentItemPosition = projectViewModel.getCurrentEntryInListCount();
        if(currentItemPosition != 0 && currentItemPosition < entries.size()) {
            viewPager.setCurrentItem(currentItemPosition, false);
        }
    }

    private void classifyEntry() {
        Intent intent = new Intent(getActivity(), ClassificationActivity.class);
        Entry entry = projectViewModel.getCurrentEntriesInList().get(viewPager.getCurrentItem());
        intent.putExtra("repo", repoId);
        intent.putExtra("entry", entry.getEntryId());
        startActivity(intent);
    }

    private void deleteEntry() {
        Entry entry = projectViewModel.getCurrentEntriesInList().get(viewPager.getCurrentItem());
        projectViewModel.deleteById(entry.getEntryId(), repoId);
        requireActivity().onBackPressed();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_entry_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_delete: {
                deleteEntry();
                break;
            }
            case R.id.action_classify: {
                classifyEntry();
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