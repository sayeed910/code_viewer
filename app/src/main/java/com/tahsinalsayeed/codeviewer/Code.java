package com.tahsinalsayeed.codeviewer;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;

/**
 * Created by sayeed on 1/1/18.
 */

class Code {
    private final String fileUri;
    private final String fileName;
    private final String content;

    public Code(String fileUri, String fileName, String content) {
        Preconditions.checkNotNull(fileUri);
        Preconditions.checkNotNull(fileName);
        Preconditions.checkNotNull(content);
        this.fileUri = fileUri;
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileUri() {
        return fileUri;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Code code = (Code) o;

        return getFileUri() != null ? getFileUri().equals(code.getFileUri()) : code.getFileUri() == null;
    }

    @Override
    public int hashCode() {
        return getFileUri() != null ? getFileUri().hashCode() : 0;
    }

    public String id(){
        return Hashing.murmur3_128().hashString(getFileUri(), Charsets.UTF_8).toString();
    }
}
