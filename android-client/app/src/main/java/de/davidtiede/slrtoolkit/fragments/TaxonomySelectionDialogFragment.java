package de.davidtiede.slrtoolkit.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.viewmodels.AnalyzeViewModel;
import de.davidtiede.slrtoolkit.views.TaxonomyClassificationListAdapter;

public class TaxonomySelectionDialogFragment extends DialogFragment {
    public static String TAG = "TaxonomySelectionDialog";
    private RecyclerView recyclerView;
    private Button confirmButton;
    AnalyzeViewModel analyzeViewModel;
    private TaxonomyClassificationListAdapter.RecyclerViewClickListener listener;
    TaxonomyClassificationListAdapter taxonomyListAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        analyzeViewModel = new ViewModelProvider(requireActivity()).get(AnalyzeViewModel.class);
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.fragment_taxonomy_selection_dialog);
        recyclerView = dialog.findViewById(R.id.taxonomy_select_list_view);
        confirmButton = dialog.findViewById(R.id.confirm_taxonomies_button);
        setOnConfirmButtonListener();
        initListViewData();
        return dialog;
    }

    private void setOnConfirmButtonListener() {
        confirmButton.setOnClickListener(v -> dismiss());
    }

    private void initListViewData() {
        int parentTaxonomyId = 0;
        if (analyzeViewModel.getCurrentTaxonomySpinner() == 1) {
            parentTaxonomyId = analyzeViewModel.getParentTaxonomyToDisplayChildrenFor1();
        } else if (analyzeViewModel.getCurrentTaxonomySpinner() == 2) {
            parentTaxonomyId = analyzeViewModel.getParentTaxonomyToDisplayChildrenFor2();
        }
        int repoId = analyzeViewModel.getCurrentRepoId();
        List<TaxonomyWithEntries> taxonomies = new ArrayList<>();
        try {
            taxonomies = analyzeViewModel.getChildTaxonomiesForTaxonomyId(repoId, parentTaxonomyId);
        } catch (ExecutionException | InterruptedException exception) {
            exception.printStackTrace();
        }

        setOnClickListener();

        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        taxonomyListAdapter = new TaxonomyClassificationListAdapter(new TaxonomyClassificationListAdapter.TaxonomyDiff(), listener, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(taxonomyListAdapter);
        setSelectedTaxonomies();
        taxonomyListAdapter.submitList(taxonomies);
        taxonomyListAdapter.notifyDataSetChanged();

    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            TaxonomyWithEntries clickedTaxonomy = taxonomyListAdapter.getItemAtPosition(position);
            List<TaxonomyWithEntries> selectedTaxonomies = getSelectedTaxonomies();
            List<TaxonomyWithEntries> updatedSelectedTaxonomies = new ArrayList<>();
            List<Integer> selectedTaxonomiesIds = getTaxonomyIds(selectedTaxonomies);

            if (selectedTaxonomiesIds.contains(clickedTaxonomy.taxonomy.getTaxonomyId())) {
                for (TaxonomyWithEntries taxonomyWithEntries : selectedTaxonomies) {
                    if (taxonomyWithEntries.taxonomy.getTaxonomyId() != clickedTaxonomy.taxonomy.getTaxonomyId()) {
                        updatedSelectedTaxonomies.add(taxonomyWithEntries);
                    }
                }
            } else {
                updatedSelectedTaxonomies.addAll(selectedTaxonomies);
                updatedSelectedTaxonomies.add(clickedTaxonomy);
            }

            if (analyzeViewModel.getCurrentTaxonomySpinner() == 1) {
                analyzeViewModel.setChildTaxonomiesToDisplay1(updatedSelectedTaxonomies);
            } else if (analyzeViewModel.getCurrentTaxonomySpinner() == 2) {
                analyzeViewModel.setChildTaxonomiesToDisplay2(updatedSelectedTaxonomies);
            }
            setSelectedTaxonomies();
        };
    }

    public void setSelectedTaxonomies() {
        List<TaxonomyWithEntries> selectedTaxonomies = getSelectedTaxonomies();
        List<Integer> selectedTaxonomiesIds = getTaxonomyIds(selectedTaxonomies);

        taxonomyListAdapter.setCurrentTaxonomyIds(selectedTaxonomiesIds);
    }

    public List<Integer> getTaxonomyIds(List<TaxonomyWithEntries> taxonomyWithEntries) {
        ArrayList<Integer> selectedTaxonomiesIds = new ArrayList<>();

        for (TaxonomyWithEntries selectedTaxonomy : taxonomyWithEntries) {
            selectedTaxonomiesIds.add(selectedTaxonomy.taxonomy.getTaxonomyId());
        }

        return selectedTaxonomiesIds;
    }

    public List<TaxonomyWithEntries> getSelectedTaxonomies() {
        List<TaxonomyWithEntries> selectedTaxonomies = new ArrayList<>();

        if (analyzeViewModel.getCurrentTaxonomySpinner() == 1) {
            selectedTaxonomies = analyzeViewModel.getChildTaxonomiesToDisplay1();

        } else if (analyzeViewModel.getCurrentTaxonomySpinner() == 2) {
            selectedTaxonomies = analyzeViewModel.getChildTaxonomiesToDisplay2();
        }
        return selectedTaxonomies;
    }
}
