package hw3;

import java.io.*;
import java.util.*;
import java.util.Collections.*;

public class Trie {
    public static class TrieNode {
        private boolean wordEnd = false;
        private HashMap<Character, TrieNode> children = new HashMap<>();//map of children

        public TrieNode(boolean wordEnd) {
            this.wordEnd = wordEnd;
        }
        public TrieNode(){
            new TrieNode(false);
        }

        public void insert(String s){
            if (s.length() > 1) {
                children.putIfAbsent(s.charAt(0), new TrieNode());
                children.get(s.charAt(0)).insert(s.substring(1));
            } else {
                children.put(s.charAt(0), new TrieNode(true));
            }
        }

        public boolean contains(String s){
            if (s.length() == 0) return false;
            if (s.length() > 1) {
                return children.containsKey(s.charAt(0)) && children.get(s.charAt(0)).contains(s.substring(1));
            } else {
                return children.containsKey(s.charAt(0)) && children.get(s.charAt(0)).wordEnd;
            }
        }
    }
    public TrieNode root = new TrieNode();
    public void insert(String s){
        root.insert(s);
    }
    public boolean contains(String s) {
        return root.contains(s);
    }
    public void insertDictionary(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String word;
        while ((word = reader.readLine()) != null) this.insert(word);

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

    public String toStringDFS() {
        Stack<TrieNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TrieNode node = stack.pop();
            for (Character c : node.children.keySet()) {
                stack.add(node.children.get(c));
            }
        }
        return "";
    }

}
