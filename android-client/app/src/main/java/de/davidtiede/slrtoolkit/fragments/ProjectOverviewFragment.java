package de.davidtiede.slrtoolkit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.davidtiede.slrtoolkit.AnalyzeActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.TaxonomiesActivity;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectOverviewFragment} factory method to
 * create an instance of this fragment.
 */
public class ProjectOverviewFragment extends Fragment {

    private Button allEntryButton;
    private Button filterButton;
    private Button taxonomyButton;
    private Button classifyButton;
    private Button analyzeButton;
    private ProjectViewModel projectViewModel;
    private int repoId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_overview, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        allEntryButton = view.findViewById(R.id.button_all_entries);
        filterButton = view.findViewById(R.id.button_filter);
        taxonomyButton = view.findViewById(R.id.button_entries_by_taxonomy);
        classifyButton = view.findViewById(R.id.button_classify);
        analyzeButton = view.findViewById(R.id.button_analyze);
        projectViewModel = new ViewModelProvider(getActivity()).get(ProjectViewModel.class);
        repoId = projectViewModel.getCurrentRepoId();

                allEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProjectOverviewFragment.this)
                        .navigate(R.id.action_projectOverviewFragment_to_bibtexEntriesListFragment);
            }
        });

        filterButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProjectOverviewFragment.this)
                    .navigate(R.id.action_projectOverviewFragment_to_filterFragment);
        });

        taxonomyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TaxonomiesActivity.class);
            intent.putExtra("repo", projectViewModel.getCurrentRepoId());
            startActivity(intent);
            ((Activity) getActivity()).overridePendingTransition(0, 0);
        });

        classifyButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProjectOverviewFragment.this)
                    .navigate(R.id.action_projectOverviewFragment_to_entriesToClassifyViewPagerFragment);
        });

        analyzeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AnalyzeActivity.class);
            intent.putExtra("repo", projectViewModel.getCurrentRepoId());
            startActivity(intent);
            getActivity().overridePendingTransition(0,0);
        });

        final Observer entryAmountObserver = (Observer<Integer>) amount -> allEntryButton.setText("All Entries (" + amount.toString() + ")");
        projectViewModel.getEntryAmount(repoId).observe(getViewLifecycleOwner(), entryAmountObserver);

        final Observer openEntryAmountObserver = (Observer<Integer>) amount -> filterButton.setText("Filter (" + amount.toString() + ")");
        projectViewModel.getOpenEntryAmount(repoId).observe(getViewLifecycleOwner(), openEntryAmountObserver);

        final Observer entriesToClassifyObserver = (Observer<Integer>) amount -> classifyButton.setText("Classify (" + amount.toString() + ")");
        projectViewModel.getEntriesWithoutTaxonomiesCount(repoId).observe(getViewLifecycleOwner(), entriesToClassifyObserver);
    }
}