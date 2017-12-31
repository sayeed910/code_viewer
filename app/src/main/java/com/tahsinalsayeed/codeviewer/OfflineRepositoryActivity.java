package com.tahsinalsayeed.codeviewer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sayeed on 1/1/18.
 */

public class OfflineRepositoryActivity extends AppCompatActivity implements CodeDisplayer {
    NavigationFragment navigationFragment;
    private Code lastCode;
    private TextView codeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_offline);
        navigationFragment = (NavigationFragment) getFragmentManager().findFragmentByTag("NAV");
        if (navigationFragment == null)
            navigationFragment = NavigationFragment.offline();
        codeView = (TextView)findViewById(R.id.code_view);
    }


    public void displayNavigationFragment(View view) {
        navigationFragment.show(getFragmentManager(), "NAV");
    }

    @Override
    public void displayCode(Code code) {
        lastCode = code;
        codeView.setText(new CodeHighligher(code.getContent()).highlight());
    }
}
