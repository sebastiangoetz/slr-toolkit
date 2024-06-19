package de.slrtoolkit.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.util.FileUtil;
import de.slrtoolkit.viewmodels.RepoViewModel;
import de.slrtoolkit.worker.InitWorker;

public class AddGitDataDialog extends DialogFragment {

    public static final String TAG = "AddGitDataDialog";
    private TextInputEditText remoteUrl;
    private TextInputEditText token;
    private TextInputEditText gitName;
    private TextInputEditText gitEmail;

    private RepoViewModel repoViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);

        View view1 = getLayoutInflater().inflate(R.layout.dialog_add_project_to_git, null);
        remoteUrl = view1.findViewById(R.id.editeremoteurl_git);
        token = view1.findViewById(R.id.edittoken_git);
        gitName = view1.findViewById(R.id.editname_git);
        gitEmail = view1.findViewById(R.id.editemail_git);
        Button addButton = view1.findViewById(R.id.button_add_git_repo);

        addButton.setOnClickListener(view -> {
            Repo repo = repoViewModel.getCurrentRepo();
            repo.setRemote_url(remoteUrl.getText().toString());
            repo.setToken(token.getText().toString());
            repo.setGit_name(gitName.getText().toString());
            repo.setGit_email(gitEmail.getText().toString());
            repo.setUsername(gitName.getText().toString());


            repoViewModel.update(repo);

            FileUtil fileUtil = new FileUtil();
            File fileSlr = fileUtil.accessFiles(repo.getLocal_path(), getParentFragment().getActivity().getApplication(), ".slrproject");
            File fileBib = fileUtil.accessFiles(repo.getLocal_path(), getParentFragment().getActivity().getApplication(), ".bib");
            File fileTaxonomy = fileUtil.accessFiles(repo.getLocal_path(), getParentFragment().getActivity().getApplication(), ".taxonomy");

            File newFileSlr = new File(getParentFragment().getActivity().getApplication().getApplicationContext().getFilesDir(), repo.getLocal_path() + "git/"+fileSlr.getName());
            File newFileBib = new File(getParentFragment().getActivity().getApplication().getApplicationContext().getFilesDir(), repo.getLocal_path() + "git/"+fileBib.getName());
            File newFileTaxonomy = new File(getParentFragment().getActivity().getApplication().getApplicationContext().getFilesDir(), repo.getLocal_path() + "git/"+fileTaxonomy.getName());

            WorkRequest initWorkRequest =
                    new OneTimeWorkRequest.Builder(InitWorker.class)
                            .setInputData(
                                    new Data.Builder()
                                            .putString("REMOTE_URL", repo.getRemote_url())
                                            .putString("USERNAME", repo.getUsername())
                                            .putString("EMAIL", repo.getGit_email())
                                            .putString("TOKEN", repo.getToken())
                                            .putString("LOCAL_PATH", repo.getLocal_path())
                                            .putString("LOCAL_PATH_GIT", repo.getLocal_path()+"git")
                                            .putString("PATH_SLR", fileSlr.toString())
                                            .putString("PATH_BIB", fileBib.toString())
                                            .putString("PATH_TAX", fileTaxonomy.toString())
                                            .putString("NEW_PATH_SLR", newFileSlr.toString())
                                            .putString("NEW_PATH_BIB", newFileBib.toString())
                                            .putString("NEW_PATH_TAX", newFileTaxonomy.toString())
                                            .build()
                            )
                            .build();

            WorkManager workManager = WorkManager.getInstance(getContext());
            workManager.enqueue(initWorkRequest);

            workManager.getWorkInfoByIdLiveData(initWorkRequest.getId())
                    .observe(getParentFragment(), worker -> {
                        if (worker.getState() == WorkInfo.State.SUCCEEDED) {

                                repo.setLocal_path(repo.getLocal_path() + "git");
                                repoViewModel.update(repo);


                            Toast.makeText(view.getContext(),
                                    getString(R.string.toast_commit_succeeded),
                                    Toast.LENGTH_SHORT).show();
                        } else if (worker.getState() == WorkInfo.State.FAILED) {
                            Toast.makeText(view.getContext(),
                                    worker.getOutputData().getString("RESULT_MSG"),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

            dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        return builder
                .setView(view1)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create();
    }

}
