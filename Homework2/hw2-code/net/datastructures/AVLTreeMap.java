/*
THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - Matthew Chau
 */
package net.datastructures;

import java.util.Comparator;

public class AVLTreeMap<K,V> extends TreeMap<K,V> {
    public AVLTreeMap() { super(); }

    public AVLTreeMap(Comparator<K> comp) { super(comp); }

    protected void rebalance(Position<Entry<K,V>> p) {
        if (balanceFactor(p) > 1) {//aux(left) > aux(right)
            if (balanceFactor(left(p)) == -1) {//zig-zag on the left
                rotate(right(left(p)));
                resetHeights(left(left(p)));
            }
            rotate(left(p));
            resetHeights(p);
        } else if (balanceFactor(p) < -1) {//aux(right) > aux(left)
            if (balanceFactor(right(p)) == 1) {//zig-zag on the right
                rotate(left(right(p)));
                resetHeights(right(right(p)));
            }
            rotate(right(p));
            resetHeights(p);
        } else { if (!isRoot(p)) rebalance(parent(p)); }
    }

    /**
     * Rebalances the tree after an insertion of specified position.
     * @param p the position which was recently inserted
     */
    @Override
    protected void rebalanceInsert(Position<Entry<K,V>> p) {
        resetHeights(p);
        rebalance(p);
    }


    /**
     * Rebalances the tree after a child of specified position has been
     * removed.
     * @param p the position of the sibling of the removed leaf
     */
    @Override
    protected void rebalanceDelete(Position<Entry<K,V>> p) {
        if (p.getElement() == null) {
            if (sibling(p) != null) { rebalanceInsert(sibling(p)); }
        } else { rebalanceInsert(p); }
    }

    /**
     *   reset aux on the path from p to root
     * @param p the position of the leaf node
     */
    protected void resetHeights(Position<Entry<K,V>> p) {
        if (p.getElement() != null) {
            int leftHeight = 0;
            int rightHeight = 0;
            if (hasLeftChild(p)) { leftHeight = left(p).getElement() != null ? tree.getAux(left(p)) : 0; }
            if (hasRightChild(p)){ rightHeight = right(p).getElement() != null ? tree.getAux(right(p)): 0; }
            int height = Math.max(leftHeight, rightHeight) + 1;
            if (height != tree.getAux(p)) { tree.setAux(p, height); }
            if (!isRoot(p)) resetHeights(parent(p));
        }
    }

    /*
        the tree needs to be balanced if there exists a node with position p that needsBalance(p) != -1, 0, or 1
     */
    public int balanceFactor(Position<Entry<K, V>> p) {
        if (hasLeftChild(p) && hasRightChild(p)) { return tree.getAux(left(p)) - tree.getAux(right(p));}
        //if two aux heights differs more than 1, balance
        else if (hasLeftChild(p)) { return tree.getAux(left(p)); }
        else if (hasRightChild(p)) { return -tree.getAux(right(p)); }
        else return 0;
    }

    private int blackdepth(Position<Entry<K, V>> p) {
        int countBlack = 0;
        Position<Entry<K, V>> pos = p;
        while (pos != this.root()) {
            if (!this.tree.getColor(pos)) countBlack++;
            pos = parent(pos);
        }
        return countBlack;
    }

    public void redBlackRecolor(){
        for (Position<Entry<K, V>> pos : this.tree.positions()) this.tree.setColor(pos, false);
        recolorTree(this.root());
    }

    public void recolorTree(Position<Entry<K, V>> p) {
        if (isInternal(p) && hasChildren(p)) {
            if (this.tree.getAux(p) % 2 == 0) {
                if (hasLeftChild(p) && this.tree.getAux(left(p)) % 2 == 1) { this.tree.setColor(left(p), true); }
                if (hasRightChild(p) && this.tree.getAux(right(p)) % 2 == 1) { this.tree.setColor(right(p), true); }
            }
            if (hasLeftChild(p)) recolorTree(left(p));
            if (hasRightChild(p)) recolorTree(right(p));
        }
    }

    public boolean isValidRedBlack(){
        if (this.tree.getColor(this.tree.root())) {
            System.out.println("Root property violated");
            return false;
        }
        for (Position<Entry<K, V>> pos : this.tree.positions()) {
            if (this.tree.getColor(pos)) {
                if (isExternal(pos)) {
                    System.out.println("External property violated");
                    return false;
                }
                if (hasLeftChild(pos)) {
                    if (this.tree.getColor(left(pos))) {
                        System.out.println("Red property violated");
                        return false;
                    }
                }
                if (hasRightChild(pos)) {
                    if (this.tree.getColor(right(pos))) {
                        System.out.println("Red property violated");
                        return false;
                    }
                }
            }
            if (isExternal(pos)) {
                if (blackdepth(pos) != (this.tree.getAux(this.root()) + 1) / 2) {
                    System.out.println("Depth property violated");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasLeftChild(Position<Entry<K, V>> p) { return left(p).getElement() != null; }

    private boolean hasRightChild(Position<Entry<K, V>> p) { return right(p).getElement() != null; }

    private boolean hasChildren(Position<Entry<K, V>> p) { return hasLeftChild(p) || hasRightChild(p); }

    public static void main(String[] args) {
        AVLTreeMap<Integer, Integer> avlTree = new AVLTreeMap<>();
        //adding
        avlTree.put(107,35);
        avlTree.put(46,21);
        avlTree.put(11,87);
        avlTree.put(76,23);
        avlTree.put(58,2);
        avlTree.put(69,19);
        avlTree.put(5,1);
        avlTree.redBlackRecolor();
        System.out.println(avlTree.toString(avlTree.tree));

        //removing
        System.out.println(avlTree.isValidRedBlack());
    }

    public String toString(BalanceableBinaryTree<K, V> tree) {
        String result = "";

        for (Position<Entry<K, V>> pos : tree.positions()) {
            Entry<K, V> e = pos.getElement();
            if (e != null) {
                result += e.getKey() + "\t aux:" + this.tree.getAux(pos) + "\t color:" + this.tree.getColor(pos) + " -> (" +
                        left(pos).getElement() + ", " + right(pos).getElement() + ") \n ";
            }
        }
        result += "------------------------- \n";
        return result;
    }
}
