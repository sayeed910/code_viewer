package com.tahsinalsayeed.codeviewer;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
            return new URL(REPOSITORY_URL + encodeURL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid path " + url);
        }
    }

    private String encodeURL(String url){
        String encodedUrl = "";
        String urlParts[] = url.split("/");
        try {
            for (int i = 0; i < urlParts.length - 1; i++)
                encodedUrl += encodeUrlPart(urlParts[i]) + "/";
            encodedUrl += encodeUrlPart(urlParts[urlParts.length - 1]);
            return encodedUrl;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    private String encodeUrlPart(String urlPart) throws UnsupportedEncodingException {
        return URLEncoder.encode(urlPart, "UTF-8").replaceAll("\\+", "%20");
    }

    public List<DirectoryEntryDto> getDirectoryEntry(String directoryPath) {
        List<DirectoryEntryDto> entries = new ArrayList<>(15);
        String response = connection.getResponse(makeUrl(directoryPath));
        Object json = getObjectFromJSONString(response);
        if (!(json instanceof JSONArray)) throw new NotADirectory(String.format("%s does not point to a directory", directoryPath));
        JSONArray contents = (JSONArray)json;

        for (int i = 0; i < contents.length(); i++) {
            JSONObject entry = getJsonObjectInArray(contents, i);
            entries.add(getDirectoryEntryFromJSON(entry));
        }
        return entries;
    }

    private JSONObject getJsonObjectInArray(JSONArray contents, int index) {
        try {
            return contents.getJSONObject(index);
        } catch (JSONException e) {
            throw new RuntimeException("Could not parse response from the server");
        }
    }

    private DirectoryEntryDto getDirectoryEntryFromJSON(JSONObject entry) {
        try {
            return new DirectoryEntryDto(entry.getString("name"), entry.getString("path"), entry.getString("type"));
        } catch (JSONException ex){
            throw new RuntimeException("Could not parse response from the server");
        }
    }

    public class NotAFile extends RuntimeException {
        public NotAFile() {
        }

        public NotAFile(String message) {
            super(message);
        }
    }

    public class NotADirectory extends RuntimeException {
        public NotADirectory() {
        }

        public NotADirectory(String message) {
            super(message);
        }
    }
}
