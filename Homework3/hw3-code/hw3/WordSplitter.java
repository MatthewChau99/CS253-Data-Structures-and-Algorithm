package hw3;

import java.io.*;
import java.util.*;

public class WordSplitter {
    public static void main(String[] args) throws IOException {
        Trie trie = new Trie();
        trie.insertDictionary(args[0]);
        System.out.println(split(args[1], trie));
    }

    private static String split(String s, Trie trie) {
        if (s.length() == 0) return "No splitting found.";
        Map<Integer, int[]> map = new HashMap<>();
        map.put(0, new int[]{0, 0});
        s = " " + s;
        for (int i = 1; i < s.length(); i++) {
            Set<Integer> keyset = new HashSet<>(map.keySet());
            for (Integer key : keyset) {
                if (trie.contains(s.substring(key + 1, i + 1))) {
                    map.put(i, new int[]{key, i});
                }
            }
        }

        if (map.containsKey(s.length() - 1)) {
            StringBuilder result = new StringBuilder();
            int startIndex = map.get(s.length() - 1)[0];
            int endIndex = map.get(s.length() - 1)[1];

            do {
                result.insert(0, s.substring(startIndex == 0 ? startIndex : startIndex + 1, endIndex + 1));
                endIndex = map.get(startIndex)[1];
                startIndex = map.get(startIndex)[0];
                result.insert(0, " ");
            } while (startIndex != 0 || endIndex != 0);
            result.delete(0, 2);

            return result.toString();
        }
        return "No splitting found.";
    }

}
