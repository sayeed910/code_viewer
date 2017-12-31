package com.tahsinalsayeed.codeviewer;

import android.support.v4.provider.DocumentFile;
import android.util.Log;

/**
 * Created by sayeed on 12/24/17.
 */

class DirectoryEntryDto {
    public final String fileName;
    public final String relativePath;
    private final boolean isDir;

    public DirectoryEntryDto(String fileName, String relativePath, String fileType) {

        this.fileName = fileName;
        this.relativePath = relativePath;
        this.isDir = fileType.equalsIgnoreCase("dir");
    }

    public static DirectoryEntryDto fromDocumentFile(DocumentFile file){
        String fileName = file.getName();
        String filePath = (file.getUri().toString());
        Log.d("DIRENT", filePath);
        return new DirectoryEntryDto(fileName, filePath, file.isDirectory() ? "dir": "file");
    }
    public boolean isDir(){
        return isDir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectoryEntryDto that = (DirectoryEntryDto) o;

        if (isDir() != that.isDir()) return false;
        if (!fileName.equals(that.fileName)) return false;
        return relativePath.equals(that.relativePath);
    }

    @Override
    public int hashCode() {
        int result = fileName.hashCode();
        result = 31 * result + relativePath.hashCode();
        result = 31 * result + (isDir() ? 1 : 0);
        return result;
    }
}
