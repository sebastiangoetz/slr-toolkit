package de.slrtoolkit.fragments;

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

import de.slrtoolkit.ClassificationActivity;
import de.slrtoolkit.ProjectActivity;
import de.slrtoolkit.R;
import de.slrtoolkit.EntriesByTaxonomiesActivity;
import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.viewmodels.ProjectViewModel;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BibtexEntriesDetailViewPagerFragment extends Fragment {
    ViewPager2 viewPager;
    ProjectViewModel projectViewModel;
    TaxonomiesViewModel taxonomiesViewModel;
    int repoId;
    private TextView noEntriesDetails;
    private FragmentStateAdapter pagerAdapter;

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
        taxonomiesViewModel = new ViewModelProvider(requireActivity()).get(TaxonomiesViewModel.class);
        if (requireActivity() instanceof ProjectActivity) {
            repoId = projectViewModel.getCurrentRepoId();
        } else if (requireActivity() instanceof EntriesByTaxonomiesActivity) {
            repoId = taxonomiesViewModel.getCurrentRepoId();
        }
        viewPager = view.findViewById(R.id.entries_detail_viewpager);
        noEntriesDetails = view.findViewById(R.id.textview_no_entries_details);
        pagerAdapter = new BibtexEntriesDetailViewPagerFragment.EntrySlidePagerAdapter(BibtexEntriesDetailViewPagerFragment.this.getActivity(), new ArrayList<>());
        viewPager.setAdapter(pagerAdapter);

        setViewPager();
    }

    private void setViewPager() {
        List<BibEntry> entries = getCurrentEntries();
        if (entries.isEmpty()) {
            noEntriesDetails.setVisibility(View.VISIBLE);
        } else {
            noEntriesDetails.setVisibility(View.INVISIBLE);
            pagerAdapter = new BibtexEntriesDetailViewPagerFragment.EntrySlidePagerAdapter(BibtexEntriesDetailViewPagerFragment.this.getActivity(), entries);
            viewPager.setAdapter(pagerAdapter);
        }
        int currentItemPosition = 0;
        if (requireActivity() instanceof ProjectActivity) {
            currentItemPosition = projectViewModel.getCurrentBibEntryInListCount();
        } else if (requireActivity() instanceof EntriesByTaxonomiesActivity) {
            currentItemPosition = taxonomiesViewModel.getCurrentEntryInListCount();
        }
        if (currentItemPosition != 0 && currentItemPosition < entries.size()) {
            viewPager.setCurrentItem(currentItemPosition, false);
        }
    }

    private void classifyEntry() {
        Intent intent = new Intent(getActivity(), ClassificationActivity.class);
        List<BibEntry> entries = getCurrentEntries();
        int index = viewPager.getCurrentItem();
        if (entries.size() > index) {
            BibEntry bibEntry = entries.get(index);
            intent.putExtra("repo", repoId);
            intent.putExtra("entry", bibEntry.getEntryId());
            startActivity(intent);
        }
    }

    private void deleteEntry() {
        int index = viewPager.getCurrentItem();
        List<BibEntry> entries = getCurrentEntries();
        if (entries.size() > index) {
            BibEntry bibEntry = entries.get(index);
            projectViewModel.deleteBibEntryById(bibEntry.getEntryId(), repoId);
            requireActivity().onBackPressed();
        }
    }

    public List<BibEntry> getCurrentEntries() {
        List<BibEntry> entries = new ArrayList<>();
        if (requireActivity() instanceof ProjectActivity) {
            entries = projectViewModel.getCurrentBibEntriesInList();
        } else if (requireActivity() instanceof EntriesByTaxonomiesActivity) {
            entries = taxonomiesViewModel.getCurrentEntriesInList();
        }
        return entries;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_entry_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        final List<BibEntry> entries;
        final FragmentActivity fa;

        public EntrySlidePagerAdapter(FragmentActivity fa, List<BibEntry> entries) {
            super(fa);
            this.entries = entries;
            this.fa = fa;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            BibEntry bibEntry = entries.get(position);
            if (fa instanceof ProjectActivity) {
                projectViewModel.setCurrentBibEntryIdForCard(bibEntry.getEntryId());
            } else if (fa instanceof EntriesByTaxonomiesActivity) {
                taxonomiesViewModel.setCurrentEntryIdForCard(bibEntry.getEntryId());
            }
            return new BibtexEntryDetailFragment();
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }
    }
}