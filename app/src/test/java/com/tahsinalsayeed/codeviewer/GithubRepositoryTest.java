package com.tahsinalsayeed.codeviewer;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static java.util.Base64.getEncoder;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@RunWith(MockitoJUnitRunner.class)
public class GithubRepositoryTest {

    private Repository repository;
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
        repository.getCode("path/to/file");
    }
    @Test
    public void getFileContent_ContentOfFile() throws Exception{
        String response = "public class MyClass {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"I am the response\");\n" +
                "    }\n" +
                "}";
        String responseToReturn = String.format("{\"content\" : \"%s\"}", getEncoder().encodeToString(response.getBytes()));
        URL url = new URL("https://api.github.com/repos/user/repo/contents/path/to/fi%20le");
        Mockito.when(connection.getResponse(url)).thenReturn(responseToReturn);
        assertEquals(response,repository.getCode("path/to/fi le"));
    }

    @Test
    public void getDirectoryEntries_EntriesInADirectory() throws Exception{
        String responseToReturn = "[\n" +
                "  {\n" +
                "    \"name\": \"file1\",\n" +
                "    \"path\": \"file1\",\n" +
                "    \"type\": \"file\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"dir 1\",\n" +
                "    \"path\": \"dir 1\",\n" +
                "    \"type\": \"dir\"\n" +
                "  }\n" +
                "]";
        URL directoryUrl = new URL("https://api.github.com/repos/user/repo/contents/path/to/direc%20tory");
        Mockito.when(connection.getResponse(directoryUrl)).thenReturn(responseToReturn);

        List<DirectoryEntryDto> expected = Arrays.asList(
                new DirectoryEntryDto("file1", "file1", "file"),
                new DirectoryEntryDto("dir 1", "dir 1", "dir"));

        assertEquals(expected, repository.getDirectoryEntry("path/to/direc tory"));
    }

    @Test (expected = GithubRepository.NotADirectory.class)
    public void getDirectoryEntryThrowsIfPathIsNotDirectory() throws Exception{
        String responseToReturn = "{\"content\": \"abc==12?\"}";
        URL directoryUrl = new URL("https://api.github.com/repos/user/repo/contents/path/to/direc%20tory");
        Mockito.when(connection.getResponse(any(URL.class))).thenReturn(responseToReturn);
        repository.getDirectoryEntry("path/to/direc tory");
    }

    @Test
    public void content() throws Exception{
        Repository repo = new GithubRepository("sayeed910", "Java", new HttpConnectionImpl());
        System.out.println(repo.getCode("Bubble Sort.java"));
    }

    @Test
    public void hash() throws Exception{
        HashFunction hash = Hashing.murmur3_128();
        HashFunction hash2 = Hashing.murmur3_32();
        HashCode code = hash.hashString("https://api.github.com/repos/user/repo/contents/path/to/directory/path/to/file.java", Charsets.UTF_8);
        HashCode code2 = hash2.hashString("https://api.github.com/repos/user/repo/contents/path/to/directory/path/to/file.java", Charsets.UTF_8);
        System.out.println(code.toString());
        System.out.println(code.asInt());
    }



}