package com.tahsinalsayeed.codeviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_OPEN_DIRECTORY = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

   public void startGithubRepositoryActivity(View view) {
        Intent repositoryIntent = new Intent(this, GithubRepositoryActivity.class);
        startActivity(repositoryIntent);

    }

    public void startLocalRepositoryActivity(View view) {
        Intent repositoryIntent = new Intent(this, LocalRepositoryActivity.class);
        startActivity(repositoryIntent);

    }

    public void startOfflineRepositoryActivity(View view) {
        Intent repositoryIntent = new Intent(this, OfflineRepositoryActivity.class);
        startActivity(repositoryIntent);

    }





    }
