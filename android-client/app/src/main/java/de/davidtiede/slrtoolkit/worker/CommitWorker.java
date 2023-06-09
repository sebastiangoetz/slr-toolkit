package de.davidtiede.slrtoolkit.worker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.repositories.RepoRepository;

public class CommitWorker extends Worker {

    private final RepoRepository repoRepository;

    public CommitWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.repoRepository = new RepoRepository((Application) context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data.Builder outputData = new Data.Builder();

        int repoId = getInputData().getInt("REPOID", 0);
        if (repoId == 0) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_commit_failed)
            ).build());
        }

        Repo repo;
        try {
            repo = repoRepository.getRepoByIdDirectly(repoId);
        } catch (ExecutionException | InterruptedException e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_commit_failed)
            ).build());
        }

        String gitName = repo.getGit_name();
        String gitEmail = repo.getGit_email();

        if (gitName.isEmpty()) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_commit_git_name)
            ).build());
        }

        if (gitEmail.isEmpty()) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_commit_git_email)
            ).build());
        }

        String gitMessage = getInputData().getString("GIT_MESSAGE");
        if (gitMessage == null || gitMessage.isEmpty()) {
            gitMessage = "Commit by Android-App";
            /*
             return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_commit_git_message)
             ).build());
            */
        }

        File path = new File(getApplicationContext().getFilesDir(), repo.getLocal_path());

        Git git;
        try {
            git = Git.open(path);
        } catch (IOException e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_commit_failed)
            ).build());
        }

        CommitCommand commitCommand = git.commit();
        commitCommand.setCommitter(gitName, gitEmail).setAll(true).setMessage(gitMessage);

        try {
            commitCommand.call();
        } catch (Exception e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_commit_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        }

        return Result.success();
    }
}
