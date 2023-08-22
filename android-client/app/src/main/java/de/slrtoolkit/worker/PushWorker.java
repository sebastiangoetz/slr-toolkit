package de.slrtoolkit.worker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.repositories.RepoRepository;

public class PushWorker extends Worker {

    private final RepoRepository repoRepository;

    public PushWorker(
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
                    getApplicationContext().getString(R.string.error_push_failed)
            ).build());
        }

        Repo repo;
        try {
            repo = repoRepository.getRepoByIdDirectly(repoId);
        }
        catch (ExecutionException | InterruptedException e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_push_failed)
            ).build());
        }

        File path = new File(getApplicationContext().getFilesDir(), repo.getLocal_path());

        Git git;
        try {
            git = Git.open(path);
        } catch (IOException e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_push_failed)
            ).build());
        }

        PushCommand pushCommand = git.push();

        if (repo.getUsername() != null && repo.getToken() != null && !repo.getUsername().trim().isEmpty() && !repo.getToken().trim().isEmpty()) {
            UsernamePasswordCredentialsProvider auth = new UsernamePasswordCredentialsProvider(repo.getUsername(), repo.getToken());
            pushCommand.setCredentialsProvider(auth);
        }

        try {
            pushCommand.call();
        } catch (InvalidRemoteException | TransportException e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_push_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        } catch (Exception e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_push_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        } catch (Throwable e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_push_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        }

        return Result.success();
    }
}
