package de.davidtiede.slrtoolkit.fragments;

import android.graphics.Color;
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
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.viewmodels.ClassificationViewModel;
import de.davidtiede.slrtoolkit.views.TaxonomyClassificationListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassificationFragment extends Fragment {

    private static final String ARG_PARAM1 = "currentTaxonomy";
    private int currentTaxonomy;
    private int repoId;
    private int entryId;
    private RecyclerView taxonomyRecyclerView;
    private TaxonomyClassificationListAdapter taxonomyListAdapter;
    private TaxonomyClassificationListAdapter.RecyclerViewClickListener listener;
    private ClassificationViewModel classificationViewModel;

    public ClassificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentTaxonomy Parameter 1.
     * @return A new instance of fragment ClassificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassificationFragment newInstance(int currentTaxonomy) {
        ClassificationFragment fragment = new ClassificationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, currentTaxonomy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentTaxonomy = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classification, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setOnClickListener();
        classificationViewModel = new ViewModelProvider(requireActivity()).get(ClassificationViewModel.class);
        repoId = classificationViewModel.getCurrentRepoId();
        entryId = classificationViewModel.getCurrentEntryId();
        taxonomyRecyclerView = view.findViewById(R.id.taxonomy_classification_recyclerview);
        taxonomyRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        taxonomyListAdapter = new TaxonomyClassificationListAdapter(new TaxonomyClassificationListAdapter.TaxonomyDiff(), listener, repoId);
        taxonomyRecyclerView.setAdapter(taxonomyListAdapter);

        classificationViewModel.getChildTaxonomiesWithEntries(repoId, currentTaxonomy).observe(getViewLifecycleOwner(), this::onLoaded);
    }

    public void onLoaded(List<TaxonomyWithEntries> taxonomyList) {
        taxonomyListAdapter.submitList(taxonomyList);
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            TaxonomyWithEntries clickedTaxonomy = taxonomyListAdapter.getItemAtPosition(position);
            if(clickedTaxonomy.taxonomy.isHasChildren()) {
                Fragment classificationFragment = ClassificationFragment.newInstance(clickedTaxonomy.taxonomy.getTaxonomyId());
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.classification_fragment_container_view, classificationFragment);
                ft.addToBackStack(null);
                ft.commit();
            } else {
                boolean entryContainsTaxonomy = false;
                for(Entry entry :clickedTaxonomy.entries) {
                    if(entry.getId() == entryId) {
                        entryContainsTaxonomy = true;
                    }
                }
                if(entryContainsTaxonomy) {
                    v.setBackgroundColor(Color.WHITE);
                    //TODO: delete taxonomy from entry
                } else {
                    v.setBackgroundColor(Color.BLUE);
                    //TODO: add taxonomy to entry
                }
            }
        };
    }
}