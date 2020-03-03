/*
THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - Matthew Chau
 */
package Homework1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArrayHeapPQ<K extends Comparable, V> extends AbstractPriorityQueue<K, V> {

    private List<PQEntry<K, V>> arr;

    public ArrayHeapPQ() {
        super();
        arr = new ArrayList<>();
        arr.add(null);
    }

    public ArrayHeapPQ(Comparator<K> comparator) {
        super(comparator);
        arr = new ArrayList<>();
        arr.add(null);
    }

    public void insert(PQEntry<K, V> e) {
        arr.add(e);
        swim(size());
    }

    private void swim(int k) {
        while (1 < k && compare(arr.get(k / 2), arr.get(k)) > 0) {
            Collections.swap(arr, k / 2, k);
            k /= 2;
        }
    }

    public PQEntry removeMin(int k) {
        if (isEmpty()) {
            return null;
        }
        Collections.swap(arr, 1, size());//swap the root with the last node
        PQEntry min = arr.remove(size());
        sink(1);
        return min;
    }

    private void sink(int k) {
        for (int i = k * 2; i <= size(); k = i, i *= 2) {
            if (i < size() && compare(arr.get(i), arr.get(i + 1)) > 0) i++;//find the larger child
            if (compare(arr.get(k), arr.get(i)) <= 0) break;//if parent > child then swap
            Collections.swap(arr, k, i);
        }
    }

    public PQEntry<K, V> min() {
        return !isEmpty() ? arr.get(1) : null;
    }

    public boolean isEmpty() {
        return arr.isEmpty();
    }

    public int size() {
        return arr.size() - 1;
    }


}
