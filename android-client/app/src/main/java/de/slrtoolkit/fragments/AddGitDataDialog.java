package de.slrtoolkit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Author;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.repositories.RepoRepository;
import de.slrtoolkit.util.FileUtil;
import de.slrtoolkit.viewmodels.RepoViewModel;
import de.slrtoolkit.worker.CloneWorker;

public class AddGitDataDialog extends DialogFragment {

    public static String TAG = "AddGitDataDialog";
    private Button addButton;
    private TextInputEditText remoteUrl;
    private TextInputEditText token;
    private TextInputEditText gitName;
    private TextInputEditText gitEmail;

    private RepoViewModel repoViewModel;

    public interface DialogListener {
        void onDialogDismissed(View view);
    }

    private DialogListener mListener;

    public void setDialogListener(DialogListener listener) {
        mListener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
//        if (mListener != null) {
//            // Pass arguments when triggering the callback
//          //  mListener.onDialogDismissed("view", 42); // Example arguments
//        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);

        View view1 = getLayoutInflater().inflate(R.layout.add_git_dialog, null);
        remoteUrl = view1.findViewById(R.id.editeremoteurl_git);
        token = view1.findViewById(R.id.edittoken_git);
        gitName = view1.findViewById(R.id.editname_git);
        gitEmail = view1.findViewById(R.id.editemail_git);
        addButton = view1.findViewById(R.id.button_add_git_repo);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Repo repo = repoViewModel.getCurrentRepo();
                repo.setRemote_url(remoteUrl.getText().toString());
                repo.setToken(token.getText().toString());
                repo.setGit_name(gitName.getText().toString());
                repo.setGit_email(gitEmail.getText().toString());
                repo.setUsername(gitName.getText().toString());

                repoViewModel.update(repo);

                WorkRequest cloneWorkRequest =
                        new OneTimeWorkRequest.Builder(CloneWorker.class)
                                .setInputData(
                                        new Data.Builder()
                                                .putString("REMOTE_URL", repo.getRemote_url())
                                                .putString("USERNAME", repo.getUsername())
                                                .putString("TOKEN", repo.getToken())
                                                .putString("LOCAL_PATH", repo.getLocal_path() + "git")
                                                .build()
                                )
                                .build();

                WorkManager workManager = WorkManager.getInstance(getContext());
                workManager.enqueue(cloneWorkRequest);

                workManager.getWorkInfoByIdLiveData(cloneWorkRequest.getId())
                        .observe(getParentFragment(), worker -> {
                            if (worker.getState() == WorkInfo.State.SUCCEEDED) {
                                FileUtil fileUtil = new FileUtil();
                                //TODO: stopped here. add files from repo dir to git_repo dir. delete repo dir and rename git_repo dir to repo
//                                File fileSlr = fileUtil.accessFiles(repo.getLocal_path(), getParentFragment().getActivity().getApplication(), ".slrproject");
//                                File fileBib = fileUtil.accessFiles(repo.getLocal_path(), getParentFragment().getActivity().getApplication(), ".bib");
//                                File fileTaxonomy = fileUtil.accessFiles(repo.getLocal_path(), getParentFragment().getActivity().getApplication(), ".taxonomy");
//                                repo.setLocal_path(repo.getLocal_path()+"git");
//                                repoViewModel.update(repo);
//                                try {
////                                    FileUtils.copy(new FileInputStream(fileSlr), new FileOutputStream(repo.getLocal_path() + "git/meta.srlproject"));
////                                    FileUtils.copy(new FileInputStream(fileBib), new FileOutputStream(repo.getLocal_path() + "git/bib.bib"));
////                                    FileUtils.copy(new FileInputStream(fileTaxonomy), new FileOutputStream(repo.getLocal_path() + "git/tax.taxonomy"));
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
                           //     fileUtil.copyFile(new File(""),new File(repo.getLocal_path() + "git" ));

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
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        return builder
                .setView(view1)
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
    }

}
