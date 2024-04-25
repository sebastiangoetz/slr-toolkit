package de.slrtoolkit.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

import de.slrtoolkit.R;

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
        if (remote_url == null) {
            return Result.failure();
        }

        String local_path = getInputData().getString("LOCAL_PATH");
        if(local_path == null) local_path = "";
        File path = new File(getApplicationContext().getFilesDir(), local_path);
        boolean isDirectoryCreated = path.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = path.mkdir();
        }
        if (!isDirectoryCreated) {
            return Result.failure(outputData.putString("RESULT_MSG", "Could not create directory.").build());
        }

        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(remote_url)
                .setDirectory(path);

        String username = getInputData().getString("USERNAME");
        String password = getInputData().getString("TOKEN");

        if (username != null && password != null && !username.trim().isEmpty() && !password.trim().isEmpty()) {
            UsernamePasswordCredentialsProvider auth = new UsernamePasswordCredentialsProvider(username, password);
            cloneCommand.setCredentialsProvider(auth);
        }

        try {
            cloneCommand.call()
        } catch (Throwable e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_clone_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        }
        return Result.success();
    }
}
