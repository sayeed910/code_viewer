package com.tahsinalsayeed.codeviewer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sayeed on 1/1/18.
 */

class OfflineCodeGateway implements Gateway {
    private final SQLiteDatabase db;
    private final Context context;

    OfflineCodeGateway(SQLiteDatabase db, Context context) {
        this.db = db;
        this.context = context;
    }

    @Override
    public void save(Code code) {
        ContentValues values = new ContentValues(3);
        String id = code.id();
        values.put("ID", id);
        values.put("NAME", code.getFileName());
        values.put("URI", code.getFileUri());
        db.insert("OFFLINE_FILES", "", values);

        try (FileOutputStream stream = context.openFileOutput(id, Context.MODE_PRIVATE)) {
            stream.write(code.getContent().getBytes());
            stream.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Code get(String id) {
        try (Cursor res = db.rawQuery("SELECT * FROM OFFLINE_FILES WHERE ID=?", new String[]{id})) {
            res.moveToNext();
            File codeFile = new File(context.getFilesDir() + "/" + id);
            String content = Files.asCharSource(codeFile, Charsets.UTF_8).read();
            return new Code(res.getString(2), res.getString(1), content);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Map<String, String> getAllIdsAndFileNames() {
        Map<String , String> idAndFileNames = new HashMap<>(20);
        try (Cursor res = db.rawQuery("SELECT * FROM OFFLINE_FILES", null)) {
            res.moveToNext();
            while(!res.isAfterLast()){
                System.out.println("searching");
                idAndFileNames.put(res.getString(res.getColumnIndex("ID")), res.getString(res.getColumnIndex("NAME")));
                res.moveToNext();
            }
            return idAndFileNames;
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
