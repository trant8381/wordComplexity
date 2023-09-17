
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;


public class Main {
    static List<String> values = Arrays.asList("a", "o", "u", "i", "e", "y");
    static public int countSyllables(String s) {
        int syllables = 0;
        boolean isVowel = false;
        for (String item : s.split("")) {
            boolean contains = values.contains(item.toLowerCase());
            if (contains) {
                if (!isVowel) {
                    syllables++;
                }
                isVowel = true;
            } else {
                isVowel = false;
            }
        }
        if (s.endsWith("e")) {
            return syllables - 1;
        } else {
            return syllables;
        }
    }

    public static long calculateAge(long score) {
        return score + 6;
    }
    public static long ariIndex(int chars, int words, int sentences) {
        double ARI = (4.71 * (chars / (double) words)) + (0.5 * (words / (double) sentences)) - 21.43;

        long intScore = Math.round(ARI);
        long age = calculateAge(intScore);
        
        System.out.printf("\nAutomated Readability Index: %.2f (about %d-year-olds).\n", ARI, age);
        
        return age;
    }

    public static long fkIndex(int words, int sentences, int syllables) {
        double FK = 0.39 * ((double) words / sentences) + 11.8 * ((double) syllables / words) - 15.59;
        long intScore = Math.round(FK);
        long age = calculateAge(intScore);
        
        System.out.printf("Flesch-Kincaid readability tests: %.2f (about %d-year-olds).\n", FK, age);
        return age;
    }

    public static long SMOGIndex(int sentences, int poly) {
        double SMOG = 1.043 * Math.sqrt(poly * (30 / (double) sentences)) + 3.1291;
        long intScore = Math.round(SMOG);
        long age = calculateAge(intScore);
        
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n", SMOG, age);
        return age;
    }

    public static long CLIndex(int letters, int words, int sentences) {
        double CL = (0.0588 * ((letters / (double) words) * 100)) - (0.296 * ((sentences / (double) words) * 100)) - 15.8;
        long intScore = Math.round(CL);
        long age = calculateAge(intScore);
        
        System.out.printf("Coleman-Liau index: %.2f (about %d-year-olds).\n", CL, age);
        return age;
    }

    public static void main(String[] args) {
        File file = new File(args[0]);

        try {
            Scanner fileScanner = new Scanner(file);
            String text = fileScanner.nextLine();
            fileScanner.close();
            String[] tokens = text.split("[.?!]");
            
            int words = 0;
            int chars = text.replaceAll("\s", "").length();
            int sentences = 0;
            int syllables = 0;
            int poly = 0;

            for (String i : tokens) {
                int d = 0;
                for (String j : i.split("\\s")) {
                    if (!j.equals("")) {
                        d += 1;
                        String charOnly = j.replaceAll("[^A-Za-z]", "");
                        int wordSyllables = countSyllables(charOnly);
                        syllables += wordSyllables <= 0 ? 1 : wordSyllables;
                        if (wordSyllables > 2) {
                            poly++;
                        }
                    }
                }
                sentences++;
                words += d;
            }


            Scanner scanner = new Scanner(System.in);
            System.out.println("The text is:");
            System.out.println(text + "\n");
            System.out.printf("Words: %d\nSentences: %d\nCharacters: %d\nSyllables: %d\nPolysyllables: %d\n", words, sentences, chars, syllables, poly);
            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");

            String score = scanner.next();
            int totalAges = 0;
            
            switch (score) {
                case "ARI" -> ariIndex(chars, words, sentences);
                case "FK" -> fkIndex(words, sentences, syllables);
                case "SMOG" -> SMOGIndex(sentences, poly);
                case "CL" -> CLIndex(chars, words, sentences);
                default -> totalAges += ariIndex(chars, words, sentences) +
                             fkIndex(words, sentences, syllables)+
                             SMOGIndex(sentences, poly) +
                             CLIndex(chars, words, sentences);
            }
            System.out.printf("This text should be understood in average by %.2f-year-olds.\n", totalAges / 4.0);
            scanner.close();
        } catch (java.io.FileNotFoundException ignored) {}

    }
}