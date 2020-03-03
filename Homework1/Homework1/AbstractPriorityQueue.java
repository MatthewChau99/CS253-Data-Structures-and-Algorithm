/*
THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - Matthew Chau
 */
package Homework1;

import java.util.Map.Entry;
import java.util.Comparator;
import java.util.PriorityQueue;

/* An abstract base class to assist implementations of the PriorityQueue interface.*/
public abstract class AbstractPriorityQueue<K extends Comparable, V> extends PriorityQueue<Entry<K, V>> {
    //---------------- nested PQEntry class ----------------

    protected static class PQEntry<K extends Comparable, V> implements Entry<K, V> {

        private K k; // key
        private V v; // value

        public PQEntry(K key, V value) {
            k = key;
            v = value;
        }

        // methods of the Entry interface
        public K getKey() {
            return k;
        }

        public V getValue() {
            return v;
        }

        // utilities not exposed as part of the Entry interface
        protected void setKey(K key) {
            k = key;
        }

        public V setValue(V value) {
            v = value;
            return value;
        }
    } //----------- end of nested PQEntry class -----------


    // instance variable for an AbstractPriorityQueue
    /**
     * The comparator defining the ordering of keys in the priority queue.
     */
    protected Comparator<K> comparator;

    /**
     * Creates an empty priority queue using the given comparator to order keys.
     */
    protected AbstractPriorityQueue(Comparator<K> c) {
        comparator = c;
    }

    /**
     * Creates an empty priority queue based on the natural ordering of its keys.
     */
    protected AbstractPriorityQueue() {
        this(Comparator.naturalOrder());
    }

    /**
     * Method for comparing two entries according to key
     */
    protected int compare(Entry<K, V> a, Entry<K, V> b) {
        return comparator.compare(a.getKey(), b.getKey());
    }

    /**
     * Determines whether a key is valid.
     */
    protected boolean checkKey(K key) throws IllegalArgumentException {
        try {
            return (comparator.compare(key, key) == 0); // see if key can be compared to itself
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Incompatible key");
        }
    }

    /**
     * Tests whether the priority queue is empty.
     */
    public boolean isEmpty() {
        return size() == 0;
    }
}
