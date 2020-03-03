/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.datastructures;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An abbreviated version of the balanced binary tree map from Goodrich et al.
 */

public class TreeMap<K,V> {
    protected static class MapEntry<K,V> implements Entry<K,V> {
        private K k;  // key
        private V v;  // value

        public MapEntry(K key, V value) {
            k = key;
            v = value;
        }

        // public methods of the Entry interface
        public K getKey() { return k; }
        public V getValue() { return v; }

        // utilities not exposed as part of the Entry interface
        protected void setKey(K key) { k = key; }
        protected V setValue(V value) {
            V old = v;
            v = value;
            return old;
        }

        /** Returns string representation (for debugging only) */
        public String toString() { return "<" + k + ", " + v + ">"; }
    }
    protected static class BalanceableBinaryTree<K,V> extends LinkedBinaryTree<Entry<K,V>> {
        //-------------- nested BSTNode class --------------
        // this extends the inherited LinkedBinaryTree.Node class
        protected static class BSTNode<E> extends Node<E> {
            int aux = 0;
            boolean red = false;
            BSTNode(E e, Node<E> parent, Node<E> leftChild, Node<E> rightChild) {
                super(e, parent, leftChild, rightChild);
            }
            public int getAux() { return aux; }
            public void setAux(int value) { aux = value; }
            public boolean getColor() { return red; }
            public void setColor(boolean value) { red = value; }
        }

        public int getAux(Position<Entry<K,V>> p) {
            return ((BSTNode<Entry<K,V>>) p).getAux();
        }

        public void setAux(Position<Entry<K,V>> p, int value) {
            ((BSTNode<Entry<K,V>>) p).setAux(value);
        }

        public boolean getColor(Position<Entry<K,V>> p) {
            return ((BSTNode<Entry<K,V>>) p).getColor();
        }

        public void setColor(Position<Entry<K,V>> p, boolean value) {
            ((BSTNode<Entry<K,V>>) p).setColor(value);
        }

        // Override node factory function to produce a BSTNode (rather than a Node)
        @Override
        protected Node<Entry<K,V>> createNode(Entry<K,V> e, Node<Entry<K,V>> parent, Node<Entry<K,V>> left, Node<Entry<K,V>> right){
            return new BSTNode<>(e, parent, left, right);
        }

        /** Relinks a parent node with its oriented child node. */
        private void relink(Node<Entry<K,V>> parent, Node<Entry<K,V>> child,
                                                boolean makeLeftChild) {
            child.setParent(parent);
            if (makeLeftChild)
                parent.setLeft(child);
            else
                parent.setRight(child);
        }

        /**
         * Rotates Position p above its parent.  Switches between these
         * configurations, depending on whether p is a or p is b.
         *<pre>
         *          b                  a
         *         / \                / \
         *        a  t2             t0   b
         *       / \                    / \
         *      t0  t1                 t1  t2
         *</pre>
         *  Caller should ensure that p is not the root.
         */
        public void rotate(Position<Entry<K,V>> p) {
            Node<Entry<K,V>> x = validate(p);
            Node<Entry<K,V>> y = x.getParent();        // we assume this exists
            Node<Entry<K,V>> z = y.getParent();        // grandparent (possibly null)
            if (z == null) {
                root = x;                                // x becomes root of the tree
                x.setParent(null);
            } else
                relink(z, x, y == z.getLeft());          // x becomes direct child of z
            // now rotate x and y, including transfer of middle subtree
            if (x == y.getLeft()) {
                relink(y, x.getRight(), true);           // x's right child becomes y's left
                relink(x, y, false);                     // y becomes x's right child
            } else {
                relink(y, x.getLeft(), false);           // x's left child becomes y's right
                relink(x, y, true);                      // y becomes left child of x
            }
        }

        /**
         *
         * Returns the Position that becomes the root of the restructured subtree.
         *
         * Assumes the nodes are in one of the following configurations:
         *<pre>
         *     z=a                 z=c           z=a               z=c
         *    /  \                /  \          /  \              /  \
         *   t0  y=b             y=b  t3       t0   y=c          y=a  t3
         *      /  \            /  \               /  \         /  \
         *     t1  x=c         x=a  t2            x=b  t3      t0   x=b
         *        /  \        /  \               /  \              /  \
         *       t2  t3      t0  t1             t1  t2            t1  t2
         *</pre>
         * The subtree will be restructured so that the node with key b becomes its root.
         *<pre>
         *           b
         *         /   \
         *       a       c
         *      / \     / \
         *     t0  t1  t2  t3
         *</pre>
         * Caller should ensure that x has a grandparent.
         */
        public Position<Entry<K,V>> restructure(Position<Entry<K,V>> x) {
            Position<Entry<K,V>> y = parent(x);
            Position<Entry<K,V>> z = parent(y);
            if ((x == right(y)) == (y == right(z))) {   // matching alignments
                rotate(y);                                // single rotation (of y)
                return y;                                 // y is new subtree root
            } else {                                    // opposite alignments
                rotate(x);                                // double rotation (of x)
                rotate(x);
                return x;                                 // x is new subtree root
            }
        }
    } //----------- end of nested BalanceableBinaryTree class -----------

    /** Representation of the underlying tree structure. */
    protected BalanceableBinaryTree<K,V> tree = new BalanceableBinaryTree<>();

    /**
     * Initializes the comparator for the map.
     * @param c comparator defining the order of keys in the map
     */
    private Comparator<K> comp;

    /** Constructs an empty map using the natural ordering of keys. */
    public TreeMap() {
        this(new DefaultComparator<K>());    // default comparator uses natural ordering
    }

    /**
     * Constructs an empty map using the given comparator to order keys.
     * @param comp comparator defining the order of keys in the map
     */
    public TreeMap(Comparator<K> comp) {
        this.comp = comp;
        tree.addRoot(null);       // create a sentinel leaf as root
    }

    /**
     * Returns the number of entries in the map.
     * @return number of entries in the map
     */
    public int size() {
        return (tree.size() - 1) / 2;        // only internal nodes have entries
    }

    /** Utility used when inserting a new entry at a leaf of the tree */
    private void expandExternal(Position<Entry<K,V>> p, Entry<K,V> entry) {
        tree.set(p, entry);            // store new entry at p
        tree.addLeft(p, null);         // add new sentinel leaves as children
        tree.addRight(p, null);
    }
      /** Method for comparing two entries according to key */
    protected int compare(Entry<K,V> a, Entry<K,V> b) {
        return comp.compare(a.getKey(), b.getKey());
    }

    /** Method for comparing a key and an entry's key */
    protected int compare(K a, Entry<K,V> b) {
        return comp.compare(a, b.getKey());
    }

    /** Method for comparing a key and an entry's key */
    protected int compare(Entry<K,V> a, K b) {
        return comp.compare(a.getKey(), b);
    }

    /** Method for comparing two keys */
    protected int compare(K a, K b) {
        return comp.compare(a, b);
    }


    // Some notational shorthands for brevity (yet not efficiency)
    protected Position<Entry<K,V>> root() { return tree.root(); }
    protected Position<Entry<K,V>> parent(Position<Entry<K,V>> p) { return tree.parent(p); }
    protected Position<Entry<K,V>> left(Position<Entry<K,V>> p) { return tree.left(p); }
    protected Position<Entry<K,V>> right(Position<Entry<K,V>> p) { return tree.right(p); }
    protected Position<Entry<K,V>> sibling(Position<Entry<K,V>> p) { return tree.sibling(p); }
    protected boolean isRoot(Position<Entry<K,V>> p) { return tree.isRoot(p); }
    protected boolean isExternal(Position<Entry<K,V>> p) { return tree.isExternal(p); }
    protected boolean isInternal(Position<Entry<K,V>> p) { return tree.isInternal(p); }
    protected void set(Position<Entry<K,V>> p, Entry<K,V> e) { tree.set(p, e); }
    protected Entry<K,V> remove(Position<Entry<K,V>> p) { return tree.remove(p); }
    protected void rotate(Position<Entry<K,V>> p) { tree.rotate(p); }
    protected Position<Entry<K,V>> restructure(Position<Entry<K,V>> x) { return tree.restructure(x); }

    /**
     * Returns the position in p's subtree having the given key (or else the terminal leaf).
     * @param key  a target key
     * @param p  a position of the tree serving as root of a subtree
     * @return Position holding key, or last node reached during search
     */
    protected Position<Entry<K,V>> treeSearch(Position<Entry<K,V>> p, K key) {
        if (isExternal(p))
            return p;                          // key not found; return the final leaf
        int comp = compare(key, p.getElement());
        if (comp == 0)
            return p;                          // key found; return its position
        else if (comp < 0)
            return treeSearch(left(p), key);   // search left subtree
        else
            return treeSearch(right(p), key);  // search right subtree
    }

    /**
     * Returns position with the minimal key in the subtree rooted at Position p.
     * @param p  a Position of the tree serving as root of a subtree
     * @return Position with minimal key in subtree
     */
    protected Position<Entry<K,V>> treeMin(Position<Entry<K,V>> p) {
        Position<Entry<K,V>> walk = p;
        while (isInternal(walk))
            walk = left(walk);
        return parent(walk);              // we want the parent of the leaf
    }

    /**
     * Returns the position with the maximum key in the subtree rooted at p.
     * @param p  a Position of the tree serving as root of a subtree
     * @return Position with maximum key in subtree
     */
    protected Position<Entry<K,V>> treeMax(Position<Entry<K,V>> p) {
        Position<Entry<K,V>> walk = p;
        while (isInternal(walk))
            walk = right(walk);
        return parent(walk);              // we want the parent of the leaf
    }

    /**
     * Returns the value associated with the specified key, or null if no such entry exists.
     * @param key  the key whose associated value is to be returned
     * @return the associated value, or null if no such entry exists
     */
    public V get(K key) throws IllegalArgumentException {
        Position<Entry<K,V>> p = treeSearch(root(), key);
        rebalanceAccess(p);                     // hook for balanced tree subclasses
        if (isExternal(p)) return null;         // unsuccessful search
        return p.getElement().getValue();       // match found
    }

    /**
     * Associates the given value with the given key. If an entry with
     * the key was already in the map, this replaced the previous value
     * with the new one and returns the old value. Otherwise, a new
     * entry is added and null is returned.
     * @param key    key with which the specified value is to be associated
     * @param value  value to be associated with the specified key
     * @return the previous value associated with the key (or null, if no such entry)
     */
    public V put(K key, V value) throws IllegalArgumentException {
        Entry<K,V> newEntry = new MapEntry<>(key, value);
        Position<Entry<K,V>> p = treeSearch(root(), key);
        if (isExternal(p)) {                    // key is new
            expandExternal(p, newEntry);
            rebalanceInsert(p);                   // hook for balanced tree subclasses
            return null;
        } else {                                // replacing existing key
            V old = p.getElement().getValue();
            set(p, newEntry);
            rebalanceAccess(p);                   // hook for balanced tree subclasses
            return old;
        }
    }

    /**
     * Removes the entry with the specified key, if present, and returns
     * its associated value. Otherwise does nothing and returns null.
     * @param key  the key whose entry is to be removed from the map
     * @return the previous value associated with the removed key, or null if no such entry exists
     */
    public V remove(K key) throws IllegalArgumentException {
        Position<Entry<K,V>> p = treeSearch(root(), key);
        if (isExternal(p)) {                    // key not found
            rebalanceAccess(p);                   // hook for balanced tree subclasses
            return null;
        } else {
            V old = p.getElement().getValue();
            if (isInternal(left(p)) && isInternal(right(p))) { // both children are internal
                Position<Entry<K,V>> replacement = treeMax(left(p));
                set(p, replacement.getElement());
                p = replacement;
            } // now p has at most one child that is an internal node
            Position<Entry<K,V>> leaf = (isExternal(left(p)) ? left(p) : right(p));
            Position<Entry<K,V>> sib = sibling(leaf);
            remove(leaf);
            remove(p);                            // sib is promoted in p's place
            rebalanceDelete(sib);                 // hook for balanced tree subclasses
            return old;
        }
    }

    // Stubs for balanced search tree operations (subclasses can override)

    /**
     * Rebalances the tree after an insertion of specified position.  This
     * version of the method does not do anything, but it can be
     * overridden by subclasses.
     * @param p the position which was recently inserted
     */
    protected void rebalanceInsert(Position<Entry<K,V>> p) { }
    /**
     * Rebalances the tree after a child of specified position has been
     * removed.  This version of the method does not do anything, but it
     * can be overridden by subclasses.
     * @param p the position of the sibling of the removed leaf
     */
    protected void rebalanceDelete(Position<Entry<K,V>> p) { }

    /**
     * Rebalances the tree after an access of specified position.  This
     * version of the method does not do anything, but it can be
     * overridden by a subclasses.
     * @param p the Position which was recently accessed (possibly a leaf)
     */
    protected void rebalanceAccess(Position<Entry<K,V>> p) { }
}
