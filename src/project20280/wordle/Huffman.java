package project20280.wordle;

import project20280.priorityqueue.HeapPriorityQueue;
import project20280.tree.LinkedBinaryTree;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Huffman {
    private static List<String> dictionary;

    private static class HuffmanNode implements Comparable<HuffmanNode> {
        private char character;
        private int frequency;
        private HuffmanNode left;
        private HuffmanNode right;

        HuffmanNode(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }
        private Boolean isLeaf() {
            return (right == null && left == null);
        }
        @Override
        public int compareTo(HuffmanNode otherNode) {
            return Integer.compare(this.frequency, otherNode.frequency);
        }
    }

    public static void readDictionary(String fileName) {
        List<String> wordList = new ArrayList<>();
        try {
            // Open and read the dictionary file
            InputStream in = Huffman.class.getClassLoader().getResourceAsStream(fileName);
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
        dictionary = wordList;
    }

    // Get a string with all the dictionary words concatenated
    public static String getWordListString() {
        StringBuilder wordList = new StringBuilder();
        for (String word : dictionary) {
            wordList.append(word);
        }
        return wordList.toString();
    }

    // Calculate the frequency of each character in the dictionary
    public static HashMap<Character, Integer> calculateLetterFreq(String wordList) {
        HashMap<Character,Integer> letterFreq = new HashMap<>();

        // add the frequency of each letter in the word to the hashmap
        for (char c : wordList.toCharArray()) {
            if (!letterFreq.containsKey(c)) { // if the character hasn't been counted before, add it now
                letterFreq.put(c, 1);
            } else {
                letterFreq.put(c, letterFreq.get(c) + 1); // else increment frequency
            }
        }

        return letterFreq;
    }

    // Build a Huffman tree with the character frequencies
    public static LinkedBinaryTree<HuffmanNode> buildHuffmanTree(HashMap<Character, Integer> dictionaryLetterFreq) {
        HeapPriorityQueue<Integer, HuffmanNode> pq = new HeapPriorityQueue<>();

        // add the characters and their frequencies as trees to the priority queue
        for (Map.Entry<Character, Integer> entry : dictionaryLetterFreq.entrySet()) {
            pq.insert(entry.getValue(), new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // build the tree
        while (pq.size() > 1) {
            HuffmanNode left = pq.removeMin().getValue();
            HuffmanNode right = pq.removeMin().getValue();

            // combine the two nodes into a new one (with combined frequencies)
            HuffmanNode combinedNode = new HuffmanNode('\0', left.frequency+ right.frequency);
            combinedNode.left = left;
            combinedNode.right = right;

            pq.insert(combinedNode.frequency, combinedNode);
        }

        // at the end there should only be one node in the priority queue (or tree)
        HuffmanNode root = pq.removeMin().getValue();
        LinkedBinaryTree<HuffmanNode> huffmanTree = new LinkedBinaryTree<>();
        huffmanTree.addRoot(root);
        return huffmanTree;
    }

    // Generate the codewords
    public static HashMap<Character,String> compress(LinkedBinaryTree<HuffmanNode> huffmanTree) {
        HashMap<Character,String> codeWords = new HashMap<>();
        if (huffmanTree == null) {
            System.out.println("Huffman Tree is null.");
            return codeWords;
        }
        compressRecursive(huffmanTree.root().getElement(), "", codeWords);
        return codeWords;
    }

    private static void compressRecursive(HuffmanNode root, String codeWord, HashMap<Character, String> codeWords) {
        if (root == null) return;

        if (!root.isLeaf()) { // traverse if not a leaf node
            compressRecursive(root.left, codeWord+"0", codeWords);
            compressRecursive(root.right, codeWord+"1", codeWords);
        } else { // otherwise that's the end of the codeword
            codeWords.put(root.character, codeWord);
        }
    }

    public static HashMap<String,String> encodeDictionary(HashMap<Character,String> codewords) {
        HashMap<String,String> encodedDictionary = new HashMap<>();
        for (String word : dictionary) {
            StringBuilder encodedWord = new StringBuilder();
            for (char c : word.toCharArray()) {
                String codeword = codewords.get(c);
                if (codeword != null) encodedWord.append(codeword);
                else System.out.println("Char '"+c+"' does not have a codeword");
            }
            encodedDictionary.put(word, encodedWord.toString());
        }
        return encodedDictionary;
    }

    public static int getNumAsciiBits() {
        return dictionary.size() * 5 * 8; // dictionary contains n amount of 5-letter words, ascii takes 8 bits per character
    }
    public static int getNumHuffmanBits(HashMap<Character, String> codewords, HashMap<Character, Integer> letterFreq) {
        int huffmanBitCount = 0;
        for (Map.Entry<Character,String> entry : codewords.entrySet()) {
            huffmanBitCount += entry.getValue().length() * letterFreq.get(entry.getKey());
        }
        return huffmanBitCount;
    }

    public static void findLongestAndShortestCode(HashMap<String, String> encodedDictionary) {
        String longestWord = "";
        String shortestWord = "";
        int longestLength = Integer.MIN_VALUE;
        int shortestLength = Integer.MAX_VALUE;

        for (Map.Entry<String, String> entry : encodedDictionary.entrySet()) {
            String word = entry.getKey();
            String encodedWord = entry.getValue();
            int length = encodedWord.length();

            if (length > longestLength) {
                longestLength = length;
                longestWord = word;
            }

            if (length < shortestLength) {
                shortestLength = length;
                shortestWord = word;
            }
        }

        System.out.println("Word with the longest code: " + longestWord + " with " + longestLength + " bits");
        System.out.println("Word with the shortest code: " + shortestWord + " with " + shortestLength + " bits");
    }

    public static void main(String[] args) {
        readDictionary("project20280/wordle/resources/dictionary.txt");
//        readDictionary("project20280/wordle/resources/extended-dictionary.txt");
        String dictionaryWords = getWordListString();
        HashMap<Character,Integer> letterFreq = calculateLetterFreq(dictionaryWords);
        LinkedBinaryTree<HuffmanNode> huffmanTree = buildHuffmanTree(letterFreq);
        HashMap<Character, String> codewords = compress(huffmanTree);
        HashMap<String,String> encodedDictionary = encodeDictionary(codewords);

        System.out.println(codewords.size());

        /* Codewords */
        System.out.println("Huffman Codewords:");
        for (Map.Entry<Character,String> entry : codewords.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        /* Encoded Dictionary */
        System.out.println("\nEncoded Dictionary:");
        System.out.println("   Word    |        Encoded        |");
        System.out.println("------------------------------------");
        for (Map.Entry<String,String> entry : encodedDictionary.entrySet()) {
            System.out.println("   " + entry.getKey() + "   | " + entry.getValue());
        }

        /* Compression ratio */
        int asciiBitCount = getNumAsciiBits();
        int huffmanBitCount = getNumHuffmanBits(codewords, letterFreq);

        double compressionRatio = (double) asciiBitCount / huffmanBitCount;
        double compressionPercentage = (1/compressionRatio) * 100;

        System.out.println("\nBits required for ASCII encoding : " + asciiBitCount);
        System.out.println("Bits required for Huffman encoding : " + huffmanBitCount);
        System.out.println("Compression ratio: " + compressionRatio);
        System.out.printf("Compression percentage: %.2f percent\n", compressionPercentage);

        /* Longest and Shortest Huffman Words */
        findLongestAndShortestCode(encodedDictionary);
    }
}
