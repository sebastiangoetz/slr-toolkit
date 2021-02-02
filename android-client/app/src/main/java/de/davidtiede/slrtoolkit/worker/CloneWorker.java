package de.davidtiede.slrtoolkit.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import java.io.File;

public class CloneWorker extends Worker {
    public CloneWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public @NonNull Result doWork() {
        Data.Builder outputData = new Data.Builder();

        String remote_url = getInputData().getString("REMOTE_URL");
        if(remote_url == null) {
            return Result.failure();
        }

        String local_path = getInputData().getString("LOCAL_PATH");
        File path = new File(getApplicationContext().getFilesDir(), local_path);
        boolean isDirectoryCreated = path.exists();
        if(!isDirectoryCreated) {
            isDirectoryCreated = path.mkdir();
        }
        if(!isDirectoryCreated) {
            return Result.failure(outputData.putString("RESULT_MSG", "Could not create directory.").build());
        }

        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(remote_url)
                .setDirectory(path);

        try {
            cloneCommand.call();
        } catch (InvalidRemoteException | TransportException e) {
            return Result.failure(outputData.putString("RESULT_MSG", e.getMessage()).build());
        } catch (GitAPIException e) {
            return Result.failure(outputData.putString("RESULT_MSG", e.getMessage()).build());
        } catch (Throwable e) {
            return Result.failure(outputData.putString("RESULT_MSG", e.getMessage()).build());
        }

        return Result.success();
    }
}
