package project20280.wordle;

import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/*
 * 26/04/2024
 * Created as part of a Data Structures (COMP20280) assignment
 * Data structure classes provided by Aonghus Lawler (lecturer for Data Structures)
 * Wordle & WordleTest classes made by Elvin Jiby
 *
 * A simple Wordle solver that helps you guess a word based on a sample dictionary of words!
 * You can play by running the Main method down below!
 *
 * When you start, the console will display all the words in the dictionary. Choose one and enter it as your guess.
 * Then the program will display a hint based on your guess
 *  -> + for correct letter in the correct position,
 *  -> o for correct letter but not in the correct position
 *  -> _ for incorrect letter (not present in the target word)
 *
 * You'll then get a list of the best words to guess next, with their scores sorted from highest to lowest
 * Continue picking the highest scored word until you win
 * Do note that this doesn't guarantee you correctly guess the word in all 6 guesses.
 *
 * Have fun!
 */


public class Wordle {
    Random rand = new Random();

    String fileName = "project20280/wordle/resources/dictionary.txt";
    //String fileName = "project20280/wordle/resources/extended-dictionary.txt";
    static List<String> dictionary = null;

    static final String winMessage = "CONGRATULATIONS! YOU WON! :)";
    static final String lostMessage = "YOU LOST :( THE WORD CHOSEN BY THE GAME IS: ";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    final int num_guesses = 6; // Number of attempts given to player

    Wordle() {dictionary = readDictionary(fileName);}

    Wordle(String fileName) {
        dictionary = readDictionary(fileName);
    }

    public static void main(String[] args) {
        Wordle game = new Wordle(); // load dictionary
        String target = game.getRandomTargetWord();
//        System.out.println("target: " + target);
        game.play(target);
    }

    public void play(String target) {
        List<String> dictionaryCurrent; // used to refresh dictionary after current guess

//        System.out.println("dict length: " + dictionary.size());
        System.out.println("dict: " + dictionary);

        for(int i = 0; i < num_guesses; ++i) {
            dictionaryCurrent = dictionary; // copy of the dictionary (important for functions ahead)
            String guess = getGuess(); // get user's guess from STDIN

            if(guess.equals(target)) { // you won!
                win(target);
                return;
            }

            // hashmap for frequency of each letter in the target & guess
            ChainHashMap<Character,Integer> targetFreqCount = getLetterFreq(target);
            ChainHashMap<Character,Integer> guessFreqCount = getLetterFreq(guess);

            // Compute the hint string
            String [] hint = {"_", "_", "_", "_", "_"};

            // Set the arrays for green (which letters are correct)
            int num_green = setGreenLetters(target, guess, hint, targetFreqCount, guessFreqCount);

            // Check for a win
            if(num_green == 5) {
                win(target);
                return;
            }

            // Set the arrays for yellow (whether letters appear in the word or not)
            setYellowLetters(guess, targetFreqCount, hint);

            // After setting the yellow and green positions, the remaining hint positions must be "not present" or "_"
            System.out.println("hint: " + Arrays.toString(hint));

            // Removing words that don't match the hint
            dictionaryCurrent.removeIf(word -> !matchesHint(word, hint, guess));

            // At this point we removed some words based on the hint, so store the score of the current dictionary
            ChainHashMap<String,Integer> possible_words = buildWordHashmap(dictionaryCurrent);

            // To get the best guesses, we need to sort by highest score
            List<Entry<String, Integer>> scoreList = new ArrayList<>();
            for (Entry<String,Integer> entry : possible_words.entrySet()) {
                scoreList.add(entry);
            }
            scoreList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            // Print best guesses list (to give user some help)
            System.out.print("Best Guesses {" + scoreList.size() + " words} [");
            for (int j = 0; j < possible_words.size(); j++) {
                System.out.print(scoreList.get(j).getKey()+"="+scoreList.get(j).getValue());
                if (j < scoreList.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
        }

        lost(target); // No guess remaining at this point. So user loses.
    }

    private static int setGreenLetters(String target, String guess, String[] hint, ChainHashMap<Character, Integer> targetFreqCount, ChainHashMap<Character, Integer> guessFreqCount) {
        int num_green = 0;
        for (int k = 0; k < 5; k++) { // For each letter in the user's guess, check if it matches the letter in the target in the same position
            if (guess.charAt(k) == target.charAt(k)) {
                hint[k] = "+";
                num_green++;

                char letter = guess.charAt(k);
                targetFreqCount.put(letter, targetFreqCount.get(letter)-1);
                guessFreqCount.put(letter, guessFreqCount.get(letter)-1);
            }
        }
        return num_green;
    }

    private void setYellowLetters(String guess, ChainHashMap<Character, Integer> targetFreqCount, String[] hint) {
        char letter;
        for (int k = 0; k < 5; k++) {
            if (hint[k].equals("+")) continue; // if hint == "+" (green) skip it

            letter = guess.charAt(k);
            // if the letter occurs atleast once in the target, set it to 'o'
            if (targetFreqCount.get(letter) != null && targetFreqCount.get(letter) > 0) { // check if the letter is present in the target word
                hint[k] = "o"; // set to "o" (yellow)
                // decrement the count, so it only sets it to 'o' depending on the frequency of that letter in the target
                targetFreqCount.put(letter, targetFreqCount.get(letter)-1);
            }
        }
    }

    public static ChainHashMap<String, Integer> buildWordHashmap(List<String> dictionaryCurrent) {
        ChainHashMap<String,Integer> wordHashmap = new ChainHashMap<>();
        ChainHashMap<Character,Integer> dictionaryLetterFreq = calculateDictionary_LetterFreq(); // get hashmap that contains all letter frequencies in dictionary
        int sumLetterFrequency;

        // calculate sum of letter frequencies for current word
        for (String word : dictionaryCurrent) {
            sumLetterFrequency = 0;
            for (char c : word.toCharArray()) {
                if (dictionaryLetterFreq.get(c) != null) {
                    sumLetterFrequency += dictionaryLetterFreq.get(c);
                }
            }
            // assign to that particular word in wordHashmap
            wordHashmap.put(word, sumLetterFrequency);
        }
        return wordHashmap;
    }

    private static ChainHashMap<Character, Integer> calculateDictionary_LetterFreq() {
        ChainHashMap<Character,Integer> letterFreq = new ChainHashMap<>();

        // add the frequency of each letter in the word to the hashmap
        for (String word : dictionary) {
            for (char c : word.toCharArray()) {
                if (letterFreq.get(c) == null) { // if the character hasn't been counted before, add it now
                    letterFreq.put(c, 1);
                } else {
                    letterFreq.put(c, letterFreq.get(c) + 1); // else increment frequency
                }
            }
        }
        return letterFreq;
    }

    private ChainHashMap<Character, Integer> getLetterFreq(String word) {
        ChainHashMap<Character, Integer> letterFreq = new ChainHashMap<>();

        for (char c : word.toCharArray()) {
            if (letterFreq.get(c) == null) { // if the character hasn't been counted before, add it now
                letterFreq.put(c, 1);
            } else {
                letterFreq.put(c, letterFreq.get(c) + 1); // else increment frequency
            }
        }
        return letterFreq;
    }

    private boolean matchesHint(String word, String[] hint, String guess) {
        for (int i = 0; i < 5; i++) {
            char wordLetter = word.charAt(i);
            char guessLetter = guess.charAt(i);

            switch (hint[i]) {
                case "_" -> { // gray hint - words with a letter that is not in the hint at all
                    if (guessLetter == wordLetter) return false;
                }
                case "o" -> { // yellow hint
                    if (guessLetter == wordLetter || !word.contains(String.valueOf(guessLetter))) return false;
                    // if the guess letter is the same as the word letter, then that word contains the letter at the wrong place
                    // meaning that that word is not correct.
                }
                case "+" -> { // green hint
                    if (guessLetter != wordLetter) return false; // if the word and the guess letter don't match
                }
                default -> throw new IllegalArgumentException("Unknown letter found in hint String.");
            }
        }
        return true;
    }

    public void lost(String target) {
        System.out.println();
        System.out.println(lostMessage + target.toUpperCase() + ".");
        System.out.println();

    }
    public void win(String target) {
        System.out.println(ANSI_GREEN_BACKGROUND + target.toUpperCase() + ANSI_RESET);
        System.out.println();
        System.out.println(winMessage);
        System.out.println();
    }

    public String getGuess() {
        Scanner myScanner = new Scanner(System.in, StandardCharsets.UTF_8.displayName());  // Create a Scanner object
        System.out.println("Guess:");

        String userWord = myScanner.nextLine();  // Read user input
        userWord = userWord.toLowerCase(); // covert to lowercase

        // check the length of the word and if it exists
        while ((userWord.length() != 5) || !(dictionary.contains(userWord))) {
            if ((userWord.length() != 5)) {
                System.out.println("The word " + userWord + " does not have 5 letters.");
            } else {
                System.out.println("The word " + userWord + " is not in the word list.");
            }
            // Ask for a new word
            System.out.println("Please enter a new 5-letter word.");
            userWord = myScanner.nextLine();
        }
        return userWord;
    }
    public String getRandomTargetWord() {
        // generate random values from 0 to dictionary size
        return dictionary.get(rand.nextInt(dictionary.size()));
    }
    public List<String> readDictionary(String fileName) {
        List<String> wordList = new ArrayList<>();

        try {
            // Open and read the dictionary file
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            assert in != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strLine;

            //Read file line By line
            while ((strLine = reader.readLine()) != null) {
                wordList.add(strLine.toLowerCase());
            }
            //Close the input stream
            in.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return wordList;
    }

    public String[] getHints(String target, String guess) {
        String [] hint = {"_", "_", "_", "_", "_"};
        // hashmap for frequency of each letter in the target
        ChainHashMap<Character,Integer> targetFreqCount = getLetterFreq(target);
        ChainHashMap<Character,Integer> guessFreqCount = getLetterFreq(guess);

        // set the arrays for green
        for (int k = 0; k < 5; k++) {
            if (guess.charAt(k) == target.charAt(k)) {
                hint[k] = "+";
                char letter = guess.charAt(k);
                targetFreqCount.put(letter, targetFreqCount.get(letter)-1);
                guessFreqCount.put(letter, guessFreqCount.get(letter)-1);
            }
        }

        // set the arrays for yellow
        setYellowLetters(guess, targetFreqCount, hint);

        return hint;
    }

    public List<String> getDictionary() {
        return dictionary;
    }
}
