package com.tahsinalsayeed.codeviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;



class HttpConnectionImpl implements HttpConnection {
    @Override
    public String getResponse(URL url) {
        HttpURLConnection connection = (HttpURLConnection) getUrlConnection(url);

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            connection.connect();
            String response = "";
            String line;

            while((line = reader.readLine()) != null)
                response += line;

            return response;
        } catch (Exception ex){
            connection.disconnect();
            throw new RuntimeException("Error connecting to the server", ex);
        }finally{
            connection.disconnect();
        }
    }

    private URLConnection getUrlConnection(URL url) {
        try {
            return url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException("Can not read from server");
        }
    }
}
