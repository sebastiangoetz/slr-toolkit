package de.slrtoolkit.worker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.repositories.RepoRepository;

public class PullWorker extends Worker {

    private final RepoRepository repoRepository;

    public PullWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.repoRepository = new RepoRepository((Application) context);
    }

    @Override
    public @NonNull Result doWork() {
        Data.Builder outputData = new Data.Builder();

        int repoId = getInputData().getInt("REPOID", 0);
        if (repoId == 0) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_pull_failed)
            ).build());
        }

        Repo repo;
        try {
            repo = repoRepository.getRepoByIdDirectly(repoId);
        }
        catch (ExecutionException | InterruptedException e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_pull_failed)
            ).build());
        }

        File path = new File(getApplicationContext().getFilesDir(), repo.getLocal_path());

        Git git;
        try {
            git = Git.open(path);
        } catch (IOException e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_pull_failed)
            ).build());
        }

        PullCommand pullCommand = git.pull();

        if (repo.getUsername() != null && repo.getToken() != null && !repo.getUsername().trim().isEmpty() && !repo.getToken().trim().isEmpty()) {
            UsernamePasswordCredentialsProvider auth = new UsernamePasswordCredentialsProvider(repo.getUsername(), repo.getToken());
            pullCommand.setCredentialsProvider(auth);
        }

        try {
            pullCommand.call();
        } catch (Throwable e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_pull_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        }

        return Result.success();
    }
}
