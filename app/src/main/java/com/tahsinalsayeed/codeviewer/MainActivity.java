package com.tahsinalsayeed.codeviewer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_local).setOnClickListener(view -> requestStoragePermission());


    }

    private void requestStoragePermission() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;


        int requestCode = 100;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            startRepositoryActivityWithLocal();
        }
    }

    public void startRepositoryActivity(View view) {

        String username = ((TextInputEditText)findViewById(R.id.username)).getText().toString();
        String repositoryName = ((TextInputEditText)findViewById(R.id.repository_name)).getText().toString();
        Intent repositoryIntent = new Intent(this, RepositoryActivity.class);
        repositoryIntent.putExtra("username", username);
        repositoryIntent.putExtra("repositoryName", repositoryName);
        startActivity(repositoryIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //use switch cases and conditional to check if permission was granted
        startRepositoryActivityWithLocal();

    }

    private void startRepositoryActivityWithLocal() {
//        Intent repositoryIntent = new Intent(this, RepositoryActivity.class);
//        repositoryIntent.putExtra("type", "local");
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "code/jdk";
//        repositoryIntent.putExtra("path", path);
//        startActivity(repositoryIntent);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 42);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("URI", data.getDataString());

    }
}
