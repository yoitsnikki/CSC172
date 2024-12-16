/*
 * Niharika Agrawal
 * CSC 172
 * URQueue from scratch
 */

import java.util.NoSuchElementException;
import java.util.Iterator;

public class URQueue<E> implements Iterable<E> {
    private URNode<E> head;
    private URNode<E> tail;
    private int size;

    // initiate queue
    public URQueue() {
        this.head = this.tail = null;
        this.size = 0;
    }

    // check if it is empty
    public boolean isEmpty() {
        return head == null;
    }

    // return size
    public int size() {
        return size;
    }

    // enqueue
    public void enqueue (E element) {
        URNode<E> newNode = new URNode<> (element, null, null);
        if (isEmpty()) {
            head = tail = newNode;
        }

        else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    // dequeue
    public E dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        E value = head.element();
        head  = head.next();
        size--;
        if (size == 0) tail = null;
        return value;
    }

    // peek
    public E peek() {
        if (isEmpty()) throw new NoSuchElementException();
        return head.element();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private URNode<E> current = head;

            @Override
            public boolean hasNext() {
                return current!= null;
            }

            @Override
            public E next() {
                E value = current.element();
                current = current.next();
                return value;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        URQueue<E> tempQueue = new URQueue<>();  // temporary queue to preserve original order
        while (!isEmpty()) {
            E element = dequeue();
            sb.append(element).append(", ");
            tempQueue.enqueue(element);  // preserve the dequeued element
        }
        while (!tempQueue.isEmpty()) {
            enqueue(tempQueue.dequeue());  // restore original order
        }
        if (sb.length() > 1) sb.setLength(sb.length() - 2);  // remove the last comma and space
        sb.append("]");
        return sb.toString();
    }


}
