package de.davidtiede.slrtoolkit.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.viewmodels.AnalyzeViewModel;
import de.davidtiede.slrtoolkit.views.TaxonomyClassificationListAdapter;
import de.davidtiede.slrtoolkit.views.TaxonomyListAdapter;

public class TaxonomySelectionDialogFragment extends DialogFragment {
    public static String TAG = "PurchaseConfirmationDialog";
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
        setListView();
        initListViewData();
        return dialog;
    }

    private void setOnConfirmButtonListener() {
        confirmButton.setOnClickListener(v -> dismiss());
    }

    private void setListView() {

    }

    private void initListViewData()  {
        int parentTaxonomyId = analyzeViewModel.getParentTaxonomyToDisplayChildrenFor1();
        int repoId = analyzeViewModel.getCurrentRepoId();
        List<TaxonomyWithEntries> taxonomies = new ArrayList<>();
        try {
            taxonomies = analyzeViewModel.getChildTaxonomiesForTaxonomyId(repoId, parentTaxonomyId);
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        setOnClickListener();
        System.out.println("Hello");
        System.out.println(recyclerView);
        System.out.println(taxonomies);

        // android.R.layout.simple_list_item_checked:
        // ListItem is very simple (Only one CheckedTextView).
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        taxonomyListAdapter = new TaxonomyClassificationListAdapter(new TaxonomyClassificationListAdapter.TaxonomyDiff(), listener);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(taxonomyListAdapter);
        setSelectedTaxonomies();
        taxonomyListAdapter.submitList(taxonomies);
        taxonomyListAdapter.notifyDataSetChanged();

    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            TaxonomyWithEntries clickedTaxonomy = taxonomyListAdapter.getItemAtPosition(position);
        };
    }

    public void setSelectedTaxonomies() {
        List<TaxonomyWithEntries> selectedTaxonomies = analyzeViewModel.getChildTaxonomiesToDisplay1();
        ArrayList<Integer> selectedTaxonomiesIds = new ArrayList<>();

        for(TaxonomyWithEntries selectedTaxonomy: selectedTaxonomies) {
            selectedTaxonomiesIds.add(selectedTaxonomy.taxonomy.getTaxonomyId());
        }

        taxonomyListAdapter.setCurrentTaxonomyIds(selectedTaxonomiesIds);
    }
}
