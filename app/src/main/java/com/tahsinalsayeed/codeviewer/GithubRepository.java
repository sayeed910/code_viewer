package com.tahsinalsayeed.codeviewer;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.MalformedURLException;
import java.net.URL;

import static java.util.Base64.getDecoder;



class GithubRepository {
    private final String  REPOSITORY_URL;
    private final String username;
    private final String repositoryName;
    private HttpConnection connection;

    public GithubRepository(String username, String repositoryName, HttpConnection connection) {
        this.username = username;
        this.repositoryName = repositoryName;
        this.connection = connection;
        REPOSITORY_URL = String.format("https://api.github.com/repos/%s/%s/contents/", username, repositoryName);
    }

    public String getFileContent(String filePath) {
        String response = connection.getResponse(makeUrl(filePath));
        Object json = getObjectFromJSONString(response);
        if (json instanceof JSONArray) throw new NotAFile();
        try {
            String fileContent = ((JSONObject)json).getString("content");
            return new String(getDecoder().decode(fileContent));
        } catch (JSONException e) {
            throw new RuntimeException("File content could not be found for file " + filePath);
        }
    }

    @NonNull
    private Object getObjectFromJSONString(String json){
        try {
            return new JSONTokener(json).nextValue();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    private URL makeUrl(String url) {
        try {
            return new URL(REPOSITORY_URL + url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid path " + url);
        }
    }

    public class NotAFile extends RuntimeException {
        public NotAFile() {
        }

        public NotAFile(String message) {
            super(message);
        }
    }
}
