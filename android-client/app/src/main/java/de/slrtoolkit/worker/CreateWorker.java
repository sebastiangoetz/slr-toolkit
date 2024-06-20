package de.slrtoolkit.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;

import java.io.File;

public class CreateWorker extends Worker {

    public CreateWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data.Builder outputData = new Data.Builder();

        String remote_url = getInputData().getString("REMOTE_URL");
        if (remote_url == null) {
            return Result.failure();
        }

        String local_path = getInputData().getString("LOCAL_PATH");
        if(local_path == null) {
            return Result.failure();
        } else {
            File path = new File(getApplicationContext().getFilesDir(), local_path);
            boolean isDirectoryCreated = path.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = path.mkdir();
            }
            if (!isDirectoryCreated) {
                return Result.failure(outputData.putString("RESULT_MSG", "Could not create directory.").build());
            }
            return Result.success();
        }
    }
}
