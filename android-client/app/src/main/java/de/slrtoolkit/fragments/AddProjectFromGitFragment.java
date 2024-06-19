package de.slrtoolkit.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.MainActivity;
import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.viewmodels.RepoViewModel;
import de.slrtoolkit.views.ProgressButtonCloneProject;
import de.slrtoolkit.worker.CloneWorker;

public class AddProjectFromGitFragment extends Fragment {
    private TextInputEditText edittext_url;
    private TextInputEditText edittext_username;
    private TextInputEditText edittext_token;
    private TextInputEditText edittext_git_name;
    private TextInputEditText edittext_git_email;
    private CardView button_clone_project;
    private ProgressButtonCloneProject progressButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton floatingActionButton = ((MainActivity) requireActivity()).getFloatingActionButton();
        floatingActionButton.setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.fragment_add_project_from_git, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edittext_url = view.findViewById(R.id.edittext_url);
        edittext_username = view.findViewById(R.id.edittext_username);
        edittext_token = view.findViewById(R.id.edittext_token);
        edittext_git_name = view.findViewById(R.id.edittext_git_name);
        edittext_git_email = view.findViewById(R.id.edittext_git_email);
        button_clone_project = view.findViewById(R.id.button_clone_project);
        progressButton = new ProgressButtonCloneProject(getContext(), view);
        button_clone_project.setOnClickListener(cardview_clone_project -> actionCloneRepo(view));

    }

    private void actionCloneRepo(View view) {
        if (TextUtils.isEmpty(edittext_url.getText())) {
            Toast.makeText(requireActivity().getApplicationContext(),
                    getString(R.string.toast_empty_url), Toast.LENGTH_SHORT).show();
            return;
        }

        edittext_url.setEnabled(false);
        edittext_username.setEnabled(false);
        edittext_token.setEnabled(false);
        edittext_git_name.setEnabled(false);
        edittext_git_email.setEnabled(false);
        button_clone_project.setOnClickListener(null);
        progressButton.onLoading();

        Repo repo = new Repo(
                Objects.requireNonNull(edittext_url.getText()).toString(),
                Objects.requireNonNull(edittext_username.getText()).toString(),
                Objects.requireNonNull(edittext_token.getText()).toString(),
                Objects.requireNonNull(edittext_git_name.getText()).toString(),
                Objects.requireNonNull(edittext_git_email.getText()).toString()
        );

        RepoViewModel repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
        int id = (int) repoViewModel.insert(repo);
        try {
            repo = repoViewModel.getRepoDirectly(id);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(AddProjectFromGitFragment.class.getName(), "actionCloneRepo: ", e);
        }

        repoViewModel.setCurrentRepo(repo);
        repo.setLocal_path("repo_" + repo.getId());
        repoViewModel.update(repo);

        WorkRequest cloneWorkRequest =
                new OneTimeWorkRequest.Builder(CloneWorker.class)
                        .setInputData(
                                new Data.Builder()
                                        .putString("REMOTE_URL", repo.getRemote_url())
                                        .putString("USERNAME", repo.getUsername())
                                        .putString("TOKEN", repo.getToken())
                                        .putString("LOCAL_PATH", repo.getLocal_path())
                                        .build()
                        )
                        .build();

        WorkManager workManager = WorkManager.getInstance(view.getContext());
        workManager.enqueue(cloneWorkRequest);
        workManager.getWorkInfoByIdLiveData(cloneWorkRequest.getId())
                .observe(getViewLifecycleOwner(), worker -> {
                    if (worker.getState() == WorkInfo.State.SUCCEEDED) {
                        progressButton.onSucceeded();
                        button_clone_project.setOnClickListener(cardview_clone_project ->
                                NavHostFragment.findNavController(AddProjectFromGitFragment.this)
                                        .navigate(R.id.action_AddProjectFromGitFragment_to_AddProjectSetNameFragment));
                    } else if (worker.getState() == WorkInfo.State.FAILED) {
                        progressButton.onFailed();
                        TextView textview_clone_project_failed = view.findViewById(R.id.textview_clone_project_failed);
                        textview_clone_project_failed.setText(worker.getOutputData().getString("RESULT_MSG"));
                        edittext_url.setEnabled(true);
                        edittext_username.setEnabled(true);
                        edittext_token.setEnabled(true);
                        edittext_git_name.setEnabled(true);
                        edittext_git_email.setEnabled(true);
                        button_clone_project.setOnClickListener(cardview_clone_project -> actionCloneRepo(view));
                    }
                });
    }
}