package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.viewmodels.ClassificationViewModel;
import de.davidtiede.slrtoolkit.views.TaxonomyClassificationListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassificationFragment extends Fragment {

    private static final String ARG_PARAM1 = "currentTaxonomy";
    private int currentTaxonomy;
    private int entryId;
    private TaxonomyClassificationListAdapter taxonomyListAdapter;
    private TaxonomyClassificationListAdapter.RecyclerViewClickListener listener;
    private ClassificationViewModel classificationViewModel;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentTaxonomy Parameter 1.
     * @return A new instance of fragment ClassificationFragment.
     */
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
        int repoId = classificationViewModel.getCurrentRepoId();
        entryId = classificationViewModel.getCurrentEntryId();
        RecyclerView taxonomyRecyclerView = view.findViewById(R.id.taxonomy_classification_recyclerview);
        taxonomyRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        taxonomyListAdapter = new TaxonomyClassificationListAdapter(new TaxonomyClassificationListAdapter.TaxonomyDiff(), listener, true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(taxonomyRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        taxonomyRecyclerView.addItemDecoration(dividerItemDecoration);
        taxonomyRecyclerView.setAdapter(taxonomyListAdapter);

        try {
            List<TaxonomyWithEntries> taxonomyWithEntries = classificationViewModel.getTaxonomyWithEntriesDirectly(repoId, currentTaxonomy);
            setSelectedTaxonomies(taxonomyWithEntries);
            taxonomyListAdapter.submitList(taxonomyWithEntries);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setSelectedTaxonomies(List<TaxonomyWithEntries> taxonomyWithEntries) {
        ArrayList<Integer> selectedTaxonomies = new ArrayList<>();
        for (int i = 0; i < taxonomyWithEntries.size(); i++) {
            TaxonomyWithEntries currentTaxWithEntries = taxonomyWithEntries.get(i);
            for (Entry entry : currentTaxWithEntries.entries) {
                if (entry.getEntryId() == entryId) {
                    selectedTaxonomies.add(currentTaxWithEntries.taxonomy.getTaxonomyId());
                }
            }
        }
        classificationViewModel.setSelectedTaxonomies(selectedTaxonomies);
        taxonomyListAdapter.setCurrentTaxonomyIds(classificationViewModel.getSelectedTaxonomies());
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            TaxonomyWithEntries clickedTaxonomy = taxonomyListAdapter.getItemAtPosition(position);
            if (clickedTaxonomy.taxonomy.isHasChildren()) {
                Fragment classificationFragment = ClassificationFragment.newInstance(clickedTaxonomy.taxonomy.getTaxonomyId());
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.classification_fragment_container_view, classificationFragment);
                ft.addToBackStack(null);
                ft.commit();
            } else {
                boolean entryContainsTaxonomy = false;
                List<Integer> selectedTaxonomyIds = classificationViewModel.getSelectedTaxonomies();
                for (int taxId : selectedTaxonomyIds) {
                    if (taxId == clickedTaxonomy.taxonomy.getTaxonomyId()) {
                        entryContainsTaxonomy = true;
                        break;
                    }
                }
                if (entryContainsTaxonomy) {
                    classificationViewModel.delete(clickedTaxonomy.taxonomy.getTaxonomyId(), entryId);

                } else {
                    classificationViewModel.insertEntryForTaxonomy(clickedTaxonomy.taxonomy.getTaxonomyId(), entryId);
                }
                taxonomyListAdapter.setCurrentTaxonomyIds(classificationViewModel.getSelectedTaxonomies());
            }
        };
    }
}