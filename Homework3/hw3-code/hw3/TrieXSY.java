package hw3;

import java.util.*;
import java.io.*;
import java.io.FileNotFoundException;

public class TrieXSY {
    public static class TrieNode {
        private HashMap<Character, TrieNode> children;
        private boolean end_state;
        private char key;

        public TrieNode(char key) {
            this.children = new HashMap<>();
            this.end_state = false;
            this.key = key;
        }

        /* return the next TrieNode */
        public TrieNode getChild(char key) {
            return children.get(key);
        }

        public TrieNode insert(char key) {
            TrieNode child = getChild(key);
            if (child == null) {
                child = new TrieNode(key);
                children.put(key, child);
            }
            return child;
        }

        public boolean contains(String s) {
            return children.containsKey(s.charAt(0));
        }
    }


    public TrieNode root = new TrieNode('0');

    public void insert(String s) {
        TrieNode node = root;
        for (int i = 0; i < s.length(); i++) {
            node.insert(s.charAt(i));
            node = node.children.get(s.charAt(i));
        }
    }

    public boolean contains(String s) {
        TrieNode node = root;
        for (int i = 0; i < s.length(); i++) {
            if (!node.contains(s.substring(i))) return false;
            node = node.getChild(s.charAt(i));
        }
        return node.end_state;
    }

    public void insertDictionary(String filename) throws IOException {
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String word = br.readLine();
        while (word != null) {
            System.out.println(word);
            insert(word);
            word = br.readLine();
        }
    }

    public String toStringBFS() {
        Deque<TrieNode> deque = new ArrayDeque<>();
        deque.add(root);
        StringBuilder result = new StringBuilder();
        while (!deque.isEmpty()) {
            TrieNode node = deque.pop();
            for (Character c : node.children.keySet()) {
                result.append(c);
                deque.add(node.children.get(c));
            }
        }
        return result.toString();
    }
}

