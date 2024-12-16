/*
 * Niharika Agrawal
 * CSC 172
 */

public class URPriorityQueue<T extends Comparable<T>> {
    private URArrayList<T> heap;

    public URPriorityQueue() {
        this.heap = new URArrayList<>();
    }

    public void insert(T item) {
        heap.add(item);  // Add item at the end of the list
        bubbleUp(heap.size() - 1);  // Bubble up the item to maintain the heap property
    }

    public T removeMin() {
        if (heap.size() == 0) return null;
        T minItem = heap.get(0);  // The root is the minimum
        T lastItem = heap.remove(heap.size() - 1);  // Remove last item
        if (heap.size() > 0) {
            heap.set(0, lastItem);  // Replace root with the last item
            bubbleDown(0);  // Rebalance the heap
        }
        return minItem;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parent)) >= 0) break;  // Stop if heap property is satisfied
            swap(index, parent);  // Swap with parent
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        int leftChild, rightChild, smallest;
        while ((leftChild = 2 * index + 1) < heap.size()) {
            rightChild = leftChild + 1;
            smallest = index;
            if (heap.get(leftChild).compareTo(heap.get(smallest)) < 0) smallest = leftChild;
            if (rightChild < heap.size() && heap.get(rightChild).compareTo(heap.get(smallest)) < 0)
                smallest = rightChild;
            if (smallest == index) break;  // Stop if heap property is satisfied
            swap(index, smallest);  // Swap with smallest child
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }
}
