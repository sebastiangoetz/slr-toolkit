package de.davidtiede.slrtoolkit.fragments;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.davidtiede.slrtoolkit.AnalyzeActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.TaxonomiesActivity;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectOverviewFragment extends Fragment {

    private Button allEntryButton;
    private Button filterButton;

    private Button pullButton;
    private Button commitButton;
    private Button classifyButton;
    private ProjectViewModel projectViewModel;
    private TextView projectNameTextView;


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
        classifyButton = view.findViewById(R.id.button_classify);
        filterButton = view.findViewById(R.id.button_filter);

        pullButton = view.findViewById(R.id.button_pull);
        commitButton = view.findViewById(R.id.button_commit);

        allEntryButton = view.findViewById(R.id.button_all_entries);
        Button taxonomyButton = view.findViewById(R.id.button_entries_by_taxonomy);
        Button analyzeButton = view.findViewById(R.id.button_analyze);

        projectNameTextView = view.findViewById(R.id.project_name_text_view);
        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);
        int repoId = projectViewModel.getCurrentRepoId();


        classifyButton.setOnClickListener(v -> findNavController(ProjectOverviewFragment.this)
                .navigate(R.id.action_projectOverviewFragment_to_entriesToClassifyViewPagerFragment));

        filterButton.setOnClickListener(v -> findNavController(ProjectOverviewFragment.this)
                .navigate(R.id.action_projectOverviewFragment_to_filterFragment));

        pullButton.setOnClickListener(v -> actionPullRepo(view));

        commitButton.setOnClickListener(v -> actionCommitRepo(view));

        allEntryButton.setOnClickListener(v -> findNavController(ProjectOverviewFragment.this)
                .navigate(R.id.action_projectOverviewFragment_to_bibtexEntriesListFragment));

        taxonomyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TaxonomiesActivity.class);
            intent.putExtra("repo", projectViewModel.getCurrentRepoId());
            startActivity(intent);
            (requireActivity()).overridePendingTransition(0, 0);
        });

        analyzeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AnalyzeActivity.class);
            intent.putExtra("repo", projectViewModel.getCurrentRepoId());
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);
        });


        final Observer<Repo> repoTitleObserver = repo -> projectNameTextView.setText(repo.getName());
        projectViewModel.getRepoById(repoId).observe(getViewLifecycleOwner(), repoTitleObserver);

        final Observer<Integer> entriesToClassifyObserver =
                amount -> classifyButton.setText(getResources().getString(R.string.button_classify) + " (" + amount.toString() + ")");
        projectViewModel.getEntriesWithoutTaxonomiesCount(repoId).observe(getViewLifecycleOwner(), entriesToClassifyObserver);

        final Observer<Integer> openEntryAmountObserver =
                amount -> filterButton.setText(getResources().getString(R.string.button_filter) + " (" + amount.toString() + ")");
        projectViewModel.getOpenEntryAmount(repoId).observe(getViewLifecycleOwner(), openEntryAmountObserver);

        final Observer<Integer> entryAmountObserver =
                amount -> allEntryButton.setText(getResources().getString(R.string.button_all_entries) + " (" + amount.toString() + ")");
        projectViewModel.getEntryAmount(repoId).observe(getViewLifecycleOwner(), entryAmountObserver);
    }

    private void actionPullRepo(View view) {
        pullButton.setEnabled(false);
    }

    private void actionCommitRepo(View view) {
        commitButton.setEnabled(false);
    }
}