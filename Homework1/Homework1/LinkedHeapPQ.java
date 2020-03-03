package Homework1;

import java.util.Comparator;

public class LinkedHeapPQ<K extends Comparable, V> extends AbstractPriorityQueue<K, V> {

    protected static class Node<K extends Comparable, V> extends PQEntry<K, V> {
        private K key;
        private V value;
        private Node<K, V> parent;
        private Node<K, V> left;
        private Node<K, V> right;

        public Node(K key, V value, Node<K, V> parent, Node<K, V> left, Node<K, V> right) {
            super(key, value);
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getParent() {
            return parent;
        }

        public Node<K, V> getLeft() {
            return left;
        }

        public Node<K, V> getRight() {
            return right;
        }

        // update methods
        public void setElement(K k, V v) {
            this.key = k;
            this.value = v;
        }

        public void setParent(Node<K, V> parentNode) {
            this.parent = parentNode;
        }

        public void setLeft(Node<K, V> leftChild) {
            this.left = leftChild;
        }

        public void setRight(Node<K, V> rightChild) {
            this.right = rightChild;
        }

        protected Node<K, V> createNode(K k, V v, Node<K, V> parent, Node<K, V> left, Node<K, V> right) {
            return new Node<K, V>(k, v, parent, left, right);
        }
    }

    // LinkedBinaryTree instance variables
    protected Node<K, V> root = null;
    private int size = 0;
    private int height = 0;

    // constructor
    protected LinkedHeapPQ(Comparator<K> c) {
        comparator = c;
    }

    protected LinkedHeapPQ() {
        super();
    }

    public void insert(PQEntry<K, V> entry) {
        insert(new Node<>(entry.getKey(), entry.getValue(), findInsertPositionParent(size()), null, null));
    }

    public void insert(Node<K, V> newNode) {
        //add to last
        if (this.size == 0) {
            this.root = newNode;
        } else {
            Node<K, V> parentNode = this.findInsertPositionParent(size());
            if (parentNode.getLeft() == null) {
                parentNode.setLeft(newNode);
            } else {
                parentNode.setRight(newNode);
            }
            newNode.setParent(parentNode);
            swim(newNode);
        }
        this.size++;
    }

    public void swim(Node<K, V> node) {
        while (node.getParent() != null && comparator.compare(node.getParent().getKey(), node.getKey()) > 0) {
            swap(node, node.getParent());
            node = node.getParent();
        }
    }

    public Node<K, V> findLastNodePosition(int size) {
        Node<K, V> node = root;
        String s = "";
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return root;
        } else {
            while (size != 1) {
                int i = size % 2;
                s = i + s;
                size /= 2;
            }
            for (int j = 0; j < s.length(); j++) {
                node = s.charAt(j) == '1' ? node.right : node.left;
            }
            return node;
        }
    }

    public Node<K, V> findInsertPositionParent(int size) {
        Node<K, V> node = root;
        boolean rightEnd = true;
        String s = "";
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return root;
        } else {
            size++;//next position for insert
            while (size != 1) {
                int i = size % 2;
                s = i + s;
                size /= 2;
            }
            s = s.substring(0, s.length() - 1);
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '0') {
                    rightEnd = false;
                }
                node = s.charAt(j) == '1' ? node.right : node.left;
            }
            return node;
        }
    }


    public Node removeMin() {
        //swap last node with root
        if (isEmpty()) throw new IllegalArgumentException("Nothing to remove.");
        else if (size == 1) {
            Node node = this.root.createNode(this.root.getKey(), this.root.getValue(), null, null, null);
            this.root = null;
            size--;
            return node;
        }
        Node<K, V> nodeLast = findLastNodePosition(size());
        Node<K, V> nodeRoot = this.root;
        Node<K, V> temp;
        swap(nodeLast, nodeRoot);
        if (nodeLast.getParent().getLeft().equals(nodeLast)) {//is left child
            temp = new Node<>(nodeLast.getKey(), nodeLast.getValue(), nodeLast.getParent(), nodeLast.getLeft(), nodeLast.getRight());
            nodeLast.getParent().setLeft(null);
        } else {
            temp = new Node<>(nodeLast.getKey(), nodeLast.getValue(), nodeLast.getParent(), nodeLast.getLeft(), nodeLast.getRight());
            nodeLast.getParent().setRight(null);
        }
        sink(nodeRoot);
        this.size--;
        return temp;
    }

    public void sink(Node<K, V> node) {
        Node<K, V> smaller = node;
        for (int i = 0; i < Math.log(size()); i++) {
            node = smaller;
            if ((node.getLeft() != null && node.getRight() != null)) {
                if (comparator.compare(node.getLeft().getKey(),
                        node.getRight().getKey()) < 0) {
                    smaller = node.getLeft();
                } else {
                    smaller = node.getRight();
                }
            } else if (node.getLeft() != null) {
                smaller = node.getLeft();
            }
            if (comparator.compare(node.getKey(), smaller.getKey()) < 0) break;
            swap(node, smaller);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void swap(Node<K, V> N1, Node<K, V> N2) {
        Node<K, V> temp = new Node<>(N1.getKey(), N1.getValue(), N1.getParent(), N1.getLeft(), N1.getRight());
        N1.setElement(N2.getKey(), N2.getValue());
        N2.setElement(temp.getKey(), temp.getValue());
    }
}
