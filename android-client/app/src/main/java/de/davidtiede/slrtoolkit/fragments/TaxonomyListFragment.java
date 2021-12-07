package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.viewmodels.EntriesByTaxonomyViewModel;
import de.davidtiede.slrtoolkit.views.TaxonomyListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaxonomyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaxonomyListFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "currentTaxonomyId";
    private static final String ARG_PARAM2 = "repoId";

    private static EntriesByTaxonomyViewModel entriesByTaxonomyViewModel;
    private RecyclerView taxonomyRecyclerView;
    private TaxonomyListAdapter taxonomyListAdapter;
    private TaxonomyListAdapter.RecyclerViewClickListener listener;

    private int repoId;
    private int currentTaxonomyId;

    public TaxonomyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentItemId Parameter 1.
     * @return A new instance of fragment TaxonomyListFragment.
     */
    public static TaxonomyListFragment newInstance(int repoId, int currentItemId) {
        TaxonomyListFragment fragment = new TaxonomyListFragment();
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
        return inflater.inflate(R.layout.fragment_taxonomy_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setOnClickListener();
        taxonomyRecyclerView = view.findViewById(R.id.taxonomyRecyclerview);
        taxonomyRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        taxonomyListAdapter = new TaxonomyListAdapter(new TaxonomyListAdapter.TaxonomyDiff(), listener, repoId);
        taxonomyRecyclerView.setAdapter(taxonomyListAdapter);

        entriesByTaxonomyViewModel = new ViewModelProvider(requireActivity()).get(EntriesByTaxonomyViewModel.class);
        entriesByTaxonomyViewModel.getChildrenForTaxonomy(repoId, currentTaxonomyId).observe(getViewLifecycleOwner(), this::onLoaded);
    }

    public void onLoaded(List<Taxonomy> taxonomyList) {
        taxonomyListAdapter.submitList(taxonomyList);
    }

    private void setOnClickListener() {
        listener = new TaxonomyListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                //TODO: if children
                Taxonomy clickedTaxonomy = taxonomyListAdapter.getItemAtPosition(position);
                Fragment taxonomyFragment = TaxonomyListFragment.newInstance(repoId, clickedTaxonomy.getTaxonomyId());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.taxonomyFragment, taxonomyFragment);
                ft.addToBackStack(null);
                ft.commit();

                //TODO: code if no children -> display entries
            }
        };
    }
}