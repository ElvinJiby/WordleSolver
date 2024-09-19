package project20280.wordle;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuffmanTest {
    @Test
    void testNumAsciiBits() {
        Huffman.readDictionary("project20280/wordle/resources/dictionary.txt");

        // 2310 words * each are 5 letters * 8 bits for ascii representation
        int expected = 92400;
        int actual = Huffman.getNumAsciiBits();

        assertEquals(expected,actual);
    }

    @Test
    void testNumHuffmanBits() { // test to check if huffman bits are correctly being calculated
        HashMap<Character, Integer> letterFreq = new HashMap<>();
        letterFreq.put('a', 60);
        letterFreq.put('b', 5);
        letterFreq.put('c', 30);

        HashMap<Character, String> codewords = new HashMap<>();
        codewords.put('a', "0");
        codewords.put('b', "10");
        codewords.put('c', "11");

        int expected = 130;
        int result = Huffman.getNumHuffmanBits(codewords,letterFreq);
        assertEquals(expected, result);
    }
}
