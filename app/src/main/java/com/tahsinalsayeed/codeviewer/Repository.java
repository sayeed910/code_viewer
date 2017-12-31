package com.tahsinalsayeed.codeviewer;

import java.util.List;

/**
 * Created by sayeed on 12/28/17.
 */

interface Repository {
    List<DirectoryEntryDto> load();

    Code getCode(String filePath);

    List<DirectoryEntryDto> getDirectoryEntry(String directoryPath);

    public static class NotAFile extends RuntimeException {
        public NotAFile() {
        }

        public NotAFile(String message) {
            super(message);
        }
    }

    public static class NotADirectory extends RuntimeException {
        public NotADirectory() {
        }

        public NotADirectory(String message) {
            super(message);
        }
    }
}
