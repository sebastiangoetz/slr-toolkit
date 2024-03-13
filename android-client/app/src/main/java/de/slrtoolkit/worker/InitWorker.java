package de.slrtoolkit.worker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.repositories.RepoRepository;
import de.slrtoolkit.util.FileUtil;

public class InitWorker extends Worker {

    private final RepoRepository repoRepository;

    public InitWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.repoRepository = new RepoRepository((Application) context);
    }

    @Override
    public @NonNull Result doWork() {
        Data.Builder outputData = new Data.Builder();

        String remote_url = getInputData().getString("REMOTE_URL");
        if (remote_url == null) {
            return Result.failure();
        }

        String local_path = getInputData().getString("LOCAL_PATH");
        String local_path_git = getInputData().getString("LOCAL_PATH_GIT");
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
        } catch (InvalidRemoteException | TransportException e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_clone_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        } catch (GitAPIException e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_clone_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        } catch (Throwable e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_clone_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        }

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


        Git git;
        try {
            git = Git.open(path);


            AddCommand add = git.add();
//            add.addFilepattern(getApplicationContext().getFilesDir() + local_path_git);
            add.addFilepattern(newSlr);
            add.addFilepattern(newBib);
            add.addFilepattern(newTax);

            add.call();



            String gitName = getInputData().getString("USERNAME");
            String gitEmail = getInputData().getString("EMAIL");

            String gitMessage = getInputData().getString("GIT_MESSAGE");
            if (gitMessage == null || gitMessage.isEmpty()) {
                gitMessage = "Commit by Android-App";
            }

            CommitCommand commitCommand = git.commit();
            commitCommand.setCommitter(gitName, gitEmail).setMessage(gitMessage);
            commitCommand.call();
        } catch (IOException e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_push_failed)
            ).build());
        } catch (NoFilepatternException e) {
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return Result.failure(outputData.putString("RESULT_MSG",
                    getApplicationContext().getString(R.string.error_commit_failed)
                            + System.getProperty("line.separator")
                            + e.getMessage()).build());
        }

        Status status = null;
        try {
            status = git.status().call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

        Set<String> added = status.getAdded();
        for (String add : added) {
            System.out.println("Added: " + add);
        }
        Set<String> uncommittedChanges = status.getUncommittedChanges();
        for (String uncommitted : uncommittedChanges) {
            System.out.println("Uncommitted: " + uncommitted);
        }

        Set<String> untracked = status.getUntracked();
        for (String untrack : untracked) {
            System.out.println("Untracked: " + untrack);
        }


        return Result.success();
    }





}
