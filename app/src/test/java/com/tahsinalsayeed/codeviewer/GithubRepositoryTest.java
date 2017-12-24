package com.tahsinalsayeed.codeviewer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URL;

import static java.util.Base64.getEncoder;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@RunWith(MockitoJUnitRunner.class)
public class GithubRepositoryTest {

    private GithubRepository repository;
    @Mock
    HttpConnection connection;

    @Before
    public void setup() throws Exception {
        repository = new GithubRepository("user", "repo", connection);
    }

    @Test (expected = GithubRepository.NotAFile.class)
    public void getFileContentThrowsIfNotIsFile() throws Exception {
        String responseToReturn = "[{\"type\": \"file\"}, {\"type\": \"file\"}, {\"type\": \"file\"}]";
        Mockito.when(connection.getResponse(any(URL.class))).thenReturn(responseToReturn);
        repository.getFileContent("path/to/file");
    }
    @Test
    public void getFileContent_ContentOfFile() throws Exception{
        String response = "public class MyClass {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"I am the response\");\n" +
                "    }\n" +
                "}";
        String responseToReturn = String.format("{\"content\" : \"%s\"}", getEncoder().encodeToString(response.getBytes()));
        Mockito.when(connection.getResponse(any(URL.class))).thenReturn(responseToReturn);
        assertEquals(response,repository.getFileContent("path/to/file"));
    }

    @Test
    public void getDirectoryEntries() throws Exception{
        ;
    }


}