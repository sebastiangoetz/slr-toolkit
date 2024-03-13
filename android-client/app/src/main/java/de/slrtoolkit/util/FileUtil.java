package de.slrtoolkit.util;

import android.app.Application;
import android.os.Build;
import android.os.FileUtils;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Scanner;

public class FileUtil {
    public File accessFiles(String path, Application application, String type) {
        File directoryPath = new File(application.getApplicationContext().getFilesDir(),path);
        File[] files = directoryPath.listFiles();
        File bibFile = null;
        for (File file : Objects.requireNonNull(files)) {
            //if (file.isDirectory()) {
                //for (File f : Objects.requireNonNull(file.listFiles())) {
                    if (file.getName().endsWith(type)) {
                        bibFile = file;
                    }
                //}
            //}
        }
        return bibFile;
    }

    public void accessFilesOfLocalRepo(String path, Application application, String type){
        File directoryPath = new File(application.getApplicationContext().getFilesDir(), path);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String readContentFromFile(String path, Application application) throws FileNotFoundException {
        File file = accessFiles(path, application, ".taxonomy");
        Scanner scanner = new Scanner(file);
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();
        return text;
    }

    public File createFileIfNotExists(Application application, String path, String name) throws IOException {
        File file = new File(application.getApplicationContext().getFilesDir(), path + "/" + name);
        if (!file.exists() && !file.isDirectory()) {
            file.createNewFile();
        }
        return file;
    }

    public void copyFile(File sourceFile, File destFile)  {
        try {
            FileUtils.copy(new FileInputStream(sourceFile.toString()), new FileOutputStream(destFile.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
