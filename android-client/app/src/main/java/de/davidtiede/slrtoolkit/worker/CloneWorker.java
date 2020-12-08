package de.davidtiede.slrtoolkit.worker;

import android.content.Context;

import androidx.annotation.NonNull;
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
    public Result doWork() {
        File path = new File(getApplicationContext().getFilesDir(),"testrepo");
        if (!path.exists()) {
            path.mkdir();
        }

        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI("https://github.com/tidenhub/urlwatch")
                .setDirectory(path);

        try {
            cloneCommand.call();
        } catch (InvalidRemoteException e) {
            return Result.failure();
        } catch (TransportException e) {
            return Result.failure();
        } catch (GitAPIException e) {
            return Result.failure();
        } catch (Throwable e) {
            return Result.failure();
        }

        return Result.success();
    }
}
