/*
THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - Matthew Chau
 */
package Homework1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HeapComparisonTest extends AbstractPriorityQueue{

    public static void main(String[] args) {

        StringBuilder s = new StringBuilder();
        long tArrTemp = 0;
        long tLinkTemp = 0;
        s.append("size \t").append("array \t").append("linked \n");

        // for (int i = 1000; i <= 10000; i += 1000) {
        //     List<PQEntry<Integer, Integer>> keys = createList(i);
        //     ArrayHeapPQ<Integer, Integer> arrHeap = new ArrayHeapPQ<>();
        //     LinkedHeapPQ<Integer, Integer> linkHeap = new LinkedHeapPQ<>();
        //     s.append(i);
        //     s.append("\t");
        //
        //     /* Test Array Heap */
        //     long sArr = System.currentTimeMillis();
        //     keys.forEach(arrHeap::insert);
        //     while (arrHeap.size() != 0) {
        //         arrHeap.removeMin(arrHeap.size());
        //     }
        //     long tArr = tArrTemp + System.currentTimeMillis() - sArr;
        //     tArrTemp = tArr;
        //     s.append((double) tArr / 1000.);
        //     s.append('\t');
        //
        //     /* Test Linked Heap */
        //     long sLink = System.currentTimeMillis();
        //     keys.forEach(linkHeap::insert);
        //     while (!linkHeap.isEmpty()) {
        //         linkHeap.removeMin();
        //     }
        //     long tLink = tLinkTemp + System.currentTimeMillis() - sLink;
        //     tLinkTemp = tLink;
        //     s.append((double) tLink / 1000.);
        //     s.append('\n');
        // }
        // System.out.println(s);

        List<PQEntry<Integer, Integer>> keys = createList(100);
        List<Integer> intKeys = new ArrayList<>();
        List<PQEntry<Integer, Integer>> sortedkeys = new ArrayList<>();
        List<Integer> sortedIntKeys = new ArrayList<>();

        for (PQEntry<Integer, Integer> entry : keys) {
          intKeys.add(entry.getKey());
        }
        System.out.println(intKeys);

        LinkedHeapPQ<Integer, Integer> linkHeap = new LinkedHeapPQ<>();
        keys.forEach(linkHeap::insert);

        while (!linkHeap.isEmpty()) {
          System.out.println(linkHeap.removeMin().getKey());
        }
        // for (PQEntry<Integer, Integer> entry : sortedkeys) {
        //   sortedIntKeys.add(entry.getKey());
        // }
        //
        // System.out.println(sortedIntKeys.toString());


    }

    public static List<PQEntry<Integer, Integer>> createList(int size) {
        List<PQEntry<Integer, Integer>> keys = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            keys.add(new PQEntry<>(i, (int) (Math.random() * 1000)));
        }
        Collections.shuffle(keys);
        return keys;
    }

}
