package com.towel.file;

import com.towel.collections.CollectionsUtil;
import java.io.File;
import java.io.FilenameFilter;

public class Directory {
    private File current;

    public Directory(File file) {
        if (!file.isDirectory()) {
            throw new RuntimeException("File is not a directory: " + file.getAbsolutePath());
        }
        this.current = file;
    }

    public Directory(String dirName) {
        this(new File(dirName));
    }

    public Directory bd() {
        if (this.current.getParentFile() != null) {
            this.current = this.current.getParentFile();
        }
        return this;
    }

    public Directory root() {
        while (this.current.getParentFile() != null) {
            this.current = this.current.getParentFile();
        }
        return this;
    }

    public Directory cd(String path) {
        this.current = new File(this.current.getAbsolutePath().concat("/").concat(path));
        return this;
    }

    public Directory[] getDirs() {
        int currentPos;
        File[] files = this.current.listFiles();
        int i = files.length;
        Directory[] dirs = new Directory[i];
        int j = 0;
        int currentPos2 = 0;
        while (j < i) {
            if (files[j].isDirectory()) {
                currentPos = currentPos2 + 1;
                dirs[currentPos2] = new Directory(files[j]);
            } else {
                currentPos = currentPos2;
            }
            j++;
            currentPos2 = currentPos;
        }
        return (Directory[]) CollectionsUtil.trim(dirs);
    }

    public File[] getFiles() {
        int currentPos;
        File[] files = this.current.listFiles();
        int i = files.length;
        File[] returnValue = new File[i];
        int j = 0;
        int currentPos2 = 0;
        while (j < i) {
            if (files[j].isFile()) {
                currentPos = currentPos2 + 1;
                returnValue[currentPos2] = files[j];
            } else {
                currentPos = currentPos2;
            }
            j++;
            currentPos2 = currentPos;
        }
        return (File[]) CollectionsUtil.trim(returnValue);
    }

    public File[] allFiles() {
        return this.current.listFiles();
    }

    public File[] getFilesByExt(String ext) {
        return this.current.listFiles(new Filter(ext));
    }

    public String getDirName() {
        return this.current.getAbsolutePath();
    }

    private class Filter implements FilenameFilter {
        private String ext;

        public Filter(String ext2) {
            this.ext = ext2;
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(this.ext);
        }
    }
}
