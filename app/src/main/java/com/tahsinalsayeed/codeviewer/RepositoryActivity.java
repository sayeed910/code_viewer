package com.tahsinalsayeed.codeviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sayeed on 12/31/17.
 */

abstract class RepositoryActivity extends AppCompatActivity implements CodeDisplayer {
    protected static final String FRAGMENT_TAG = "Navigation";
    protected NavigationFragment navigationFragment;
    protected TextView codeView;
    private Code lastCode;
    private Gateway codeGateway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            requestRepositoryDetails();
        else{
            initiateRepositoryDetails(savedInstanceState);
            instantiateNavigationFragment();
        }
        setContentView(R.layout.activity_repository);
        codeGateway = Gateways.offlineCode(this);
        codeView = findViewById(R.id.code_view);
    }

    protected abstract void instantiateNavigationFragment();

    protected abstract void initiateRepositoryDetails(Bundle savedInstanceState);

    protected abstract void requestRepositoryDetails();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initiateRepositoryDetailsFromActivityResult(requestCode, resultCode, data);
        instantiateNavigationFragment();
    }

    protected abstract void initiateRepositoryDetailsFromActivityResult(int requestCode, int resultCode, Intent data);

    public void displayNavigationFragment(View view) {
        getFragmentManager().beginTransaction().add(navigationFragment, GithubRepositoryActivity.FRAGMENT_TAG).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (navigationFragment != null)
            outState.putString("path", navigationFragment.getCurrentPath());
        saveRepositoryDetails(outState);

    }

    protected abstract void saveRepositoryDetails(Bundle outState);

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String path = savedInstanceState.getString("path");

        if (path != null) {
            navigationFragment.setCurrentPath(path);
        }

    }

    @Override
    public void displayCode(Code code) {
        lastCode = code;
        codeView.setText(new CodeHighligher(code.getContent()).highlight());
    }

    public void saveCode(View view){
        try {
            codeGateway.save(lastCode);
            Toast.makeText(this, "Code saved", Toast.LENGTH_SHORT).show();
        } catch (RuntimeException ex){
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
