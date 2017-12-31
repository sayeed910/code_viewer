package com.tahsinalsayeed.codeviewer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by sayeed on 12/31/17.
 */

public class LocalRepositoryActivity extends RepositoryActivity {

    public static final int READ_EXTERNAL_STORAGE_REQUEST = 100;
    public static final int LOCAL_REPO_DETAILS_REQUEST = 42;
    private String rootUri;

    @Override
    protected void requestRepositoryDetails() {
        if (!hasReadPermission()) {
            requestReadPermission();
        } else {
            requestRepositoryPath();
        }
    }

    private boolean hasReadPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST);
    }

    private void requestRepositoryPath() {
        Intent getRepositoryPathIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(getRepositoryPathIntent, LOCAL_REPO_DETAILS_REQUEST);
    }

    @Override
    protected void initiateRepositoryDetails(Bundle savedInstanceState) {
        rootUri = savedInstanceState.getString("rootUri");
    }

    @Override
    protected void instantiateNavigationFragment() {
        navigationFragment =(NavigationFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (navigationFragment == null)
            navigationFragment = NavigationFragment.local(rootUri);

    }

    @Override
    protected void initiateRepositoryDetailsFromActivityResult(int requestCode, int resultCode, Intent data) {
        rootUri = data.getDataString();
    }

    @Override
    protected void saveRepositoryDetails(Bundle outState) {
        if (rootUri != null)
            outState.putString("rootUri", rootUri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestRepositoryPath();
    }
}
