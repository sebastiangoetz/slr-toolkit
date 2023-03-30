package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.viewmodels.TaxonomiesViewModel;
import de.davidtiede.slrtoolkit.views.TaxonomyListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaxonomyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaxonomyListFragment extends Fragment {

    private static final String ARG_PARAM1 = "currentTaxonomyId";
    private TaxonomiesViewModel taxonomiesViewModel;
    private TaxonomyListAdapter taxonomyListAdapter;
    private TaxonomyListAdapter.RecyclerViewClickListener listener;
    private TextView noTaxonomiesTextview;
    private TextView taxonomiesBreadCrumbTextview;
    ConstraintLayout constraintLayout;
    private int currentTaxonomyId;
    private int repoId;

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
    public static TaxonomyListFragment newInstance(int currentItemId) {
        TaxonomyListFragment fragment = new TaxonomyListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, currentItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentTaxonomyId = getArguments().getInt(ARG_PARAM1);
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
        taxonomiesViewModel = new ViewModelProvider(requireActivity()).get(TaxonomiesViewModel.class);
        repoId = taxonomiesViewModel.getCurrentRepoId();
        RecyclerView taxonomyRecyclerView = view.findViewById(R.id.taxonomyRecyclerview);
        noTaxonomiesTextview = view.findViewById(R.id.textview_no_taxonomies);
        taxonomiesBreadCrumbTextview = view.findViewById(R.id.textview_taxonomies_breadcrumb);
        constraintLayout = view.findViewById(R.id.taxonomy_list_constraint_layout);
        taxonomyRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        taxonomyListAdapter = new TaxonomyListAdapter(new TaxonomyListAdapter.TaxonomyDiff(), listener);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(taxonomyRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        taxonomyRecyclerView.addItemDecoration(dividerItemDecoration);
        taxonomyRecyclerView.setAdapter(taxonomyListAdapter);

        taxonomiesViewModel.getChildrenForTaxonomy(repoId, currentTaxonomyId).observe(getViewLifecycleOwner(), this::onLoaded);
        setHeader();
    }

    public void setHeader() {
        if (currentTaxonomyId > 0) {
            taxonomiesViewModel.getTaxonomyWithEntries(repoId, currentTaxonomyId).observe(getViewLifecycleOwner(), t -> {
                String path = t.taxonomy.getPath();
                if (path.length() > 1) {
                    path = path.replaceAll("#", " > ");
                    if (path.charAt(1) == ">".charAt(0)) {
                        path = path.replaceFirst(" > ", "");
                    }
                    taxonomiesBreadCrumbTextview.setText(path);
                }
            });
        } else {
            taxonomiesBreadCrumbTextview.setVisibility(View.INVISIBLE);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.taxonomyRecyclerview, ConstraintSet.TOP, 0, ConstraintSet.TOP, 0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    public void onLoaded(List<Taxonomy> taxonomyList) {
        if (taxonomyList.size() == 0) {
            noTaxonomiesTextview.setVisibility(View.VISIBLE);
        } else {
            noTaxonomiesTextview.setVisibility(View.INVISIBLE);
            taxonomyListAdapter.submitList(taxonomyList);
        }
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            Taxonomy clickedTaxonomy = taxonomyListAdapter.getItemAtPosition(position);
            if (clickedTaxonomy.isHasChildren()) {
                //there are child taxonomies, display those
                Fragment taxonomyFragment = TaxonomyListFragment.newInstance(clickedTaxonomy.getTaxonomyId());
                FragmentTransaction ft = this.getParentFragmentManager().beginTransaction();
                ft.replace(R.id.taxonomies_fragment_container_view, taxonomyFragment);
                ft.addToBackStack(null);
                ft.commit();
            } else {
                //no child taxonomies, display entries for the taxonomy
                taxonomiesViewModel.setCurrentTaxonomyId(clickedTaxonomy.getTaxonomyId());
                Fragment entriesFragment = new TaxonomyEntriesListFragment();
                FragmentTransaction ft = this.getParentFragmentManager().beginTransaction();
                ft.replace(R.id.taxonomies_fragment_container_view, entriesFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        };
    }
}