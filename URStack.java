/*
 * Niharika Agrawal
 * CSC 172
 * URStack from scratch
 */


import java.util.EmptyStackException;

public class URStack<E> {
    private Object[] elements; // array to hold stack elements
    private int size; // current number of elements in the stack
    private static final int INITIAL_CAPACITY = 10; // initial capacity of the stack

    // constructor to initialize the stack
    public URStack() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    //  add an item to the top of the stack
    public void push(E item) {
        if (size == elements.length) {
            resize(); // resize the stack if it's full
        }
        elements[size++] = item; // add the item and increment size
        //System.out.println("Pushed onto stack: " + item); // debugging output
    }

    // remove and return the top item of the stack
    @SuppressWarnings("unchecked")
    public E pop() {
        if (isEmpty()) {
            throw new EmptyStackException(); // throw exception if stack is empty
        }
        E item = (E) elements[--size]; // decrement size and retrieve item
        elements[size] = null; // avoid memory leak
        // System.out.println("Popped from stack: " + item); // Debugging output
        return item; // return the popped item
    }

    // return the top item without removing it
    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty()) {
            throw new EmptyStackException(); // rhrow exception if stack is empty
        }
        return (E) elements[size - 1]; // return the top item
    }

    // check if the stack is empty
    public boolean isEmpty() {
        return size == 0; // return true if size is 0
    }

    // return the current number of elements in the stack
    public int size() {
        return size; // return the size of the stack
    }

    // resize the stack when it reaches capacity
    private void resize() {
        int newCapacity = elements.length * 2; // double the capacity
        Object[] newArray = new Object[newCapacity]; // create a new array
        System.arraycopy(elements, 0, newArray, 0, elements.length); // copy old elements to new array
        elements = newArray; // update the elements array
        // System.out.println("Stack resized to capacity: " + newCapacity); // debugging output
    }
}
