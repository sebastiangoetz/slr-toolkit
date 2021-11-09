package de.davidtiede.slrtoolkit.util;

import android.app.Application;

import java.io.File;

public class FileUtil {
    public File accessFiles(String path, Application application) {
        File directoryPath = new File(application.getApplicationContext().getFilesDir(), path);
        File[] files = directoryPath.listFiles();
        File bibFile = null;
        for(File file: files) {
            if(file.isDirectory()) {
                for(File f: file.listFiles()) {
                    if(f.getName().endsWith(".bib")) {
                        bibFile = f;
                    }
                }
            }
        }
        return bibFile;
    }
}
