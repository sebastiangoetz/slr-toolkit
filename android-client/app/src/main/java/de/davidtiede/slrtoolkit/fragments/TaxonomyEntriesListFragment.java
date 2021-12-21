package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;
import de.davidtiede.slrtoolkit.views.BibTexEntriesListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaxonomyEntriesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaxonomyEntriesListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "currentTaxonomyId";
    private static final String ARG_PARAM2 = "repoId";

    private static ProjectViewModel projectViewModel;
    private RecyclerView taxonomyEntriesRecyclerView;
    private BibTexEntriesListAdapter bibTexEntriesListAdapter;
    private BibTexEntriesListAdapter.RecyclerViewClickListener listener;
    private TextView noTaxonomyEntriesTextview;

    private int repoId;
    private int currentTaxonomyId;

    public TaxonomyEntriesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param repoId Parameter 1.
     * @param currentItemId Parameter 2.
     * @return A new instance of fragment TaxonomyEntriesFragment.
     */
    public static TaxonomyEntriesListFragment newInstance(int repoId, int currentItemId) {
        TaxonomyEntriesListFragment fragment = new TaxonomyEntriesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, repoId);
        args.putInt(ARG_PARAM2, currentItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            repoId = getArguments().getInt(ARG_PARAM1);
            currentTaxonomyId = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_taxonomy_entries_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setOnClickListener();
        taxonomyEntriesRecyclerView = view.findViewById(R.id.taxonomyEntriesRecyclerview);
        noTaxonomyEntriesTextview = view.findViewById(R.id.textview_no_taxonomy_entries);
        taxonomyEntriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        bibTexEntriesListAdapter = new BibTexEntriesListAdapter(new BibTexEntriesListAdapter.EntryDiff(), listener, repoId);
        taxonomyEntriesRecyclerView.setAdapter(bibTexEntriesListAdapter);

        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);

        projectViewModel.getTaxonomyWithEntries(repoId, currentTaxonomyId).observe(getViewLifecycleOwner(), this::onLoaded);
    }

    public void onLoaded(TaxonomyWithEntries taxonomyWithEntries) {
        List<Entry> entries = taxonomyWithEntries.entries;
        if(entries.size() == 0) {
            noTaxonomyEntriesTextview.setVisibility(View.VISIBLE);
        } else {
            noTaxonomyEntriesTextview.setVisibility(View.INVISIBLE);
            bibTexEntriesListAdapter.submitList(entries);
        }
    }

    private void setOnClickListener() {
        listener = new BibTexEntriesListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Entry clickedEntry = bibTexEntriesListAdapter.getItemAtPosition(position);
                //TODO: navigate to detail view!
            }
        };
    }
}