package project20280.wordle;

import org.junit.jupiter.api.Test;
import project20280.hashtable.ChainHashMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordleTest {
    @Test
    void testNumCollisions() { // for checking the number of collisions with both dictionaries
        Wordle wordle = new Wordle("project20280/wordle/resources/dictionary.txt");
        List<String> regularDictionary = wordle.getDictionary();
        ChainHashMap<String,Integer> possible_words = Wordle.buildWordHashmap(regularDictionary);
        System.out.println("Number of collisions in the regular dictionary: " + possible_words.getNumOfCollisions());

        Wordle wordle2 = new Wordle("project20280/wordle/resources/extended-dictionary.txt");
        List<String> extendedDictionary = wordle2.getDictionary();
        ChainHashMap<String,Integer> possible_words2 = Wordle.buildWordHashmap(extendedDictionary);
        System.out.println("Number of collisions in the extended dictionary: " + possible_words2.getNumOfCollisions());
    }

    @Test
    void testLoadFactor() { // ensure that the expected load factor is gotten from the dictionary
        Wordle wordle = new Wordle("project20280/wordle/resources/dictionary.txt");
        List<String> regularDictionary = wordle.getDictionary();
        ChainHashMap<String,Integer> possible_words = Wordle.buildWordHashmap(regularDictionary);

        double expected = 0.28194800439399487;
        double actual = possible_words.getLoadFactor();
        assertEquals(expected, actual);
        System.out.println("Regular dictionary Load Factor: " + actual);

        Wordle wordle2 = new Wordle("project20280/wordle/resources/extended-dictionary.txt");
        List<String> extendedDictionary = wordle2.getDictionary();
        ChainHashMap<String,Integer> possible_words2 = Wordle.buildWordHashmap(extendedDictionary);

        expected = 0.2680277091153224;
        actual = possible_words2.getLoadFactor();
        assertEquals(expected, actual);
        System.out.println("Extended dictionary Load Factor: " + actual);
    }

    @Test
    void testChainVSNormalHashMap() {
        // For Q4 (g) of assignment
        ChainHashMap<String, Integer> chainHashMap= new ChainHashMap<>();
        HashMap<String, Integer> javaHashmap = new HashMap<>();

        // put the same entry in both hashmaps
        chainHashMap.put("word1", 2);
        chainHashMap.put("word2", 16);
        chainHashMap.put("word3", 5);
        javaHashmap.put("word1", 2);
        javaHashmap.put("word2", 16);
        javaHashmap.put("word3", 5);

        // check if the values retrieved are equal
        assertEquals(javaHashmap.get("word1"), chainHashMap.get("word1"));
        assertEquals(javaHashmap.get("word2"), chainHashMap.get("word2"));
        assertEquals(javaHashmap.get("word3"), chainHashMap.get("word3"));
    }

    @Test
    void testHints() { // test to see if the hints are computed correctly
        Wordle wordleA = new Wordle();
        String target, guess;
        String [] hints;

        target = "abbey";
        guess = "keeps";
        hints = wordleA.getHints(target, guess);
        assertEquals("[_, o, _, _, _]", Arrays.toString(hints));

        target = "abbey";
        guess = "kebab";
        hints = wordleA.getHints(target, guess);
        assertEquals( "[_, o, +, o, o]", Arrays.toString(hints));

        target = "abbey";
        guess = "babes";
        hints = wordleA.getHints(target, guess);
        assertEquals( "[o, o, +, +, _]", Arrays.toString(hints));

        target = "lobby";
        guess = "table";
        hints = wordleA.getHints(target, guess);
        assertEquals("[_, _, +, o, _]", Arrays.toString(hints));

        target = "ghost";
        guess = "pious";
        hints = wordleA.getHints(target, guess);
        assertEquals("[_, _, +, _, o]", Arrays.toString(hints));

        target = "ghost";
        guess = "slosh";
        hints = wordleA.getHints(target, guess);
        assertEquals("[_, _, +, +, o]", Arrays.toString(hints));

        target = "kayak";
        guess = "aorta";
        hints = wordleA.getHints(target, guess);
        assertEquals("[o, _, _, _, o]", Arrays.toString(hints));

        target = "kayak";
        guess = "kayak";
        hints = wordleA.getHints(target, guess);
        System.out.println(target + ", " + guess + ", " + Arrays.toString(hints));
        assertEquals("[+, +, +, +, +]", Arrays.toString(hints));

        target = "kayak";
        guess = "fungi";
        hints = wordleA.getHints(target, guess);
        System.out.println(target + ", " + guess + ", " + Arrays.toString(hints));
        assertEquals("[_, _, _, _, _]", Arrays.toString(hints));
    }
}
