package de.slrtoolkit.util;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class FileUtil {
    public File accessFiles(String path, Application application, String type) {
        File directoryPath = new File(application.getApplicationContext().getFilesDir(), path);
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
}
