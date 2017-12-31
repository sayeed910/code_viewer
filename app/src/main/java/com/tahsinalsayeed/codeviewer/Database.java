package com.tahsinalsayeed.codeviewer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sayeed on 1/1/18.
 */

public class Database extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "CODE_VIEWER";


    public Database(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE OFFLINE_FILES ( ID VARCHAR PRIMARY KEY, NAME VARCHAR, URI VARCHAR )";
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS OFFLINE_FILES");
        onCreate(sqLiteDatabase);
    }

    public SQLiteDatabase readableDB(){
        return this.getReadableDatabase();
    }
    public SQLiteDatabase writableDB(){
        return this.getWritableDatabase();
    }

}
