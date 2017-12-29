package com.tahsinalsayeed.codeviewer;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleInstrumentedTest {
    @Test
    public void multiline() throws Exception{
        String text = "code code code \n/**\n" +
                " * Instrumented test, which will execute on an Android device.\n" +
                " *\n" +
                " * @see <a href=\"http://d.android.com/tools/testing\">Testing documentation</a>\n" +
                " */\n more code more code/**fjdasldjf falsdfjlasjfa\n*/";
//        Pattern multilineComment = Pattern.compile("(?s)/\\*.*?\\*/");
        Pattern multilineComment = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
        Matcher matcher = multilineComment.matcher(text);
        assertTrue(matcher.find());
        assertEquals(text.indexOf("/*"),matcher.start());
        assertEquals(text.indexOf("*/")+ "*/".length(),matcher.end());
    }

    @Test
    public void singleLine() throws Exception{
        String text = "code code //afdjlsakdjflsajfdlsakjfdlsajfdfjdslafjdslafjd\ncode code code\n";
        Pattern singleLinePattern = Pattern.compile("//.*\n");
        Matcher matcher = singleLinePattern.matcher(text);
        assertTrue(matcher.find());
        assertEquals(text.indexOf("//"),matcher.start());
        assertEquals(text.indexOf("\n") + 1,matcher.end());
    }
}
