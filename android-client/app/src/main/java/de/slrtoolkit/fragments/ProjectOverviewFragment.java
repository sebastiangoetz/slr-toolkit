package de.slrtoolkit.fragments;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.ExecutionException;

import de.slrtoolkit.AnalyzeActivity;
import de.slrtoolkit.ManageTaxonomyActivity;
import de.slrtoolkit.R;
import de.slrtoolkit.TaxonomiesActivity;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.viewmodels.ProjectViewModel;
import de.slrtoolkit.viewmodels.RepoViewModel;
import de.slrtoolkit.worker.CommitWorker;
import de.slrtoolkit.worker.PullWorker;
import de.slrtoolkit.worker.PushWorker;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectOverviewFragment extends Fragment {

    private Button allEntryButton;
    private Button filterButton;

    private Button pullButton;
    private Button commitButton;
    private Button pushButton;

    private Button classifyButton;
    private ProjectViewModel projectViewModel;
    private TextView projectNameTextView;
    private RepoViewModel repoViewModel;

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

        ImageButton editMetadataButton = view.findViewById(R.id.button_edit_project_metadata);

        pullButton = view.findViewById(R.id.button_pull);
        commitButton = view.findViewById(R.id.button_commit);
        pushButton = view.findViewById(R.id.button_push);

        Button manageTaxonomyButton = view.findViewById(R.id.button_manage_taxonomy);

        allEntryButton = view.findViewById(R.id.button_all_entries);
        Button taxonomyButton = view.findViewById(R.id.button_entries_by_taxonomy);
        Button analyzeButton = view.findViewById(R.id.button_analyze);

        projectNameTextView = view.findViewById(R.id.project_name_text_view);
        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);
        int repoId = projectViewModel.getCurrentRepoId();
        repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);

        classifyButton.setOnClickListener(v -> findNavController(ProjectOverviewFragment.this)
                .navigate(R.id.action_projectOverviewFragment_to_entriesToClassifyViewPagerFragment));

        filterButton.setOnClickListener(v -> findNavController(ProjectOverviewFragment.this)
                .navigate(R.id.action_projectOverviewFragment_to_filterFragment));

        editMetadataButton.setOnClickListener(v -> findNavController(ProjectOverviewFragment.this).navigate(R.id.action_projectOverviewFragment_to_editProjectMetadataFragment));

        pullButton.setOnClickListener(v -> actionPullRepo(view));
        commitButton.setOnClickListener(v -> {
            try {
                repoViewModel.setCurrentRepo(repoViewModel.getRepoDirectly(projectViewModel.getCurrentRepoId()));
                Repo currentRepo = repoViewModel.getCurrentRepo();
                if (currentRepo.getRemote_url().equals("") || currentRepo.getGit_name().equals("") || currentRepo.getToken().equals("") || currentRepo.getGit_email().equals("")) {
                    AddGitDataDialog dialog = new AddGitDataDialog();
                   // dialog.setDialogListener(this);
                    dialog.show(getChildFragmentManager(), AddGitDataDialog.TAG);
                } else {
                    actionCommitRepo(view);
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        pushButton.setOnClickListener(v -> actionPushRepo(view));

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

        manageTaxonomyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ManageTaxonomyActivity.class);
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

        WorkRequest workRequest =
                new OneTimeWorkRequest.Builder(PullWorker.class)
                        .setInputData(
                                new Data.Builder()
                                        .putInt("REPOID", projectViewModel.getCurrentRepoId())
                                        .build()
                        )
                        .build();

        WorkManager workManager = WorkManager.getInstance(view.getContext());
        workManager.enqueue(workRequest);
        workManager.getWorkInfoByIdLiveData(workRequest.getId())
                .observe(getViewLifecycleOwner(), worker -> {
                    if (worker.getState() == WorkInfo.State.SUCCEEDED) {
                        pullButton.setEnabled(true);
                        Toast.makeText(view.getContext(),
                                getString(R.string.toast_pull_succeeded),
                                Toast.LENGTH_SHORT).show();
                    } else if (worker.getState() == WorkInfo.State.FAILED) {
                        pullButton.setEnabled(true);
                        Toast.makeText(view.getContext(),
                                worker.getOutputData().getString("RESULT_MSG"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void actionCommitRepo(View view) {
        commitButton.setEnabled(false);

        WorkRequest workRequest =
                new OneTimeWorkRequest.Builder(CommitWorker.class)
                        .setInputData(
                                new Data.Builder()
                                        .putInt("REPOID", projectViewModel.getCurrentRepoId())
                                        .build()
                        )
                        .build();

        WorkManager workManager = WorkManager.getInstance(view.getContext());
        workManager.enqueue(workRequest);
        workManager.getWorkInfoByIdLiveData(workRequest.getId())
                .observe(getViewLifecycleOwner(), worker -> {
                    if (worker.getState() == WorkInfo.State.SUCCEEDED) {
                        commitButton.setEnabled(true);
                        Toast.makeText(view.getContext(),
                                getString(R.string.toast_commit_succeeded),
                                Toast.LENGTH_SHORT).show();
                    } else if (worker.getState() == WorkInfo.State.FAILED) {
                        commitButton.setEnabled(true);
                        Toast.makeText(view.getContext(),
                                worker.getOutputData().getString("RESULT_MSG"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void actionPushRepo(View view) {
        pushButton.setEnabled(false);

        WorkRequest workRequest =
                new OneTimeWorkRequest.Builder(PushWorker.class)
                        .setInputData(
                                new Data.Builder()
                                        .putInt("REPOID", projectViewModel.getCurrentRepoId())
                                        .build()
                        )
                        .build();

        WorkManager workManager = WorkManager.getInstance(view.getContext());
        workManager.enqueue(workRequest);
        workManager.getWorkInfoByIdLiveData(workRequest.getId())
                .observe(getViewLifecycleOwner(), worker -> {
                    if (worker.getState() == WorkInfo.State.SUCCEEDED) {
                        pushButton.setEnabled(true);
                        Toast.makeText(view.getContext(),
                                getString(R.string.toast_push_succeeded),
                                Toast.LENGTH_SHORT).show();
                    } else if (worker.getState() == WorkInfo.State.FAILED) {
                        pushButton.setEnabled(true);
                        Toast.makeText(view.getContext(),
                                worker.getOutputData().getString("RESULT_MSG"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}