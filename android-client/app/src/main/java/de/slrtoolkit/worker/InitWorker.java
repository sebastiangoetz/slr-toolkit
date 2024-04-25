package de.slrtoolkit.worker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

import de.slrtoolkit.R;
import de.slrtoolkit.repositories.RepoRepository;
import de.slrtoolkit.util.FileUtil;

public class InitWorker extends Worker {

    public InitWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        new RepoRepository((Application) context);
    }

    @Override
    public @NonNull Result doWork() {
        Data.Builder outputData = new Data.Builder();

        String remote_url = getInputData().getString("REMOTE_URL");
        if (remote_url == null) {
            return Result.failure();
        }

        String local_path_git = getInputData().getString("LOCAL_PATH_GIT");
        if(local_path_git == null) local_path_git = "";
        File path = new File(getApplicationContext().getFilesDir(), local_path_git);
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
            cloneCommand.call();
        } catch (Throwable e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_clone_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        }

        Git git;
        try {
            git = Git.open(path);
            Repository repository = git.getRepository();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Status status = null;

        FileUtil fileUtil = new FileUtil();
        String oldSlr = getInputData().getString("PATH_SLR");
        String oldBib = getInputData().getString("PATH_BIB");
        String oldTax = getInputData().getString("PATH_TAX");
        String newSlr = getInputData().getString("NEW_PATH_SLR");
        String newBib = getInputData().getString("NEW_PATH_BIB");
        String newTax = getInputData().getString("NEW_PATH_TAX");

        fileUtil.copyFile(new File(oldSlr), new File(newSlr));
        fileUtil.copyFile(new File(oldBib), new File(newBib));
        fileUtil.copyFile(new File(oldTax), new File(newTax));


        File slrFile = new File(git.getRepository().getDirectory().getParent(), new File(newSlr).getName());
        File bibFile = new File(git.getRepository().getDirectory().getParent(), new File(newBib).getName());
        File taxonomyFile = new File(git.getRepository().getDirectory().getParent(), new File(newTax).getName());

        try {
            status = git.status().call();
            git.add().addFilepattern(new File(newSlr).getName()).addFilepattern(new File(newBib).getName()).addFilepattern(new File(newTax).getName()).call();
            status = git.status().call();
            String gitName = getInputData().getString("USERNAME");
            String gitEmail = getInputData().getString("EMAIL");
            git.commit().setCommitter(gitName, gitEmail).setMessage("Commit from Android App").call();
            status = git.status().call();

        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

        try {
            status = git.status().call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }


}
