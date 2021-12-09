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

import java.util.List;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;
import de.davidtiede.slrtoolkit.views.TaxonomyListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaxonomyClassificationFragment extends Fragment {
    private static ProjectViewModel projectViewModel;
    private RecyclerView taxonomyRecyclerView;
    private TaxonomyListAdapter taxonomyListAdapter;
    private TaxonomyListAdapter.RecyclerViewClickListener listener;

    private int repoId;
    private int currentTaxonomyId;
    private int entryId;

    public TaxonomyClassificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_taxonomy_classification, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setOnClickListener();
        taxonomyRecyclerView = view.findViewById(R.id.taxonomyRecyclerview);
        taxonomyRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        taxonomyListAdapter = new TaxonomyListAdapter(new TaxonomyListAdapter.TaxonomyDiff(), listener, repoId);
        taxonomyRecyclerView.setAdapter(taxonomyListAdapter);
        repoId = projectViewModel.getCurrentRepoId();

        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);
        projectViewModel.getChildrenForTaxonomy(repoId, currentTaxonomyId).observe(getViewLifecycleOwner(), this::onLoaded);
    }

    public void onLoaded(List<Taxonomy> taxonomyList) {
        taxonomyListAdapter.submitList(taxonomyList);
    }

    private void setOnClickListener() {
        listener = new TaxonomyListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Taxonomy clickedTaxonomy = taxonomyListAdapter.getItemAtPosition(position);
                if(clickedTaxonomy.isHasChildren()) {
                    //there are child taxonomies, display those
                    /*Fragment taxonomyFragment = TaxonomyListFragment.newInstance(repoId, clickedTaxonomy.getTaxonomyId());
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.taxonomyFragment, taxonomyFragment);
                    ft.addToBackStack(null);
                    ft.commit();*/
                } else {
                    //no child taxonomies, display entries for the taxonomy
                    /*Fragment entriesFragment = TaxonomyListFragment.newInstance(repoId, clickedTaxonomy.getTaxonomyId());
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.taxonomyFragment, entriesFragment);
                    ft.addToBackStack(null);
                    ft.commit();*/
                }
            }
        };
    }
}