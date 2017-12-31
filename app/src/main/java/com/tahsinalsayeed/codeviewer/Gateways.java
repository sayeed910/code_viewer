package com.tahsinalsayeed.codeviewer;

import android.content.Context;

/**
 * Created by sayeed on 1/1/18.
 */

public class Gateways {
    private static Database DB;

    public static Gateway offlineCode(Context context){
        if (DB == null) DB = new Database(context);
        return new OfflineCodeGateway(DB.writableDB(), context);
    }
}
