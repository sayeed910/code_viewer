package com.tahsinalsayeed.codeviewer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepositoryActivity extends AppCompatActivity implements CodeDisplayer {

    private static final String FRAGMENT_TAG = "Navigation";
    private NavigationFragment navigationFragment;
    private TextView codeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        instantiateNavigationFragment();
        codeView = findViewById(R.id.code_view);
        String text = Environment.getRootDirectory().getAbsolutePath() + "\n" + Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("FILES", Arrays.toString(Environment.getExternalStorageDirectory().list()));
        codeView.setText(text);
    }

    private void instantiateNavigationFragment() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type.equalsIgnoreCase("local")){
            navigationFragment =(NavigationFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);

            if (navigationFragment == null){
                navigationFragment = NavigationFragment.local(intent.getStringExtra("path"));
            }
        } else {


            String username = intent.getStringExtra("username");
            String repositoryName = intent.getStringExtra("repositoryName");
            navigationFragment = (NavigationFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);

            if (navigationFragment == null) {
                navigationFragment = NavigationFragment.github(username, repositoryName);
            }
        }
    }

    public void displayNavigationFragment(View view) {
        getFragmentManager().beginTransaction().add(navigationFragment, FRAGMENT_TAG).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path", navigationFragment.getCurrentPath());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String path = savedInstanceState.getString("path");
        if (path != null) {
            navigationFragment.setCurrentPath(path);
        }

    }

    @Override
    public void displayCode(String code) {

        codeView.setText(new CodeHighligher(code).highlight());
    }

    private static class CodeHighligher{

        String code;
        private final String[] keywords = {"abstract", "continue", "for", "new", "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while" };
        private Spannable span;

        public CodeHighligher(String code) {
            this.code = code;
        }

        public Spannable highlight(){
            span = new SpannableString(code);
            for(String keyword: keywords)
                highlightKeyword(keyword);
            highlightComments();
            return span;
        }

        private void highlightKeyword(String keyword) {
            Pattern keywordPattern = Pattern.compile(String.format("\\W%s\\W", keyword));
            Matcher matcher = keywordPattern.matcher(code);
            while(matcher.find())
                span.setSpan(new ForegroundColorSpan(Color.BLUE), matcher.start(),
                        matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        private void highlightMethods(){
            Pattern keywordPattern = Pattern.compile("[a-zA-z0-9-_]+\\(\\)");
            Matcher matcher = keywordPattern.matcher(code);
            while(matcher.find())
                span.setSpan(new ForegroundColorSpan(Color.BLUE), matcher.start(),
                        matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        private void highlightComments(){
            Pattern singleLineComment = Pattern.compile("//.*\n");
            Pattern multilineComment = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
            Matcher matcher = singleLineComment.matcher(code);
            while(matcher.find())
                span.setSpan(new ForegroundColorSpan(Color.GRAY), matcher.start(),
                        matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            matcher = multilineComment.matcher(code);
            while(matcher.find()) {
                System.out.println(matcher.start() + " " + matcher.end() + code.charAt(matcher.end() + 10));
                span.setSpan(new ForegroundColorSpan(Color.GRAY), matcher.start(),
                        matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
    }
}
