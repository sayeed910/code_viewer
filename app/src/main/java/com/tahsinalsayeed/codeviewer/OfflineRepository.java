package com.tahsinalsayeed.codeviewer;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sayeed on 1/1/18.
 */

public class OfflineRepository implements Repository {

    private final Gateway offlineGateway;

    OfflineRepository(Gateway offlineGateway) {
        this.offlineGateway = offlineGateway;
    }

    @Override
    public List<DirectoryEntryDto> load() {
        return null;
    }

    @Override
    public Code getCode(String id) {
        return offlineGateway.get(id);
    }

    @Override
    public List<DirectoryEntryDto> getDirectoryEntry(String directoryPath)
    {
        Log.d("OFFLINE", "starting" + "");
        Map<String, String> map = offlineGateway.getAllIdsAndFileNames();
        List<DirectoryEntryDto> entries = new ArrayList<>();
        for(String key: map.keySet()){
            entries.add(new DirectoryEntryDto(map.get(key), key, "file"));
        }
        return entries;
    }
}
