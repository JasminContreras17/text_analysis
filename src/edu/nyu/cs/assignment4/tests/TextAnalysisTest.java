// DO NOT TOUCH THIS FILE!
package edu.nyu.cs.assignment4.tests;

// import the class to be tested
import edu.nyu.cs.assignment4.TextAnalysis;

// import junit 4 testing framework
import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;

import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule; // system rules lib - useful for capturing system output
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TextAnalysisTest {
    @ClassRule
    public static final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule();

    @Before
    public void init() {
        // any pre-test setup here
    }

    @Test
    public void testGetFilePathFromUser() {
        String[] expecteds = {
                "test/data/test1.txt", "test/data/trump_speech_010621.txt", "foo/bar/baz" };
        for (String expected : expecteds) {
            systemInMock.provideLines(expected);
            try {
                String actual = TextAnalysis.getFilepathFromUser().trim();
                String expectedFormatted = Paths.get(expected).toString();
                System.out.println(actual);
                if (!(actual.equals(expected) || actual.equals(expectedFormatted))) {
                    assertEquals(
                            "Expected the getFilePathFromUser() function to return the file path we entered",
                            "Returned " + actual + " instead.");
                }
            } catch (Exception e) {
                assertEquals("Expected the getFilePathFromUser() not to crash when called",
                        "The function threw an Exception when called.");
            }
        }
    }

    @Test
    public void testGetTicsFromUser() {
        String[] mockInputs = {
                "you,know,um,sort,of", "you,know ,foo,  suppa", "well,now, then, what",
                ",,, , , , ,,, , , , ,,,", "dummy   dummy" };
        String[][] expecteds = { { "you", "know", "um", "sort", "of" }, { "you", "know", "foo", "suppa" },
                { "well", "now", "then", "what" }, {}, { "dummy", "dummy" } };
        for (int i = 0; i < mockInputs.length; i++) {
            String mockInput = mockInputs[i];
            String[] expected = expecteds[i];
            systemInMock.provideLines(mockInput);
            try {
                String[] actual = TextAnalysis.getTicsFromUser();
                if (!(expected.length == actual.length)) {
                    assertEquals("Expected the getTicsFromUser() function to return an array of "
                            + expected.length + " tics, given the input: " + mockInput,
                            "Returned an array of " + actual.length + " instead");
                    return;
                }
                for (int j = 0; j < expected.length; j++) {
                    if (!(expected[j].equals(actual[j]))) {
                        if (j >= 3) {
                            assertEquals(
                                    "Hint: There could be many dummy commas and spaces\n" +
                                            "Expected the getTicsFromUser() function to return an array including the string "
                                            + expected[j] + " at position " + j
                                            + ", given the input: " + mockInput,
                                    "Returned an array with the string " + actual[j] + " at position " + j
                                            + " instead.");
                            return;
                        } else {
                            assertEquals(
                                    "Expected the getTicsFromUser() function to return an array including the string "
                                            + expected[j] + " at position " + j
                                            + ", given the input: " + mockInput,
                                    "Returned an array with the string " + actual[j] + " at position " + j
                                            + " instead.");
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                assertEquals("Expected the getTicsFromUser() function not to crash when called",
                        "The function crashed when given the input: " + mockInput);
            }
        }
    }

    @Test
    public void testCountOccurrences() {
        String[] haystacks = { "foo foo bar baz.baz.foo? baz bar,bum,foo foofoo!",
                "this that, the other.  and this too", "which.is? it.is.it.working or.is.it.not",
                "this is a easy test, this is a easy test, this is a easy test",
                "Capital words should pass Too, Capital words!",
                "What's wrOng wITh YoU? WRoNg WitH YOU, wItH yoU, you" };
        String[] needles = { "foo", "this", "it", "TEST", "capital", "wItH" };
        int[] expecteds = { 4, 2, 3, 3, 2, 3 };

        for (int i = 0; i < haystacks.length; i++) {
            String needle = needles[i];
            String haystack = haystacks[i];
            int expected = expecteds[i];
            try {
                int actual = TextAnalysis.countOccurrences(needle, haystack);
                if (actual != expected) {
                    assertEquals("Expected the countOccurrences() function to return " + expected
                            + " when counting the occurrences of '" + needle + "' in the text '"
                            + haystack + "'",
                            "Returned " + actual + " instead.");
                }
            } catch (Exception e) {
                assertEquals("Expected the countOccurrences() function not to crash.",
                        "The function crashed when passed the arguments '" + needle + "' and '"
                                + haystack + "'.");
            }
        }
    }

    @Test
    public void testCalculatePercentages() {
        int[][] mockInputs = {
                { 10, 1 },
                { 1, 10 },
                { 1, 10 },
                { 30, 100 },
        };
        int[] expecteds = { 1000, 10, 10, 30 };
        for (int i = 0; i < mockInputs.length; i++) {
            int[] mockInput = mockInputs[i];
            int expected = expecteds[i];
            try {
                if (i == 4) {
                    systemOutRule.enableLog(); // start capturing System.out
                    int actual = TextAnalysis.calculatePercentage(mockInput[0], mockInput[1]);
                    String output = systemOutRule.getLogWithNormalizedLineSeparator();
                    if (expected != actual) {
                        assertEquals("Expected the calculatePercentage() function to return " + expected
                                + ", given the arguments " + mockInput[0] + " and "
                                + mockInput[1],
                                "Instead it returned " + actual);
                    }
                    if (!output.contains("warning: num2 is 0.0! return 0.0")) {
                        assertEquals("Expected the calculatePercentage() function to print a warning message: " +
                                "warning: num2 is 0.0! return 0.0", "Instead it returned: " + output);
                    }
                } else {
                    int actual = TextAnalysis.calculatePercentage(mockInput[0], mockInput[1]);
                    if (expected != actual) {
                        assertEquals("Expected the calculatePercentage() function to return " + expected
                                + ", given the arguments " + mockInput[0] + " and " + mockInput[1],
                                "Instead it returned " + actual);
                    }
                }
            } catch (Exception e) {
                assertEquals(
                        "Expected the calculatePercentage() function not to crash when called.",
                        "The function threw an Exception when given the arguments " + mockInput[0]
                                + " and " + mockInput[1]);
            }
        }
    }

    // text = "", then length should be 0. and should return 0.0
    // some problems students could have
    // the input ticks could have repeat value
    @Test
    public void testCalculateTicDensity() {
        // basic
        {
            String[][] mockTics = { { "foo", "bar" }, { "goo", "gar" }, { "Boo", "bar", "bAz" },
                    { "emmm", "what", "to", "do" }, { "foo", "bar" },
                    { "gar", "goo" },
                    { "Boo", "bar", "bAz" } };
            String[] mockTexts = {
                    "foo bar, bar foo foo. bar bar foo?",
                    "foo goo gar, Foo goo. gar bar foo!",
                    "foo bar? goo. gar boo bar. baz Bum? Foo bar? goo. gar boo bar. baz Bum?",
                    "", "foo bar, bar foo foo. bar bar foo?",
                    "foo goo gar, Foo goo. gar bar foo!",
                    "foo bar? goo. gar boo bar. baz Bum? Foo bar? goo. gar boo bar. baz Bum?"
            };
            double[] expecteds = { 1, .5, .5, 0, 1, .5, .5, 0 };

            for (int i = 0; i < mockTics.length; i++) {
                String[] mockTic = mockTics[i];
                String mockText = mockTexts[i];
                double expected = expecteds[i];
                try {
                    if (i == 3) {
                        systemOutRule.enableLog(); // start capturing System.out
                        double actual = TextAnalysis.calculateTicDensity(mockTic, mockText);
                        String output = systemOutRule.getLogWithNormalizedLineSeparator();
                        if (expected != actual) {
                            assertEquals("Expected the calculateTicDensity() function to return " + expected
                                    + ", given the arguments " + Arrays.toString(mockTic) + " and "
                                    + mockText,
                                    "Instead it returned " + actual);
                        }
                        if (!output.contains("warning: the text is empty! return 0.0")) {
                            assertEquals("Expected the calculateTicDensity() function to print a warning message:" +
                                    "warning: the text is empty! return 0.0", "Instead it returned: " + output);
                        }
                    } else {
                        double actual = TextAnalysis.calculateTicDensity(mockTic, mockText);
                        if (expected != actual) {
                            assertEquals("Expected the calculateTicDensity() function to return " + expected
                                    + ", given the arguments " + Arrays.toString(mockTic) + " and "
                                    + mockText,
                                    "Instead it returned " + actual);
                        }
                    }
                } catch (Exception e) {
                    assertEquals(
                            "Hint: In Java, you always have to notice to avoid operating null, " +
                                    "checking null is also the first thing to do! Never trust the user!" +
                                    "\n Expected the countOccurrences() function not to crash.",
                            "The function crashed when passed the arguments '" + Arrays.toString(mockTic) + "' and '"
                                    + mockText + "'.");
                }
            }
        }
    }

    @Test
    public void totalTicCount() {
        String[][] mockTics = { { "hi", "hahaha" }, { "ho", "hahaha" },
                { "I", "me", "12131" }, { "won't", "me", "tell" },
                { "..", "you", "And" }, { "ya!", "this", "is", "a", "dummy" }
        };
        String[] mockTexts = {
                "hi hahaha ho ho ho ",
                "hi hahaha hahah ahaha ho ho hoa hahaha hahaha ahaha ho ho ho hahaha ho ho ho ",
                "I won't do what you tell me! I won't do what you tell me! I won't do what you tell me",
                "I won't do what you tell me! I won't do what you tell me! I won't do what you tell me",
                "And now .. you do .. what they told ya! .... And now you do what they told ya!",
                "And now .. you do .. what they told ya! .... And now you do what they told ya!"
        };
        int[] expecteds = { 2, 12, 6, 9, 4, 0 };

        for (int i = 0; i < mockTics.length; i++) {
            String[] mockTic = mockTics[i];
            String mockText = mockTexts[i];
            int expected = expecteds[i];
            try {
                int actual = TextAnalysis.totalTicCount(mockTic, mockText);
                if (expected != actual) {
                    assertEquals(
                            "Hint: Please check if you removed the repeated elements!"
                                    + "And please use countOccurrences() in totalTicCount()\n" +
                                    "Expected the totalTicCount() function to return " + expected
                                    + ", given the arguments " + Arrays.toString(mockTic) + " and "
                                    + mockText,
                            "Instead it returned " + actual);
                }
            } catch (Exception e) {
                assertEquals(
                        "Hint: In Java, you always have to notice to avoid operating null, " +
                                "checking null is also the first thing to do! Never trust the user!" +
                                "\n Expected the countOccurrences() function not to crash.",
                        "The function crashed when passed the arguments '" + Arrays.toString(mockTic) + "' and '"
                                + mockText + "'.");
            }
        }
    }

    @Test
    public void testMainTrumpSpeech() {
        systemOutRule.enableLog(); // start capturing System.out
        try {
            {
                String filePath = TextAnalysis.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                String trumpSpeechPath = filePath + "../trump_speech_010621.txt";
                String[] input = { trumpSpeechPath, "uh,like, um,so" };
                String[] args = {};
                systemInMock.provideLines(input);
                TextAnalysis.main(args);
                String output = systemOutRule.getLogWithNormalizedLineSeparator();
                assertTrue(output.contains("87"));
                assertTrue(output.contains("0.01"));
                assertTrue(output.contains("32"));
                assertTrue(output.contains("55"));
                assertTrue(output.contains("37"));
                assertTrue(output.contains("63"));
            }
            {
                String filePath = TextAnalysis.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                String trumpSpeechPath = filePath + "../trump_speech_010621.txt";
                String[] input = { trumpSpeechPath, "uh,like, um,so, uh,like,um so,,,,,, like ,like" };
                String[] args = {};
                systemInMock.provideLines(input);
                TextAnalysis.main(args);
                String output = systemOutRule.getLogWithNormalizedLineSeparator();
                assertTrue(output.contains("87"));
                assertTrue(output.contains("0.01"));
                assertTrue(output.contains("32"));
                assertTrue(output.contains("55"));
                assertTrue(output.contains("37"));
                assertTrue(output.contains("63"));
            }
            {
                String filePath = TextAnalysis.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                String trumpSpeechPath = filePath + "../trump_speech_010621.txt";
                String[] input = { trumpSpeechPath, "maKE, America, gREAT, ,,,,     ,AGAIN" };
                String[] args = {};
                systemInMock.provideLines(input);
                TextAnalysis.main(args);
                String output = systemOutRule.getLogWithNormalizedLineSeparator();
                assertTrue(output.contains("54"));
                assertTrue(output.contains("10"));
                assertTrue(output.contains("19"));
                assertTrue(output.contains("7"));
                assertTrue(output.contains("13"));
                assertTrue(output.contains("24"));
                assertTrue(output.contains("44"));
            }
            {
                String filePath = TextAnalysis.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                String trumpSpeechPath = filePath + "THIS DEFINITLY IS NOT A VALID DIRECTION";
                String[] input = { trumpSpeechPath, "dummy" };
                String[] args = {};
                systemInMock.provideLines(input);
                TextAnalysis.main(args);
                String output = systemOutRule.getLogWithNormalizedLineSeparator();
                assertTrue(output.contains("Oh no... can't find the file!"));
            }
        } catch (Exception e) {
            assertEquals("Expected the main() not to crash when called",
                    "The function threw an Exception when called.");
        }
    }

}
