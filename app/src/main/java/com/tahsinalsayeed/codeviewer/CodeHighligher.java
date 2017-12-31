package com.tahsinalsayeed.codeviewer;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sayeed on 1/1/18.
 */
class CodeHighligher {

    String code;
    private final String[] keywords = {"abstract", "continue", "for", "new", "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"};
    private Spannable span;

    public CodeHighligher(String code) {
        this.code = code;
    }

    public Spannable highlight() {
        span = new SpannableString(code);
        for (String keyword : keywords)
            highlightKeyword(keyword);
        highlightComments();
        return span;
    }

    private void highlightKeyword(String keyword) {
        Pattern keywordPattern = Pattern.compile(String.format("\\W%s\\W", keyword));
        Matcher matcher = keywordPattern.matcher(code);
        while (matcher.find())
            span.setSpan(new ForegroundColorSpan(Color.BLUE), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void highlightMethods() {
        Pattern keywordPattern = Pattern.compile("[a-zA-z0-9-_]+\\(\\)");
        Matcher matcher = keywordPattern.matcher(code);
        while (matcher.find())
            span.setSpan(new ForegroundColorSpan(Color.BLUE), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void highlightComments() {
        Pattern singleLineComment = Pattern.compile("//.*\n");
        Pattern multilineComment = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
        Matcher matcher = singleLineComment.matcher(code);
        while (matcher.find())
            span.setSpan(new ForegroundColorSpan(Color.GRAY), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        matcher = multilineComment.matcher(code);
        while (matcher.find()) {
            System.out.println(matcher.start() + " " + matcher.end() + code.charAt(matcher.end() + 10));
            span.setSpan(new ForegroundColorSpan(Color.GRAY), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }
}
