package com.tahsinalsayeed.codeviewer;

/**
 * Created by sayeed on 12/28/17.
 */

public class Repositories {
    public static Repository github(String username, String repositoryName){
        return new GithubRepository(username, repositoryName, new HttpConnectionImpl());
    }

    public static Repository local(String path) {
        return new LocalRepository(path);
    }

//    public static Repository local(String path){
//
//    }
}
