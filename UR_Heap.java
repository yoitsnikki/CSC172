/*
 * Niharika Agrawal
 * CSC 172
 * Lab 7 - Heaps
 */

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class UR_Heap <T extends Comparable <T>> {
    private T[] heap;
    private int size;
    private int capacity;

    // default constructor with capacity 10
    public UR_Heap() {
        this.capacity = 10;
        this.heap = (T[]) new Comparable[capacity];
        this.size = 0;
    }

    // constructor with specified capacity
    public UR_Heap(int capacity) {
        this.capacity = capacity;
        this.heap = (T[]) new Comparable[capacity];
        this.size = 0;
    }

    // check if the heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // return the size of the heap
    public int size() {
        return size;
    }

    // insert a new item into the heap
    public void insert(T item) {
        if (size == capacity) {
            expandHeap();
        }
        heap[size] = item;
        bubbleUp(size);
        size++;
    }

    // constructor to heapify an existing array
    public UR_Heap(T[] array) {
        this.capacity = array.length;
        this.size = array.length;
        this.heap = (T[]) new Comparable[capacity];
        System.arraycopy(array, 0, heap, 0, array.length);
        heapify();
    }

    // bubbleup: helper method to maintain the heap property after insertion
    private void bubbleUp(int index) {
        int parentIndex = (index - 1) / 2;
        while (index > 0 && heap[index].compareTo(heap[parentIndex]) < 0) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    // bubbledown: helper method to maintain the heap property after deletion
    private void bubbleDown(int index) {
        int smallest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;

        if (leftChild < size && heap[leftChild].compareTo(heap[smallest]) < 0) {
            smallest = leftChild;
        }

        if (rightChild < size && heap[rightChild].compareTo(heap[smallest]) < 0) {
            smallest = rightChild;
        }

        if (smallest != index) {
            swap(index, smallest);
            bubbleDown(smallest);
        }
    }

    // remove and return the minimum item from the heap
    public T deleteMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        T min = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        bubbleDown(0);
        return min;
    }

    // Print the contents of the heap
    public void printHeap() {
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + " ");
        }
        System.out.println();
    }

    // Helper method to expand the heap array if capacity is reached
    private void expandHeap() {
        capacity *= 2;
        heap = Arrays.copyOf(heap, capacity);
    }

    // convert an existing array into a heap
    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            bubbleDown(i);
        }
    }

    // swap two elements in the heap
    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

}
