package com.tahsinalsayeed.codeviewer;

import java.util.Map;

/**
 * Created by sayeed on 1/1/18.
 */

public interface Gateway {
    void save(Code code);
    Code get(String id);
    Map<String, String> getAllIdsAndFileNames();
}
