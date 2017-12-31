package com.tahsinalsayeed.codeviewer;

import android.content.Context;

/**
 * Created by sayeed on 12/28/17.
 */

public class Repositories {
    private static Gateway codeGateway;

    public static Repository github(String username, String repositoryName){
        return new GithubRepository(username, repositoryName, new HttpConnectionImpl());
    }

    public static Repository offline(Context context){
        if (codeGateway == null) codeGateway = Gateways.offlineCode(context);
        return new OfflineRepository(codeGateway);
    }

    public static Repository local(String path, Context context) {
        return new LocalRepository(path, context);
    }

    public static final int GITHUB = 0;
    public static final int LOCAL = 1;

//    public static Repository local(String path){
//
//    }
}
