package edu.nyu.cs.assignment4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A program to analyze the use of verbal tics in any transcribed text.
 * Complete the functions to perform the tasks indicated in the comments.
 * Refer to the README.md file for additional requirements and helpful hints.
 */
public class TextAnalysis {
    // use this "global"-ish Scanner variable when getting keyboard input from the
    // user within any function; this avoids common problems using several different
    // Scanners within different functions
    public static Scanner scanner = new Scanner(System.in);

    /**
     * The main function is automatically called first in a Java program.
     * This function contains the main logic of the program that makes use of all
     * the other functions to solve the problem.
     * This main function MUST make use of the other functions to perform the tasks
     * those functions are designed for, i.e.:
     * - you must use the getFilepathFromUser() to get the file path to the text
     * file that the user wants to analyze
     * - you must use the getContentsOfFile() function whenever you need to get the
     * contents of the text file
     * - you must use the getTicsFromUser() function whenever you need to get the
     * set of tics the user wants to analyze in the text
     * - you must use the countOccurrences() function whenever you want to count the
     * number of occurrences of a given tic within the text
     * - you must use the calculatePercentage() function whenever you want to
     * calculate the percentage of all tics in the text that a given tic consumes
     * - you must use the calculateTicDensity() function to calculate the proportion
     * of all words in the text that are tic words
     *
     * @param args An array of any command-line arguments.
     */
    public static void main(String[] args) throws Exception {

        // complete this function according to the instructions
        // System.out.println("Second step was a gift "
        String filepath;
        String text;

        filepath = getFilepathFromUser();
        text = getContentsOfFile(filepath);

        String[] tics = getTicsFromUser();

        System.out.println("\n...............................Analyzing text...............................\n");
        int totalTics = totalTicCount(tics, text);
        double ticDensity = calculateTicDensity(tics, text);

        System.out.println("Total number of tics: " + totalTics);
        System.out.println("Density of tics: " + String.format("%.2f", ticDensity) + "\n");

        System.out.println("...............................Tic breakdown.................................\n");

        for (String tic : tics) {
            int occurrences = countOccurrences(tic, text);
            int percentage = calculatePercentage(occurrences, totalTics);
            System.out.print(String.format("%-10s", tic) + "/ "
                    + String.format("%-19s", (Integer.toString(countOccurrences(tic, text)))) + "/ ");
            System.out.println(Integer.toString(percentage) + "% of all tics");

        }
    }

    /**
     * getFilepathFromUser method
     * Asks the user to enter the path to the text file they want to analyze.
     * Hint:
     * - use the "global"-ish Scanner variable scn to get the response from the
     * user, rather than creating a new Scanner variable ithin this function.
     * - do not close the "global"-ish Scanner so that you can use it in other
     * functions
     *
     * @return The file path that the user enters, e.g.
     *         "trump_speech_010621.txt"
     */
    public static String getFilepathFromUser() {
        // complete the getFilepathFromUser function according to the instructions above
        System.out.println("What file would you like to open?");
        String fileName = scanner.nextLine();
        return fileName;

    }

    /**
     * getContentsOfFile method
     * Opens the specified file and returns the text therein.
     * If the file can't be opened, print out the message,
     * "Oh no... can't find the file!"
     *
     * @param filename The path to a text file containing a speech transcript with
     *                 verbal tics in it.
     * @return The full text in the file as a String.
     */
    public static String getContentsOfFile(String filepath) {
        // the code in this function is given to you as a gift... don't change it.

        String fullText = "";
        // opening up a file may fail if the file is not there, so use try/catch to
        // protect against such errors
        try {
            // try to open the file and extract its contents
            Scanner scn = new Scanner(new File(filepath));
            while (scn.hasNextLine()) {
                String line = scn.nextLine();
                fullText += line + "\n"; // nextLine() removes line breaks, so add them back in
            }
            scn.close(); // be nice and close the Scanner
        } catch (FileNotFoundException e) {
            // in case we fail to open the file, output a friendly message.
            System.out.println("Oh no... can't find the file!");
        }
        return fullText.toLowerCase(); // return the full text
    }

    /**
     * getTicsFromUser method
     * Asks the user to enter a comma-separated list of tics, e.g. "uh,like, um,
     * so", and returns an array containing those tics, e.g. { "uh", "like",
     * "um", "so" }. You don't need to care about the repeated words, just
     * seperate them all.
     * 
     * Hint:
     * - use the "global"-ish Scanner variable scn to get the response from the
     * user, rather than creating a new Scanner variable within this function.
     * - do not close the "global"-ish Scanner so that you can use it in other
     * functions
     * 
     * Notice: Never trust users! The input could have many dummy commas and spaces
     * such as ",,, ,,,, ,,,, , , , , , , ". Return a String array with size = 0
     * when there is no word in the input.
     *
     * @return A String array containing each of the tics to analyze, with any
     *         leading or trailing whitespace removed from each tic.
     */

    public static String[] getTicsFromUser() {
        System.out.println("What words would you like to look for?");
        String inputTics = scanner.nextLine();
        String[] tics = inputTics.split("[, \n\t.?!]+");
        tics = Arrays.stream(tics)
                .filter(tic -> !tic.trim().isEmpty())
                .toArray(String[]::new);

        return tics;
    }

    /**
     * // * countOccurrences method
     * Counts how many times a given string (the needle) occurs within another
     * string (the haystack), ignoring case.
     * String tic = "my"
     * String myString = "hello my name is Alex"
     * String[] myStringarr = myString.split(" ") --> [hello, my, name, is, ...]
     * Notice: Please use a for loop to count the token number. And there could
     * be a tricky bug during you implement this function, make sure your code
     * will not get into the for loop when the input is null or "". when the
     * input is "" or null, return 0.
     *
     * @param needle   The String to search for.
     * @param haystack The String within which to search.
     * @return THe number of occurrences of the "needle" String within the
     *         "haystack" String, ignoring case.
     */

    public static int countOccurrences(String needle, String haystack) {
        if (haystack == null || haystack.isEmpty()) {
            return 0;
        }

        int occurrences = 0;

        String[] haystackArr = haystack.split("[\\s.,?!]+");
        for (String word : haystackArr) {
            if (word.equalsIgnoreCase(needle)) {
                occurrences++;
            }
        }
        return occurrences;
    }

    /**
     * calculatePercentage method
     * Calculates the equivalent percentage from the proportion of one number to
     * another number.
     *
     * @param num1 The number to be converted to a percentage. i.e. the numerator in
     *             the ratio of num1 to num2.
     * @param num2 The overall number out of which the num1 number is taken. i.e.
     *             the denominator in the ratio of num1 to num2.
     * @return The percentage that rum1 represents out of the total of num2, rounded
     *         to the nearest integer.
     */

    public static int calculatePercentage(double num1, double num2) {
        double percentage = (num1 / num2) * 100;
        int roundedPercentage = (int) Math.round(percentage);
        return roundedPercentage;
    }

    /**
     * calculateTicDensity method
     * Calculates the "density" of tics in the text. In other words, the proportion
     * of tic words to the total number of words in a text.
     * Hint:
     * - assume that words in the text are separated from one another by any of the
     * following characters: space ( ), line break (\n), tab (\t), period (.), comma
     * (,), question mark (?), or exclamation mark (!)
     * - all Strings have a .split() method which can split by any of a collection
     * of characters given as an argument; This function returns an array of the
     * remaining text that was separated by any of those characters
     * - e.g. "foo-bar;baz.bum".split("[-;.]+") will result in an array with {
     * "foo", "bar", "baz", and "bum" } as the values.
     * 
     * Notice: the input text could be "", which means you might divide by 0. In
     * that case, return 0.0 and print a message "the text is empty! return 0.0".
     * 
     * Also, take care of the case that the input is null.
     *
     * @param tics     An array of tic words to analyze.
     * @param fullText The full text.
     * @return The proportion of the number of tic words present in the text to the
     *         total number of words in the text, as a double.
     */

    public static double calculateTicDensity(String[] tics, String fullText) {
        if (fullText == null || fullText.isEmpty()) {
            System.out.println("warning: the text is empty! return 0.0");
            return 0.0;
        }
        int totalWords = 0;
        int ticWordsCount = 0;
        String[] words = fullText.split("[\\s.,?!]+");
        for (String word : words) {
            totalWords++;
            for (String tic : tics) {
                if (word.equalsIgnoreCase(tic)) {
                    ticWordsCount++;
                    break;
                }
            }
        }
        if (totalWords == 0) {
            return 0.0;
        }
        return (double) ticWordsCount / totalWords;
    }

    /**
     * totalTicCount method
     * Given a String fullText and a String array tics, compute the total number of
     * tics in fullText.
     * 
     * @param tics
     * @param fullText
     * @return
     */
    public static int totalTicCount(String[] tics, String fullText) {
        int total = 0;
        for (String tic : tics) {
            total += countOccurrences(tic, fullText);
        }
        return total;
    }

}
// end of class