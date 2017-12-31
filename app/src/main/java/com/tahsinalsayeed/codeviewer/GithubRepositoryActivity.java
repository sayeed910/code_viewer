package com.tahsinalsayeed.codeviewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GithubRepositoryActivity extends RepositoryActivity {

    private static final String TAG = GithubRepositoryActivity.class.getSimpleName();
    public static final int GITHUB_REPO_DETAILS_REQUEST = 42;
    private String username;
    private String repositoryName;

    protected void initiateRepositoryDetails(Bundle savedInstanceState) {
        username = savedInstanceState.getString("username");
        repositoryName = savedInstanceState.getString("repositoryName");
    }

    protected void requestRepositoryDetails() {
        Intent intent = new Intent(this, GithubDetailsActivity.class);
        startActivityForResult(intent, GITHUB_REPO_DETAILS_REQUEST);
    }

    protected void initiateRepositoryDetailsFromActivityResult(int requestCode, int resultCode, Intent data) {
        username = data.getStringExtra("username");
        repositoryName = data.getStringExtra("repositoryName");

        Log.d(TAG, username + " " + repositoryName);


    }
    protected void instantiateNavigationFragment() {
        navigationFragment = (NavigationFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (navigationFragment == null) {
            navigationFragment = NavigationFragment.github(username, repositoryName);
        }
    }


    protected void saveRepositoryDetails(Bundle outState) {
        if (username != null && repositoryName != null){
            outState.putString("username", username);
            outState.putString("repositoryName", repositoryName);
        }
    }




}
