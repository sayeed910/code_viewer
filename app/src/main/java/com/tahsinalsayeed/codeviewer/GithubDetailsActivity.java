package com.tahsinalsayeed.codeviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sayeed on 12/31/17.
 */

public class GithubDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_details);
    }

    public void returnResult(View view) {
        Intent resultIntent = new Intent();
        String username = ((TextView) findViewById(R.id.username)).getText().toString();
        String repositoryName = ((TextView) findViewById(R.id.repository_name)).getText().toString();
        resultIntent.putExtra("username", username);
        resultIntent.putExtra("repositoryName", repositoryName);
        setResult(GithubRepositoryActivity.GITHUB_REPO_DETAILS_REQUEST, resultIntent);
        finish();
    }
}
