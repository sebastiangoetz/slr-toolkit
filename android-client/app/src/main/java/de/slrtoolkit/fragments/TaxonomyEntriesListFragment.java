package de.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.slrtoolkit.R;
import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.database.TaxonomyWithEntries;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;
import de.slrtoolkit.views.BibTexEntriesListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaxonomyEntriesListFragment extends Fragment {
    private static TaxonomiesViewModel taxonomiesViewModel;
    int repoId;
    int currentTaxonomyId;
    private BibTexEntriesListAdapter bibTexEntriesListAdapter;
    private BibTexEntriesListAdapter.RecyclerViewClickListener listener;
    private TextView noTaxonomyEntriesTextview;
    private TextView taxonomiesBreadCrumbTextview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_taxonomy_entries_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setOnClickListener();
        taxonomiesViewModel = new ViewModelProvider(requireActivity()).get(TaxonomiesViewModel.class);
        repoId = taxonomiesViewModel.getCurrentRepoId();
        currentTaxonomyId = taxonomiesViewModel.getCurrentTaxonomyId();
        RecyclerView taxonomyEntriesRecyclerView = view.findViewById(R.id.taxonomyEntriesRecyclerview);
        noTaxonomyEntriesTextview = view.findViewById(R.id.textview_no_taxonomy_entries);
        taxonomiesBreadCrumbTextview = view.findViewById(R.id.textview_entries_taxonomies_breadcrumb);
        taxonomyEntriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        bibTexEntriesListAdapter = new BibTexEntriesListAdapter(new BibTexEntriesListAdapter.EntryDiff(), listener, repoId);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(taxonomyEntriesRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        taxonomyEntriesRecyclerView.addItemDecoration(dividerItemDecoration);
        taxonomyEntriesRecyclerView.setAdapter(bibTexEntriesListAdapter);

        taxonomiesViewModel.getTaxonomyWithEntries(repoId, currentTaxonomyId).observe(getViewLifecycleOwner(), this::onLoaded);
    }

    public void setHeader(TaxonomyWithEntries taxonomyWithEntries) {
        if (taxonomyWithEntries != null) {
            String title = getResources().getString(R.string.entries_for_taxonomy) + " " + taxonomyWithEntries.taxonomy.getName();
            taxonomiesBreadCrumbTextview.setText(title);
        }
    }

    public void onLoaded(TaxonomyWithEntries taxonomyWithEntries) {
        setHeader(taxonomyWithEntries);
        List<BibEntry> entries = taxonomyWithEntries.entries;
        taxonomiesViewModel.setCurrentEntriesInList(entries);
        if (entries.isEmpty()) {
            noTaxonomyEntriesTextview.setVisibility(View.VISIBLE);
        } else {
            noTaxonomyEntriesTextview.setVisibility(View.INVISIBLE);
            bibTexEntriesListAdapter.submitList(entries);
        }
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            BibEntry clickedBibEntry = bibTexEntriesListAdapter.getItemAtPosition(position);
            if (clickedBibEntry == null) return;

            taxonomiesViewModel.setCurrentEntryIdForCard(clickedBibEntry.getEntryId());
            int indexOfEntryInOriginalList = taxonomiesViewModel.getCurrentEntriesInList().indexOf(clickedBibEntry);
            taxonomiesViewModel.setCurrentEntryInListCount(indexOfEntryInOriginalList);
            Fragment entryFragment = new BibtexEntriesDetailViewPagerFragment();
            FragmentTransaction ft = TaxonomyEntriesListFragment.this.getParentFragmentManager().beginTransaction();
            ft.replace(R.id.entries_by_taxonomies_fragment_container_view, entryFragment);
            ft.addToBackStack(null);
            ft.commit();
        };
    }
}