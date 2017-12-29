package com.tahsinalsayeed.codeviewer;

import android.support.annotation.NonNull;

import com.google.common.collect.Maps;
import com.google.common.io.BaseEncoding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import static java.util.Base64.getDecoder;



class GithubRepository implements Repository {
    private final String  REPOSITORY_URL;
    private final String username;
    private final String repositoryName;
    private HttpConnection connection;
    private HashMap<String, String> cache;

    public GithubRepository(String username, String repositoryName, HttpConnection connection) {
        this.username = username;
        this.repositoryName = repositoryName;
        this.connection = connection;
        REPOSITORY_URL = String.format("https://api.github.com/repos/%s/%s/contents/", username, repositoryName);
        cache = Maps.newHashMap();
    }

    /**
     *
     * @return List of the DirectoryEntries at the top level of the repository
     */
    @Override
    public List<DirectoryEntryDto> load(){
        return getDirectoryEntry("");
    }

    @Override
    public String getFileContent(String filePath) {
        String response ="";
        if (cache.containsKey(filePath))
            response = cache.get(filePath);
        else {
            response = connection.getResponse(makeUrl(filePath));
            cache.put(filePath, response);
        }
        Object json = getObjectFromJSONString(response);
        if (json instanceof JSONArray) throw new NotAFile();
        try {
            String fileContent = ((JSONObject)json).getString("content");
            fileContent = fileContent.replaceAll("\n", "");
            System.out.println(fileContent);
            return new String(BaseEncoding.base64().decode(fileContent));
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

    @Override
    public List<DirectoryEntryDto> getDirectoryEntry(String directoryPath) {
        List<DirectoryEntryDto> entries = new ArrayList<>(15);
        String response = "";
        if (cache.containsKey(directoryPath))
            response = cache.get(directoryPath);
        else {
            response = connection.getResponse(makeUrl(directoryPath));
            cache.put(directoryPath, response);
        }
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

}
