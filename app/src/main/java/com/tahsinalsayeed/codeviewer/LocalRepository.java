package com.tahsinalsayeed.codeviewer;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sayeed on 12/28/17.
 */

class LocalRepository implements Repository {
    /**
     * The absolute path of the project directory without trailing File.separator char
     */
    private final String rootDirectoryPath;

    public LocalRepository(String rootDir) {
        this.rootDirectoryPath = CharMatcher.is('/').trimTrailingFrom(rootDir);
    }

    @Override
    public List<DirectoryEntryDto> load() {
        return getDirectoryEntry("");
    }

    @Override
    public String getFileContent(String filePath) {
        filePath = CharMatcher.is('/').trimFrom(filePath);
        File file = new File(rootDirectoryPath + File.separator + filePath);
        checkIsValidFile(file);
        try {
            return Files.asCharSource(file, Charsets.UTF_8).read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkIsValidFile(File file) {
        if (!file.isFile()) throw new NotAFile();
    }

    @Override
    public List<DirectoryEntryDto> getDirectoryEntry(String directoryPath) {
        File dir = new File(rootDirectoryPath + File.separator + CharMatcher.is('/').trimFrom(directoryPath));
        if (! dir.isDirectory()) throw new NotADirectory();
        List<DirectoryEntryDto> entries = new ArrayList<>(15);
        for(File entry : dir.listFiles()){
            entries.add(DirectoryEntryDto.fromFile(entry, rootDirectoryPath));
        }
        return entries;
    }
}
