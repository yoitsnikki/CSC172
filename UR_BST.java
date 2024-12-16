/*
 * Niharika Agrawal
 * CSC 172
 * Lab 6 - Binary Search tree
 */

import java.util.*;

abstract public class UR_BST <Key extends Comparable<Key>, Value> implements Iterable<Key> {
    private UR_Node root; // root of BST

    // node class
    private class UR_Node {
        private Key key;
        private Value val;
        private UR_Node left, right;
        private int size; // number of nodes in subtree

        public UR_Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    // return true if bst is empty
    public boolean isEmpty() {
        return size() == 0;
    }

    // return number of nodes in bst
    public int size() {
        return size(root);
    }

    private int size(UR_Node node) {
        return node == null ? 0 : node.size;
    }

    // check if bst contains specific ckey
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        return get(key) != null;
    }

    // retrieve value associated with specific key
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        return get(root, key);
    }

    private Value get(UR_Node node, Key key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return get(node.left, key);
        else if (cmp > 0) return get(node.right, key);
        else return node.val;
    }

    // insert or update key pair
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        root = put(root, key, val);
    }

    private UR_Node put(UR_Node node, Key key, Value val) {
        if (node == null) return new UR_Node(key, val, 1);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, val);
        else if (cmp > 0) node.right = put(node.right, key, val);
        else node.val = val;
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    // remove smallest key
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow: cannot delete from an empty tree.");
        root = deleteMin(root);
    }

    private UR_Node deleteMin(UR_Node node) {
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    // remove largest key
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow: cannot delete from an empty tree.");
        root = deleteMax(root);
    }

    private UR_Node deleteMax(UR_Node node) {
        if (node.right == null) return node.left;
        node.right = deleteMax(node.right);
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }
    
    // remove specific key
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        root = delete(root, key);
    }

    private UR_Node delete(UR_Node node, Key key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = delete(node.left, key);
        else if (cmp > 0) node.right = delete(node.right, key);
        else {
            if (node.right == null) return node.left;
            if (node.left == null) return node.right;
            UR_Node temp = node;
            node = min(temp.right);
            node.right = deleteMin(temp.right);
            node.left = temp.left;
        }
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    private UR_Node min(UR_Node node) {
        return node.left == null ? node : min(node.left);
    }

    // return height
    public int height() {
        return height(root);
    }

    private int height(UR_Node node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // return iterable for keys
    public Iterable<Key> keys() {
        URQueue<Key> queue = new URQueue<>(); // Using URQueue
        inorder(root, queue);
        return queue;
    }

    private void inorder(UR_Node node, URQueue<Key> queue) {
        if (node == null) return;
        inorder(node.left, queue);
        queue.enqueue(node.key);
        inorder(node.right, queue);
    }

    // return iterable for level order transversal
    public Iterable<Key> levelOrder() {
        URQueue<Key> keys = new URQueue<>(); // Using URQueue
        URQueue<UR_Node> nodes = new URQueue<>(); // Using URQueue
        if (root != null) nodes.enqueue(root);
        while (!nodes.isEmpty()) {
            UR_Node node = nodes.dequeue();
            keys.enqueue(node.key);
            if (node.left != null) nodes.enqueue(node.left);
            if (node.right != null) nodes.enqueue(node.right);
        }
        return keys;
    }

    // iterator for keys
    @Override
    public Iterator<Key> iterator() {
        return keys().iterator();
    }
}
