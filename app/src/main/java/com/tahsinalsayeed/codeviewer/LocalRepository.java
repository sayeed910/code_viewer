package com.tahsinalsayeed.codeviewer;

import android.content.Context;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by sayeed on 12/28/17.
 */

class LocalRepository implements Repository {
    /**
     * The absolute path of the project directory without trailing File.separator char
     */
    private final String rootDirectoryUri;
    private Context context;

    public LocalRepository(String rootDir, Context applicationContext) {
        this.rootDirectoryUri = rootDir;
        context = applicationContext;
    }


    public List<DirectoryEntryDto> load() {
        return getDirectoryEntry("");
    }


    public Code getCode(String filePath) {
        DocumentFile file = DocumentFile.fromSingleUri(context, Uri.parse(filePath));
        checkIsValidFile(file);
        try(Scanner scanner = new Scanner(context.getContentResolver().openInputStream(file.getUri()))) {
            return new Code(filePath, file.getName(), scanner.useDelimiter("\\A").next());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkIsValidFile(DocumentFile file) {

    }


    public List<DirectoryEntryDto> getDirectoryEntry(String directoryPath) {
        Log.d("LR", directoryPath);
        DocumentFile dir;
        if (directoryPath.isEmpty()){
            dir = DocumentFile.fromTreeUri(context, Uri.parse(rootDirectoryUri));
        } else {
            try {
                Uri uri = Uri.parse(directoryPath);
                Class<?> c = Class.forName("android.support.v4.provider.TreeDocumentFile");
                Constructor<?> constructor = c.getDeclaredConstructor(DocumentFile.class, Context.class, Uri.class);
                constructor.setAccessible(true);

                dir = (DocumentFile) constructor.newInstance(null, context, uri);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }


//        if (!dir.isDirectory()) throw new NotADirectory(directoryPath);
        List<DirectoryEntryDto> entries = new ArrayList<>(15);
        for(DocumentFile entry : dir.listFiles()){
            System.out.println(entry.getName());
            entries.add(DirectoryEntryDto.fromDocumentFile(entry));
        }
        return entries;
    }
}
